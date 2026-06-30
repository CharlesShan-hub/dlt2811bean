# CMS 远程命令执行脚本
# 将命令发送给本地 CMS CLI 的 API 服务器执行。
#
# 用法:
#   .\cms.ps1 connect --ap C_B5041X/S1
#   .\cms.ps1 sgcb-vals --refs "LD0/LLN0.SG1"
#   .\cms.ps1 --port 7899 connect --ap C_B5041X/S1
#
# 要求: CMS CLI (start-cli.ps1) 必须在运行中。

$port = 7899
$status = $false
$help = $false
$cmdArgs = @()

$i = 0
while ($i -lt $args.Count) {
    switch ($args[$i]) {
        "--port" {
            $i++
            $port = [int]$args[$i]
        }
        "--status" {
            $status = $true
        }
        "--help" {
            $help = $true
        }
        default {
            $cmdArgs += $args[$i]
        }
    }
    $i++
}

if ($help) {
    Write-Host "CMS 远程命令执行" -ForegroundColor Cyan
    Write-Host "用法: .\cms.ps1 [--port <端口>] <命令> [参数...]" -ForegroundColor Gray
    Write-Host "  .\cms.ps1 --status" -ForegroundColor Gray
    Write-Host "示例:" -ForegroundColor Cyan
    Write-Host "  .\cms.ps1 connect --ap C_B5041X/S1" -ForegroundColor Gray
    Write-Host "  .\cms.ps1 data-dir --ref LD0/LLN0" -ForegroundColor Gray
    exit 0
}

if ($status) {
    try {
        $response = Invoke-RestMethod -Uri "http://127.0.0.1:$port/api/status" -Method GET -TimeoutSec 10
        Write-Host $response
    }
    catch {
        Write-Error "无法连接 CMS CLI API 服务器 (127.0.0.1:$port)"
        exit 1
    }
    exit 0
}

if ($cmdArgs.Count -eq 0) {
    Write-Error "未提供命令。用法: .\cms.ps1 connect --ap C_B5041X/S1"
    exit 1
}

# 构建命令参数字符串
$cmdParts = @()
foreach ($arg in $cmdArgs) {
    $s = "$arg"
    if ($s -match '[\s"]') {
        $cmdParts += "`"$($s -replace '"', '\"')`""
    } else {
        $cmdParts += $s
    }
}

$cmdLine = $cmdParts -join ' '

try {
    $body = @{ cmd = $cmdLine }
    $response = Invoke-RestMethod -Uri "http://127.0.0.1:$port/api/execute" -Method POST `
        -Body $body -TimeoutSec 30
    Write-Host $response
}
catch {
    Write-Error "无法连接 CMS CLI API 服务器 (127.0.0.1:$port)"
    Write-Error "请确保 CLI 已在运行 (start-cli.ps1)。"
    Write-Error "错误: $($_.Exception.Message)"
    exit 1
}
