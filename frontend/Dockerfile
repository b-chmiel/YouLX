FROM node:16.17.1-alpine3.15 as build
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY . /app
ARG configuration=production
RUN npm run build -- --outputPath=./dist/out --configuration $configuration

FROM nginx:1.23.1-alpine
COPY --from=build /app/dist/out/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
CMD ["/bin/sh", "-c", "envsubst '${PORT},${HEROKU_APP_BACKEND_URL}' < /etc/nginx/conf.d/default.conf | tee /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"] 