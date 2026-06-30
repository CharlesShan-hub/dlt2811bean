# 启动 CMS CLI 客户端，支持远程命令执行
# 用法:
#   .\start-cli.ps1                  # 启动交互式 CLI（原有方式）
#   .\start-cli.ps1 --connect "C_B5041X/S1"   # 启动后自动连接
# 单个命令模式（需要 CLI 已在运行）:
#   .\cms.ps1 connect --ap C_B5041X/S1
#   .\cms.ps1 sgcb-vals --refs "LD0/LLN0.SG1"
param(
    [string]$connect = "",
    [switch]$help
)

$Root = Split-Path -Parent $PSScriptRoot
Set-Location $Root

if ($help) {
    Write-Host "CMS CLI 客户端" -ForegroundColor Cyan
    Write-Host "用法:" -ForegroundColor Cyan
    Write-Host "  启动交互式 CLI: .\start-cli.ps1" -ForegroundColor Gray
    Write-Host "  启动并自动连接: .\start-cli.ps1 --connect C_B5041X/S1" -ForegroundColor Gray
    Write-Host ""
    Write-Host "  CLI 运行中时，可用 cms.ps1 发送命令:" -ForegroundColor Cyan
    Write-Host "  .\cms.ps1 connect --ap C_B5041X/S1" -ForegroundColor Gray
    Write-Host "  .\cms.ps1 data-dir --ref LD0/LLN0" -ForegroundColor Gray
    exit 0
}

# 切换到 UTF-8 代码页
& chcp 65001 | Out-Null

$autoExec = ""
if ($connect) {
    $autoExec = "connect --ap $connect;"
}

$env:CMS_AUTO_EXEC = $autoExec

# 两步走：先 install，再启动
mvn -q install -DskipTests -pl jcms-app -am
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

mvn -q exec:java -pl jcms-app `
    "-Dlogback.configurationFile=jcms-app/src/main/resources/logback-cli.xml" `
    "-Dexec.mainClass=com.ysh.jcms.app.console.CmsClientConsole" `
    "-Dfile.encoding=UTF-8"
