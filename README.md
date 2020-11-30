### Примечания
- Перед сборкой приложения, в файле src/main/resources/application.yml переменные kafka.bootstrap-servers  
и cassandra.connection-host меняем на свои.  
- Скрипт создания таблицы cassandra:  
CREATE KEYSPACE linux_logs WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 1};  
USE linux_logs;  
CREATE TABLE logs (id int PRIMARY KEY, datetime timestamp, hostname text, process text, message text, priority int);  
- Сборка приложения: mvn clean install  
- Запуск приложения: java -jar target/sbloghandler-0.0.1-SNAPSHOT.jar
- Отправка логов: POST -x http://localhost:8082/app/send?filePath=<путь до файла с логами>  
- Получение статистики: GET -x http://localhost:8082/app/compute  

#### На всякий случай  
установка Kafka с помощью docker:  
 - git clone https://github.com/wurstmeister/kafka-docker  
 - в файле docker-compose.yml убираем переменную KAFKA_ADVERTISED_HOST_NAME,  
 вместо неё вписываем: HOSTNAME_COMMAND: "route -n | awk '/UG[ \t]/{print $$2}'"  
 - docker-compose up -d  
 - проверяем старт контейнеров командой docker ps