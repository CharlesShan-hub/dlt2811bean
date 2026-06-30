$scdPaths = @(
    "C:\Users\17428\Downloads\longyang\big-file.scd",
    "D:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\resources\config\sample-scd-full.scd"
)

Write-Host "=== 搜索定值类数据 (SPG/ING/ASG/CSG) ===" -ForegroundColor Cyan

foreach ($file in $scdPaths) {
    if (!(Test-Path $file)) {
        Write-Host "文件不存在: $file" -ForegroundColor Red
        continue
    }
    Write-Host "`n扫描: $file" -ForegroundColor Yellow
    $xml = [xml](Get-Content $file -Raw)
    $ns = @{ns = "http://www.iec.ch/61850/2003/SCL"}

    # 1. 找所有 DOType 中 cdc=SPG/ING/ASG/CSG 的
    $sgTypes = $xml.SelectNodes("//ns:DOType[@cdc='SPG' or @cdc='ING' or @cdc='ASG' or @cdc='CSG']", $ns)
    
    if ($sgTypes.Count -eq 0) {
        Write-Host "  无定值类 DOType" -ForegroundColor DarkGray
        continue
    }

    $sgTypeIds = @{}
    foreach ($t in $sgTypes) {
        $sgTypeIds[$t.id] = $t.cdc
        Write-Host "  DOType id=$($t.id) cdc=$($t.cdc)" -ForegroundColor Green
    }

    # 2. 找使用这些 DOType 的 LNodeType
    $lnTypes = $xml.SelectNodes("//ns:LNodeType", $ns)
    foreach ($lnt in $lnTypes) {
        $dos = $lnt.SelectNodes("ns:DO", $ns)
        $matches = @()
        foreach ($do in $dos) {
            if ($sgTypeIds.ContainsKey($do.type)) {
                $matches += "$($do.name) [$($sgTypeIds[$do.type])]"
            }
        }
        if ($matches.Count -gt 0) {
            Write-Host "  LNodeType id=$($lnt.id) lnClass=$($lnt.lnClass): $($matches -join ', ')" -ForegroundColor Cyan
        }
    }

    # 3. 找实际 IED 中实例化这些 LNodeType 的 LN
    $ieds = $xml.SelectNodes("//ns:IED", $ns)
    foreach ($ied in $ieds) {
        $iedName = $ied.name
        $accessPoints = $ied.SelectNodes("ns:AccessPoint", $ns)
        foreach ($ap in $accessPoints) {
            $apName = $ap.name
            $ldevs = $ap.SelectNodes("ns:Server/ns:LDevice", $ns)
            foreach ($ld in $ldevs) {
                $ldInst = $ld.inst
                $lns = $ld.SelectNodes("ns:LN|ns:LN0", $ns)
                foreach ($ln in $lns) {
                    $lnType = $ln.lnType
                    if ($sgTypeIds.ContainsKey($lnType)) {
                        continue # 整个 LN 就是 SG 类型
                    }
                    $lnClass = $ln.lnClass
                    $lnInst = $ln.inst
                    $prefix = $ln.prefix
                    $fullName = "$prefix$lnClass$lnInst"
                    
                    # 检查这个 LNType 是否有 SG DO
                    $lntNode = $xml.SelectNodes("//ns:LNodeType[@id='$lnType']", $ns)
                    if ($lntNode.Count -eq 0) { continue }
                    $sgDos = @()
                    foreach ($do in $lntNode[0].SelectNodes("ns:DO", $ns)) {
                        if ($sgTypeIds.ContainsKey($do.type)) {
                            $sgDos += "$($do.name) [$($sgTypeIds[$do.type])]"
                        }
                    }
                    if ($sgDos.Count -gt 0) {
                        Write-Host "  ! IED=$iedName AP=$apName LD=$ldInst LN=$fullName : $($sgDos -join ', ')" -ForegroundColor Magenta
                    }
                }
            }
        }
    }
}

Write-Host "`n=== 扫描完成 ===" -ForegroundColor Cyan
