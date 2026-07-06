# ──────────────────────────────────────────────
#  DL/T 2811 (CMS) 项目构建命令
#  `just` — https://github.com/casey/just
#
#  single-c-xxx → C 原子级
#  single-j-xxx → Java 原子级
#  xxx          → 批量级（组合 single-*）
#  支持 Windows / macOS / Linux
# ──────────────────────────────────────────────

# 平台检测
_family := os_family()

# ── 原子级 ──

single-c-clean:
{{ if _family == "windows" }}    if exist ccms/build rmdir /s /q ccms/build
{{ else }}    rm -rf ccms/build
{{ end }}

single-c-build:
{{ if _family == "windows" }}    cd ccms && powershell -NoProfile -File win_ccms.ps1
{{ else }}    cd ccms && bash ccms.sh
{{ end }}

single-c-build-fast:
{{ if _family == "windows" }}    cd ccms/build && mingw32-make -j
{{ else }}    cd ccms/build && make -j
{{ end }}

single-c-copy-dll:
{{ if _family == "windows" }}    copy /Y ccms\dist\ccms.dll jcms\jcms-core\src\main\resources\win32-x86-64\ccms.dll
{{ else }}    cp ccms/dist/ccms.dylib jcms/jcms-core/src/main/resources/darwin-x86-64/ccms.dylib
{{ end }}

single-c-sync-cc:
{{ if _family == "windows" }}    copy ccms\build\compile_commands.json ccms\
{{ else }}    cp ccms/build/compile_commands.json ccms/
{{ end }}

single-c-test-run:
{{ if _family == "windows" }}    ccms/build/bin/test_per.exe
{{ else }}    ./ccms/build/bin/test_per
{{ end }}

# ── 批量级 ──

c-rebuild: single-c-clean single-c-build single-c-copy-dll

c-test: single-c-clean single-c-build single-c-test-run

# ╔═══════════════════════════════════════════╗
# ║  JCMS — Java 应用                         ║
# ╚═══════════════════════════════════════════╝

single-j-clean:
    cd jcms && mvn clean -q

single-j-compile-core:
    cd jcms && mvn compile -pl jcms-core -q

single-j-compile:
    cd jcms && mvn compile -pl jcms-app -am -q

single-j-test-run:
    cd jcms && mvn test -pl jcms-core -am -q

single-j-package:
    cd jcms && mvn package -pl jcms-app -am -DskipTests -q

j-test: single-j-clean single-j-compile single-j-test-run

# ╔═══════════════════════════════════════════╗
# ║  顶层                                     ║
# ╚═══════════════════════════════════════════╝

build-all: single-c-build single-c-copy-dll single-j-compile

server scl="config/sample-scd-full.scd":
    cd jcms && mvn exec:java -pl jcms-app -am -Dexec.mainClass="com.ysh.jcms.app.console.CmsServerConsole" -Dcms.server.sclFiles="{{scl}}"

client host="localhost" port="20482" api_port="22000":
    cd jcms && mvn exec:java -pl jcms-app -am -Dexec.mainClass="com.ysh.jcms.app.console.CmsClientConsole" -Dcms.client.connection.host="{{host}}" -Dcms.client.connection.port={{port}} -Dcms.client.console.apiPort={{api_port}}
