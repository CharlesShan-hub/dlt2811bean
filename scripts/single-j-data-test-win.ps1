Push-Location jcms
mvn test -pl jcms-data -am -q
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-data 测试通过" -ForegroundColor Green
