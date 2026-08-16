Push-Location csasn1
if (Test-Path ..\jcms\jcms-data) { Remove-Item -Recurse -Force -ErrorAction SilentlyContinue ..\jcms\jcms-data }
cargo run --release -- --src specs/dlt2811.asn --dest ..\jcms\jcms-data --prefix Inner --enc aper --package com.ysh.jcms.data
if ($LASTEXITCODE -ne 0) { Pop-Location; Write-Error "jcms-data 生成失败（cargo 未安装或编译错误）"; exit 1 }

# Deploy the native codec library into the JNA platform dir of jcms-data.
$ResDir = "..\jcms\jcms-data\src\main\resources\win32-x86-64"
$LibCandidates = @("target\release\asn1.dll", "target\release\libasn1.dll")
$Lib = $LibCandidates | Where-Object { Test-Path $_ } | Select-Object -First 1
if (-not $Lib) { Pop-Location; Write-Error "未找到 native 库（asn1.dll），请先构建 csasn1"; exit 1 }
New-Item -ItemType Directory -Force -Path $ResDir | Out-Null
Copy-Item $Lib $ResDir
Pop-Location
Write-Host "[OK] jcms-data 生成完成（native 库已部署至 resources\win32-x86-64）" -ForegroundColor Green
