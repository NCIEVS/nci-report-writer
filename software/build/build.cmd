@echo off
cls
echo.
echo ******************************************
echo **** Command file to invoke build.xml ****
echo ******************************************
echo.
setlocal
set DEVPROPFILE=C:\SVN-Projects\ncireportwriter-properties\properties\dev-upgrade.properties
set QAPROPFILE=C:\SVN-Projects\ncireportwriter-properties\properties\qa-upgrade.properties
if "%1" == ""  (
    echo.
    echo Available targets are:
    echo.
    echo   1. clean                -- Remove classes directory for clean build
    echo   2. all                  -- Normal build of application
    echo   3. install              -- Builds, installs JBoss and database locally
    echo   4. upgrade:wdbinstall   -- Builds, upgrades JBoss and installs database locally
    echo   5. upgrade              -- Build and upgrade application
    echo   6. dev:wdbinstall       -- Builds, upgrades JBoss and installs database on DEV
    echo   7. dev                  -- Builds, upgrades JBoss on DEV
    echo   8. qa:wdbinstall        -- Builds, upgrades JBoss and install database on QA
    echo   9. qa                   -- Builds, upgrades JBoss on QA
    echo  10. deploy               -- Redeploy application locally
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
    ant -Dproperties.file=%DEVPROPFILE% deploy:remote:upgrade
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
    ant -Dproperties.file=%DEVPROPFILE% -Dupgrade.target=upgrade-ncm:with-dbinstall -Ddatabase.re-create=true deploy:remote:upgrade
    goto DONE
:d1
if "%1" == "qa" (
    ant -Dproperties.file=%QAPROPFILE% deploy:remote:upgrade
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
    ant -Dproperties.file=%QAPROPFILE% -Dupgrade.target=upgrade-ncm:with-dbinstall -Ddatabase.re-create=true deploy:remote:upgrade
    goto DONE
:q1
if "%1" == "clean" (
    ant clean
    if exist ..\target\*.* (
       rmdir /Q /S ..\target
    )
    goto DONE
)
:DONE
endlocal