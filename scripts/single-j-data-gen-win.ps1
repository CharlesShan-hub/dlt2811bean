Push-Location csasn1
if (Test-Path ..\jcms\jcms-data) { Remove-Item -Recurse -Force -ErrorAction SilentlyContinue ..\jcms\jcms-data }
cargo run --release -- --src specs/dlt2811.asn --dest ..\jcms\jcms-data --prefix Inner --enc aper --package com.ysh.jcms.data
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] jcms-data 生成完成" -ForegroundColor Green
