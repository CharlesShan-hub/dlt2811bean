#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

JAVA_HOME="${JAVA_HOME:-}"
if [ -z "$JAVA_HOME" ]; then
    JAVA_PATH="$(which java 2>/dev/null || true)"
    if [ -n "$JAVA_PATH" ] && [ -x "$JAVA_PATH" ]; then
        # Resolve symlinks, compatible with both Linux (readlink -f) and macOS (greadlink or manual loop)
        if command -v greadlink >/dev/null 2>&1; then
            JAVA_PATH="$(greadlink -f "$JAVA_PATH")"
        elif command -v readlink >/dev/null 2>&1; then
            JAVA_PATH="$(readlink -f "$JAVA_PATH" 2>/dev/null || readlink "$JAVA_PATH" 2>/dev/null || echo "$JAVA_PATH")"
        fi
        JAVA_HOME="$(dirname "$(dirname "$JAVA_PATH")")"
    fi
fi
export JAVA_HOME
export PATH="$JAVA_HOME/bin:$PATH"

if [ $# -gt 0 ]; then
    # 一次性执行模式：把所有参数传给 CLI
    exec mvn exec:java@cms-cli-exec -q -Dexec.args="$*"
else
    # 交互式 Shell
    exec mvn exec:java@cms-cli -q
fi