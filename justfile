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

# ── 批量级 ──

c-rebuild: single-c-clean single-c-check-env single-c-build single-c-test single-c-load
c-quick: single-c-clean single-c-build single-c-load

# ╔═══════════════════════════════════════════╗
# ║  JCMS — Java 应用                         ║
# ╚═══════════════════════════════════════════╝

# 通用的 clean

[windows]
single-j-all-clean:
    powershell -NoProfile -File scripts\single-j-all-clean-win.ps1

[unix]
single-j-all-clean:
    chmod +x scripts/single-j-all-clean-unix.sh && ./scripts/single-j-all-clean-unix.sh

# jcms-core

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

j-core-compile: single-j-all-clean single-j-core-compile
j-core-test: j-core-compile single-j-core-test
j-core-test-c: c-quick j-core-test

j-utils-compile: single-j-all-clean single-j-utils-compile
j-utils-test: j-utils-compile single-j-utils-test
j-utils-test-c: c-quick j-utils-compile single-j-core-test single-j-utils-test



# jcms-app


# ╔═══════════════════════════════════════════╗
# ║  顶层                                     ║
# ╚═══════════════════════════════════════════╝

build-all: single-c-build single-c-load single-j-core-compile

server scl="config/sample-scd-full.scd":
    cd jcms && mvn exec:java -pl jcms-app -am -Dexec.mainClass="com.ysh.jcms.app.console.CmsServerConsole" -Dcms.server.sclFiles="{{scl}}"

client host="localhost" port="20482" api_port="22000":
    cd jcms && mvn exec:java -pl jcms-app -am -Dexec.mainClass="com.ysh.jcms.app.console.CmsClientConsole" -Dcms.client.connection.host="{{host}}" -Dcms.client.connection.port={{port}} -Dcms.client.console.apiPort={{api_port}}
