Write-Host "[WAIT] 等待解锁..." -ForegroundColor Yellow
while ((Get-Content build\lock -ErrorAction SilentlyContinue) -ne "1") {
    Start-Sleep -Milliseconds 200
}
Write-Host "[OK] 解锁完成" -ForegroundColor Green
