FROM alpine:3.22.2

RUN apk update && apk upgrade --no-cache \
    && apk add --no-cache nginx pcre2=10.46-r0 \
    && rm -rf /var/cache/apk/*

COPY . /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
