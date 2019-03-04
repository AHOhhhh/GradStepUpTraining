# Add this arg for support muti registry
FROM nginx:1.13.6-alpine

ARG BUILD_DIR=.

# Copy source
ADD ${BUILD_DIR}/dist/public /usr/share/nginx/html
ADD ${BUILD_DIR}/nginx_conf/nginx.conf /etc/nginx/nginx.conf
ADD ${BUILD_DIR}/nginx_conf/default.conf /etc/nginx/conf.d/default.conf
ADD ${BUILD_DIR}/nginx_conf/404.js /usr/share/nginx/html

ENV LANG=en_US.UTF8
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
