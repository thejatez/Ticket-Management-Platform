-- =========================
-- DATABASE
-- =========================
CREATE DATABASE IF NOT EXISTS ticket_db;
USE ticket_db;



-- =========================
-- USERS
-- =========================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);



-- =========================
-- TICKETS
-- =========================
CREATE TABLE tickets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(30),
    created_by BIGINT,

    CONSTRAINT fk_ticket_user
    FOREIGN KEY (created_by)
    REFERENCES users(id)
    ON DELETE CASCADE
);



-- =========================
-- AUDIT LOGS
-- =========================
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    action VARCHAR(100),
    resource_type VARCHAR(100),
    resource_id BIGINT,
    timestamp DATETIME
);



-- =========================
-- INDEXES (performance)
-- =========================
CREATE INDEX idx_ticket_user ON tickets(created_by);
CREATE INDEX idx_audit_resource ON audit_logs(resource_id);



-- =========================
-- SAMPLE DATA
-- Password = admin123 (bcrypt encoded)
-- =========================

INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$Dow1P2o9U6Dbb9ZKJx1O1e6q5y6s8K4C5b6QJmW0bK4a9G7y6Qh3K', 'ADMIN'),
('user1', '$2a$10$Dow1P2o9U6Dbb9ZKJx1O1e6q5y6s8K4C5b6QJmW0bK4a9G7y6Qh3K', 'USER');



-- =========================
-- SAMPLE TICKETS
-- =========================
INSERT INTO tickets (title, description, status, created_by) VALUES
('Login issue', 'Cannot login to system', 'OPEN', 2),
('Page crash', 'Dashboard crashes sometimes', 'IN_PROGRESS', 2);
