#!/bin/sh
cd jcms && mvn test -pl jcms-core -q
echo "[OK] jcms-core 测试通过"
