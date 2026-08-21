Push-Location csasn1
if (Test-Path ..\jcms\jcms-data) { Remove-Item -Recurse -Force -ErrorAction SilentlyContinue ..\jcms\jcms-data }
cargo run --release -- --src specs/dlt2811.asn --dest ..\jcms\jcms-data --prefix Inner --enc aper --package com.ysh.jcms.data
$exit = $LASTEXITCODE
Pop-Location
if ($exit -ne 0) { Write-Error "jcms-data 生成失败（cargo 未安装或编译错误）"; exit 1 }
# asn1.dll 已由生成器自动部署到 jcms-data/src/main/resources/win32-x86-64/
Write-Host "[OK] jcms-data 生成完成（native 库已部署至 resources\win32-x86-64）" -ForegroundColor Green
