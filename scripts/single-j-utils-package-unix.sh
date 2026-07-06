#!/bin/sh
cd jcms && mvn package -pl jcms-utils -am -q -DskipTests
echo "[OK] jcms-utils 打包完成"
