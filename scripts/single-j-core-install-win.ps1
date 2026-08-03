Push-Location jcms
mvn install -pl jcms-core -q -DskipTests
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-core 打包并安装完成" -ForegroundColor Green
