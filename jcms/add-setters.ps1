# Add chain setters to all composite PDU types in jcms-core
$ErrorActionPreference = "Stop"
$utf8NoBom = New-Object System.Text.UTF8Encoding $false
$srcRoot = "d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java"

$scalarMap = @{ "CmsBoolean"="boolean"; "CmsInt8"="int"; "CmsInt8U"="int"
    "CmsInt16"="int"; "CmsInt16U"="int"; "CmsInt24U"="int"
    "CmsInt32"="int"; "CmsInt32U"="long"; "CmsInt64"="long"
    "CmsInt64U"="java.math.BigInteger"; "CmsFloat32"="float"; "CmsFloat64"="double"
    "CmsReqId"="int"
    "CmsObjectClass"="int"; "CmsAcsiClass"="int"; "CmsServiceError"="int"
    "CmsAbortReason"="int"; "CmsDbpos"="int"; "CmsTcmd"="int"
    "CmsOrCat"="int"; "CmsAddCause"="int"; "CmsSmpMod"="int"; "CmsEnumerated"="int" }

$byteStrTypes = @("CmsUint8Array","CmsObjectReference","CmsSubReference",
    "CmsObjectName","CmsEntryId","CmsFunctionalConstraint",
    "CmsVisibleString","CmsUtf8String","CmsOctetString","CmsBitString",
    "CmsAssociationId")

$skip = @('CmsType','CmsArray','CmsChoice','CmsEnumerated','CmsCodedEnum',
    'CmsBoolean','CmsInt8','CmsInt8U','CmsInt16','CmsInt16U','CmsInt24U',
    'CmsInt32','CmsInt32U','CmsInt64','CmsInt64U',
    'CmsFloat32','CmsFloat64','CmsUint8Array',
    'CmsVisibleString','CmsUtf8String','CmsOctetString','CmsBitString',
    'CmsObjectReference','CmsSubReference','CmsObjectName',
    'CmsEntryId','CmsPhyComAddr','CmsFunctionalConstraint',
    'CmsReqId')

Get-ChildItem -Path $srcRoot -Recurse -Filter "*.java" | ForEach-Object {
    $file = $_.FullName; $text = [System.IO.File]::ReadAllText($file)
    $lines = Get-Content $file
    if (-not ($text -match 'extends CmsType' -and $text -match 'List.*children\(\)')) { return }
    $cn = $_.BaseName; if ($skip -contains $cn) { return }
    
    # Skip if already has chain setters
    if ($text -match '// -- chain') { Write-Host "[SKIP] $cn"; return }
    
    # Remove any old setter sections
    $newL = @(); $skipSec = $false
    foreach ($line in $lines) {
        if ($line -match '// -- chain') { $skipSec = $true }
        if (-not $skipSec) { $newL += $line }
        if ($skipSec -and $line -match '@Override') { $skipSec = $false; $newL += $line }
    }
    $lines = $newL

    # Collect all field names and types
    $fields = @{}; $fieldOrder = @(); $hasAlt = $false; $hasChoice = $false
    foreach ($line in $lines) {
        if ($line -match '^\s+public\s+(\S+)\s+(\w+);') {
            $tn = $Matches[1]; $fn = $Matches[2]
            $fields[$fn] = $tn; $fieldOrder += $fn
            if ($fn -match '^alt') { $hasAlt = $true }
            if ($fn -eq 'choice') { $hasChoice = $true }
        }
    }

    $setters = @()
    $setters += "    // -- chain setters --"

    # CHOICE types: only generate choice(int) setter
    if ($hasChoice -and $hasAlt) {
        $setters += "    public $cn choice(int v) { this.choice.value(v); return this; }"
    } else {
        # Regular SEQUENCE types: generate setters for all fields
        foreach ($fn in $fieldOrder) {
            $tn = $fields[$fn]
            $present = ""; foreach ($c in @("${fn}_present","${fn}Present")) { if ($fields.ContainsKey($c)) { $present = $c } }

            if ($scalarMap.ContainsKey($tn)) {
                $setters += "    public $cn $fn($($scalarMap[$tn]) v) { this.$fn.value(v); return this; }"
            } elseif ($byteStrTypes -contains $tn) {
                if ($present) {
                    $setters += "    public $cn $fn(byte[] v) { this.$present.value(v != null && v.length > 0); if (v != null) this.$fn.value(v); return this; }"
                    $setters += "    public $cn $fn(String v) { this.$present.value(v != null); if (v != null) this.$fn.value(v); return this; }"
                } else {
                    $setters += "    public $cn $fn(byte[] v) { this.$fn.value(v); return this; }"
                    $setters += "    public $cn $fn(String v) { this.$fn.value(v); return this; }"
                }
            } else {
                $setters += "    public $cn $fn($tn v) { this.$fn = v; return this; }"
            }
        }
    }

    if ($setters.Count -le 1) { return }

    $idx = -1
    for ($i = 0; $i -lt $lines.Count; $i++) {
        if ($lines[$i] -match '@Override' -and $i+1 -lt $lines.Count -and $lines[$i+1] -match 'List.*children') {
            $idx = $i - 1; break
        }
    }
    if ($idx -lt 0) { return }

    $result = @()
    for ($i = 0; $i -lt $lines.Count; $i++) {
        $result += $lines[$i]
        if ($i -eq $idx) { $result += $setters }
    }
    [System.IO.File]::WriteAllText($file, ($result -join "`r`n"), $utf8NoBom)
    $label = if ($hasChoice -and $hasAlt) { "CHOICE" } else { "SEQUENCE" }
    Write-Host "[OK] $cn ($label - $($setters.Count-1) setters)"
}
Write-Host "=== Done ==="
