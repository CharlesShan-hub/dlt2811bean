#!/bin/sh
set -e

BUILD_DIR="ccms/build"
SCRIPT_DIR="ccms"

# 来源 ccms.sh L57: 创建 build 目录
mkdir -p "$BUILD_DIR"

# 来源 ccms.sh L58-62: 删除旧 CMakeCache
CACHE_FILE="$BUILD_DIR/CMakeCache.txt"
if [ -f "$CACHE_FILE" ]; then
    rm -f "$CACHE_FILE"
fi

# 来源 ccms.sh L67: cmake 配置
cd "$BUILD_DIR"
GENERATOR="Unix Makefiles"
if command -v ninja &>/dev/null; then
    GENERATOR="Ninja"
fi
cmake "$SCRIPT_DIR/.." -G "$GENERATOR" -DCMAKE_EXPORT_COMPILE_COMMANDS=ON
if [ $? -ne 0 ]; then exit 1; fi

# 来源 ccms.sh L79-86: 编译
NPROC="$(nproc 2>/dev/null || sysctl -n hw.ncpu 2>/dev/null || echo 4)"
if command -v ninja &>/dev/null; then
    ninja -j"$NPROC"
else
    make -j"$NPROC"
fi
if [ $? -ne 0 ]; then exit 1; fi
cd ../..

# 来源 ccms.sh L110-139: 打包到 dist
PKG_DIR="ccms/dist"
mkdir -p "$PKG_DIR"

SRC=""
OUT=""
if [ -f "$BUILD_DIR/bin/libccms.dylib" ]; then
    cp "$BUILD_DIR/bin/libccms.dylib" "$PKG_DIR/ccms.dylib"
elif [ -f "$BUILD_DIR/bin/libccms.so" ]; then
    cp "$BUILD_DIR/bin/libccms.so" "$PKG_DIR/ccms.so"
fi

echo "[OK] ccms 构建完成"
