Push-Location jcms
mvn package -pl jcms-utils -am -q -DskipTests
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-utils 打包完成" -ForegroundColor Green
