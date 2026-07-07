# ──────────────────────────────────────────────
#  DL/T 2811 (CMS) 项目构建命令
#  `just` — https://github.com/casey/just
#
#  single-c-xxx → C 原子级
#  single-j-xxx → Java 原子级
#  xxx          → 批量级（组合 single-*）
# ──────────────────────────────────────────────

set shell := ["cmd.exe", "/C"]

_default:
    just --list

# ╔═══════════════════════════════════════════╗
# ║  CCMS — C 动态库                          ║
# ╚═══════════════════════════════════════════╝

[windows]
single-c-clean:
    powershell -NoProfile -File scripts\single-c-clean-win.ps1

[unix]
single-c-clean:
    chmod +x scripts/single-c-clean-unix.sh && ./scripts/single-c-clean-unix.sh

[windows]
single-c-check-env:
    powershell -NoProfile -File scripts\single-c-check-env-win.ps1

[unix]
single-c-check-env:
    chmod +x scripts/single-c-check-env-unix.sh && ./scripts/single-c-check-env-unix.sh

[windows]
single-c-build:
    powershell -NoProfile -File scripts\single-c-build-win.ps1

[unix]
single-c-build:
    chmod +x scripts/single-c-build-unix.sh && ./scripts/single-c-build-unix.sh

[windows]
single-c-test:
    powershell -NoProfile -File scripts\single-c-test-win.ps1

[unix]
single-c-test:
    chmod +x scripts/single-c-test-unix.sh && ./scripts/single-c-test-unix.sh

[windows]
single-c-load:
    copy /Y ccms\dist\ccms.dll jcms\jcms-core\src\main\resources\win32-x86-64\ccms.dll

[unix]
single-c-load:
    chmod +x scripts/single-c-load-unix.sh && ./scripts/single-c-load-unix.sh

# ── C 批量级 ──

c-rebuild: single-c-clean single-c-check-env single-c-build single-c-test single-c-load
c-quick: single-c-clean single-c-build single-c-load

# ╔═══════════════════════════════════════════╗
# ║  JCMS — Java 应用                         ║
# ╚═══════════════════════════════════════════╝

# ── 通用 ──

[windows]
single-j-all-clean:
    powershell -NoProfile -File scripts\single-j-all-clean-win.ps1

[unix]
single-j-all-clean:
    chmod +x scripts/single-j-all-clean-unix.sh && ./scripts/single-j-all-clean-unix.sh

# ── jcms-core ──

[windows]
single-j-core-compile:
    powershell -NoProfile -File scripts\single-j-core-compile-win.ps1

[unix]
single-j-core-compile:
    chmod +x scripts/single-j-core-compile-unix.sh && ./scripts/single-j-core-compile-unix.sh

[windows]
single-j-core-test:
    powershell -NoProfile -File scripts\single-j-core-test-win.ps1

[unix]
single-j-core-test:
    chmod +x scripts/single-j-core-test-unix.sh && ./scripts/single-j-core-test-unix.sh

[windows]
single-j-core-package:
    powershell -NoProfile -File scripts\single-j-core-package-win.ps1

[unix]
single-j-core-package:
    chmod +x scripts/single-j-core-package-unix.sh && ./scripts/single-j-core-package-unix.sh

# ── jcms-utils ──

[windows]
single-j-utils-compile:
    powershell -NoProfile -File scripts\single-j-utils-compile-win.ps1

[unix]
single-j-utils-compile:
    chmod +x scripts/single-j-utils-compile-unix.sh && ./scripts/single-j-utils-compile-unix.sh

[windows]
single-j-utils-test:
    powershell -NoProfile -File scripts\single-j-utils-test-win.ps1

[unix]
single-j-utils-test:
    chmod +x scripts/single-j-utils-test-unix.sh && ./scripts/single-j-utils-test-unix.sh

[windows]
single-j-utils-package:
    powershell -NoProfile -File scripts\single-j-utils-package-win.ps1

[unix]
single-j-utils-package:
    chmod +x scripts/single-j-utils-package-unix.sh && ./scripts/single-j-utils-package-unix.sh

# ── jcms-app ──

[windows]
single-j-app-compile:
    powershell -NoProfile -File scripts\single-j-app-compile-win.ps1

[unix]
single-j-app-compile:
    chmod +x scripts/single-j-app-compile-unix.sh && ./scripts/single-j-app-compile-unix.sh

[windows]
single-j-app-test:
    powershell -NoProfile -File scripts\single-j-app-test-win.ps1

[unix]
single-j-app-test:
    chmod +x scripts/single-j-app-test-unix.sh && ./scripts/single-j-app-test-unix.sh

[windows]
single-j-app-package:
    powershell -NoProfile -File scripts\single-j-app-package-win.ps1

[unix]
single-j-app-package:
    chmod +x scripts/single-j-app-package-unix.sh && ./scripts/single-j-app-package-unix.sh

# 批量级

j-core-compile: single-j-all-clean single-j-core-compile
j-core-test: j-core-compile single-j-core-test
j-core-test-c: c-quick j-core-test

j-utils-compile: single-j-all-clean single-j-utils-compile
j-utils-test: j-utils-compile single-j-utils-test
j-utils-test-c: c-quick j-utils-compile single-j-core-test single-j-utils-test

j-all-compile: single-j-all-clean single-j-app-compile
j-all-test: j-all-compile single-j-app-test
j-all-test-c: c-quick j-all-compile single-j-core-test single-j-utils-test single-j-app-test

# ── 锁机制（进程间同步） ──

[windows]
single-lock:
    powershell -NoProfile -File scripts\single-lock-win.ps1

[unix]
single-lock:
    chmod +x scripts/single-lock-unix.sh && ./scripts/single-lock-unix.sh

[windows]
single-unlock:
    powershell -NoProfile -File scripts\single-unlock-win.ps1

[unix]
single-unlock:
    chmod +x scripts/single-unlock-unix.sh && ./scripts/single-unlock-unix.sh

[windows]
single-check-lock:
    powershell -NoProfile -File scripts\single-check-lock-win.ps1

[unix]
single-check-lock:
    chmod +x scripts/single-check-lock-unix.sh && ./scripts/single-check-lock-unix.sh

# ── 运行（不编译，需先 java-quick） ──

[windows]
single-server:
    powershell -NoProfile -File scripts\single-server-win.ps1

[unix]
single-server:
    chmod +x scripts/single-server-unix.sh && ./scripts/single-server-unix.sh

[windows]
single-client:
    powershell -NoProfile -File scripts\single-client-win.ps1

[unix]
single-client:
    chmod +x scripts/single-client-unix.sh && ./scripts/single-client-unix.sh

# ╔═══════════════════════════════════════════╗
# ║  顶层工作流                                ║
# ╚═══════════════════════════════════════════╝

[windows]
java-quick:
    powershell -NoProfile -File scripts\quick-java-win.ps1

[unix]
java-quick:
    chmod +x scripts/quick-java-unix.sh && ./scripts/quick-java-unix.sh

run-server: single-check-lock single-server
run-client: single-check-lock single-client
build-java-run-server:single-lock java-quick single-unlock run-server
build-java-run-client:single-lock java-quick single-unlock run-client
build-c-java-run-server:single-lock c-quick java-quick single-unlock run-server
build-c-java-run-client:single-lock c-quick java-quick single-unlock run-client
