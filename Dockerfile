FROM openjdk:8-jre-alpine

RUN apk add tzdata

RUN cp /usr/share/zoneinfo/Etc/GMT+3 /etc/localtime

ADD target/*.jar /app.jar


CMD java -jar /app.jar

EXPOSE 8080
