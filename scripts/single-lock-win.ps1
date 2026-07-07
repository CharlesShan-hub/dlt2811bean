if (-not (Test-Path build)) { New-Item -ItemType Directory -Path build | Out-Null }
"0" | Out-File -FilePath build\lock -Encoding ascii -NoNewline
Write-Host "[OK] 已锁定 (build/lock = 0)" -ForegroundColor Yellow
