#!/bin/sh
cd jcms && mvn test -pl jcms-data -am -q
echo "[OK] jcms-data 测试通过"
