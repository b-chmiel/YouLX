FROM node as build
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY . /app
ARG configuration=production
RUN npm run build -- --outputPath=./dist/out --configuration $configuration

FROM nginx:1.17.1-alpine
COPY --from=build /app/dist/out/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
CMD ["/bin/sh", "-c", "envsubst '${PORT},${HEROKU_APP_BACKEND_URL}' < /etc/nginx/nginx.conf | tee /etc/nginx/nginx.conf && nginx -g 'daemon off;'"] 