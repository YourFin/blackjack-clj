FROM clojure
COPY . /app
WORKDIR /app
RUN lein deps
CMD ["lein", "run"]
