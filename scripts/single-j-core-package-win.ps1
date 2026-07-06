Push-Location jcms
mvn package -pl jcms-core -q -DskipTests
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-core 打包完成" -ForegroundColor Green
