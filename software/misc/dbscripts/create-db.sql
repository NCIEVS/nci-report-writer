-- Create DB instance (Example)
--
-- ------------------------------------------------------

CREATE DATABASE IF NOT EXISTS reportwriter;
CREATE USER reportwriteruser@localhost IDENTIFIED BY 'rwexamplepassword';
CREATE USER reportwriteruser@'%' IDENTIFIED BY 'rwexamplepassword';
GRANT ALL PRIVILEGES ON reportwriter.* TO reportwriteruser@localhost;
GRANT ALL PRIVILEGES ON reportwriter.* TO reportwriteruser@'%';
COMMIT;
