# 国密测试证书生成脚本 (使用 JDK keytool)
# 注意：使用 RSA 算法，仅用于开发测试

$OUTPUT_DIR = "src\main\resources\certs"
$PASSWORD = "changeit"
$DAYS = 365

Write-Host "===== 生成测试证书 =====" -ForegroundColor Cyan
Write-Host "输出目录: $OUTPUT_DIR"
Write-Host ""

# 创建输出目录
if (-not (Test-Path $OUTPUT_DIR)) {
    New-Item -ItemType Directory -Path $OUTPUT_DIR -Force | Out-Null
}

# 1. 生成 CA 根证书
Write-Host "1. 生成 CA 根证书..." -ForegroundColor Yellow
$caCmd = @"
keytool -genkeypair -alias ca -keyalg RSA -keysize 2048 -validity $DAYS -keystore `"$OUTPUT_DIR\ca.pfx`" -storetype PKCS12 -storepass $PASSWORD -keypass $PASSWORD -dname `"CN=Test CA, O=TestOrg, C=CN`"
"@
Invoke-Expression $caCmd

# 导出 CA 证书
Write-Host "   导出 CA 证书..." -ForegroundColor Gray
keytool -exportcert -alias ca -keystore "$OUTPUT_DIR\ca.pfx" -storetype PKCS12 -storepass $PASSWORD -file "$OUTPUT_DIR\ca.cer" -rfc | Out-Null

# 2. 生成服务端证书
Write-Host "2. 生成服务端证书..." -ForegroundColor Yellow
$serverCmd = @"
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -validity $DAYS -keystore `"$OUTPUT_DIR\server.pfx`" -storetype PKCS12 -storepass $PASSWORD -keypass $PASSWORD -dname `"CN=localhost, O=TestOrg, C=CN`"
"@
Invoke-Expression $serverCmd

# 用 CA 签发服务端证书
Write-Host "   用 CA 签发服务端证书..." -ForegroundColor Gray
$importServerCmd = @"
keytool -keystore `"$OUTPUT_DIR\server.pfx`" -storetype PKCS12 -storepass $PASSWORD -certreq -alias server -file `"$OUTPUT_DIR\server.csr`" | Out-Null
keytool -gencert -alias ca -keystore `"$OUTPUT_DIR\ca.pfx`" -storetype PKCS12 -storepass $PASSWORD -infile `"$OUTPUT_DIR\server.csr`" -outfile `"$OUTPUT_DIR\server.cer`" -rfc | Out-Null
keytool -keystore `"$OUTPUT_DIR\server.pfx`" -storetype PKCS12 -storepass $PASSWORD -importcert -alias ca -file `"$OUTPUT_DIR\ca.cer`" -noprompt | Out-Null
keytool -keystore `"$OUTPUT_DIR\server.pfx`" -storetype PKCS12 -storepass $PASSWORD -importcert -alias server -file `"$OUTPUT_DIR\server.cer`" -noprompt | Out-Null
"@
Invoke-Expression $importServerCmd

# 3. 生成客户端证书
Write-Host "3. 生成客户端证书..." -ForegroundColor Yellow
$clientCmd = @"
keytool -genkeypair -alias client -keyalg RSA -keysize 2048 -validity $DAYS -keystore `"$OUTPUT_DIR\client.pfx`" -storetype PKCS12 -storepass $PASSWORD -keypass $PASSWORD -dname `"CN=TestClient, O=TestOrg, C=CN`"
"@
Invoke-Expression $clientCmd

# 用 CA 签发客户端证书
Write-Host "   用 CA 签发客户端证书..." -ForegroundColor Gray
$importClientCmd = @"
keytool -keystore `"$OUTPUT_DIR\client.pfx`" -storetype PKCS12 -storepass $PASSWORD -certreq -alias client -file `"$OUTPUT_DIR\client.csr`" | Out-Null
keytool -gencert -alias ca -keystore `"$OUTPUT_DIR\ca.pfx`" -storetype PKCS12 -storepass $PASSWORD -infile `"$OUTPUT_DIR\client.csr`" -outfile `"$OUTPUT_DIR\client.cer`" -rfc | Out-Null
keytool -keystore `"$OUTPUT_DIR\client.pfx`" -storetype PKCS12 -storepass $PASSWORD -importcert -alias ca -file `"$OUTPUT_DIR\ca.cer`" -noprompt | Out-Null
keytool -keystore `"$OUTPUT_DIR\client.pfx`" -storetype PKCS12 -storepass $PASSWORD -importcert -alias client -file `"$OUTPUT_DIR\client.cer`" -noprompt | Out-Null
"@
Invoke-Expression $importClientCmd

# 清理临时文件
Remove-Item "$OUTPUT_DIR\*.csr" -Force -ErrorAction SilentlyContinue
Remove-Item "$OUTPUT_DIR\*.cer" -Force -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "===== 完成 =====" -ForegroundColor Cyan
Write-Host "生成的文件:" -ForegroundColor White
Get-ChildItem "$OUTPUT_DIR\*.pfx" | ForEach-Object {
    Write-Host "  - $($_.Name)" -ForegroundColor Green
}
Write-Host ""
Write-Host "所有证书密码: $PASSWORD" -ForegroundColor Yellow
Write-Host ""
Write-Host "注意: 这是使用 RSA 算法生成的测试证书" -ForegroundColor Yellow
Write-Host "      生产环境请使用 GmSSL 生成国密证书" -ForegroundColor Yellow
