FROM coney/serverjre:8

WORKDIR /app

COPY build/libs/@@SERVICE@@-1.0.0.jar /app

EXPOSE 8001

ENV LANG=en_US.UTF8
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone


CMD ["java", "-jar", "/app/@@SERVICE@@-1.0.0.jar"]
