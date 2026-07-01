#!/usr/bin/env bash
# 启动 CMS 服务端
# 用法:
#   ./scripts/start-server.sh                         # 默认端口
#   ./scripts/start-server.sh --port 18780            # 指定端口
#
# 端口从 application.yaml 配置读取，此处仅用于 exec.args 传递

set -euo pipefail

cd "$(dirname "$0")/.."

# Parse args
port=""
while [[ $# -gt 0 ]]; do
    case "$1" in
        --port) port="$2"; shift 2 ;;
        --help|-h) echo "Usage: $0 [--port <port>]"; exit 0 ;;
        *) echo "Unknown option: $1"; exit 1 ;;
    esac
done

# Kill any old server process
old_pids=$(ps aux | grep 'CmsServerConsole' | grep -v grep | awk '{print $2}' 2>/dev/null || true)
if [ -n "$old_pids" ]; then
    echo "Killing old server processes: $old_pids"
    kill $old_pids 2>/dev/null || true
    sleep 2
fi

exec_args=""
if [ -n "$port" ]; then
    exec_args="-Dexec.args=$port"
fi

mvn -q install -DskipTests -pl jcms-app -am
mvn -q exec:java -pl jcms-app \
    -Dexec.mainClass=com.ysh.jcms.app.console.CmsServerConsole \
    $exec_args
