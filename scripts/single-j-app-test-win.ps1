Push-Location jcms
mvn test -pl jcms-app -am -q
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-app 测试通过" -ForegroundColor Green
