#!/bin/sh
# 来源 ccms.sh L13-19: check cmake
if ! command -v cmake >/dev/null 2>&1; then
    echo "[FAIL] cmake not found"
    exit 1
fi

# 来源 ccms.sh L25-30: check C compiler
if ! command -v clang >/dev/null 2>&1; then
    if ! command -v gcc >/dev/null 2>&1; then
        echo "[FAIL] No C compiler found (clang or gcc)"
        exit 1
    fi
fi

# 来源 ccms.sh L43-52: check make
if ! command -v make >/dev/null 2>&1; then
    echo "[FAIL] make not found"
    exit 1
fi

echo "[OK] 工具链就绪"
