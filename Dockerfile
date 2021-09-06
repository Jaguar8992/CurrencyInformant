FROM openjdk:11

EXPOSE 8080

RUN mkdir ./app

COPY ./CurrencyInformant-1.0-SNAPSHOT.jar ./app

CMD java -jar ./app/CurrencyInformant-1.0-SNAPSHOT.jar
