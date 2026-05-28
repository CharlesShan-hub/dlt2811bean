# cmsgenerator build script
# Usage: .\win_cmsgen.ps1 [input.asn] [output_dir]
# cd d:\project\work\standard\dlt2811bean\cmsgenerator
# .\win_cmsgen.ps1 -InputAsn ..\docs\dlt2811b-datatypes.asn -OutputDir ..\cmsper\generated
param(
    [string]$InputAsn = "",
    [string]$OutputDir = ""
)

$ScriptDir = Split-Path $PSCommandPath -Parent
$BuildDir = Join-Path $ScriptDir "build"

Write-Host "=== cmsgenerator build script ===" -ForegroundColor Cyan
Write-Host ""

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
if (-not $cmakePath) {
    Write-Host "[FAIL] cmake not found" -ForegroundColor Red; exit 1
}
$cmakeDir = Split-Path $cmakePath -Parent
$env:Path = "$cmakeDir;$env:Path"
Write-Host ("[OK]   cmake " + (& cmake --version | Select-Object -First 1)) -ForegroundColor Green

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
if (-not $ccPath) {
    Write-Host "[FAIL] no C compiler" -ForegroundColor Red; exit 1
}
$ccDir = Split-Path $ccPath -Parent
$env:Path = "$ccDir;$env:Path"
Write-Host ("[OK]   C compiler found") -ForegroundColor Green

# Determine input and output (resolve paths BEFORE Push-Location)
if (-not $InputAsn) {
    $InputAsn = Join-Path $ScriptDir "..\docs\cms.asn1"
} elseif (-not ([System.IO.Path]::IsPathRooted($InputAsn))) {
    $InputAsn = Join-Path $ScriptDir $InputAsn
}
$InputAsn = $ExecutionContext.SessionState.Path.GetUnresolvedProviderPathFromPSPath($InputAsn)
if (-not (Test-Path $InputAsn)) {
    Write-Host "[FAIL] input not found: $InputAsn" -ForegroundColor Red
    Pop-Location; exit 1
}
Write-Host ("[OK]   input: $InputAsn") -ForegroundColor Green

if (-not $OutputDir) { $OutputDir = Join-Path $ScriptDir "generated" }
elseif (-not ([System.IO.Path]::IsPathRooted($OutputDir))) {
    $OutputDir = Join-Path $ScriptDir $OutputDir
}
$OutputDir = $ExecutionContext.SessionState.Path.GetUnresolvedProviderPathFromPSPath($OutputDir)
Write-Host ("[OK]   output: $OutputDir") -ForegroundColor Green

# Build generator
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
& cmake $ScriptDir -G "MinGW Makefiles"
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }

Write-Host ""
Write-Host "--- build ---" -ForegroundColor Cyan
$make = Get-Command mingw32-make -ErrorAction SilentlyContinue
if (-not $make) { $make = Get-Command make -ErrorAction SilentlyContinue }
if ($make) { & $make } else { & cmake --build . }

Write-Host ""
Write-Host "--- generate ---" -ForegroundColor Cyan
$exe = Join-Path $BuildDir "cmsgen.exe"
if (Test-Path $exe) {
    & $exe $InputAsn $OutputDir
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK]   generated files in $OutputDir" -ForegroundColor Green
        Write-Host ""
        Write-Host "--- build datatypes library ---" -ForegroundColor Cyan
        $cacheFile2 = Join-Path $BuildDir "CMakeCache.txt"
        if (Test-Path $cacheFile2) { Remove-Item $cacheFile2 -Force }
        & cmake $ScriptDir -G "MinGW Makefiles"
        if ($make) { & $make } else { & cmake --build . }
        if ($LASTEXITCODE -eq 0) {
            Write-Host "[OK]   built libcmsper_datatypes.a" -ForegroundColor Green
        }
    }
} else {
    # try .\cmsgen.exe
    & ".\cmsgen.exe" $InputAsn $OutputDir
}

Pop-Location
Write-Host ""
Write-Host "=== done ===" -ForegroundColor Cyan
