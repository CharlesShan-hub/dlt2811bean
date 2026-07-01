#!/usr/bin/env bash
# CMS 远程命令执行脚本
# 将命令发送给本地 CMS CLI 的 API 服务器执行。
#
# 用法:
#   ./scripts/cms.sh connect --ap C_B5041X/S1
#   ./scripts/cms.sh sgcb-vals --refs "LD0/LLN0.SG1"
#   ./scripts/cms.sh --port 7899 connect --ap C_B5041X/S1
#
# 要求: CMS CLI (start-cli.sh) 必须在运行中。

set -euo pipefail

port=7899
status=false
help=false
cmd_args=()

while [[ $# -gt 0 ]]; do
    case "$1" in
        --port)
            port="$2"; shift 2 ;;
        --status)
            status=true; shift ;;
        --help|-h)
            help=true; shift ;;
        *)
            cmd_args+=("$1"); shift ;;
    esac
done

if $help; then
    echo "CMS 远程命令执行"
    echo "用法: $0 [--port <端口>] <命令> [参数...]"
    echo "  $0 --status"
    echo "示例:"
    echo "  $0 connect --ap C_B5041X/S1"
    echo "  $0 data-dir --ref LD0/LLN0"
    exit 0
fi

if $status; then
    resp=$(curl -s --max-time 10 "http://127.0.0.1:${port}/api/status" 2>&1) || {
        echo "无法连接 CMS CLI API 服务器 (127.0.0.1:${port})" >&2
        exit 1
    }
    echo "$resp"
    exit 0
fi

if [ ${#cmd_args[@]} -eq 0 ]; then
    echo "用法: $0 connect --ap C_B5041X/S1" >&2
    exit 1
fi

# Build command line string, quoting args with spaces
cmd_line=()
for arg in "${cmd_args[@]}"; do
    if [[ "$arg" =~ [[:space:]\"] ]]; then
        # Escape double quotes inside and wrap in double quotes
        escaped="${arg//\"/\\\"}"
        cmd_line+=("\"${escaped}\"")
    else
        cmd_line+=("$arg")
    fi
done

# Join with spaces
IFS=' ' joined="${cmd_line[*]}"

resp=$(curl -s --max-time 30 \
    -X POST \
    -d "cmd=${joined}" \
    "http://127.0.0.1:${port}/api/execute" 2>&1) || {
    echo "无法连接 CMS CLI API 服务器 (127.0.0.1:${port})" >&2
    echo "请确保 CLI 已在运行 (start-cli.sh)。" >&2
    exit 1
}

echo "$resp"
