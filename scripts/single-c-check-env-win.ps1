$env:Path = "$env:USERPROFILE\scoop\shims;$env:Path"

# 来源 win_ccms.ps1 L8
$cmake = Get-Command cmake -ErrorAction SilentlyContinue
if (-not $cmake) {
    Write-Host "[FAIL] cmake not found. Install it: scoop install cmake" -ForegroundColor Red
    exit 1
}

# 来源 win_ccms.ps1 L31, L70
$cc = Get-Command gcc -ErrorAction SilentlyContinue
if (-not $cc) {
    Write-Host "[FAIL] gcc not found. Install mingw: scoop install mingw" -ForegroundColor Red
    exit 1
}

$make = Get-Command mingw32-make -ErrorAction SilentlyContinue
if (-not $make) {
    Write-Host "[FAIL] mingw32-make not found." -ForegroundColor Red
    exit 1
}

Write-Host "[OK] 工具链就绪" -ForegroundColor Green
