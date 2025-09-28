\c
database_name

-- Enable PostGIS extension
CREATE
EXTENSION IF NOT EXISTS postgis;


CREATE TABLE app_user
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    username      VARCHAR(100) NOT NULL UNIQUE,
    first_name    VARCHAR(100),
    last_name     VARCHAR(100),
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);


CREATE TABLE role
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE -- ADMIN, MODERATOR, USER
);

-- для багатьох ролей (наприклад user + premium, user + tester)
CREATE TABLE user_role
(
    user_id UUID NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    role_id INT  NOT NULL REFERENCES role (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);


CREATE TABLE water_closet
(
    id                     UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name                   VARCHAR(255) NOT NULL,
    description            TEXT,
    location               GEOGRAPHY(Point, 4326) NOT NULL,
    created_by             UUID         NOT NULL REFERENCES app_user (id),
    is_free                BOOLEAN      NOT NULL DEFAULT TRUE,
    accessibility_features JSONB,
    status                 VARCHAR(20)  NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'ACTIVE', 'REJECTED', 'CLOSED')),
    created_at             TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at             TIMESTAMPTZ  NOT NULL DEFAULT now()
);


CREATE TABLE opening_hours
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    water_closet_id UUID     NOT NULL REFERENCES water_closet (id) ON DELETE CASCADE,
    day_of_week     SMALLINT NOT NULL CHECK (day_of_week BETWEEN 0 AND 6), -- 0 = Monday, 6 = Sunday
    open_time       TIME     NOT NULL,
    close_time      TIME     NOT NULL
);

-- Індекс для швидкого пошуку годин за туалетом
CREATE INDEX idx_opening_hours_wc ON opening_hours (water_closet_id);


CREATE TABLE comment
(
    id              UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    water_closet_id UUID        NOT NULL REFERENCES water_closet (id) ON DELETE CASCADE,
    user_id         UUID        NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    text            TEXT        NOT NULL,
    rating          SMALLINT CHECK (rating >= 1 AND rating <= 5),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);


CREATE TABLE photo
(
    id              UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    water_closet_id UUID REFERENCES water_closet (id) ON DELETE CASCADE,
    comment_id      UUID REFERENCES comment (id) ON DELETE CASCADE,
    image_data      BYTEA       NOT NULL,
    is_primary      BOOLEAN     NOT NULL DEFAULT FALSE,
    uploaded_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_photo_water_closet_comment CHECK (
        (water_closet_id IS NOT NULL) OR (comment_id IS NOT NULL)
        )
);


CREATE TABLE draft_photo
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    uploaded_by UUID        NOT NULL REFERENCES app_user (id),
    image_data  BYTEA       NOT NULL,
    uploaded_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at  TIMESTAMPTZ NOT NULL
);


CREATE TABLE queue_presence
(
    id              UUID PRIMARY KEY,
    water_closet_id UUID                     NOT NULL REFERENCES water_closet (id) ON DELETE CASCADE,
    user_id         UUID                     NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    entered_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    last_seen_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);


CREATE INDEX idx_water_closet_location ON water_closet USING GIST (location);
CREATE INDEX idx_comment_water_closet ON comment (water_closet_id);
CREATE INDEX idx_photo_water_closet ON photo (water_closet_id);
CREATE INDEX idx_queue_presence_water_closet_time ON queue_presence (water_closet_id, last_seen_at DESC);
