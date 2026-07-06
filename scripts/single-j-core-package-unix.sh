#!/bin/sh
cd jcms && mvn package -pl jcms-core -q -DskipTests
echo "[OK] jcms-core 打包完成"
