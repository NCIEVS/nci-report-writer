@echo off
rem cls
echo.
echo ******************************************
echo **** Command file to invoke build.xml ****
echo ******************************************
echo.
setlocal
@rem Environment settings here...
set DEVPROPFILE=C:\NCI-Projects\ncireportwriter-properties\properties\dev-upgrade.properties
set CIPROPFILE=C:\NCI-Projects\ncireportwriter-properties\properties\ci-upgrade.properties
set QAPROPFILE=C:\NCI-Projects\ncireportwriter-properties\properties\qa-upgrade.properties
set DATAQAPROPFILE=C:\NCI-Projects\ncireportwriter-properties\properties\data-qa-upgrade.properties
set DEBUG=-Denable.install.debug=false
set TAG=-Danthill.build.tag_built=desktop
@rem Test is debug has been set
if "%2" == "debug" (
    set DEBUG=-Denable.install.debug=true -debug
)
if "%1" == ""  (
    echo.
    echo Available targets are:
    echo.
    echo   clean                -- Remove classes directory for clean build
    echo   all                  -- Normal build of application
    echo   install              -- Builds, installs JBoss and database locally
    echo   upgrade:wdbinstall   -- Builds, upgrades JBoss and installs database locally
    echo   upgrade              -- Build and upgrade application
    echo   dev:wdbinstall       -- Builds, upgrades JBoss and installs database on DEV
    echo   dev                  -- Builds, upgrades JBoss on DEV
    echo   ci:wdbinstall        -- Builds, upgrades JBoss and install database on CI
    echo   ci                   -- Builds, upgrades JBoss on CI
    echo   qa:wdbinstall        -- Builds, upgrades JBoss and install database on QA
    echo   qa                   -- Builds, upgrades JBoss on QA
    echo   data-qa              -- Builds, upgrades JBoss on DATA-QA
    echo   deploy               -- Hot deploy application
    echo   jsp                  -- Hot deploy JSP files
    echo   stop                 -- Stop war file
    echo   start                -- Start war file
    echo   cissh                -- Test SSH login in CI
    goto DONE
)
if "%1" == "usage" (
    ant usage
    goto DONE
)
if "%1" == "all" (
    ant %TAG% build:all
    goto DONE
)
if "%1" == "upgrade" (
    ant %TAG% %DEBUG% deploy:local:upgrade
    goto DONE
)
if "%1" == "install" (
    ant %TAG% %DEBUG% deploy:local:install
    goto DONE
)
if "%1" == "deploy" (
    ant %TAG% %DEBUG% deploy:hot
    goto DONE
)
if "%1" == "stop" (
    ant %TAG% %DEBUG% deploy:stop
    goto DONE
)
if "%1" == "start" (
    ant %TAG% %DEBUG% deploy:start
    goto DONE
)
if "%1" == "jsp" (
    ant %DEBUG% deploy:hot:jsp
    goto DONE
)
if "%1" == "upgrade:wdbinstall" (
    ant -Dupgrade.target=upgrade-ncm:with-dbinstall -Ddatabase.re-create=true %TAG% %DEBUG% deploy:local:upgrade
    goto DONE
)
if "%1" == "dev" (
    ant -Dproperties.file=%DEVPROPFILE% %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
)
if not "%1" == "dev:wdbinstall" goto d1
    @rem *** Remember to set database.drop-schema=true in dev-upgrade.properties file ***
    type %DEVPROPFILE% | findstr "database.drop-schema=true" >nul
    if "%errorlevel%" == "0" goto d2
        echo Error 1:
        echo   Please set 'database.drop-schema=true' in 'dev-upgrade.properties' file
        echo   before running this command.
        echo.
        goto DONE
    :d2
    ant -Dproperties.file=%DEVPROPFILE% -Dupgrade.target=upgrade-ncm:with-dbinstall -Ddatabase.re-create=true %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
:d1
if "%1" == "ci" (
    ant -Dproperties.file=%CIPROPFILE% %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
)
if not "%1" == "ci:wdbinstall" goto c1
    @rem *** Remember to set database.drop-schema=true in ci-upgrade.properties file ***
    type %CIPROPFILE% | findstr "database.drop-schema=true" >nul
    if "%errorlevel%" == "0" goto c2
        echo Error 1:
        echo   Please set 'database.drop-schema=true' in 'ci-upgrade.properties' file
        echo   before running this command.
        echo.
        goto DONE
    :c2
    ant -Dproperties.file=%CIPROPFILE% -Dupgrade.target=upgrade-ncm:with-dbinstall -Ddatabase.re-create=true %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
:d1
if "%1" == "qa" (
    ant -Dproperties.file=%QAPROPFILE% %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
)
if "%1" == "data-qa" (
    ant -Dproperties.file=%DATAQAPROPFILE% %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
)
if not "%1" == "qa:wdbinstall" goto q1
    @rem *** Remember to set database.drop-schema=true in qa-upgrade.properties file ***
    type %QAPROPFILE% | findstr "database.drop-schema=true" >nul
    if "%errorlevel%" == "0" goto q2
        echo Error 1:
        echo   Please set 'database.drop-schema=true' in 'qa-upgrade.properties' file
        echo   before running this command.
        echo.
        goto DONE
    :q2
    ant -Dproperties.file=%QAPROPFILE% -Dupgrade.target=upgrade-ncm:with-dbinstall -Ddatabase.re-create=true %TAG% %DEBUG% deploy:remote:upgrade
    goto DONE
:q1
if "%1" == "clean" (
    ant clean
    if exist ..\target\*.* (
       rmdir /Q /S ..\target
    )
    goto DONE
)

if "%1" == "cissh" (
    ssh jboss51a@ncias-c512-v.nci.nih.gov -i C:\NCI-Projects\ncit-properties\properties\ssh-keys\id_dsa_bda echo "Test worked!"
    goto DONE
)
echo.
echo Unknown command "%1"
echo.
:DONE
endlocal