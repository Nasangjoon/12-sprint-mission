-- 1. binary_contents 테이블 (프로필 및 첨부파일 관리)
CREATE TABLE binary_contents (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    file_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes BYTEA NOT NULL
);

-- 2. users 테이블 (사용자 정보)
CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(60) NOT NULL,
    profile_id UUID,
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_profile_id UNIQUE (profile_id),
    CONSTRAINT fk_users_profile FOREIGN KEY (profile_id)
        REFERENCES binary_contents(id) ON DELETE SET NULL
);

-- 3. user_statuses 테이블 (사용자 온라인 상태)
CREATE TABLE user_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    user_id UUID NOT NULL,
    last_active_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_statuses_user_id UNIQUE (user_id),
    CONSTRAINT fk_user_statuses_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

-- 4. channels 테이블 (대화방 정보)
CREATE TABLE channels (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    name VARCHAR(100),
    description VARCHAR(500),
    type VARCHAR(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- 5. read_statuses 테이블 (채널별 마지막 읽은 시점)
CREATE TABLE read_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    user_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    last_read_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_read_statuses_user_channel UNIQUE (user_id, channel_id),
    CONSTRAINT fk_read_statuses_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_read_statuses_channel FOREIGN KEY (channel_id)
        REFERENCES channels(id) ON DELETE CASCADE
);

-- 6. messages 테이블 (메시지 내용)
CREATE TABLE messages (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    content TEXT,
    channel_id UUID NOT NULL,
    author_id UUID,
    CONSTRAINT fk_messages_channel FOREIGN KEY (channel_id)
        REFERENCES channels(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_author FOREIGN KEY (author_id)
        REFERENCES users(id) ON DELETE SET NULL
);

-- 7. message_attachments 테이블 (메시지-첨부파일 다대다 관계)
CREATE TABLE message_attachments (
    message_id UUID NOT NULL,
    attachment_id UUID NOT NULL,
    PRIMARY KEY (message_id, attachment_id),
    CONSTRAINT fk_attachments_message FOREIGN KEY (message_id)
        REFERENCES messages(id) ON DELETE CASCADE,
    CONSTRAINT fk_attachments_binary FOREIGN KEY (attachment_id)
        REFERENCES binary_contents(id) ON DELETE CASCADE
);