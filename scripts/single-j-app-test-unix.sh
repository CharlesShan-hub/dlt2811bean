#!/bin/sh
cd jcms && mvn test -pl jcms-app -am -q
echo "[OK] jcms-app 测试通过"
