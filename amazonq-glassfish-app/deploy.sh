#!/bin/bash

echo "=== GlassFish Deployment Script ==="

# Build the application first (this installs GlassFish)
echo "Building application..."
mvn package -q

# Path to GlassFish installation (available after build)
GLASSFISH_HOME="target/glassfish7"
ASADMIN="${GLASSFISH_HOME}/bin/asadmin"

# Function to check if GlassFish is running
is_glassfish_running() {
    curl -s http://localhost:4848 > /dev/null 2>&1
}

# Function to start GlassFish
start_glassfish() {
    echo "Starting GlassFish..."
    ${ASADMIN} start-domain
}

# Function to create datasource
create_datasource() {
    echo "Creating database connection pool and datasource..."
    
    # Create connection pool
    ${ASADMIN} create-jdbc-connection-pool \
        --restype javax.sql.DataSource \
        --datasourceclassname org.postgresql.ds.PGSimpleDataSource \
        --property serverName=localhost:portNumber=5432:databaseName=peopledb:user=postgres:password=postgres \
        peoplePool 2>/dev/null || true
    
    # Create JDBC resource
    ${ASADMIN} create-jdbc-resource \
        --connectionpoolid peoplePool \
        jdbc/peopleDS 2>/dev/null || true
}

# Function to create security realm
create_security_realm() {
    echo "Creating security realm..."
    # Copy security files to GlassFish config
    cp users.properties ${GLASSFISH_HOME}/glassfish/domains/domain1/config/
    cp groups.properties ${GLASSFISH_HOME}/glassfish/domains/domain1/config/
    
    # Create file realm
    ${ASADMIN} create-auth-realm \
        --classname com.sun.enterprise.security.auth.realm.file.FileRealm \
        --property file=${GLASSFISH_HOME}/glassfish/domains/domain1/config/users.properties:jaas-context=fileRealm:assign-groups=${GLASSFISH_HOME}/glassfish/domains/domain1/config/groups.properties \
        schoolRealm 2>/dev/null || true
}

# Function to deploy application
deploy_app() {
    echo "Deploying application..."
    ${ASADMIN} undeploy --name people-app 2>/dev/null || true
    ${ASADMIN} deploy --force target/people-app.war
}

# Check if GlassFish installation exists after build
if [ ! -f "${ASADMIN}" ]; then
    echo "Error: GlassFish not found at ${GLASSFISH_HOME}"
    echo "Build may have failed or GlassFish plugin not configured"
    exit 1
fi

# Check if GlassFish is running
if ! is_glassfish_running; then
    start_glassfish
    sleep 5
else
    echo "GlassFish is already running"
fi

# Create datasource
create_datasource

# Create security realm
create_security_realm

# Deploy the application
deploy_app

echo ""
echo "=== Deployment Complete ==="
echo "🌐 Application: http://localhost:8080/people-app/"
echo "🔐 Login: admin / admin"
echo "📊 Demo data: 10 teachers, 6 students, 3 grades, subjects, and schedules"
echo ""
echo "To stop GlassFish: ${ASADMIN} stop-domain"
