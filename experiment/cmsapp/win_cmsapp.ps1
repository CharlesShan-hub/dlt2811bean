# cmsapp build script
# Usage: .\win_cmsapp.ps1

$ScriptDir = Split-Path $PSCommandPath -Parent
$BuildDir = Join-Path $ScriptDir "build"
$CcmsDir = Join-Path $ScriptDir "..\ccms"
$CcmsLib = Join-Path $CcmsDir "build\libccms.dll.a"
$CcmsCore = Join-Path $CcmsDir "build\libccms_core.a"

Write-Host "=== cmsapp build script ===" -ForegroundColor Cyan
Write-Host ""

if (-not (Test-Path $CcmsLib)) {
    Write-Host "[FAIL] libccms.dll.a not found — run ccms/win_ccms.ps1 first" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   found libccms.dll.a" -ForegroundColor Green

if (-not (Test-Path $CcmsCore)) {
    Write-Host "[FAIL] libccms_core.a not found — run ccms/win_ccms.ps1 first" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   found libccms_core.a" -ForegroundColor Green

$cmake = Get-Command cmake -ErrorAction SilentlyContinue
if (-not $cmake) {
    Write-Host "[FAIL] cmake not found" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   cmake $(& cmake --version | Select-Object -First 1)" -ForegroundColor Green

$ccDir = $null
$cc = Get-Command clang -ErrorAction SilentlyContinue
if (-not $cc) { $cc = Get-Command gcc -ErrorAction SilentlyContinue }
if ($cc) { $ccDir = Split-Path $cc.Source -Parent }
$make = Get-Command mingw32-make -ErrorAction SilentlyContinue
if (-not $make -and $ccDir) {
    $makePath = Join-Path $ccDir "mingw32-make.exe"
    if (Test-Path $makePath) { $make = $makePath }
}

$generator = ""
if ($make) { $generator = "MinGW Makefiles" }
Write-Host "[OK]   Generator: $(if($generator){$generator}else{'Default'})" -ForegroundColor Green

if (-not (Test-Path $BuildDir)) {
    New-Item -ItemType Directory -Path $BuildDir | Out-Null
}
Set-Location $BuildDir

$cacheFile = Join-Path $BuildDir "CMakeCache.txt"
if (Test-Path $cacheFile) {
    Remove-Item $cacheFile -Force
    Write-Host "[OK]   removed stale CMakeCache.txt" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "--- cmake configure ---" -ForegroundColor Cyan
if ($generator) {
    cmake $ScriptDir -G $generator
} else {
    cmake $ScriptDir
}
if ($LASTEXITCODE -ne 0) { Write-Host "[FAIL] cmake configure" -ForegroundColor Red; exit 1 }
Write-Host "[OK]   cmake configure" -ForegroundColor Green

Write-Host ""
Write-Host "--- build ---" -ForegroundColor Cyan
cmake --build .
if ($LASTEXITCODE -ne 0) { Write-Host "[FAIL] build failed" -ForegroundColor Red; exit 1 }
Write-Host "[OK]   build succeeded" -ForegroundColor Green
