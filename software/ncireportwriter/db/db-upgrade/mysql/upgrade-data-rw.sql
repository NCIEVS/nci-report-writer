-- Upgrade Report Writer database
--
-- ------------------------------------------------------

USE @database.name@;

--
-- Perform Database Upgrade
--

select "Starting RW database upgrade." as ctest_text;

UPDATE csm_user SET LAST_NAME='Test 200' WHERE LOGIN_NAME='rwadmin';