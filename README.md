# Программные сущности
Основные сущности приложения - предмет (Item) и коробка (Box):
- Ящики могут быть пустыми или содержать предметы или другие ящики;
- У каждого ящика и предмета есть id;
- ID какого-либо предмета и какого-либо ящика могут совпадать, но в совокупности предметов они уникальны (как и в совокупности ящиков);
- Предметы могут не иметь цвета;
- Предметы могут быть не в ящике
- Вложенность ящиков может быть любой;

# Входные данные 
На вход в виде параметра командной строки приложению передается ссылка на XML-файл, в котором задано взаимное положение предметов (Item) и ящиков (Box).

Пример такого файла:

<?xml version="1.0" encoding="UTF-8"?>
<Storage>
 <Box id="1">
   <Item id="1"/>
   <Item color="red" id="2"/>
   <Box id="3">
       <Item id="3" color="red" />
       <Item id="4" color="black" />    
   </Box>
   <Box id="6"/>
   <Item id="5"/>
 </Box>
 <Item id="6"/>
</Storage>

Ссылка имеет следующий формат: type:path, где:

- type - тип ссылки,
- path - путь к файлу

Ссылка определяет источник, из которого загружаются данные в XML-формате.

Тип ссылки (type): 
- file (внешний файл)
- classpath (файл в classpath)
- url (URL)

Примеры:
- file:input.xml
- classpath:input.xml
- url:file:/input.xml

# Требования к системе

1) При старте приложение создается таблицы по указанной ниже схеме и заполняет их данными в соответствии с переданным XML-файлом:

CREATE TABLE BOX
(ID INTEGER PRIMARY KEY,
CONTAINED_IN INTEGER
);

CREATE TABLE ITEM
(ID INTEGER PRIMARY KEY,
CONTAINED_IN INTEGER REFERENCES BOX(ID),
COLOR VARCHAR(100)
);

Выбор СУБД остается на ваше усмотрение. Можно также использовать in-memory базу (например,  H2 или SQLite)
В случае использования in-memory СУБД нужно записать в файл содержимое таблиц после их заполнения.

2) После загрузки файла приложение должно работать, как REST-сервис, который возвращает id предметов
заданного цвета (Color), содержащиеся в ящике c заданным идентификатором (Box)
с учётом того, что в ящике может быть ещё ящик с предметами требуемого цвета.

Например, на POST-запрос с телом запроса в JSON вида:

POST /test HTTP/1.1
Host: localhost
Accept: application/json
Content-Type:application/json
Content-Length: 25
{"box":"1","color":"red"}

для вышеприведённого XML должен быть ответ вида:

HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 01 Sep 2019 12:00:26 GMT
[2,3]

# Решение
Использовались следующие библиотеки:

org.xerial/sqlite-jdbc - для работы с локальной БД SQLite
org.apache.cxf/cxf-rt-frontend-jaxrs - для работы с XML
org.springframework.boot/spring-boot-starter-web - для создания web-сервиса

Пример работы:
![alt text](https://github.com/Gluk87/boxitemtask/blob/main/Screens/Screen1.png)
![alt text](https://github.com/Gluk87/boxitemtask/blob/main/Screens/Screen2.png)

Добавление строк в таблицы логируется:
![alt text](https://github.com/Gluk87/boxitemtask/blob/main/Screens/Screen3.png)
