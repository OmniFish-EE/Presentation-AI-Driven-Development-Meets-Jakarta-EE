#!/bin/bash

# Stop the PostgreSQL container
docker stop person-db-container 2>/dev/null && echo "PostgreSQL database stopped" || echo "Failed to stop PostgreSQL database"


docker rm person-db-container 2>/dev/null && echo "PostgreSQL container removed" || echo "Failed to remove PostgreSQL container"


