#!/usr/bin/env bash
set -euo pipefail

# GM test certificate generation script (using JDK keytool)
# NOTE: Uses RSA algorithm, for development/testing only

OUTPUT_DIR="src/main/resources/certs"
PASSWORD="changeit"
DAYS=365

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUTPUT_DIR="$SCRIPT_DIR/$OUTPUT_DIR"

echo "===== Generating Test Certificates ====="
echo "Output directory: $OUTPUT_DIR"
echo ""

mkdir -p "$OUTPUT_DIR"

# 1. Generate CA root certificate
echo "1. Generating CA root certificate..."
keytool -genkeypair -alias ca -keyalg RSA -keysize 2048 -validity "$DAYS" \
  -keystore "$OUTPUT_DIR/ca.pfx" -storetype PKCS12 \
  -storepass "$PASSWORD" -keypass "$PASSWORD" \
  -dname "CN=Test CA, O=TestOrg, C=CN"

echo "   Exporting CA certificate..."
keytool -exportcert -alias ca -keystore "$OUTPUT_DIR/ca.pfx" \
  -storetype PKCS12 -storepass "$PASSWORD" \
  -file "$OUTPUT_DIR/ca.cer" -rfc > /dev/null 2>&1

# 2. Generate server certificate
echo "2. Generating server certificate..."
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -validity "$DAYS" \
  -keystore "$OUTPUT_DIR/server.pfx" -storetype PKCS12 \
  -storepass "$PASSWORD" -keypass "$PASSWORD" \
  -dname "CN=localhost, O=TestOrg, C=CN"

echo "   Signing server certificate with CA..."
keytool -keystore "$OUTPUT_DIR/server.pfx" -storetype PKCS12 \
  -storepass "$PASSWORD" -certreq -alias server \
  -file "$OUTPUT_DIR/server.csr" > /dev/null 2>&1
keytool -gencert -alias ca -keystore "$OUTPUT_DIR/ca.pfx" \
  -storetype PKCS12 -storepass "$PASSWORD" \
  -infile "$OUTPUT_DIR/server.csr" -outfile "$OUTPUT_DIR/server.cer" -rfc > /dev/null 2>&1
keytool -keystore "$OUTPUT_DIR/server.pfx" -storetype PKCS12 \
  -storepass "$PASSWORD" -importcert -alias ca \
  -file "$OUTPUT_DIR/ca.cer" -noprompt > /dev/null 2>&1
keytool -keystore "$OUTPUT_DIR/server.pfx" -storetype PKCS12 \
  -storepass "$PASSWORD" -importcert -alias server \
  -file "$OUTPUT_DIR/server.cer" -noprompt > /dev/null 2>&1

# 3. Generate client certificate
echo "3. Generating client certificate..."
keytool -genkeypair -alias client -keyalg RSA -keysize 2048 -validity "$DAYS" \
  -keystore "$OUTPUT_DIR/client.pfx" -storetype PKCS12 \
  -storepass "$PASSWORD" -keypass "$PASSWORD" \
  -dname "CN=TestClient, O=TestOrg, C=CN"

echo "   Signing client certificate with CA..."
keytool -keystore "$OUTPUT_DIR/client.pfx" -storetype PKCS12 \
  -storepass "$PASSWORD" -certreq -alias client \
  -file "$OUTPUT_DIR/client.csr" > /dev/null 2>&1
keytool -gencert -alias ca -keystore "$OUTPUT_DIR/ca.pfx" \
  -storetype PKCS12 -storepass "$PASSWORD" \
  -infile "$OUTPUT_DIR/client.csr" -outfile "$OUTPUT_DIR/client.cer" -rfc > /dev/null 2>&1
keytool -keystore "$OUTPUT_DIR/client.pfx" -storetype PKCS12 \
  -storepass "$PASSWORD" -importcert -alias ca \
  -file "$OUTPUT_DIR/ca.cer" -noprompt > /dev/null 2>&1
keytool -keystore "$OUTPUT_DIR/client.pfx" -storetype PKCS12 \
  -storepass "$PASSWORD" -importcert -alias client \
  -file "$OUTPUT_DIR/client.cer" -noprompt > /dev/null 2>&1

# Clean up temp files
rm -f "$OUTPUT_DIR"/*.csr "$OUTPUT_DIR"/*.cer

echo ""
echo "===== Done ====="
echo "Generated files:"
for f in "$OUTPUT_DIR"/*.pfx; do
  echo "  - $(basename "$f")"
done
echo ""
echo "All certificates password: $PASSWORD"
echo ""
echo "NOTE: These are test certificates using RSA algorithm."
echo "      For production, use GmSSL to generate GM certificates."