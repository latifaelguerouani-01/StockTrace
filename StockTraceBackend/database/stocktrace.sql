-- Run this ONCE if your current stocktrace database was created by an older backend.
-- WARNING: this deletes the old StockTrace database.
DROP DATABASE IF EXISTS stocktrace;
CREATE DATABASE stocktrace CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
use stocktrace;