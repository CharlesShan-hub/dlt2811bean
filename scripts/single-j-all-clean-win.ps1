# 清理所有 JCMS 模块
Push-Location jcms
mvn clean -q
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] JCMS 清理完成" -ForegroundColor Green
