REM @echo off
mvn initialize liquibase:rollback -Dliquibase.rollbackCount=1
