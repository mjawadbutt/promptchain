REM TODO-Jawad-Now: delete this
set POSTGRES_HOST=@postgres_host@
set POSTGRES_PORT=@postgres_port@
set REDIS_HOST=@redis_host@
set REDIS_PORT=@redis_port@
set REDIS_PASSWORD=@redis_password@
set APP_DATA_PARENT_DIR=@app_data_parent_dir@
set APP_DATA_DIR_NAME=@app_data_dir_name@
set APP_LOGS_PARENT_DIR=@app_logs_parent_dir@
set APP_LOGS_DIR_NAME=@app_logs_dir_name@
set APP_DB_NAME=@app_db_name@
set APP_DB_USER_NAME=@app_db_user_name@
set APP_DB_USER_PASSWORD=@app_db_user_password@

REM  Default value defined below. To change, define an env-var with the same name.
IF NOT DEFINED JAVA_OPTS (
    SET "JAVA_OPTS=-Xms1024m -Xmx2048m"
)

REM Define following two vars in the environment to enable remote debugging when the app is running inside a container:
REM
REM RUN_IN_DEBUG_MODE
REM By default the var is not set, which is treated by entrypoint.sh as false.
REM Any value except true is considered false.
REM
REM DEBUG_PORT
REM Default value of 5005 is already set in the app's entrypoint.sh.

call deploy-infrastructure-stacks.bat
