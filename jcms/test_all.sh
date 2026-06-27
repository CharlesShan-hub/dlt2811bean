#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"

echo "===== 1. build ccms ====="
cd "$ROOT/../ccms"
bash ./ccms.sh

echo "===== 2. copy ccms lib -> jcms-core resources ====="
UNAME_S="$(uname -s)"
UNAME_M="$(uname -m)"

case "$UNAME_S" in
    Darwin)
        SRC="ccms.dylib"
        JNA_NAME="libccms.dylib"
        case "$UNAME_M" in
            arm64)  JNA_DIR="darwin-aarch64"  ;;
            x86_64) JNA_DIR="darwin-x86-64"   ;;
            *)      echo "[FAIL] unsupported arch: $UNAME_M"; exit 1 ;;
        esac
        ;;
    Linux)
        SRC="ccms.so"
        JNA_NAME="libccms.so"
        JNA_DIR="linux-x86-64"
        ;;
    *)
        echo "[FAIL] unsupported OS: $UNAME_S"
        exit 1
        ;;
esac

LIB_SRC="$ROOT/../ccms/dist/$SRC"
DST_DIR="$ROOT/jcms-core/src/main/resources/$JNA_DIR"
if [ ! -f "$LIB_SRC" ]; then
    echo "[FAIL] $SRC not found at $LIB_SRC"
    exit 1
fi
mkdir -p "$DST_DIR"
cp "$LIB_SRC" "$DST_DIR/$JNA_NAME"
echo "[OK] copied to $DST_DIR/$JNA_NAME"

echo "===== 3. install parent POM ====="
cd "$ROOT"
mvn install -N --batch-mode 2>&1 | grep -E "BUILD|FAILURE|ERROR"
if [ $? -ne 0 ]; then
    echo "[FAIL] parent POM install failed"
    exit 1
fi

echo "===== 4. install jcms-core (skip tests) ====="
cd "$ROOT/jcms-core"
mvn install -DskipTests --batch-mode 2>&1 | grep -E "BUILD|FAILURE|ERROR"
if [ $? -ne 0 ]; then
    echo "[FAIL] jcms-core install failed"
    exit 1
fi

echo "===== 5. install jcms-utils (skip tests) ====="
cd "$ROOT/jcms-utils"
mvn install -DskipTests --batch-mode 2>&1 | grep -E "BUILD|FAILURE|ERROR"
if [ $? -ne 0 ]; then
    echo "[FAIL] jcms-utils install failed"
    exit 1
fi

echo "===== 6. test jcms-app ====="
cd "$ROOT/jcms-app"
mvn test --batch-mode 2>&1 | tail -20

echo "===== all done ====="
