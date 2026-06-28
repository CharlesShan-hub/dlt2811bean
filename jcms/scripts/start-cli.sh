#!/usr/bin/env bash
# 启动 CMS CLI 客户端（日志只显示 WARN+）
# 用法: ./scripts/start-cli.sh

set -euo pipefail

cd "$(dirname "$0")/.."

mvn -q install -DskipTests -pl jcms-app -am
mvn -q exec:java -pl jcms-app \
    -Dlogback.configurationFile=jcms-app/src/main/resources/logback-cli.xml \
    -Dexec.mainClass=com.ysh.jcms.app.console.CmsConsole
