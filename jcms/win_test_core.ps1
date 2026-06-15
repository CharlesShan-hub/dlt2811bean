$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host "=== step 1: build ccms.dll ===" -ForegroundColor Cyan
Push-Location (Join-Path $root "..\ccms")
try {
    & ".\win_ccms.ps1"
    if ($LASTEXITCODE -ne 0) { throw "win_ccms.ps1 failed" }
} finally {
    Pop-Location
}

Write-Host "=== step 2: copy ccms.dll -> jcms-core resources ===" -ForegroundColor Cyan
$dllSrc = Join-Path $root "..\ccms\dist\ccms.dll"
$dstDir = Join-Path $root "jcms-core\src\main\resources\win32-x86-64"
if (-not (Test-Path $dllSrc)) { throw "ccms.dll not found at $dllSrc" }
if (-not (Test-Path $dstDir)) { New-Item -ItemType Directory -Path $dstDir -Force | Out-Null }
Copy-Item $dllSrc (Join-Path $dstDir "ccms.dll") -Force
Write-Host "[OK] copied to $dstDir\ccms.dll" -ForegroundColor Green

Write-Host "=== step 3: mvn clean test -pl jcms-core ===" -ForegroundColor Cyan
Push-Location $root
try {
    mvn clean test -pl jcms-core
    if ($LASTEXITCODE -ne 0) { throw "mvn test failed" }
} finally {
    Pop-Location
}

Write-Host "=== all done ===" -ForegroundColor Green
