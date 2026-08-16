#!/bin/sh
# Generate jcms-data (Java classes) from the DL/T 2811 ASN.1 spec, then deploy
# the native codec library so JNA can load it at runtime.
cd csasn1
rm -rf ../jcms/jcms-data 2>/dev/null
cargo run --release -- --src specs/dlt2811.asn --dest ../jcms/jcms-data --prefix Inner --enc aper --package com.ysh.jcms.data
if [ $? -ne 0 ]; then
    echo "[ERROR] jcms-data 生成失败（cargo 未安装或编译错误）" >&2
    exit 1
fi

# Deploy the platform native library into the JNA platform dir of jcms-data.
case "$(uname -s)" in
    Darwin)
        RES=darwin
        LIB=target/release/libasn1.dylib
        ;;
    Linux)
        RES=linux-x86-64
        LIB=target/release/libasn1.so
        ;;
    *)
        echo "[ERROR] 不支持的平台: $(uname -s)" >&2
        exit 1
        ;;
esac

if [ ! -f "$LIB" ]; then
    echo "[ERROR] 未找到 native 库 $LIB，请先构建 csasn1" >&2
    exit 1
fi
mkdir -p ../jcms/jcms-data/src/main/resources/$RES
cp "$LIB" ../jcms/jcms-data/src/main/resources/$RES/
echo "[OK] jcms-data 生成完成（native 库已部署至 resources/$RES）"
