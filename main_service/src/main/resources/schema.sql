DROP TABLE IF EXISTS users, categories, locations, events, compilations, requests, compilations_events;

CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR NOT NULL UNIQUE CHECK (LENGTH(email) > 5 and LENGTH(email) <= 254),
    name VARCHAR NOT NULL CHECK (LENGTH(name) > 1 and LENGTH(name) <=250 )
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL
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
    annotation VARCHAR NOT NULL CHECK (LENGTH(annotation) >= 20 and LENGTH(annotation) <= 2000),
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    description VARCHAR NOT NULL CHECK (LENGTH(description) >= 20 and LENGTH(description) <= 7000),
    event_date TIMESTAMP WITHOUT TIME ZONE,
    paid BOOLEAN DEFAULT FALSE,
    participant_limit INTEGER DEFAULT 0,
    published_on TIMESTAMP,
    request_moderation BOOLEAN DEFAULT FALSE,
    confirmed_requests BIGINT DEFAULT 0,
    views  BIGINT,
    state VARCHAR(20) DEFAULT 'PENDING',
    title VARCHAR(120) NOT NULL CHECK (LENGTH(title) > 2),
    category_id BIGINT REFERENCES categories (id),
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    location_id BIGINT REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    status VARCHAR(255) NOT NULL,
    event_id BIGINT REFERENCES events (id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE,
    event_id BIGINT REFERENCES events (id) ON DELETE CASCADE
);