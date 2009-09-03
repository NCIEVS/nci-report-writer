-- Create DB instance (Example)
--
-- ------------------------------------------------------

CREATE DATABASE IF NOT EXISTS reportwriter;
CREATE USER reportwriteruser@localhost IDENTIFIED BY 't3l1m3m0r';
CREATE USER reportwriteruser@'%' IDENTIFIED BY 't3l1m3m0r';
GRANT ALL PRIVILEGES ON reportwriter.* TO reportwriteruser@localhost;
GRANT ALL PRIVILEGES ON reportwriter.* TO reportwriteruser@'%';
COMMIT;
