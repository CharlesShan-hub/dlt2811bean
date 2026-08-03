#!/bin/sh
cd jcms && mvn install -pl jcms-app -am -q -DskipTests
echo "[OK] jcms-app 打包并安装完成"
