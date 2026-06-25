# jcms-app 测试证书生成脚本
# 使用 JDK keytool 生成 RSA 测试证书（仅用于开发测试）

$OUTPUT_DIR = "jcms-app\src\test\resources\certs"
$PASSWORD = "changeit"
$DAYS = 3650

Push-Location (Split-Path $PSScriptRoot -Parent)

Write-Host "===== 生成测试证书 =====" -ForegroundColor Cyan
Write-Host "输出目录: $OUTPUT_DIR"
Write-Host ""

# 创建输出目录
$null = New-Item -ItemType Directory -Path $OUTPUT_DIR -Force

# 1. CA 根证书
Write-Host "1. 生成 CA 根证书..." -ForegroundColor Yellow
keytool -genkeypair -alias ca -keyalg RSA -keysize 2048 -validity $DAYS `
    -keystore "$OUTPUT_DIR\ca.p12" -storetype PKCS12 -storepass $PASSWORD -keypass $PASSWORD `
    -dname "CN=Test CA, O=TestOrg, C=CN" 2>$null
keytool -exportcert -alias ca -keystore "$OUTPUT_DIR\ca.p12" -storetype PKCS12 -storepass $PASSWORD `
    -file "$OUTPUT_DIR\ca.cer" -rfc 2>$null

# 2. 服务端证书（CA 签发）
Write-Host "2. 生成服务端证书..." -ForegroundColor Yellow
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -validity $DAYS `
    -keystore "$OUTPUT_DIR\server.p12" -storetype PKCS12 -storepass $PASSWORD -keypass $PASSWORD `
    -dname "CN=localhost, O=TestOrg, C=CN" 2>$null
keytool -keystore "$OUTPUT_DIR\server.p12" -storetype PKCS12 -storepass $PASSWORD `
    -certreq -alias server -file "$OUTPUT_DIR\server.csr" 2>$null
keytool -gencert -alias ca -keystore "$OUTPUT_DIR\ca.p12" -storetype PKCS12 -storepass $PASSWORD `
    -infile "$OUTPUT_DIR\server.csr" -outfile "$OUTPUT_DIR\server.cer" -rfc 2>$null
keytool -keystore "$OUTPUT_DIR\server.p12" -storetype PKCS12 -storepass $PASSWORD `
    -importcert -alias ca -file "$OUTPUT_DIR\ca.cer" -noprompt 2>$null
keytool -keystore "$OUTPUT_DIR\server.p12" -storetype PKCS12 -storepass $PASSWORD `
    -importcert -alias server -file "$OUTPUT_DIR\server.cer" -noprompt 2>$null

# 3. 客户端证书（CA 签发）
Write-Host "3. 生成客户端证书..." -ForegroundColor Yellow
keytool -genkeypair -alias client -keyalg RSA -keysize 2048 -validity $DAYS `
    -keystore "$OUTPUT_DIR\client.p12" -storetype PKCS12 -storepass $PASSWORD -keypass $PASSWORD `
    -dname "CN=TestClient, O=TestOrg, C=CN" 2>$null
keytool -keystore "$OUTPUT_DIR\client.p12" -storetype PKCS12 -storepass $PASSWORD `
    -certreq -alias client -file "$OUTPUT_DIR\client.csr" 2>$null
keytool -gencert -alias ca -keystore "$OUTPUT_DIR\ca.p12" -storetype PKCS12 -storepass $PASSWORD `
    -infile "$OUTPUT_DIR\client.csr" -outfile "$OUTPUT_DIR\client.cer" -rfc 2>$null
keytool -keystore "$OUTPUT_DIR\client.p12" -storetype PKCS12 -storepass $PASSWORD `
    -importcert -alias ca -file "$OUTPUT_DIR\ca.cer" -noprompt 2>$null
keytool -keystore "$OUTPUT_DIR\client.p12" -storetype PKCS12 -storepass $PASSWORD `
    -importcert -alias client -file "$OUTPUT_DIR\client.cer" -noprompt 2>$null

# 清理临时文件
Remove-Item "$OUTPUT_DIR\*.csr" -Force -ErrorAction SilentlyContinue
Remove-Item "$OUTPUT_DIR\*.cer" -Force -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "===== 完成 =====" -ForegroundColor Cyan
Write-Host "密码: $PASSWORD" -ForegroundColor Yellow
Get-ChildItem "$OUTPUT_DIR\*.p12" | ForEach-Object {
    Write-Host "  $($_.Name)" -ForegroundColor Green
}

Pop-Location
