FROM nginx:1.17.1-alpine
COPY nginx.conf.template /etc/nginx/conf.d/default.conf.template
COPY /dist/frontend /usr/share/nginx/html
CMD /bin/sh -c "envsubst '\$PORT' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf" && nginx -g