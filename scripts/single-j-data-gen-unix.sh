#!/bin/sh
cd csasn1
rm -rf ../jcms/jcms-data 2>/dev/null
cargo run --release -- --src specs/dlt2811.asn --dest ../jcms/jcms-data --prefix Inner --enc aper --package com.ysh.jcms.data
echo "[OK] jcms-data 生成完成"
