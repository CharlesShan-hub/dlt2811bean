#!/bin/sh
# Generate jcms-data (Java classes) from the DL/T 2811 ASN.1 spec.
# The native codec library is deployed by the generator itself.
cd csasn1
rm -rf ../jcms/jcms-data 2>/dev/null
cargo run --release -- --src specs/dlt2811.asn --dest ../jcms/jcms-data --prefix Inner --enc aper --package com.ysh.jcms.data
if [ $? -ne 0 ]; then
    echo "[ERROR] jcms-data 生成失败（cargo 未安装或编译错误）" >&2
    exit 1
fi
echo "[OK] jcms-data 生成完成（native 库已由生成器部署）"
