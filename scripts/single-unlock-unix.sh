#!/bin/sh
mkdir -p build
printf "1" > build/lock
echo "[OK] 已解锁 (build/lock = 1)"
