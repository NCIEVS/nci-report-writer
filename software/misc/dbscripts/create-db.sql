-- Create DB instance (Example)
--
-- ------------------------------------------------------

CREATE DATABASE IF NOT EXISTS ncirw;
CREATE USER ncirwuser@localhost IDENTIFIED BY 'ncirwuser';
CREATE USER ncirwuser@'%' IDENTIFIED BY 'ncirwuser';
GRANT ALL PRIVILEGES ON ncirw.* TO ncirwuser@localhost;
GRANT ALL PRIVILEGES ON ncirw.* TO ncirwuser@'%';