#!/bin/bash

echo "=== Stopping School Management System ==="

# Stop GlassFish
echo "1. Stopping GlassFish..."
if [ -f "target/glassfish7/bin/asadmin" ]; then
    target/glassfish7/bin/asadmin stop-domain 2>/dev/null || echo "   GlassFish already stopped"
else
    echo "   GlassFish not found"
fi

# Stop Docker containers
echo "2. Stopping PostgreSQL and Keycloak..."
docker compose down

echo ""
echo "=== All services stopped ==="
