# --- Stage 1: Build -----------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar proyecto completo
COPY pom.xml .
COPY src ./src

# Compilar (Maven descargara dependencias automaticamente)
RUN mvn package -DskipTests -B

# --- Stage 2: Runtime -----------------------------------------------------------------------
FROM tomcat:10.1-jre17

# Instalar unzip para extraer el WAR en runtime
RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*

# Limpiar webapps por defecto
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR como ROOT para que quede en /
COPY --from=build /app/target/01_MiProyecto-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Importar certificado CA de TiDB Cloud al truststore de Java
COPY isrgrootx1.pem /tidb-ca.pem
RUN keytool -import -noprompt -alias tidb-isrg-root-x1 \
    -keystore "$JAVA_HOME/lib/security/cacerts" \
    -storepass changeit \
    -file /tidb-ca.pem

# Copiar entrypoint
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
