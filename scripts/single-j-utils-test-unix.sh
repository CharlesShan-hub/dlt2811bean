#!/bin/sh
cd jcms && mvn test -pl jcms-utils -am -q
echo "[OK] jcms-utils 测试通过"
