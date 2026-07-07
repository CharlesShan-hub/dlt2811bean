#!/bin/sh
cd jcms && mvn package -pl jcms-app -am -q -DskipTests
echo "[OK] jcms-app 打包完成"
