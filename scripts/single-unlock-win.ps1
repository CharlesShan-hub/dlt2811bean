if (-not (Test-Path build)) { New-Item -ItemType Directory -Path build | Out-Null }
"1" | Out-File -FilePath build\lock -Encoding ascii -NoNewline
Write-Host "[OK] 已解锁 (build/lock = 1)" -ForegroundColor Green
