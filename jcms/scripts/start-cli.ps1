# 启动 CMS CLI 客户端（日志只显示 WARN+）
# 用法: .\start-cli.ps1
$Root = Split-Path -Parent $PSScriptRoot
Set-Location $Root

# 切换到 UTF-8 代码页（支持中文输出）
& chcp 65001 | Out-Null

# 两步走：先 install（确保所有模块编译并装入本地仓库），再启动
mvn -q install -DskipTests -pl jcms-app -am
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

mvn -q exec:java -pl jcms-app `
    "-Dlogback.configurationFile=jcms-app/src/main/resources/logback-cli.xml" `
    "-Dexec.mainClass=com.ysh.jcms.app.cli.CmsCli"
