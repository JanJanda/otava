FROM node:18.18.0

WORKDIR /usr/src/otava
COPY . .

WORKDIR client
RUN npm run build

WORKDIR ../server
RUN npm ci

EXPOSE 80
CMD [ "node", "index.js" ]
