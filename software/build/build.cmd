@echo off
rem cls
echo.
echo ******************************************
echo **** Command file to invoke build.xml ****
echo ******************************************
echo.
setlocal
set DEVPROPFILE=C:\NCI-Projects\ncireportwriter-properties\properties\dev-upgrade.properties
set CIPROPFILE=C:\NCI-Projects\ncireportwriter-properties\properties\ci-upgrade.properties
set QAPROPFILE=C:\NCI-Projects\ncireportwriter-properties\properties\qa-upgrade.properties
set DATAQAPROPFILE=C:\NCI-Projects\ncireportwriter-properties\properties\data-qa-upgrade.properties
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
    echo   deploy               -- Redeploy application locally
    goto DONE
)
if "%1" == "usage" (
    ant usage
    goto DONE
)
if "%1" == "all" (
    ant build:all
    goto DONE
)
if "%1" == "install" (
    ant deploy:local:install
    goto DONE
)
if "%1" == "upgrade" (
    ant deploy:local:upgrade
    goto DONE
)
if "%1" == "deploy" (
    ant deploy:hot
    goto DONE
)
if "%1" == "upgrade:wdbinstall" (
    ant -Dupgrade.target=upgrade-ncm:with-dbinstall -Denable.install.debug=true -Ddatabase.re-create=true deploy:local:upgrade
    goto DONE
)
if "%1" == "dev" (
    ant -Dproperties.file=%DEVPROPFILE% -Danthill.build.tag_built=desktop deploy:remote:upgrade
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
    ant -Dproperties.file=%DEVPROPFILE% -Danthill.build.tag_built=desktop -Dupgrade.target=upgrade-ncm:with-dbinstall -Ddatabase.re-create=true deploy:remote:upgrade
    goto DONE
:d1
if "%1" == "ci" (
    ant -Dproperties.file=%CIPROPFILE% -Danthill.build.tag_built=desktop deploy:remote:upgrade
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
    ant -Dproperties.file=%CIPROPFILE% -Danthill.build.tag_built=desktop -Dupgrade.target=upgrade-ncm:with-dbinstall -Ddatabase.re-create=true deploy:remote:upgrade
    goto DONE
:d1
if "%1" == "qa" (
    ant -Dproperties.file=%QAPROPFILE% -Danthill.build.tag_built=desktop deploy:remote:upgrade
    goto DONE
)
if "%1" == "data-qa" (
    ant -Dproperties.file=%DATAQAPROPFILE% -Danthill.build.tag_built=desktop deploy:remote:upgrade
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
    ant -Dproperties.file=%QAPROPFILE% -Danthill.build.tag_built=desktop -Dupgrade.target=upgrade-ncm:with-dbinstall -Ddatabase.re-create=true deploy:remote:upgrade
    goto DONE
:q1
if "%1" == "clean" (
    ant clean
    if exist ..\target\*.* (
       rmdir /Q /S ..\target
    )
    goto DONE
)
echo.
echo Unknown command "%1"
echo.
:DONE
endlocal