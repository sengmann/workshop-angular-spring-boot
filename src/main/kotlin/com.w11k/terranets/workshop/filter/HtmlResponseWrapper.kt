class HtmlResponseWrapper(response: HttpServletResponse) : HttpServletResponseWrapper(response) {

    private val capture: ByteArrayOutputStream
    private var output: ServletOutputStream? = null
    private var writer: PrintWriter? = null

    init {
        capture = ByteArrayOutputStream(response.bufferSize)
    }

    override fun getOutputStream(): ServletOutputStream {
        if (writer != null) {
            throw IllegalStateException(
                    "getWriter() has already been called on this response.")
        }

        if (output == null) {
            output = object : ServletOutputStream() {
                override fun setWriteListener(writeListener: WriteListener?) {
                    LOG.trace("unusual: someone wanted to set a write Listener")
                }

                override fun isReady(): Boolean = false

                @Throws(IOException::class)
                override fun write(b: Int) {
                    capture.write(b)
                }

                @Throws(IOException::class)
                override fun flush() {
                    capture.flush()
                }

                @Throws(IOException::class)
                override fun close() {
                    capture.close()
                }

            }
        }

        return output as ServletOutputStream
    }

    @Throws(IOException::class)
    override fun getWriter(): PrintWriter {
        if (output != null) {
            throw IllegalStateException(
                    "getOutputStream() has already been called on this response.")
        }

        if (writer == null) {
            writer = PrintWriter(OutputStreamWriter(capture,
                    characterEncoding))
        }

        return writer as PrintWriter
    }

    @Throws(IOException::class)
    override fun flushBuffer() {
        super.flushBuffer()

        if (writer != null) {
            writer!!.flush()
        } else if (output != null) {
            output!!.flush()
        }
    }

    val captureAsBytes: ByteArray
        @Throws(IOException::class)
        get() {
            if (writer != null) {
                writer!!.close()
            } else if (output != null) {
                output!!.close()
            }

            return capture.toByteArray()
        }

    val captureAsString: String
        @Throws(IOException::class)
        get() = String(captureAsBytes, Charset.forName("UTF-8"))

    companion object {
        val LOG= loggerFor<HtmlResponseWrapper>()
    }

}
