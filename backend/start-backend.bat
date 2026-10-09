@echo off
cd /d "%~dp0"
mvn package -DskipTests
java -jar target\smarter-1.0.0.jar
pause