# ──────────────────────────────────────────────
#  DL/T 2811 (CMS) 项目构建命令
#  `just` — https://github.com/casey/just
#
#  single-j-xxx → Java 原子级
#  xxx          → 批量级（组合 single-*）
# ──────────────────────────────────────────────

#  shell 由 just 按平台自动选择：unix → sh，windows → cmd.exe
#  （[unix]/[windows] 变体分别使用各自 shell 语法）
#  路径统一用正斜杠：cmd.exe / PowerShell / sh 都能正确解析

_default:
    just --list

# ╔═══════════════════════════════════════════╗
# ║  WebUI — Vue 前端                         ║
# ╚═══════════════════════════════════════════╝

[windows]
single-ui-install:
    yarn --cwd jcms/webui install

[unix]
single-ui-install:
    yarn --cwd jcms/webui install

[windows]
single-ui-dev:
    yarn --cwd jcms/webui dev

[unix]
single-ui-dev:
    yarn --cwd jcms/webui dev

[windows]
single-ui-build:
    yarn --cwd jcms/webui build

[unix]
single-ui-build:
    yarn --cwd jcms/webui build

[windows]
single-ui-clean:
    if exist jcms/webui/dist rmdir /s /q jcms/webui/dist

[unix]
single-ui-clean:
    rm -rf jcms/webui/dist

[windows]
single-ui-preview:
    yarn --cwd jcms/webui preview

[unix]
single-ui-preview:
    yarn --cwd jcms/webui preview

# ── UI 批量级 ──

ui-quick: single-ui-clean single-ui-install single-ui-build

# ╔═══════════════════════════════════════════╗
# ║  JCMS — Java 应用                         ║
# ╚═══════════════════════════════════════════╝

# ── 通用 ──

[windows]
single-j-all-clean:
    powershell -NoProfile -File scripts/single-j-all-clean-win.ps1

[unix]
single-j-all-clean:
    chmod +x scripts/single-j-all-clean-unix.sh && ./scripts/single-j-all-clean-unix.sh

# ── jcms-data（由 csasn1 Rust 生成） ──

[windows]
single-j-data-gen:
    powershell -NoProfile -File scripts/single-j-data-gen-win.ps1

[unix]
single-j-data-gen:
    chmod +x scripts/single-j-data-gen-unix.sh && ./scripts/single-j-data-gen-unix.sh

[windows]
single-j-data-test:
    powershell -NoProfile -File scripts/single-j-data-test-win.ps1

[unix]
single-j-data-test:
    chmod +x scripts/single-j-data-test-unix.sh && ./scripts/single-j-data-test-unix.sh

[windows]
single-j-data-install:
    powershell -NoProfile -File scripts/single-j-data-install-win.ps1

[unix]
single-j-data-install:
    chmod +x scripts/single-j-data-install-unix.sh && ./scripts/single-j-data-install-unix.sh

# ── jcms-core ──

[windows]
single-j-core-compile:
    powershell -NoProfile -File scripts/single-j-core-compile-win.ps1

[unix]
single-j-core-compile:
    chmod +x scripts/single-j-core-compile-unix.sh && ./scripts/single-j-core-compile-unix.sh

[windows]
single-j-core-test:
    powershell -NoProfile -File scripts/single-j-core-test-win.ps1

[unix]
single-j-core-test:
    chmod +x scripts/single-j-core-test-unix.sh && ./scripts/single-j-core-test-unix.sh

[windows]
single-j-core-install:
    powershell -NoProfile -File scripts/single-j-core-install-win.ps1

[unix]
single-j-core-install:
    chmod +x scripts/single-j-core-install-unix.sh && ./scripts/single-j-core-install-unix.sh

# ── jcms-utils ──

[windows]
single-j-utils-compile:
    powershell -NoProfile -File scripts/single-j-utils-compile-win.ps1

[unix]
single-j-utils-compile:
    chmod +x scripts/single-j-utils-compile-unix.sh && ./scripts/single-j-utils-compile-unix.sh

[windows]
single-j-utils-test:
    powershell -NoProfile -File scripts/single-j-utils-test-win.ps1

[unix]
single-j-utils-test:
    chmod +x scripts/single-j-utils-test-unix.sh && ./scripts/single-j-utils-test-unix.sh

[windows]
single-j-utils-install:
    powershell -NoProfile -File scripts/single-j-utils-install-win.ps1

[unix]
single-j-utils-install:
    chmod +x scripts/single-j-utils-install-unix.sh && ./scripts/single-j-utils-install-unix.sh

# ── jcms-app ──

[windows]
single-j-app-compile:
    powershell -NoProfile -File scripts/single-j-app-compile-win.ps1

[unix]
single-j-app-compile:
    chmod +x scripts/single-j-app-compile-unix.sh && ./scripts/single-j-app-compile-unix.sh

[windows]
single-j-app-test:
    powershell -NoProfile -File scripts/single-j-app-test-win.ps1

[unix]
single-j-app-test:
    chmod +x scripts/single-j-app-test-unix.sh && ./scripts/single-j-app-test-unix.sh

[windows]
single-j-app-install:
    powershell -NoProfile -File scripts/single-j-app-install-win.ps1

[unix]
single-j-app-install:
    chmod +x scripts/single-j-app-install-unix.sh && ./scripts/single-j-app-install-unix.sh

# 批量级

j-core-compile: single-j-all-clean single-j-core-compile
j-core-test: j-core-compile single-j-core-test

j-utils-compile: single-j-all-clean single-j-utils-compile
j-utils-test: j-utils-compile single-j-utils-test

j-all-compile: single-j-all-clean single-j-app-compile
j-all-test: j-all-compile single-j-app-test

# ── 锁机制（进程间同步） ──

[windows]
single-lock:
    powershell -NoProfile -File scripts/single-lock-win.ps1

[unix]
single-lock:
    chmod +x scripts/single-lock-unix.sh && ./scripts/single-lock-unix.sh

[windows]
single-unlock:
    powershell -NoProfile -File scripts/single-unlock-win.ps1

[unix]
single-unlock:
    chmod +x scripts/single-unlock-unix.sh && ./scripts/single-unlock-unix.sh

[windows]
single-check-lock:
    powershell -NoProfile -File scripts/single-check-lock-win.ps1

[unix]
single-check-lock:
    chmod +x scripts/single-check-lock-unix.sh && ./scripts/single-check-lock-unix.sh

# ── 运行（不编译，需先 java-quick） ──

[windows]
single-server:
    powershell -NoProfile -File scripts/single-server-win.ps1

[unix]
single-server:
    chmod +x scripts/single-server-unix.sh && ./scripts/single-server-unix.sh

[windows]
single-client:
    powershell -NoProfile -File scripts/single-client-win.ps1

[unix]
single-client:
    chmod +x scripts/single-client-unix.sh && ./scripts/single-client-unix.sh


# ╔═══════════════════════════════════════════╗
# ║  顶层工作流                                ║
# ╚═══════════════════════════════════════════╝

# 分级编译：复用已验证的 single-j-*-install 积木，按改动的模块层级选档
#   java-quick1 — single-j-app-install（app）
#   java-quick2 — + single-j-utils-install（utils + app）
#   java-quick3 — + single-j-core-install（core + utils + app）
#   java-quick4 — + single-j-data-install（全量，改动 data / 首次构建）
java-quick1: single-j-app-install
java-quick2: single-j-utils-install single-j-app-install
java-quick3: single-j-core-install single-j-utils-install single-j-app-install
java-quick4: single-j-data-install single-j-core-install single-j-utils-install single-j-app-install
java-quick: java-quick4

run-server: single-check-lock single-server
run-client: single-check-lock single-client
# 带档位：build-java-run-server [1|2|3|4]，内部调用 java-quick{{tier}}（默认 4 全量）
build-java-run-server tier='4':
    just single-lock
    just java-quick{{tier}}
    just single-unlock
    just run-server

build-java-run-client tier='4':
    just single-lock
    just java-quick{{tier}}
    just single-unlock
    just run-client

# ── UI 组合 ──

[windows]
run-client-ui: single-check-lock
    start "CMS WebUI" cmd /c yarn --cwd jcms/webui dev
    powershell -NoProfile -File scripts/single-client-win.ps1

[windows]
build-java-run-client-ui: single-lock java-quick single-unlock
    start "CMS WebUI" cmd /c yarn --cwd jcms/webui dev
    powershell -NoProfile -File scripts/single-client-win.ps1
