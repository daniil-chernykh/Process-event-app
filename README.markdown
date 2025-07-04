# Руководство по запуску проекта

## Описание проекта
Проект состоит из двух микросервисов:
- **`event-generator`**: Генератор событий, который отправляет запросы через REST API.
- **`event-processor`**: Обработчик событий, который анализирует и сохраняет инциденты на основе полученных событий.

Оба сервиса запускаются с использованием Docker Compose в контейнерах на базе Java 21.

## Требования
- **Установленное ПО**:
  - Docker (версия 20.10 или выше).
  - Docker Compose (встроено в Docker Desktop или установлено отдельно, версия 2.0 или выше).
  - Maven (версия 3.6 или выше) для сборки проекта.
  - Git (опционально, для клонирования репозитория, если применимо).
- **Операционная система**: Windows, macOS или Linux.
- **Интернет-соединение**: Для загрузки Docker-образов.

## Структура проекта
```
process-event-app/
├── event-generator/
│   ├── src/
│   ├── pom.xml
│   ├── target/
│   └── Dockerfile
├── event-processor/
│   ├── src/
│   ├── pom.xml
│   ├── target/
│   └── Dockerfile
├── docker-compose.yaml
└── README.md
```

## Инструкции по запуску

### Шаг 1: Клонирование или открытие проекта
1. Если проект находится в репозитории (например, Git):
   ```bash
   git clone <URL_репозитория>
   cd process-event-app
   ```
2. Если проект уже на вашем компьютере, откройте терминал и перейдите в корневую директорию проекта:
   ```bash
   cd process-event-app
   ```

### Шаг 2: Сборка проекта
1. Убедитесь, что Maven установлен. Проверьте версию:
   ```bash
   mvn -version
   ```
2. Выполните сборку обоих модулей:
   ```bash
   mvn clean package
   ```
   - Эта команда соберет `event-generator` и `event-processor`, создав файлы `target/event-generator-0.0.1-SNAPSHOT.jar` и `target/event-processor-0.0.1-SNAPSHOT.jar`.
   - Если сборка завершилась с ошибкой, проверьте логи и убедитесь, что `pom.xml` в каждом модуле настроен корректно (содержит зависимости и плагины Spring Boot).

### Шаг 3: Запуск проекта с помощью Docker Compose
1. Убедитесь, что Docker и Docker Compose запущены. Проверьте статус:
   ```bash
   docker --version
   docker-compose --version
   ```
2. Запустите контейнеры из корневой директории:
   ```bash
   docker-compose up --build
   ```
   - Флаг `--build` гарантирует, что Docker пересоберет образы с учетом новых JAR-файлов.
   - Ожидайте, пока контейнеры запустятся. Вы увидите логи в терминале.

3. Если контейнеры уже запускались ранее, очистите старые образы:
   ```bash
   docker-compose down --rmi all
   ```
   Затем повторите команду `docker-compose up --build`.

### Шаг 4: Проверка работы
1. Откройте браузер или используйте инструмент вроде Postman:
   - **Generator**: Перейдите по адресу `http://localhost:8080/swagger-ui.html` для отправки событий через REST API (эндпоинт `/api/events`).
   - **Processor**: Перейдите по адресу `http://localhost:8081/swagger-ui.html` для проверки списка инцидентов (эндпоинт `/api/incidents` с параметрами `page`, `size`, `sortBy`, `direction`).
2. Пример запроса для отправки события (через Swagger или Postman):
   - Метод: `POST`
   - URL: `http://localhost:8080/api/events`
   - Тело (JSON):
     ```json
     {
       "id": "550e8400-e29b-41d4-a716-446655440000",
       "type": "TYPE_1",
       "time": "2025-06-24T18:00:00"
     }
     ```
3. Пример запроса для получения инцидентов:
   - Метод: `GET`
   - URL: `http://localhost:8081/api/incidents?page=0&size=10&sortBy=time&direction=desc`
   - Ожидаемый ответ будет содержать список инцидентов с полем `events`.

### Шаг 5: Остановка проекта
1. Для остановки контейнеров нажмите `Ctrl+C` в терминале.
2. Для полного удаления контейнеров и образов выполните:
   ```bash
   docker-compose down --rmi all
   ```

## Устранение неполадок
- **Ошибка сборки Maven**: Проверьте логи вывода `mvn clean package`. Убедитесь, что зависимости (Spring Boot, JPA, MapStruct) указаны в `pom.xml`.
- **Ошибка Docker**: Если появляется сообщение об отсутствии JAR-файла (например, `not found`), выполните сборку заново:
  ```bash
  cd event-generator && mvn clean package && cd ..
  cd event-processor && mvn clean package && cd ..
  docker-compose up --build
  ```
- **Проблемы с подключением**: Убедитесь, что порты 8080 и 8081 не заняты другими приложениями. Измените порты в `docker-compose.yaml`, если необходимо.