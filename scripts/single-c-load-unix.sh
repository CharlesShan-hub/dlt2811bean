#!/bin/sh
set -e

cp ccms/dist/ccms.dylib jcms/jcms-core/src/main/resources/darwin-x86-64/ccms.dylib
echo "[OK] ccms 已加载到 JCMS"
