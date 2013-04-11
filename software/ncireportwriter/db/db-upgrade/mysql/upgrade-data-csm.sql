/*L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
L*/

-- Upgrade CSM database
--
-- ------------------------------------------------------

USE @database.name@;

--
-- Perform Database Upgrade
--

select "Starting CSM database upgrade." as ctest_text;
