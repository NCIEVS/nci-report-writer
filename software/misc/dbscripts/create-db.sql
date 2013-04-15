/*L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L*/

-- Create DB instance (Example)
--
-- ------------------------------------------------------

CREATE DATABASE IF NOT EXISTS reportwriter;
CREATE USER reportwriteruser@localhost IDENTIFIED BY 'rwexamplepassword';
CREATE USER reportwriteruser@'%' IDENTIFIED BY 'rwexamplepassword';
GRANT ALL PRIVILEGES ON reportwriter.* TO reportwriteruser@localhost;
GRANT ALL PRIVILEGES ON reportwriter.* TO reportwriteruser@'%';
COMMIT;
