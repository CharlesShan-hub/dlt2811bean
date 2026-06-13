#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$SCRIPT_DIR/build"

echo "=== ccms build script ==="
echo ""

# ---------------------------------------------------------------------------
# Step 1: Check cmake
# ---------------------------------------------------------------------------
if ! command -v cmake &>/dev/null; then
    echo "[FAIL] cmake not found. Install it:"
    echo "       brew install cmake         (macOS)"
    echo "       sudo apt install cmake     (Debian/Ubuntu)"
    echo "       sudo dnf install cmake     (Fedora)"
    exit 1
fi
echo "[OK]   cmake $(cmake --version | head -1)"

# ---------------------------------------------------------------------------
# Step 2: Check C compiler (clang or gcc)
# ---------------------------------------------------------------------------
CC=""
if command -v clang &>/dev/null; then
    CC=clang
elif command -v gcc &>/dev/null; then
    CC=gcc
fi
if [ -z "$CC" ]; then
    echo "[FAIL] No C compiler found (clang or gcc). Install it:"
    echo "       macOS: xcode-select --install"
    echo "       Linux: sudo apt install build-essential"
    exit 1
fi
echo "[OK]   $($CC --version | head -1)"

# ---------------------------------------------------------------------------
# Step 3: Detect generator (Ninja or Unix Makefiles)
# ---------------------------------------------------------------------------
GENERATOR=""
if command -v ninja &>/dev/null; then
    GENERATOR="Ninja"
    echo "[OK]   Generator: Ninja"
elif command -v make &>/dev/null; then
    GENERATOR="Unix Makefiles"
    echo "[OK]   Generator: Unix Makefiles"
else
    echo "[FAIL] no build tool found (install ninja or make)"
    exit 1
fi

# ---------------------------------------------------------------------------
# Step 4: Configure cmake
# ---------------------------------------------------------------------------
mkdir -p "$BUILD_DIR"
CACHE_FILE="$BUILD_DIR/CMakeCache.txt"
if [ -f "$CACHE_FILE" ]; then
    rm -f "$CACHE_FILE"
    echo "[OK]   removed stale CMakeCache.txt"
fi

cd "$BUILD_DIR"
echo ""
echo "--- cmake configure ---"
cmake "$SCRIPT_DIR" -G "$GENERATOR"
if [ $? -ne 0 ]; then
    echo "[FAIL] cmake configure failed"
    exit 1
fi
echo "[OK]   cmake configure"

# ---------------------------------------------------------------------------
# Step 5: Build all targets
# ---------------------------------------------------------------------------
echo ""
echo "--- build ---"
NPROC="$(nproc 2>/dev/null || sysctl -n hw.ncpu 2>/dev/null || echo 4)"
if command -v ninja &>/dev/null; then
    ninja -j"$NPROC"
elif command -v make &>/dev/null; then
    make -j"$NPROC"
else
    cmake --build . -j "$NPROC"
fi
if [ $? -ne 0 ]; then
    echo "[FAIL] build failed"
    exit 1
fi
echo "[OK]   build"

# ---------------------------------------------------------------------------
# Step 6: Test
# ---------------------------------------------------------------------------
echo ""
echo "--- test ---"
if command -v ctest &>/dev/null; then
    ctest --output-on-failure
else
    cmake --build . --target test
fi
if [ $? -ne 0 ]; then
    echo "[FAIL] some tests failed"
    exit 1
fi
echo "[OK]   all tests passed"

# ---------------------------------------------------------------------------
# Step 7: Package shared library
# ---------------------------------------------------------------------------
echo ""
echo "--- package ---"
PKG_DIR="$SCRIPT_DIR/dist"
mkdir -p "$PKG_DIR"

SRC_NAME=""
OUT_NAME=""
if [ -f "$BUILD_DIR/bin/libccms.dylib" ]; then
    SRC_NAME="libccms.dylib"
    OUT_NAME="ccms.dylib"
elif [ -f "$BUILD_DIR/bin/libccms.so" ]; then
    SRC_NAME="libccms.so"
    OUT_NAME="ccms.so"
elif [ -f "$BUILD_DIR/libccms.dylib" ]; then
    SRC_NAME="libccms.dylib"
    OUT_NAME="ccms.dylib"
elif [ -f "$BUILD_DIR/libccms.so" ]; then
    SRC_NAME="libccms.so"
    OUT_NAME="ccms.so"
fi

if [ -n "$SRC_NAME" ]; then
    if [ -f "$BUILD_DIR/bin/$SRC_NAME" ]; then
        cp "$BUILD_DIR/bin/$SRC_NAME" "$PKG_DIR/$OUT_NAME"
    else
        cp "$BUILD_DIR/$SRC_NAME" "$PKG_DIR/$OUT_NAME"
    fi
    echo "[OK]   packaged $OUT_NAME -> dist/"
else
    echo "[WARN] shared library not found (looked for libccms.dylib / libccms.so)"
    echo "       built products in: $BUILD_DIR/bin/"
fi

echo ""
echo "=== ccms build complete ==="
