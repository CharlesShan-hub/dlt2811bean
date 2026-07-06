#!/bin/sh
set -e

# 直接运行编译好的测试程序
./ccms/build/bin/test_per
./ccms/build/bin/test_scalar
echo "[OK] ccms 测试通过"
