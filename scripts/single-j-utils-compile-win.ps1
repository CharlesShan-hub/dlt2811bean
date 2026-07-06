Push-Location jcms
mvn compile -pl jcms-utils -am -q
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-utils 编译完成" -ForegroundColor Green
