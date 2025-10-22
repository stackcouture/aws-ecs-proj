# Use a stable Alpine base image
FROM alpine:3.22.2

# Update Alpine and install essential packages with security fixes
RUN apk update && apk upgrade --no-cache \
    && apk add --no-cache nginx pcre2=10.46-r0 \
    && rm -rf /var/cache/apk/*

# Copy your static site files
COPY . /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
