#!/bin/bash

# Build the PostgreSQL Docker image
docker build -t person-db .

# Stop and remove existing container if it exists
docker stop person-db-container 2>/dev/null || true
docker rm person-db-container 2>/dev/null || true

# Start the PostgreSQL container
docker run -d \
  --name person-db-container \
  -p 5432:5432 \
  person-db

echo "PostgreSQL database started on port 5432"
echo "Database: persondb"
echo "Username: user"
echo "Password: password"
