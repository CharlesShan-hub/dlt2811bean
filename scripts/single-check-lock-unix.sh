#!/bin/sh
echo "[WAIT] 等待解锁..."
while [ "$(cat build/lock 2>/dev/null)" != "1" ]; do
    sleep 0.2
done
echo "[OK] 解锁完成"
