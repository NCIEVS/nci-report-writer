@echo off
@rem ******************************************
@rem **** Command file to invoke build.xml ****
@rem ******************************************
setlocal
cls
if "%1" == "" (
    echo.
    echo Available targets are:
    echo.
    echo   clean        -- Remove classes directory for clean build
    echo   all          -- Normal build of application
    echo   install      -- Build and deploy application
    echo   upgrade      -- Build and upgrade application
    echo   install      -- Builds, installs JBoss and database locally
    echo   upgradewdb   -- Builds, upgrades JBoss and database locally
    echo   dev          -- Builds, upgrades JBoss and database on DEV
    echo   deploy       -- Redeploy application
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
if "%1" == "upgrade" (
    ant deploy:local:upgrade
    goto DONE
)
if "%1" == "installwdb" (
    ant -Dupgrade.target=upgrade-with-dbinstall -Denable.install.debug=true deploy:local:upgrade
    goto DONE
)
if "%1" == "upgradewdb" (
    ant -Dupgrade.target=upgrade-ncm:with-dbinstall -Denable.install.debug=true deploy:local:upgrade
    goto DONE
)
if "%1" == "dev" (
    ant -Dproperties.file=C:\SVN-Projects\reportwriter-properties\properties\dev-upgrade.properties -Dupgrade.target=upgrade-ncm:with-dbinstall deploy:remote:upgrade
    goto DONE
)
if "%1" == "deploy" (
    ant deploy:hot
    goto DONE
)
if "%1" == "install" (
    ant deploy:local:install
    goto DONE
)
if "%1" == "clean" (
    ant clean
    if exist ..\target\*.* (
       rmdir /Q /S ..\target
    )
    goto DONE
)
:DONE
endlocal