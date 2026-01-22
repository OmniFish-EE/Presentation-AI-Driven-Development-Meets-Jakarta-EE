-- Create keycloak database for authentication
CREATE DATABASE keycloak;

-- Create sample data for people
CREATE TABLE IF NOT EXISTS people (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100)
);

INSERT INTO people (first_name, last_name) VALUES
    ('John', 'Doe'),
    ('Jane', 'Smith'),
    ('Bob', 'Johnson')
ON CONFLICT DO NOTHING;
