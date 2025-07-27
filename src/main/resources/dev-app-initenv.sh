#TODO-Jawad-Now: delete this
export POSTGRES_HOST=@postgres_host@
export POSTGRES_PORT=@postgres_port@
export REDIS_HOST=@redis_host@
export REDIS_PORT=@redis_port@
export REDIS_PASSWORD=@redis_password@
export APP_DATA_PARENT_DIR=@app_data_parent_dir@
export APP_DATA_DIR_NAME=@app_data_dir_name@
export APP_LOGS_PARENT_DIR=@app_logs_parent_dir@
export APP_LOGS_DIR_NAME=@app_logs_dir_name@
export APP_DB_NAME=@app_db_name@
export APP_DB_USER_NAME=@app_db_user_name@
export APP_DB_USER_PASSWORD=@app_db_user_password@

# Default value defined below. To change, define an env-var with the same name.
export JAVA_OPTS=${JAVA_OPTS:-Xms1024m -Xmx2048m}

# Define following two vars in the environment to enable remote debugging when the app is running inside a container:
#
# RUN_IN_DEBUG_MODE
# By default the var is not set, which is treated by entrypoint.sh as false.
# Any value except true is considered false.
#
# DEBUG_PORT
# Default value of 5005 is already set in the app's entrypoint.sh.

./deploy-infrastructure-stacks.sh
