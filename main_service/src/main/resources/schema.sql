CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR NOT NULL UNIQUE,
    name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS locations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat REAL NOT NULL,
    lon REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    description VARCHAR NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    paid BOOLEAN DEFAULT FALSE,
    participant_limit INTEGER DEFAULT 0,
    published_on TIMESTAMP,
    request_moderation BOOLEAN DEFAULT FALSE,
    confirmed_requests BIGINT,
    views  BIGINT,
    state VARCHAR DEFAULT 'PENDING',
    title VARCHAR NOT NULL,
    category_id BIGINT REFERENCES categories (id),
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    location_id BIGINT REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    status VARCHAR NOT NULL,
    event_id BIGINT REFERENCES events (id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN NOT NULL,
    title VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE,
    event_id BIGINT REFERENCES events (id) ON DELETE CASCADE
);