FROM openjdk:15

EXPOSE 8080

RUN mkdir ./app

COPY ./CurrencyInformant-1.0-SNAPSHOT.jar ./app

<<<<<<< HEAD
CMD java -jar ./app/CurrencyInformant-1.0-SNAPSHOT.jar
=======
CMD java -jar ./app/CurrencyInformant-1.0-SNAPSHOT.jar
>>>>>>> c1c9e513274c9deb9a4eaf7398fe899ea376e2ab
