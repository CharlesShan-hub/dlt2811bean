# scan-sg.ps1 — 扫描服务器上所有设备，检测哪些有定值组 (SG) 值
#
# 用法:
#   .\scan-sg.ps1                              # 默认端口 7899
#   .\scan-sg.ps1 --port 7899                  # 指定端口
#   .\scan-sg.ps1 --lds "LD0,C1,PROT"          # 自定义 LD 候选列表
#   .\scan-sg.ps1 --single C_B5041X            # 只检测指定设备
#
# 要求: CMS CLI 必须在运行中 (start-cli.ps1)

param(
    [int]$port = 7899,
    [string]$lds = "",
    [string]$single = ""
)

$apiBase = "http://127.0.0.1:$port"

# 去 ANSI 转义 + trim
function Clean-Output($text) {
    return ($text -replace '\u001b\[[0-9;]*m', '').Trim()
}

# 执行 CMS 命令并返回清洗后的文本
function Invoke-Cms($cmd) {
    try {
        $r = Invoke-RestMethod -Uri "$apiBase/api/execute" -Method POST `
            -Body @{cmd=$cmd} -TimeoutSec 30
        return Clean-Output $r
    } catch {
        return "ERR: $($_.Exception.Message)"
    }
}

# 从列表输出中解析条目: "    [0] value" → "value"
# 过滤掉 PDU trace 行 (如 "[0] reqId: ..." 或 "[0]: ...")
function Parse-ListEntries($text) {
    $result = @()
    foreach ($line in ($text -split "`n")) {
        # 匹配 [N] <value> 且 value 不含 : ( ) 字符（过滤 trace）
        if ($line -match '\[\d+\]\s+([^:()]+)$') {
            $result += $matches[1].Trim()
        }
    }
    return $result
}

# 从 CB 输出中解析 SGECB 引用
function Parse-SgcbRefs($text) {
    $result = @()
    foreach ($line in ($text -split "`n")) {
        if ($line -match '\[\d+\]\s+(\S+)\s+\[SGECB\]') {
            $result += $matches[1].Trim()
        }
    }
    return $result
}

# ========== 检查 API 服务器 ==========
try {
    $status = Invoke-RestMethod -Uri "$apiBase/api/status" -Method GET -TimeoutSec 5
    Write-Host "CMS CLI API: $status`n" -ForegroundColor Gray
} catch {
    Write-Error "CMS CLI API 服务器未运行 (127.0.0.1:$port)"
    Write-Error "请先启动 CLI: start-cli.ps1"
    exit 1
}

# 关闭 PDU trace 避免输出混杂
Invoke-Cms "trace-pdu off" | Out-Null

# 解析 LD 候选列表
$ldCandidates = if ($lds -ne "") {
    $lds.Split(',') | ForEach-Object { $_.Trim() } | Where-Object { $_ -ne '' }
} else {
    @()  # 空 = 稍后动态生成
}

# ========== 获取设备列表 ==========
Write-Host "========== Step 1: 获取服务器目录 ==========" -ForegroundColor Cyan
$serverDir = Invoke-Cms "server-dir"
Write-Host $serverDir

$devices = Parse-ListEntries $serverDir
if ($devices.Count -eq 0) {
    Write-Host "`n未发现任何设备。确认服务器已启动且可访问。" -ForegroundColor Yellow
    exit 0
}

# 如果指定了 --single，只检测该设备
if ($single -ne "") {
    $devices = @($single)
}

Write-Host "`n发现 $($devices.Count) 个设备: $($devices -join ', ')" -ForegroundColor Cyan

# ========== 逐个设备检测 ==========
$results = @()

foreach ($device in $devices) {
    Write-Host "`n========================================" -ForegroundColor Green
    Write-Host "  设备: $device" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green

    # 先断开已有连接
    Invoke-Cms "disconnect" | Out-Null

    # 尝试连接 (默认 /S1)
    $apCandidates = @("$device/S1", "$device/G1", "$device/M1")
    $connected = $false
    $usedAp = ""

    foreach ($ap in $apCandidates) {
        $co = Invoke-Cms "connect --ap $ap"
        if ($co -notmatch 'ERR') {
            $connected = $true
            $usedAp = $ap
            Write-Host "  已连接: $ap" -ForegroundColor Green
            break
        }
    }

    if (-not $connected) {
        Write-Host "  [跳过] 无法连接 $device" -ForegroundColor Red
        $results += [PSCustomObject]@{
            Device    = $device
            AP        = ""
            Connected = $false
            LDs       = ""
            SGCount   = 0
            SGRefs    = ""
        }
        continue
    }

    # 尝试发现 LD 名称
    $foundLDs = @()
    
    # 1) 先用设备名本身做 LD
    $ldCandidatesToTry = @($device) + @("LD0", "C1", "PROT", "SWI", "MEAS", "CTRL", "LD1", "LD2", "IED")
    # 2) 再加上用户自定义
    if ($ldCandidates.Count -gt 0) {
        $ldCandidatesToTry = $ldCandidatesToTry + $ldCandidates
    }
    # 去重
    $ldCandidatesToTry = $ldCandidatesToTry | Select-Object -Unique

    # 尝试获取 server-dir (连接后再次获取, 有些服务器会返回当前 IED 的 LD 列表)
    $sd2 = Invoke-Cms "server-dir"
    $sd2Entries = Parse-ListEntries $sd2
    if ($sd2Entries.Count -gt 0) {
        Write-Host "  server-dir (连接后): $($sd2Entries -join ', ')" -ForegroundColor Gray
        # 把 server-dir 的条目也加到候选列表
        foreach ($e in $sd2Entries) {
            if ($ldCandidatesToTry -notcontains $e) {
                $ldCandidatesToTry += $e
            }
        }
    }

    # 对每个 LD 候选，测试其是否有 LN/数据
    Write-Host "  尝试发现 LD 名称..." -ForegroundColor Gray
    foreach ($ld in $ldCandidatesToTry) {
        # 用 all-cb 快速测试：如果 LD 存在且支持 SGECB，会返回结果
        $cb = Invoke-Cms "all-cb --ln $ld --acsi sgecb"
        $refs = Parse-SgcbRefs $cb
        
        if ($refs.Count -gt 0) {
            Write-Host "    [SG] $ld : $($refs.Count) SGECB -> $($refs -join ', ')" -ForegroundColor Green
            $foundLDs += @{LD=$ld; SgRefs=$refs}
        } else {
            # 没有 SGECB，但还是检查是否有其他内容 (LD 是否有效)
            $ldDir = Invoke-Cms "ld-dir --ld $ld"
            $lns = Parse-ListEntries $ldDir
            if ($lns.Count -gt 0) {
                Write-Host "    [无SG] $ld : $($lns.Count) LNs (无 SGECB)" -ForegroundColor DarkYellow
            } else {
                # LD 不存在，跳过
            }
        }
    }

    # 汇总 SG 结果
    $allSgRefs = @()
    $sgLDs = @()
    foreach ($entry in $foundLDs) {
        $allSgRefs += $entry.SgRefs
        $sgLDs += $entry.LD
    }

    if ($allSgRefs.Count -gt 0) {
        Write-Host "`n  查询 SGCB 值..." -ForegroundColor Cyan
        $refStr = $allSgRefs -join ' '
        $sv = Invoke-Cms "sgcb-vals --refs `"$refStr`""
        foreach ($line in ($sv -split "`n")) {
            Write-Host "    $line"
        }
    }

    $results += [PSCustomObject]@{
        Device    = $device
        AP        = $usedAp
        Connected = $true
        LDs       = ($sgLDs -join ', ')
        SGCount   = $allSgRefs.Count
        SGRefs    = ($allSgRefs -join '; ')
    }
}

# ========== 结果汇总 ==========
Write-Host "`n`n=============================================" -ForegroundColor Cyan
Write-Host "           扫描结果汇总" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

$results | Format-Table -Property @{N='设备';E={$_.Device}},
    @{N='已连接';E={if($_.Connected){'Y'}else{'N'}}},
    @{N='AP';E={$_.AP}},
    @{N='有SG的LD';E={$_.LDs}},
    @{N='SG数量';E={$_.SGCount}},
    @{N='SG引用';E={$_.SGRefs}} -AutoSize

Write-Host "`n提示: 发现 SG 的设备可通过以下方式操作:" -ForegroundColor Cyan
Write-Host "  cms connect --ap <Device>/S1" -ForegroundColor Gray
Write-Host "  cms select-active-sg --ref <SGRef> --num <N>" -ForegroundColor Gray
Write-Host "  cms select-edit-sg --ref <SGRef> --num <N>" -ForegroundColor Gray
Write-Host "  cms get-edit-sg --refs <SGRef>" -ForegroundColor Gray
