Push-Location jcms
mvn test -pl jcms-core -q
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-core 测试通过" -ForegroundColor Green
