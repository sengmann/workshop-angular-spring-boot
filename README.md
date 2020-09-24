# Java und Angular Workshop

## Spring Boot

Das Spring-Framework hat eine weite Verbreitung, ist in der Einrichtung allerdings sehr komplex. Eine vorgefertigte
Konfiguration, die in vielen Fällen zum Erfolg führt, kann über Spring Boot erreicht werden.


## Gemeinsames Deployment in einem WAR-File

Um sich über CORS keine Gedanken machen zu müssen, können wir das Backend das auch die REST-API bereitstellt auch als
Server nutzen, um unsere statischen Ressourcen auszuliefern.

Dazu wird der Client in einem eigenen Gradle-Projekt verpackt und von der Haupt-Applikation eingesammelt. 

## Gradle Build

```bash
./gradlew war
```

## Deployment des WAR im Tomcat
Zum Test verwenden wir docker. Mittels `docker run -it --rm -p 8080:8080 --name foo tomcat:9` können wir einen Tomcat-
Server starten. Nach dem Hochfahren kopieren wir das WAR-Archiv mittels der Kommandozeile als *ROOT.war*

```bash
docker cp build/libs/workshop-0.0.1-SNAPSHOT.war foo:/usr/local/tomcat/webapps/ROOT.war
```

Auf der Konsole sollte nun das Starten der Backend Anwendung zu sehen sein. Im Browser kann dann mit 
[http://localhost:8080](http://localhost:8080) zur App navigiert werden. 

Es ist auch möglich in einen anderen Kontext zu deployen, allerdings muss dann der BASE-HREF in der *package.json*
mit angepasst werden.


## Die liebe Path-Location-Strategy

Um die HTML5 Push State Navigation oder auch Path-Location-Strategy zu verwenden müssen sich Backend und Frontend
darauf verständigen, wo genau das Routing durch das Frontend beginnen soll. Der Vertrag ist das alle Routen die vom
Client bereitgestellt werden, mit einem Redirect auf die index.html antworten.

Beim Bau der Angular App wird der *base-href* mitgegeben und im Server wird ein Forwarding eingerichtet. Mit Spring
ist dies ein wenig tricky. Hier eine mögliche [Lösung](https://stackoverflow.com/a/53104682/6081477).

Die Alternative wäre das Deployment zu trennen und die Angular Anwendung von einem anderen HTTP-Server ausliefern
zu lassen. In diesem Fall muss das Backend einen CORS-Header setzen oder aber es muss ein Proxy eingerichtet werden.
Mittels diesem würde dann, analog zu dem Proxy während der Entwicklung, die Request für den Browser transparent 
weitergeleitet.


## Erweiterung des Backend um eine SQL-Datenbank

In diesem Beispiel wird zunächst nur eine H2 Inmemory Datenbank verwendet. Diese kann gegen beliebige SQL Datenbanken
ausgetauscht werden. Mit Docker lassen schnell verschiedene Systeme zur Entwicklung einrichten.


## Konfiguration von Spring-Anwendungen

Als Enterprise-Framework bietet Spring eine Vielzahl von Konfigurationsmöglichkeiten. Eine davon sind die 
*Property-Dateien*. 

```properties
spring.datasource.url= jdbc:postgresql://localhost:5432/springbootdb
spring.datasource.username=postgres
spring.datasource.password=secret
```


## Datenbank-Migration mit Flyway

Das Erzeugen und Verändern der Datenbank sollte innerhalb der Versionskontrolle erfolgen. Nur so bleibt das Schema
reproduzierbar und Änderungen nachvollziehbar. Zusätzlich können die SQL-Skripte auch für das Testen verwendet werden.

Im Ordner `src/main/resources/db/migration` werden SQL-Dateien abgelegt. Beim Start des Servers wird geprüft, ob alle
diese Migrationen auf die Datenbank angewendet worden sind. Ist dies nicht der Fall, werden diese ausgeführt. Kommt es
dabei zu einem Fehler werden die Änderungen rückgängig gemacht und der Start verweigert.


## Integration Tests mit Testcontainers

Wenn zur Entwicklung Docker eingesetzt werden kann, dann können auch Tests mit Containern im Hintergrund ausgeführt 
werden. Es ermöglicht die Erzeugung von Containern, die nur während der Ausführung der Tests existieren und anschließend
entsorgt werden. So kann das Zusammenspiel von größeren Einheiten innerhalb der Software geprüft werden. Auch Änderungen
an der Infrastruktur können simuliert werden.

Für Spring Boot existiert ein [Adapter](https://github.com/testcontainers/testcontainers-spring-boot) der allerdings
Spring Cloud voraussetzt. 

Ein einfaches Beispiel findet sich [hier](https://blog.sandra-parsick.de/2020/05/21/using-testcontainers-in-spring-boot-tests-for-database-integration-tests/)


## Authentifizierung und Autorisierung mittels JWT

Das Java Web Token kann genutzt werden, um ein signiertes Token zu verwenden. Der Vorteil zu einer reinen Session-ID
ist das per Signatur geprüft werden kann, ob das Token manipuliert wurde.

Im Payload können zum Beispiel der Name des Nutzers und seine Rollen hinterlegt werden. Sofern die Signatur gültig ist,
muss das Backend "nur" noch die Autorisierung durchführen.

Der Identity Provider kann ausgelagert werden. Ein Anbieter kann Auth0 sein. Hier ein 
[Beispiel](https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/) zur Verwendung in 
Kombination mit Spring Boot.
