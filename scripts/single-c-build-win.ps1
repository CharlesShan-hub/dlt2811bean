$env:Path = "$env:USERPROFILE\scoop\shims;$env:Path"

# 来源 win_ccms.ps1 L86-88: 创建 build 目录
if (-not (Test-Path ccms\build)) { New-Item -ItemType Directory -Path ccms\build | Out-Null }

# 来源 win_ccms.ps1 L92-93: 删除旧 CMakeCache
if (Test-Path ccms\build\CMakeCache.txt) { Remove-Item ccms\build\CMakeCache.txt -Force }

# 来源 win_ccms.ps1 L99-100: cmake 配置
Push-Location ccms\build
cmake .. -G "MinGW Makefiles" -DCMAKE_EXPORT_COMPILE_COMMANDS=ON
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }

# 来源 win_ccms.ps1 L110-113: 同步 compile_commands.json
if (Test-Path compile_commands.json) { Copy-Item compile_commands.json ..\ -Force }

# 来源 win_ccms.ps1 L128-131: 编译
mingw32-make
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location

# 来源 win_ccms.ps1 L158-164: 打包到 dist
if (-not (Test-Path ccms\dist)) { New-Item -ItemType Directory -Path ccms\dist | Out-Null }
if (Test-Path ccms\build\bin\libccms.dll) { Copy-Item ccms\build\bin\libccms.dll ccms\dist\ccms.dll -Force }

Write-Host "[OK] ccms 构建完成" -ForegroundColor Green
