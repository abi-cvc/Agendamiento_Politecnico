#!/bin/bash
set -e

WAR="/usr/local/tomcat/webapps/ROOT.war"
APP_DIR="/usr/local/tomcat/webapps/ROOT"
PERSISTENCE="$APP_DIR/WEB-INF/classes/META-INF/persistence.xml"

# Extraer el WAR para poder modificar persistence.xml en runtime
echo ">>> Extrayendo WAR..."
mkdir -p "$APP_DIR"
unzip -q "$WAR" -d "$APP_DIR"
rm -f "$WAR"

# Reemplazar configuración de base de datos si se pasan variables de entorno
if [ -n "$DB_URL" ]; then
  echo ">>> Configurando DB_URL..."
  # Paso 1: & → &amp;  (XML válido)
  XML_URL=$(printf '%s' "$DB_URL" | sed 's/&/\&amp;/g')
  # Paso 2: & → \&  (evita que sed interprete & como backreference)
  SED_URL=$(printf '%s' "$XML_URL" | sed 's/&/\\&/g')
  sed -i "s|jdbc:mysql://[^\"]*|$SED_URL|g" "$PERSISTENCE"
fi

if [ -n "$DB_USER" ]; then
  echo ">>> Configurando DB_USER..."
  sed -i "s|<property name=\"jakarta.persistence.jdbc.user\" value=\"[^\"]*\"/>|<property name=\"jakarta.persistence.jdbc.user\" value=\"$DB_USER\"/>|" "$PERSISTENCE"
fi

if [ -n "$DB_PASSWORD" ]; then
  echo ">>> Configurando DB_PASSWORD..."
  sed -i "s|<property name=\"jakarta.persistence.jdbc.password\" value=\"[^\"]*\"/>|<property name=\"jakarta.persistence.jdbc.password\" value=\"$DB_PASSWORD\"/>|" "$PERSISTENCE"
fi

echo ">>> Iniciando Tomcat..."
exec catalina.sh run
