FROM coney/serverjre:8

WORKDIR /app

COPY build/libs/@@SERVICE@@-1.0.0.jar /app

EXPOSE 8099

CMD ["java", "-jar", "/app/@@SERVICE@@-1.0.0.jar"]
