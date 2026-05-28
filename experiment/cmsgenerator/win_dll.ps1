# Build FFI DLL for cross-language invocation
# Usage: .\win_dll.ps1
# Output: build/bin/libcmsper_datatypes.dll

$ScriptDir = Split-Path $PSCommandPath -Parent
$BuildDir = Join-Path $ScriptDir "build"

Write-Host "=== build cms FFI DLL ===" -ForegroundColor Cyan
Write-Host ""

# Check prerequisites
$cmsperLib = Join-Path $ScriptDir "..\cmsper\build\libcmsper.a"
if (-not (Test-Path $cmsperLib)) {
    Write-Host "[FAIL] libcmsper.a not found — run ../cmsper/win_cmsper.ps1 first" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   libcmsper.a" -ForegroundColor Green

$genDir = Join-Path $ScriptDir "generated"
$genCms = Join-Path $genDir "gen_cms.h"
if (-not (Test-Path $genCms)) {
    Write-Host "[FAIL] gen_cms.h not found — run win_cmsgen.ps1 first" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   generated files" -ForegroundColor Green

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
Write-Host "[OK]   cmake" -ForegroundColor Green

# Find C compiler
$ccPath = $null
$cc = Get-Command clang -ErrorAction SilentlyContinue
if ($cc) { $ccPath = $cc.Source }
if (-not $ccPath) {
    $cc = Get-Command gcc -ErrorAction SilentlyContinue
    if ($cc) { $ccPath = $cc.Source }
}
if (-not $ccPath) {
    $paths = @(
        "${env:ProgramFiles}\LLVM-MinGW-UCRT\bin\clang.exe",
        "${env:LOCALAPPDATA}\Microsoft\WinGet\Packages\MartinStorsjo.LLVM-MinGW.UCRT_*\llvm-mingw-*\bin\clang.exe"
    )
    foreach ($p in $paths) {
        $found = Get-ChildItem $p -ErrorAction SilentlyContinue
        if ($found) { $ccPath = $found[0].FullName; break }
    }
}
if (-not $ccPath) { Write-Host "[FAIL] no C compiler found" -ForegroundColor Red; exit 1 }
$ccDir = Split-Path $ccPath -Parent
$env:Path = "$ccDir;$env:Path"
Write-Host "[OK]   C compiler" -ForegroundColor Green

# Build DLL
if (-not (Test-Path $BuildDir)) { New-Item -ItemType Directory -Path $BuildDir | Out-Null }
Push-Location $BuildDir

$cacheFile = Join-Path $BuildDir "CMakeCache.txt"
if (Test-Path $cacheFile) { Remove-Item $cacheFile -Force }

Write-Host ""
Write-Host "--- cmake configure ---" -ForegroundColor Cyan
cmake $ScriptDir -G "MinGW Makefiles"
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }

Write-Host ""
Write-Host "--- build DLL ---" -ForegroundColor Cyan
$make = Get-Command mingw32-make -ErrorAction SilentlyContinue
if (-not $make) { $make = Get-Command make -ErrorAction SilentlyContinue }
if ($make) { & $make } else { cmake --build . }
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }

$dll = Join-Path $BuildDir "bin\libcmsper_datatypes.dll"
if (Test-Path $dll) {
    Write-Host ""
    Write-Host "[OK]   DLL generated: $dll" -ForegroundColor Green
    $len = (Get-Item $dll).Length
    Write-Host "      size: $len bytes" -ForegroundColor Green

    # Show exported FFI symbols
    Write-Host ""
    Write-Host "--- exported FFI functions ---" -ForegroundColor Cyan
    $strings = Get-Command strings -ErrorAction SilentlyContinue
    if ($strings) {
        strings $dll | Select-String "cms_ffi_" | ForEach-Object { Write-Host "       $_" -ForegroundColor White }
    } else {
        Write-Host "       (install 'strings' to list exports)" -ForegroundColor Yellow
    }
} else {
    Write-Host "[FAIL] DLL not found at $dll" -ForegroundColor Red
    Pop-Location; exit 1
}

Pop-Location
Write-Host ""
Write-Host "=== done ===" -ForegroundColor Cyan
Write-Host "DLL path: $dll" -ForegroundColor White
Write-Host "Header:   $ScriptDir\include\cms_ffi.h" -ForegroundColor White
