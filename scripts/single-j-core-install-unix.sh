#!/bin/sh
cd jcms && mvn install -pl jcms-core -q -DskipTests
echo "[OK] jcms-core 打包并安装完成"
