# 全量编译+install（不测试），后续 server/client 不需要再编译
Push-Location jcms
mvn install -DskipTests -q
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] Java 全量编译完成" -ForegroundColor Green
