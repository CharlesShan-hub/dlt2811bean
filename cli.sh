#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

export JAVA_HOME="${JAVA_HOME:-$(dirname "$(dirname "$(readlink -f "$(which java)") )")}"
export PATH="$JAVA_HOME/bin:$PATH"

if [ $# -gt 0 ]; then
    # 一次性执行模式：把所有参数传给 CLI
    exec mvn exec:java@cms-cli-exec -q -Dexec.args="$*"
else
    # 交互式 Shell
    exec mvn exec:java@cms-cli -q
fi