#!/bin/sh
mkdir -p build
printf "0" > build/lock
echo "[OK] 已锁定 (build/lock = 0)"
