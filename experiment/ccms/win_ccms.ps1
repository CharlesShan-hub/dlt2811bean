# ccms build script — check, compile, test, package
# Usage: .\win_ccms.ps1

Write-Host "=== ccms build script ===" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check cmake
$cmake = Get-Command cmake -ErrorAction SilentlyContinue
if (-not $cmake) {
    $commonPaths = @(
        "${env:ProgramFiles}\CMake\bin\cmake.exe",
        "${env:ProgramFiles(x86)}\CMake\bin\cmake.exe",
        "${env:LOCALAPPDATA}\Programs\CMake\bin\cmake.exe"
    )
    foreach ($p in $commonPaths) {
        if (Test-Path $p) {
            $cmake = Get-Command $p -ErrorAction SilentlyContinue
            break
        }
    }
}
if (-not $cmake) {
    Write-Host "[FAIL] cmake not found. Install it:" -ForegroundColor Red
    Write-Host "       winget install Kitware.CMake" -ForegroundColor Yellow
    exit 1
}
$env:Path = (Split-Path $cmake.Source -Parent) + ";$env:Path"
Write-Host ("[OK]   cmake " + (& cmake --version | Select-Object -First 1)) -ForegroundColor Green

# Step 2: Check C compiler (clang or gcc)
$cc = Get-Command clang -ErrorAction SilentlyContinue
$ccName = "clang"
if (-not $cc) {
    $cc = Get-Command gcc -ErrorAction SilentlyContinue
    $ccName = "gcc"
}
if (-not $cc) {
    $commonPaths = @(
        "${env:ProgramFiles}\LLVM-MinGW-UCRT\bin\clang.exe",
        "${env:ProgramFiles}\LLVM-MinGW-UCRT\bin\gcc.exe",
        "${env:ProgramFiles(x86)}\LLVM-MinGW-UCRT\bin\clang.exe"
    )
    foreach ($p in $commonPaths) {
        if (Test-Path $p) {
            $cc = Get-Command $p -ErrorAction SilentlyContinue
            break
        }
    }
}
if (-not $cc) {
    $wingetDir = "${env:LOCALAPPDATA}\Microsoft\WinGet\Packages\MartinStorsjo.LLVM-MinGW.UCRT_*"
    $matches = Get-ChildItem -Path $wingetDir -Filter "clang.exe" -Recurse -ErrorAction SilentlyContinue
    if ($matches) {
        $ccPath = $matches[0].FullName
        $cc = @{ Source = $ccPath }
    }
}
if (-not $cc) {
    Write-Host "[FAIL] No C compiler found (clang or gcc)." -ForegroundColor Red
    Write-Host "       Install LLVM MinGW: winget install MartinStorsjo.LLVM-MinGW.UCRT" -ForegroundColor Yellow
    exit 1
}
$ccDir = Split-Path $cc.Source -Parent
$env:Path = "$ccDir;$env:Path"
$ccName = if ($cc -is [System.Management.Automation.CommandInfo]) { $cc.Name } else { (Get-Item $cc.Source).BaseName }
Write-Host ("[OK]   " + $ccName + " " + (& "$($cc.Source)" --version | Select-Object -First 1)) -ForegroundColor Green

# Step 3: Detect generator
$generator = ""
$make = Get-Command mingw32-make -ErrorAction SilentlyContinue
if (-not $make -and $ccDir) {
    $makePath = Join-Path $ccDir "mingw32-make.exe"
    if (Test-Path $makePath) { $make = $makePath }
}
if ($make) {
    $generator = "MinGW Makefiles"
} elseif (Get-Command msbuild -ErrorAction SilentlyContinue) {
    $generator = ""
} else {
    $generator = "MinGW Makefiles"
}
Write-Host ("[OK]   Generator: " + ($generator -replace "Makefiles", "Makefiles" -replace "MinGW ", "MinGW ")) -ForegroundColor Green

# Step 4: Configure cmake
$buildDir = Join-Path $PSScriptRoot "build"
if (-not (Test-Path $buildDir)) {
    New-Item -ItemType Directory -Path $buildDir | Out-Null
}
Set-Location $buildDir

$cacheFile = Join-Path $buildDir "CMakeCache.txt"
if (Test-Path $cacheFile) {
    Remove-Item $cacheFile -Force
    Write-Host "[OK]   removed stale CMakeCache.txt" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "--- cmake configure ---" -ForegroundColor Cyan
if ($generator) {
    cmake $PSScriptRoot -G $generator
} else {
    cmake $PSScriptRoot
}
if ($LASTEXITCODE -ne 0) {
    Write-Host "[FAIL] cmake configure failed" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   cmake configure" -ForegroundColor Green

# Step 5: Build all targets
Write-Host ""
Write-Host "--- build ---" -ForegroundColor Cyan
$make = Get-Command mingw32-make -ErrorAction SilentlyContinue
if (-not $make) {
    $make = Get-Command make -ErrorAction SilentlyContinue
}
if (-not $make -and $ccDir) {
    $makePath = Join-Path $ccDir "mingw32-make.exe"
    if (Test-Path $makePath) { $make = $makePath }
}
if ($make) {
    if ($make -is [System.Management.Automation.CommandInfo]) { & $make } else { & $make }
} else {
    cmake --build .
}
if ($LASTEXITCODE -ne 0) {
    Write-Host "[FAIL] build failed" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   build" -ForegroundColor Green

# Step 6: Test
Write-Host ""
Write-Host "--- test ---" -ForegroundColor Cyan
$ctest = Get-Command ctest -ErrorAction SilentlyContinue
if (-not $ctest) { $ctest = Get-Command (Join-Path (Split-Path $cmake.Source -Parent) "ctest.exe") -ErrorAction SilentlyContinue }
if ($ctest) {
    & $ctest --output-on-failure
} else {
    cmake --build . --target test
}
if ($LASTEXITCODE -ne 0) {
    Write-Host "[FAIL] some tests failed" -ForegroundColor Red
    exit 1
}
Write-Host "[OK]   all tests passed" -ForegroundColor Green

# Step 7: Package DLL
Write-Host ""
Write-Host "--- package ---" -ForegroundColor Cyan
$dllSrc = Join-Path $buildDir "bin\libccms.dll"
$pkgDir = Join-Path $PSScriptRoot "dist"
if (-not (Test-Path $pkgDir)) {
    New-Item -ItemType Directory -Path $pkgDir | Out-Null
}
if (Test-Path $dllSrc) {
    Copy-Item $dllSrc (Join-Path $pkgDir "libccms.dll") -Force
    Write-Host "[OK]   packaged libccms.dll -> dist/" -ForegroundColor Green
} else {
    Write-Host "[WARN] libccms.dll not found at $dllSrc" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== ccms build complete ===" -ForegroundColor Cyan
