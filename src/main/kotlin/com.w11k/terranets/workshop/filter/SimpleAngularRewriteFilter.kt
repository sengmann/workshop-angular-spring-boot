package com.w11k.terranets.workshop.filter;


import org.slf4j.LoggerFactory
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * This filter redirects all requests to index.html so angular can work with it
 * and sets base path according to contextPath
 * Created by cstrauss on 10.04.17.
 *
 * @author zuvic name and version now beeing injected
 */
@Singleton
class SimpleAngularRewriteFilter @Inject constructor(
        applicationMetaData: ApplicationMetaData
) : Filter {

    val applicationName = "app-name"
    val appPath = "client"
    private val version = "0.0.0"

    private val versionString = "${applicationName} ${version}"

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest && response is HttpServletResponse) {
            val requestedURL = request.requestURI
            val contextPath = request.servletContext.contextPath
            val requestedPath = request.requestURI.replaceFirst(contextPath.toRegex(), "")

            LOG.trace("contextPath:  {}", contextPath)

            // todo redirect to /login if /login/login/ (index.html) was requested
            if ((requestedPath.endsWith("/") || requestedPath.endsWith("index.html")) && request.servletContext.getResource(requestedPath) == null) {

                val targetUrl = "$contextPath/"
                LOG.debug("Request to path {} without index.html, redirecting to ", request.requestURI, targetUrl)

                if (request.servletContext.getResource("/index.html") != null) {
                    response.sendRedirect(targetUrl)
                    return
                } else {
                    LOG.debug("Skipping redirect because target url {} does not exist", targetUrl)
                }
            }

            val resourceExists = request.servletContext.getResource(requestedPath) != null
            if (!resourceExists) {
                forwardRequestToIndexHtml(request, response, requestedPath, contextPath)

            } else if (requestedPath.endsWith("/") || requestedPath.endsWith("index.html")) {
                val capturingResponseWrapper = HtmlResponseWrapper(response)
                chain.doFilter(request, capturingResponseWrapper) // execute filterChain to get response
                val content = capturingResponseWrapper.captureAsString

                if (response.contentType != null && response.contentType.contains("text/html") && content.contains("<base href=\"")) {
                    replaceBaseHref(response, content, contextPath)
                }
                else if (response.status == 200) {
                    val bytes = capturingResponseWrapper.captureAsBytes
                    response.outputStream.write(bytes) // send unmodified response
                } else {
                    return
                }
            } else {
                chain.doFilter(request, response) // execute filterChain to get response
            }
        } else {
            LOG.error("No HttpServletResponse in this environment, try tomcat")
            return
        }
    }

    @Throws(IOException::class, ServletException::class)
    private fun forwardRequestToIndexHtml(request: ServletRequest, response: ServletResponse, localRequestURI: String, contextPath: String) {

        val targetUrl = "/index.html"
        val targetResourceExists = request.servletContext.getResource(targetUrl) != null

        if (targetResourceExists) {
            LOG.trace("resource {} not found, forwarding request to {}", localRequestURI, targetUrl)
            request.getRequestDispatcher(targetUrl).forward(request, response)
        } else {
            LOG.error("Can not forward because target resource {} does not exist", targetUrl)
            return
        }

    }

    @Throws(IOException::class)
    private fun replaceBaseHref(response: ServletResponse, content: String, contextPath: String) {
        LOG.trace("add contextPath {} to base href", contextPath)


        val replacedContent = content
                .replaceFirst("<base href=\"(.*)\">".toRegex(), "<base href=\"$contextPath$1\">")
        response.characterEncoding = "UTF-8"
        response.setContentLength(replacedContent.length)
        response.writer.write(replacedContent)
    }

    override fun destroy() {

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SimpleAngularRewriteFilter::class.java)
    }
}
