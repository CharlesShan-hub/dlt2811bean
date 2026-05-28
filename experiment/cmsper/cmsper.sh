#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$SCRIPT_DIR/build"

echo "=== cmsper build script ==="
echo ""

# Check cmake
if ! command -v cmake &>/dev/null; then
    echo "[FAIL] cmake not found"
    exit 1
fi
echo "[OK]   cmake $(cmake --version | head -1)"

# Check C compiler
if command -v clang &>/dev/null; then
    CC=clang
elif command -v gcc &>/dev/null; then
    CC=gcc
else
    echo "[FAIL] no C compiler found (install gcc or clang)"
    exit 1
fi
echo "[OK]   $($CC --version | head -1)"

# Configure
mkdir -p "$BUILD_DIR"
cd "$BUILD_DIR"
echo ""
echo "--- cmake configure ---"
cmake "$SCRIPT_DIR" -G "Unix Makefiles"
echo "[OK]   cmake configure"

# Build
echo ""
echo "--- build ---"
make -j"$(nproc 2>/dev/null || sysctl -n hw.ncpu 2>/dev/null || echo 4)"
echo "[OK]   build"

# Test
echo ""
echo "--- test ---"
ctest --output-on-failure
echo ""
echo "[OK]   all tests passed"
echo ""
echo "=== cmsper build complete ==="
