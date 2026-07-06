#!/bin/sh
cd jcms && mvn compile -pl jcms-utils -am -q
echo "[OK] jcms-utils 编译完成"
