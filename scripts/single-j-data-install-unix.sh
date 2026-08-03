#!/bin/sh
cd jcms && mvn install -pl jcms-data -q -DskipTests
echo "[OK] jcms-data 打包并安装完成"
