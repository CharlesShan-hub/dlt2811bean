#!/usr/bin/env bash
# 启动 CMS 服务端 (端口从 application.yaml 配置读取)

set -euo pipefail
cd "$(dirname "$0")/.."

mvn -q clean install -DskipTests -pl jcms-utils,jcms-core,jcms-app -am
mvn -q exec:java -pl jcms-app \
    -Dexec.mainClass=com.ysh.jcms.app.console.CmsServerConsole
