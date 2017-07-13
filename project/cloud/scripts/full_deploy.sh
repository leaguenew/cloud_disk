cd /data/echoii/source/echoii/
svn up
mvn clean
mvn compile
mvn war:war
cd target
cp echoii.war /data/echoii/apache-tomcat-7.0.32/webapps/
tail -f /data/echoii/apache-tomcat-7.0.32/logs/catalina.out
