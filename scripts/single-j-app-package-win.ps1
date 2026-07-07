Push-Location jcms
mvn package -pl jcms-app -am -q -DskipTests
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-app 打包完成" -ForegroundColor Green
