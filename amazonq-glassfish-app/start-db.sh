#!/bin/bash

echo "Stopping any existing PostgreSQL containers..."
docker compose down 2>/dev/null || true
docker stop $(docker ps -q --filter "publish=5432") 2>/dev/null || true

echo "Starting PostgreSQL database..."
docker compose up -d

echo "Waiting for database to be ready..."
sleep 10

echo "Database is ready at localhost:5432"
echo "Database: peopledb"
echo "Username: postgres"
echo "Password: postgres"
echo ""
echo "To stop the database, run: docker compose down"
