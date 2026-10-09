-- Spending Smarter Database Schema
-- Run this manually if auto-creation fails

CREATE DATABASE IF NOT EXISTS spending_smarter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE spending_smarter;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    monthly_income DECIMAL(12,2),
    currency VARCHAR(3) DEFAULT 'USD',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    icon_name VARCHAR(50),
    color VARCHAR(7),
    is_default BOOLEAN DEFAULT FALSE,
    type ENUM('EXPENSE', 'INCOME') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Expenses table
CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    expense_date DATE NOT NULL,
    description TEXT,
    is_recurring BOOLEAN DEFAULT FALSE,
    recurring_frequency ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

-- Incomes table
CREATE TABLE IF NOT EXISTS incomes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    amount DECIMAL(12,2) NOT NULL,
    income_date DATE NOT NULL,
    description TEXT,
    is_recurring BOOLEAN DEFAULT FALSE,
    recurring_frequency ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- Budgets table
CREATE TABLE IF NOT EXISTS budgets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    spent_amount DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

-- Indexes for better query performance
CREATE INDEX idx_expenses_user_date ON expenses(user_id, expense_date);
CREATE INDEX idx_incomes_user_date ON incomes(user_id, income_date);
CREATE INDEX idx_budgets_user_dates ON budgets(user_id, start_date, end_date);
CREATE INDEX idx_categories_type ON categories(type);

-- Insert default categories
INSERT IGNORE INTO categories (name, icon_name, color, is_default, type) VALUES
-- Expense categories
('Food & Dining', 'utensils', '#FF6B6B', TRUE, 'EXPENSE'),
('Transportation', 'car', '#4ECDC4', TRUE, 'EXPENSE'),
('Shopping', 'shopping-bag', '#45B7D1', TRUE, 'EXPENSE'),
('Entertainment', 'film', '#96CEB4', TRUE, 'EXPENSE'),
('Bills & Utilities', 'file-text', '#FFEAA7', TRUE, 'EXPENSE'),
('Healthcare', 'heart', '#DDA0DD', TRUE, 'EXPENSE'),
('Education', 'book', '#98D8C8', TRUE, 'EXPENSE'),
('Travel', 'plane', '#F7DC6F', TRUE, 'EXPENSE'),
('Personal Care', 'smile', '#BB8FCE', TRUE, 'EXPENSE'),
('Other', 'more-horizontal', '#85C1E9', TRUE, 'EXPENSE'),
-- Income categories
('Salary', 'briefcase', '#2ECC71', TRUE, 'INCOME'),
('Freelance', 'laptop', '#27AE60', TRUE, 'INCOME'),
('Investments', 'trending-up', '#1ABC9C', TRUE, 'INCOME'),
('Gifts', 'gift', '#16A085', TRUE, 'INCOME'),
('Other', 'more-horizontal', '#138D75', TRUE, 'INCOME');