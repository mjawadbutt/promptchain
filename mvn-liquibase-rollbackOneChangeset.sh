#!/usr/bin/env bash

mvn initialize liquibase:rollback -Dliquibase.rollbackCount=1
