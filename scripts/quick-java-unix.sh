#!/bin/sh
cd jcms && mvn install -DskipTests -q
echo "[OK] Java 全量编译完成"
