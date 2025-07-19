call remove-infrastructure-stacks.bat
call docker volume rm postgres-stack_postgres_data
call docker system prune --all --volumes --force
