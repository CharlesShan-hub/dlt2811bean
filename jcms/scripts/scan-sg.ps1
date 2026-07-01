# scan-sg.ps1 — 扫描 all APs，检测定值组 (SG) 并验证是否有真实定值数据
#
# 对每个有 SGECB 的 AP，会进一步尝试 select-edit-sg + get-edit-sg，
# 确认 LN 里是否真的有可读写的定值数据。
#
# 用法:
#   .\scan-sg.ps1                                    # 全量扫描
#   .\scan-sg.ps1 -max 10                            # 只扫前 10 个
#   .\scan-sg.ps1 -skip 50 -max 10                   # 从第 51 个开始扫 10 个
#   .\scan-sg.ps1 -prefix P_                         # 只扫 P_ 开头的 AP
#   .\scan-sg.ps1 -prefix "C_B504"                   # 只扫 C_B504 开头的
#
# 要求: CMS CLI 必须在运行中 (start-cli.ps1)

param(
    [int]$port = 7899,
    [string]$aps = "D:\project\work\standard\dlt2811bean\cms\docs\aps.txt",
    [int]$skip = 0,
    [int]$max = 0,
    [string]$prefix = ""                 # AP 名前缀过滤，如 "P_", "C_B504"
)

$apiBase = "http://127.0.0.1:$port"

function Clean-Output($text) {
    return ($text -replace '\u001b\[[0-9;]*m', '').Trim()
}

function Invoke-Cms($cmd) {
    try {
        $r = Invoke-RestMethod -Uri "$apiBase/api/execute" -Method POST `
            -Body @{cmd=$cmd} -TimeoutSec 30
        return Clean-Output $r
    } catch {
        return "ERR: $($_.Exception.Message)"
    }
}

function Parse-ListEntries($text) {
    $result = @()
    foreach ($line in ($text -split "`n")) {
        if ($line -match '\[\d+\]\s+([^:()]+)$') {
            $result += $matches[1].Trim()
        }
    }
    return $result
}

function Parse-SgcbRefs($text) {
    $result = @()
    foreach ($line in ($text -split "`n")) {
        if ($line -match '\[\d+\]\s+(\S+)\s+\[SGECB\]') {
            $result += $matches[1].Trim()
        }
    }
    return $result
}

# 判断 get-edit-sg 输出中是否有真实定值数据（已弃用，改用 set-edit-sg 运行时验证）

# 选取测试用的 SG ref（优先 LLN0 的 SG1）
function Pick-TestRef($sgRefs, $ldsWithSG) {
    # 优先用第一个 LD 的 LLN0 ref
    foreach ($ref in $sgRefs) {
        if ($ref -match 'LLN0\.LLN0\.SG\d') {
            return $ref
        }
    }
    # 否则用第一个 ref
    return $sgRefs[0]
}

# ========== 检查 API 服务器 ==========
try {
    $statusData = Invoke-RestMethod -Uri "$apiBase/api/status" -Method GET -TimeoutSec 5
    Write-Host "CMS CLI API: connected=$($statusData.connected)  serverRunning=$($statusData.serverRunning)" -ForegroundColor Gray
} catch {
    Write-Error "CMS CLI API 服务器未运行 (127.0.0.1:$port)"
    Write-Error "请先启动 CLI: start-cli.ps1"
    exit 1
}

# 关闭 PDU trace
Invoke-Cms "trace-pdu off" | Out-Null
Invoke-Cms "disconnect" | Out-Null

# ========== 读取 aps.txt ==========
if (-not (Test-Path $aps)) {
    Write-Error "APS 文件不存在: $aps"
    exit 1
}

Write-Host "读取 AP 列表: $aps" -ForegroundColor Cyan
$allAps = Parse-ListEntries (Get-Content $aps -Raw)
Write-Host "共 $($allAps.Count) 个 AP" -ForegroundColor Cyan

# 前缀过滤
if ($prefix -ne "") {
    $allAps = $allAps | Where-Object { $_ -like "$prefix*" }
    Write-Host "过滤 '$prefix*': $($allAps.Count) 个 AP" -ForegroundColor Cyan
}

# skip / max
if ($skip -gt 0) {
    Write-Host "跳过前 $skip 个" -ForegroundColor Yellow
    $allAps = $allAps[$skip..($allAps.Count-1)]
}
if ($max -gt 0 -and $max -lt $allAps.Count) {
    Write-Host "限制扫描 $max 个" -ForegroundColor Yellow
    $allAps = $allAps[0..($max-1)]
}

# ========== 初始化扫描 ==========
$results = @()
$total = $allAps.Count
$sgFound = 0
$sgWithRealData = 0
$connected = 0

$ldCandidates = @("LD0", "PROT", "CTRL", "MEAS", "SWI", "C1",
                   "LD1", "LD2", "LD3", "IED", "M1")

$startTime = Get-Date

for ($i = 0; $i -lt $total; $i++) {
    $ap = $allAps[$i]
    $pct = [math]::Round(($i+1)/$total*100, 1)
    Write-Host "`n[$($i+1+$skip)/$($total+$skip)] ($pct%) $ap" -ForegroundColor Cyan

    # 先断开可能的残留连接，避免冲突
    Invoke-Cms "disconnect" | Out-Null
    $co = Invoke-Cms "connect --ap $ap"
    if ($co -match 'ERR|Not connected') {
        Write-Host "  连接失败" -ForegroundColor DarkGray
        continue
    }
    $connected++

    # 获取 LD 列表
    $sd = Invoke-Cms "server-dir"
    $entries = Parse-ListEntries $sd
    $ldsToTry = if ($entries.Count -gt 0) { $entries + $ldCandidates | Select-Object -Unique } else { $ldCandidates }

    # 找 SGECB
    $sgRefs = @()
    $sgLDs = @()
    foreach ($ld in $ldsToTry) {
        $cb = Invoke-Cms "all-cb --ln $ld --acsi sgecb"
        $refs = Parse-SgcbRefs $cb
        if ($refs.Count -gt 0) {
            $sgRefs += $refs
            $sgLDs += $ld
        }
    }

    if ($sgRefs.Count -eq 0) {
        Write-Host "  [无SG]" -ForegroundColor DarkGray
        Invoke-Cms "disconnect" | Out-Null
        continue
    }

    $sgFound++
    $sampleRef = Pick-TestRef $sgRefs $sgLDs
    Write-Host "  $($sgLDs -join ',') | $($sgRefs.Count) SGECB | 测试: $sampleRef" -ForegroundColor Gray

    # ─── 测试运行时编辑缓冲区（set-edit-sg + get-edit-sg）───
    $hasRealData = $false
    $sgDetail = ""
    $numOfSG = 0

    # 1) sgcb-vals 看 numOfSG
    $sv = Invoke-Cms "sgcb-vals --refs `"$sampleRef`""
    if ($sv -match 'numOfSG=(\d+)') {
        $numOfSG = [int]$matches[1]
    }

    # 2) select-edit-sg 激活编辑
    $ses = Invoke-Cms "select-edit-sg --ref `"$sampleRef`" --num 1"
    
    # 3) set-edit-sg 设一个测试值到运行时缓冲区
    $testRef = "test/set-edit-sg.Dummy"
    $es = Invoke-Cms "set-edit-sg --refs `"$testRef`" --values `"42`" --type int32"
    
    # 4) get-edit-sg 读回来验证
    $rv = Invoke-Cms "get-edit-sg --refs `"$testRef`""
    if ($rv -match '42') {
        $sgWithRealData++
        $hasRealData = $true
        Write-Host "  [运行时缓冲区 OK] 设->读: 42" -ForegroundColor Green
        $sgDetail = "runtime-buffer"
    } else {
        Write-Host "  [运行时缓冲区失败] $rv" -ForegroundColor DarkYellow
        $sgDetail = "no-runtime-buffer"
    }

    $results += [PSCustomObject]@{
        AP          = $ap
        LDs         = ($sgLDs -join ',')
        SGCount     = $sgRefs.Count
        SampleRef   = $sampleRef
        NumOfSG     = $numOfSG
        HasRealData = $hasRealData
        Detail      = $sgDetail
    }

    Invoke-Cms "disconnect" | Out-Null
}

$elapsed = (Get-Date) - $startTime
$elapsedStr = "{0}m {1}s" -f $elapsed.Minutes, $elapsed.Seconds

# ========== 结果汇总 ==========
Write-Host "`n`n=============================================" -ForegroundColor Cyan
Write-Host "           扫描结果汇总" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "AP 过滤前缀: $prefix" -ForegroundColor Gray
Write-Host "扫描范围: #$($skip+1) ~ #$($total+$skip)" -ForegroundColor Gray
Write-Host "扫描耗时: $elapsedStr" -ForegroundColor Gray
Write-Host "总 AP: $total | 已连接: $connected | 有 SGECB: $sgFound | 有真实定值: $sgWithRealData" -ForegroundColor Cyan

if ($results.Count -gt 0) {
    Write-Host "`n----------------- 完整结果 -----------------" -ForegroundColor Cyan

    # 先显示有真实数据的
    $real = $results | Where-Object { $_.HasRealData }
    if ($real.Count -gt 0) {
        Write-Host "`n>>> 有真实定值数据 ($($real.Count) 个):" -ForegroundColor Green
        $real | Format-Table -Property @{N='AP';E={$_.AP};width=22},
            @{N='LD';E={$_.LDs};width=14},
            @{N='SG数';E={$_.SGCount};width=6},
            @{N='组数';E={$_.NumOfSG};width=6},
            @{N='测试引用';E={$_.SampleRef};width=32},
            @{N='详情';E={$_.Detail}} -AutoSize
    }

    # 只有 SGECB 没有真实数据的
    $empty = $results | Where-Object { -not $_.HasRealData }
    if ($empty.Count -gt 0) {
        Write-Host "`n>>> 仅有 SGECB 框架 (无定值数据) ($($empty.Count) 个):" -ForegroundColor DarkYellow
        $empty | Format-Table -Property @{N='AP';E={$_.AP};width=22},
            @{N='LD';E={$_.LDs};width=14},
            @{N='SG数';E={$_.SGCount};width=6},
            @{N='组数';E={$_.NumOfSG};width=6},
            @{N='测试引用';E={$_.SampleRef};width=32} -AutoSize
    }

    Write-Host "`n提示: 先连到有真实定值的 AP 测试定值组操作:" -ForegroundColor Cyan
    Write-Host "  cms connect --ap <AP>" -ForegroundColor Gray
    Write-Host "  cms select-active-sg --ref <SGRef> --num 2" -ForegroundColor Gray
    Write-Host "  cms select-edit-sg --ref <SGRef> --num 3" -ForegroundColor Gray
    Write-Host "  cms get-edit-sg --refs <SGRef>" -ForegroundColor Gray
} else {
    Write-Host "`n未发现任何定值组 (SG)。" -ForegroundColor Yellow
}
