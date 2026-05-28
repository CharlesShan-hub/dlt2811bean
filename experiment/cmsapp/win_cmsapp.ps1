# cmsapp build script
# Usage: .\win_cmsapp.ps1

$ScriptDir = Split-Path $PSCommandPath -Parent
$BuildDir = Join-Path $ScriptDir "build"
$TestsDir = Join-Path $ScriptDir "tests"
$TestGenDir = Join-Path $TestsDir "generated"

Write-Host "=== cmsapp build script ===" -ForegroundColor Cyan
Write-Host ""

# Check dependencies
$cmsperLib = Join-Path $ScriptDir "..\cmsper\build\libcmsper.a"
$datatypesLib = Join-Path $ScriptDir "..\cmsgenerator\build\libcmsper_datatypes.a"
$cmsgenExe = Join-Path $ScriptDir "..\cmsgenerator\build\cmsgen.exe"

if (-not (Test-Path $cmsperLib)) {
    Write-Host "[FAIL] libcmsper.a not found — run cmsper/win_cmsper.ps1 first" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   found libcmsper.a" -ForegroundColor Green

if (-not (Test-Path $datatypesLib)) {
    Write-Host "[FAIL] libcmsper_datatypes.a not found — run cmsgenerator/win_cmsgen.ps1 first" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   found libcmsper_datatypes.a" -ForegroundColor Green

# Generate SEQUENCE OF test datatypes
$testAsn = Join-Path $TestsDir "dlt2811b-test-seq.asn"
if ((Test-Path $cmsgenExe) -and (Test-Path $testAsn)) {
    Write-Host ""
    Write-Host "--- generate test datatypes ---" -ForegroundColor Cyan
    if (-not (Test-Path $TestGenDir)) { New-Item -ItemType Directory -Path $TestGenDir | Out-Null }
    & $cmsgenExe $testAsn $TestGenDir
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK]   generated test datatypes" -ForegroundColor Green
    }
}

# Find cmake
$cmakePath = $null
$cmake = Get-Command cmake -ErrorAction SilentlyContinue
if ($cmake) { $cmakePath = $cmake.Source }
if (-not $cmakePath) {
    $paths = @(
        "${env:ProgramFiles}\CMake\bin\cmake.exe",
        "${env:ProgramFiles(x86)}\CMake\bin\cmake.exe",
        "${env:LOCALAPPDATA}\Programs\CMake\bin\cmake.exe"
    )
    foreach ($p in $paths) { if (Test-Path $p) { $cmakePath = $p; break } }
}
if (-not $cmakePath) { Write-Host "[FAIL] cmake not found" -ForegroundColor Red; exit 1 }
$cmakeDir = Split-Path $cmakePath -Parent
$env:Path = "$cmakeDir;$env:Path"

# Build
if (-not (Test-Path $BuildDir)) { New-Item -ItemType Directory -Path $BuildDir | Out-Null }
Push-Location $BuildDir

# Remove stale CMakeCache.txt if paths have changed
$cacheFile = Join-Path $BuildDir "CMakeCache.txt"
if (Test-Path $cacheFile) {
    Remove-Item $cacheFile -Force
    Write-Host "[OK]   removed stale CMakeCache.txt" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "--- cmake configure ---" -ForegroundColor Cyan
cmake $ScriptDir -G "MinGW Makefiles"
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }

Write-Host ""
Write-Host "--- build ---" -ForegroundColor Cyan
$make = Get-Command mingw32-make -ErrorAction SilentlyContinue
if (-not $make) { $make = Get-Command make -ErrorAction SilentlyContinue }
if ($make) { & $make } else { cmake --build . }
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Write-Host "[OK]   build" -ForegroundColor Green

# Run
Write-Host ""
Write-Host "--- run sgcb_roundtrip ---" -ForegroundColor Cyan
$sgcb = ".\examples\sgcb_roundtrip.exe"
if (Test-Path $sgcb) { & $sgcb }
elseif (Test-Path ".\sgcb_roundtrip.exe") { & ".\sgcb_roundtrip.exe" }

Write-Host ""
Write-Host "--- run seq_of_roundtrip ---" -ForegroundColor Cyan
$seq = ".\examples\seq_of_roundtrip.exe"
if (Test-Path $seq) { & $seq }
elseif (Test-Path ".\seq_of_roundtrip.exe") { & ".\seq_of_roundtrip.exe" }

Write-Host ""
Write-Host "--- run service_roundtrip ---" -ForegroundColor Cyan
$svc = ".\examples\service_roundtrip.exe"
if (Test-Path $svc) { & $svc }
elseif (Test-Path ".\service_roundtrip.exe") { & ".\service_roundtrip.exe" }

Write-Host ""
Write-Host "--- run apdu_roundtrip ---" -ForegroundColor Cyan
$apdu = ".\examples\apdu_roundtrip.exe"
if (Test-Path $apdu) { & $apdu }
elseif (Test-Path ".\apdu_roundtrip.exe") { & ".\apdu_roundtrip.exe" }

Pop-Location
Write-Host ""
Write-Host "=== done ===" -ForegroundColor Cyan
