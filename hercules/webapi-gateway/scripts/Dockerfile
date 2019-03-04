FROM coney/serverjre:8

WORKDIR /app

COPY build/libs/@@SERVICE@@-1.0.0.jar /app

EXPOSE 8080

ENV LANG=en_US.UTF8

CMD ["java", "-jar", "/app/@@SERVICE@@-1.0.0.jar"]
