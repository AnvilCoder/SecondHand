# SecondHand <sub><sup>by [AnvilCoder](https://github.com/AnvilCoder) <img alt="AnvilCoder Logo" height="20" src="F:\несистемная папка\AnvilCoder\photo_2023-06-15_23-01-57.jpg" width="20"/></sup></sub>

## 📚 Оглавление

- [О проекте](#-о-проекте)
- [Технологии и библиотеки](#-технологии-и-библиотеки)
- [Команда разработчиков](#-команда-разработчиков)
- [Как запустить проект](#-как-запустить-проект)
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

<img alt="Дмитрий" height="100" src="F:\несистемная папка\AnvilCoder\photo_2023-08-30_19-22-09.jpg" width="100"/>

- **Telegram:** [Burko20](https://t.me/Burko20)
- **GitHub:** [Ldv236](https://github.com/Ldv236)
- **О Дмитрии:** Опытный разработчик с фокусом на бэкенд.
  Специализируется на безопасности и архитектуре приложений. Вносит ключевой вклад в разработку безопасной и надежной
  системы.

### Алексей - Backend Developer

<img alt="Алексей" height="100" src="F:\несистемная папка\AnvilCoder\photo_2023-10-26_17-44-38.jpg" width="100"/>

- **Telegram:** [DiabluSun](https://t.me/DiabluSun)
- **GitHub:** [x3imal](https://github.com/x3imal)
- **О Алексее:** Занимается созданием API и интеграцией с фронтендом. Ведущий devOps команды.

### Александра - Backend Developer

<img alt="Александра" height="100" src="F:\несистемная папка\AnvilCoder\photo_2023-12-29_09-46-24.jpg" width="100"/>

- **Telegram:** [fifimova](https://t.me/fifimova)
- **GitHub:** [fifimova](https://github.com/fifimova)
- **О Александре:** Занимается созданием API и интеграцией с фронтендом. Имеет значительный опыт в оптимизации
  производительности.

### Атмосфера в команде

Мы верим в творческий и совместный подход к работе.
Вот скриншот из нашего Miro, который олицетворяет дух нашей команды и наш процесс работы:

<p align="center">
  <img alt="Дух нашей команды" height="300" src="F:\несистемная папка\AnvilCoder\photo_2023-12-29_02-30-51.jpg" width="550"/>
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
