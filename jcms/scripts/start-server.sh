#!/usr/bin/env bash
# 启动 CMS 服务端
# 用法: ./scripts/start-server.sh [端口号]

set -euo pipefail

PORT="${1:-18780}"
cd "$(dirname "$0")/.."

mvn -q install -DskipTests -pl jcms-app -am
mvn -q exec:java -pl jcms-app \
    -Dexec.mainClass=com.ysh.jcms.app.node.CmsServerCli \
    -Dexec.args="$PORT"
