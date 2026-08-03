Push-Location jcms
mvn install -pl jcms-data -q -DskipTests
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-data 打包并安装完成" -ForegroundColor Green
