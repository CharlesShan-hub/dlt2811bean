#!/usr/bin/env bash
# 启动 CMS CLI 客户端，支持远程命令执行
# 用法:
#   ./scripts/start-cli.sh                           # 启动交互式 CLI
#   ./scripts/start-cli.sh --connect C_B5041X/S1     # 启动后自动连接
# 远程命令模式（需要 CLI 已在运行）:
#   ./scripts/cms.sh connect --ap C_B5041X/S1

set -euo pipefail

cd "$(dirname "$0")/.."

# Parse args
connect=""
while [[ $# -gt 0 ]]; do
    case "$1" in
        --connect) connect="$2"; shift 2 ;;
        --help|-h) echo "Usage: $0 [--connect <IED/AccessPoint>]"; exit 0 ;;
        *) echo "Unknown option: $1"; exit 1 ;;
    esac
done

# Kill any old CLI process
old_pids=$(ps aux | grep 'CmsClientConsole' | grep -v grep | awk '{print $2}' 2>/dev/null || true)
if [ -n "$old_pids" ]; then
    echo "Killing old CLI processes: $old_pids"
    kill $old_pids 2>/dev/null || true
    sleep 1
fi

auto_exec=""
if [ -n "$connect" ]; then
    auto_exec="connect --ap ${connect};"
    export CMS_AUTO_EXEC="$auto_exec"
    echo "Auto-exec: $auto_exec"
fi

mvn -q install -DskipTests -pl jcms-app -am
mvn -q exec:java -pl jcms-app \
    -Dlogback.configurationFile=jcms-app/src/main/resources/logback-cli.xml \
    -Dexec.mainClass=com.ysh.jcms.app.console.CmsClientConsole
