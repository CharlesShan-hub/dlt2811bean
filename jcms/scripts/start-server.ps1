# 启动 CMS 服务端
# 用法: .\start-server.ps1 [[-Port] <int>]
param([int]$Port = 18780)

$Root = Split-Path -Parent $PSScriptRoot
Set-Location $Root

# 切换到 UTF-8 代码页（支持中文输出）
& chcp 65001 | Out-Null

# 清理旧服务端进程
$oldPids = wmic process where "name='java.exe' and commandline like '%CmsServerConsole%'" get processid 2>$null | Select-Object -Skip 1
foreach ($procId in $oldPids) {
    $procId = $procId.Trim()
    if ($procId -ne '') { taskkill /f /pid $procId 2>$null }
}

# 两步走：先 install（确保所有模块编译并装入本地仓库），再启动
mvn -q install -DskipTests -pl jcms-app -am
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

mvn -q exec:java -pl jcms-app `
    "-Dexec.mainClass=com.ysh.jcms.app.console.CmsServerConsole" `
    "-Dexec.args=$Port" `
    "-Dfile.encoding=UTF-8"
