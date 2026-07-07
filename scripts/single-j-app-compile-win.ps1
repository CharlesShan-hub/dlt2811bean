Push-Location jcms
mvn compile -pl jcms-app -am -q
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-app 编译完成" -ForegroundColor Green
