#!/bin/sh
cd jcms && mvn compile -pl jcms-app -am -q
echo "[OK] jcms-app 编译完成"
