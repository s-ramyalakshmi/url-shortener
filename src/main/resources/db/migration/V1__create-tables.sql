CREATE TABLE IF NOT EXISTS url_mapping (
    id SERIAL PRIMARY KEY,
    short_url VARCHAR(255) NOT NULL,
    long_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_date TIMESTAMP
);

CREATE UNIQUE INDEX idx_short_url ON url_mapping (short_url);