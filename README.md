# Notification Inbox Handler

Микросервис обработки уведомлений.

## Стек технологий

- Java 21
- Spring Boot 3
- Apache Kafka
- PostgreSQL

## Функционал

Сервис получает сообщения из Kafka топиков:

- sms-events
- email-events
- push-events
- telegram-events

### Inbox Pattern

Каждое сообщение сохраняется в таблицу inbox для гарантированной обработки.

### Idempotency

Проверяется уникальность сообщений по key + payload hash.

### Scheduler

Периодически обрабатывает сообщения из inbox таблиц.

## Kafka Consumers

Отдельный listener для каждого топика:

- SMS
- Email
- Push
- Telegram