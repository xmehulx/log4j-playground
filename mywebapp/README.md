# log4j-playground

Setup:
1) Ubuntu Server
2) Apache Tomcat 5.5.36
3) Log4j 2.12
4) JDK 7u80


```shell
javac -cp ".:WEB-INF/lib/*" -d WEB-INF/classes/ src/main/java/com/example/LogExampleServlet.java  -nowarn
```
```shell
jar cvf mywebapp.war *
```

## Attacker Machine

### Payload

```Shell
/PATH/TO/jdk1.7.0_80/bin/javac Evail.java -Xlint:deprecation
```
### LDAP Server 
```shell
java -cp .:/opt/unboundid-ldapsdk-7.0.0/unboundid-ldapsdk.jar Server
```
