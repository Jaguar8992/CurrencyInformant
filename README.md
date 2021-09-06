#CurrencyInformant

#О проекте:
    
    Данный проект обращается к сервису курсов валют и в зависимости от изменения 
    указанной вами валюты возвращает соответсвующий gif.

#Запуск

- jar файл

Для того, чтобы запустить jar выполните следующую команду в консоли находясь в текущей директории файла:
    
    java -jar CurrencyInformant-1.0-SNAPSHOT.jar

- Docker

Для создания Docker контейнера перейдите в директорию где лежит Dockerfile и выполните команду:

    docker build -t currency_informant .

Далее запустите его командой:
    
    docker run -p 8080:8080 currency_informant

Или же можно получить уже готовый Docker контейнер из моего репозитория:

    docker pull jaguar8992docker/currency_informant

И запустите его командой:

    docker run -p 8080:8080 jaguar8992docker/currency_informant

#Как открыть приложение

После запуска перейдите сюда http://localhost:8080/ и можно будет пользоваться.

#Endpoints 

    /currencies - получение кодов доступных валюх
    /gif - получение гиф изображения


