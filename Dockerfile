FROM openjdk:8u162-jre-slim
WORKDIR /app
COPY target/checkout-component-*.jar /app
EXPOSE 8080
CMD ["/bin/sh", "-c", "java -jar checkout-component-*.jar"]