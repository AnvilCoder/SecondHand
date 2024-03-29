# SecondHand <sub><sup>by [AnvilCoder](https://github.com/AnvilCoder) <img alt="AnvilCoder Logo" height="20" src="https://github.com/AnvilCoder/SecondHand/assets/124284597/d6e40489-986e-42b2-be2c-67c8087a0714" width="20"/></sup></sub>

## 📚 Оглавление

- [О проекте](#-о-проекте)
- [Технологии и библиотеки](#-технологии-и-библиотеки)
- [Команда разработчиков](#-команда-разработчиков)
- [Как запустить проект SecondHand](#-как-запустить-проект-secondhand)
- [Использование приложения](#-использование-приложения)

## 🌟 О проекте

**SecondHand** – это платформа для перепродажи вещей, где каждый может быстро и легко продать или купить товары б/у. Мы
создали это приложение, чтобы облегчить жизнь тем, кто хочет избавиться от ненужных вещей, и помочь другим найти
уникальные предложения по выгодным ценам.

## 🛠 Технологии и библиотеки

Наш проект разработан с использованием современных технологий и библиотек:

- **Spring Boot**: Ядро нашего приложения, обеспечивающее быстрый старт и удобное управление проектом.
- **Spring Data JPA**: Упрощает работу с базами данных, используя Java Persistence API.
- **Spring Security**: Обеспечивает безопасность приложения, управляя аутентификацией и авторизацией.
- **Springdoc OpenAPI**: Автоматически создаёт документацию API, делая её понятной и доступной.
- **MapStruct**: Помогает в маппинге объектов, упрощая преобразование данных между различными слоями приложения.
- **PostgreSQL**: PostgreSQL используется как основная система управления базами данных.
- **Liquibase**: Управляет версиями базы данных, позволяя безопасно вносить изменения.
- **Lombok**: Уменьшает количество шаблонного кода, автоматически генерируя стандартные методы (например, getters и
  setters).

## 👥 Команда разработчиков

### Дмитрий - Backend Developer

<img alt="Дмитрий" height="100" src="https://github.com/AnvilCoder/SecondHand/assets/124284597/9bde744c-34f9-4e3f-8350-9d0b38fd517f" width="100"/>

- **Telegram:** [Ldv236](https://t.me/Ldv236)
- **GitHub:** [Ldv236](https://github.com/Ldv236)
- **О Дмитрии:** Опытный разработчик с фокусом на бэкенд.
  Специализируется на работе с базами данных, безопасности и архитектуре приложений.

### Алексей - Backend Developer

<img alt="Алексей" height="100" src="https://github.com/AnvilCoder/SecondHand/assets/124284597/3ceecff6-c8bd-4491-9061-0366870cc1cd" width="100"/>

- **Telegram:** [DiabluSun](https://t.me/DiabluSun)
- **GitHub:** [x3imal](https://github.com/x3imal)
- **Об Алексее:** Занимается созданием API и интеграцией с фронтендом. Ведущий devOps команды.

### Александра - Backend Developer

<img alt="Александра" height="100" src="https://github.com/AnvilCoder/SecondHand/assets/124284597/25daaf71-9a02-42f6-8c57-f0d6b90f5f7a" width="100"/>

- **Telegram:** [fifimova](https://t.me/fifimova)
- **GitHub:** [fifimova](https://github.com/fifimova)
- **Об Александре:** Занимается созданием API и интеграцией с фронтендом. Имеет значительный опыт в оптимизации
  производительности.

### Атмосфера в команде

Мы верим в творческий и совместный подход к работе.
Вот скриншот из нашего Miro, который олицетворяет дух нашей команды и наш процесс работы:

<p align="center">
  <img alt="Дух нашей команды" height="300" src="https://github.com/AnvilCoder/SecondHand/assets/124284597/396d882e-31b7-400b-b64d-7231242ebc40" width="550"/>
</p>

# 🚀 Как запустить проект SecondHand

Проект "SecondHand" состоит из двух основных частей: бэкенда и фронтенда. Чтобы успешно запустить полнофункциональное
приложение, необходимо запустить обе части.

## 🔧 Настройка и запуск бэкенда

1. **Клонирование репозитория**:

  ```
  git clone https://github.com/AnvilCoder/SecondHand.git
  ```

2. **Настройка базы данных**:

- Убедитесь, что у вас установлен PostgreSQL.
- Создайте базу данных, которую будет использовать приложение.
- Настройте параметры подключения к базе данных в файле конфигурации проекта.

3. **Запуск приложения**:

- Откройте проект в вашей IDE.
- Запустите приложение, используя Spring Boot.

## 🌐 Настройка и запуск фронтенда

Фронтенд-часть сайта упакована в Docker контейнер для удобства развёртывания.

1. **Установка Docker**:

- Если у вас ещё не установлен Docker, скачайте и установите Docker Desktop по
  ссылке: [Docker Desktop](https://www.docker.com/products/docker-desktop/).

2. **Запуск фронтенда через Docker**:

- Откройте командную строку или терминал.
- Выполните следующую команду для запуска фронтенда:
  ```
  docker run -p 3000:3000 --rm ghcr.io/bizinmitya/front-react-avito:v1.21
  ```
- После выполнения команды фронтенд будет доступен на `http://localhost:3000`.

## 💻 Использование приложения

После запуска обеих частей проекта (бэкенда и фронтенда), вы сможете полноценно использовать все функции платформы "
SecondHand".
