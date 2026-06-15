#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"

echo "=== step 1: build ccms ==="
cd "$ROOT/../ccms"
bash ./ccms.sh

echo "=== step 2: copy ccms lib -> jcms-core resources ==="
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

echo "=== step 3: mvn clean test -pl jcms-core ==="
cd "$ROOT"
mvn clean test -pl jcms-core

echo "=== all done ==="
