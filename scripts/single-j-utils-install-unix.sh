#!/bin/sh
cd jcms && mvn install -pl jcms-utils -am -q -DskipTests
echo "[OK] jcms-utils 打包并安装完成"
