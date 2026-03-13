CREATE TABLE IF NOT EXISTS sms_inbox (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    message_key VARCHAR(36) NOT NULL,
    payload TEXT NOT NULL,
    payload_hash VARCHAR(64) NOT NULL,
    processed BOOLEAN DEFAULT FALSE,
    attempt_count INTEGER DEFAULT 0,
    last_error TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    phone_number VARCHAR(20),
    sender VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS email_inbox (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    message_key VARCHAR(36) NOT NULL,
    payload TEXT NOT NULL,
    payload_hash VARCHAR(64) NOT NULL,
    processed BOOLEAN DEFAULT FALSE,
    attempt_count INTEGER DEFAULT 0,
    last_error TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    recipient_email VARCHAR(255),
    subject VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS push_inbox (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    message_key VARCHAR(36) NOT NULL,
    payload TEXT NOT NULL,
    payload_hash VARCHAR(64) NOT NULL,
    processed BOOLEAN DEFAULT FALSE,
    attempt_count INTEGER DEFAULT 0,
    last_error TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    device_token VARCHAR(255),
    app_id VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS telegram_inbox (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    message_key VARCHAR(36) NOT NULL,
    payload TEXT NOT NULL,
    payload_hash VARCHAR(64) NOT NULL,
    processed BOOLEAN DEFAULT FALSE,
    attempt_count INTEGER DEFAULT 0,
    last_error TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    chat_id VARCHAR(100),
    parse_mode VARCHAR(20)
);

CREATE INDEX idx_sms_key_hash ON sms_inbox(message_key, payload_hash);
CREATE INDEX idx_sms_unprocessed ON sms_inbox(processed, created_at) WHERE processed = FALSE;

CREATE INDEX idx_email_key_hash ON email_inbox(message_key, payload_hash);
CREATE INDEX idx_email_unprocessed ON email_inbox(processed, created_at) WHERE processed = FALSE;

CREATE INDEX idx_push_key_hash ON push_inbox(message_key, payload_hash);
CREATE INDEX idx_push_unprocessed ON push_inbox(processed, created_at) WHERE processed = FALSE;

CREATE INDEX idx_telegram_key_hash ON telegram_inbox(message_key, payload_hash);
CREATE INDEX idx_telegram_unprocessed ON telegram_inbox(processed, created_at) WHERE processed = FALSE;