# Safe test updater - only converts direct scalar fields, not nested/choice
$ErrorActionPreference = "Stop"
$utf8NoBom = New-Object System.Text.UTF8Encoding $false
$testRoot = "d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\test\java"
$count = 0

Get-ChildItem -Path $testRoot -Recurse -Filter "*.java" | ForEach-Object {
    $file = $_.FullName
    $text = [System.IO.File]::ReadAllText($file)
    $lines = Get-Content $file
    $newLines = @()
    $changed = $false
    $i = 0

    while ($i -lt $lines.Count) {
        $line = $lines[$i]

        # Match: Type var = new Type();
        if ($line -match '^(\s+)(\w[\w<>,.?\[\]]*\s+)(\w+)\s*=\s*new\s+(\w+)\(\);$') {
            $indent = $Matches[1]
            $decl = $Matches[2]
            $varName = $Matches[3]
            $typeName = $Matches[4]

            if ($i + 1 -lt $lines.Count) {
                $j = $i + 1
                $fields = @()
                $isSimple = $true

                while ($j -lt $lines.Count) {
                    $fl = $lines[$j]
                    # Match simple: varName.fieldName.value(VALUE);
                    # Reject nested: varName.fieldName.subField.value(VALUE);
                    if ($fl -match "^\s+$varName\.(\w+)\.value\((.+)\);\s*$" -and $fl -notmatch "$varName\.\w+\.\w+\." -and $fl -notmatch "$varName\.choice\.") {
                        $val = $Matches[2].Trim()
                        $field = $Matches[1]
                        # Sanity check: field shouldn't be a CHOICE alt_ (these have no setter)
                        if ($field -match '^alt_') { $isSimple = $false; break }
                        $fields += @{field=$field; value=$val}
                        $j++
                    } elseif ($fl -match "^\s+$varName\.(\w+)\.value\((.+)\);\s*$" -and $fl -match "$varName\.\w+\.\w+\.") {
                        # Nested path like var.sub.field.value() - stop
                        $isSimple = $false; break
                    } else {
                        break
                    }
                }

                if ($isSimple -and $fields.Count -ge 1) {
                    # Generate chain
                    $newLines += "$indent$decl$varName = new $typeName()"
                    for ($k = 0; $k -lt $fields.Count; $k++) {
                        $f = $fields[$k]
                        $semi = if ($k -eq $fields.Count - 1) { ";" } else { "" }
                        $newLines += "$indent    .$($f.field)($($f.value))$semi"
                    }
                    $i = $j - 1
                    $changed = $true
                    $i++
                    continue
                }
            }
        }

        # Also match: varName = new Type(); without declaration (e.g. reuse)
        if ($line -match '^(\s+)(\w+)\s*=\s*new\s+(\w+)\(\);$' -and $i -gt 0) {
            $indent = $Matches[1]
            $varName = $Matches[2]
            $typeName = $Matches[3]

            if ($i + 1 -lt $lines.Count) {
                $j = $i + 1
                $fields = @()
                $isSimple = $true

                while ($j -lt $lines.Count) {
                    $fl = $lines[$j]
                    if ($fl -match "^\s+$varName\.(\w+)\.value\((.+)\);\s*$" -and $fl -notmatch "$varName\.\w+\.\w+\." -and $fl -notmatch "$varName\.choice\.") {
                        $val = $Matches[2].Trim()
                        $field = $Matches[1]
                        if ($field -match '^alt_') { $isSimple = $false; break }
                        $fields += @{field=$field; value=$val}
                        $j++
                    } elseif ($fl -match "^\s+$varName\.(\w+)\.value\((.+)\);\s*$" -and $fl -match "$varName\.\w+\.\w+\.") {
                        $isSimple = $false; break
                    } else {
                        break
                    }
                }

                if ($isSimple -and $fields.Count -ge 1) {
                    $newLines += "$indent$varName = new $typeName()"
                    for ($k = 0; $k -lt $fields.Count; $k++) {
                        $f = $fields[$k]
                        $semi = if ($k -eq $fields.Count - 1) { ";" } else { "" }
                        $newLines += "$indent    .$($f.field)($($f.value))$semi"
                    }
                    $i = $j - 1
                    $changed = $true
                    $i++
                    continue
                }
            }
        }

        $newLines += $line
        $i++
    }

    if ($changed) {
        $newContent = $newLines -join "`r`n"
        [System.IO.File]::WriteAllText($file, $newContent, $utf8NoBom)
        $count++
        Write-Host "[OK] $($_.Name)"
    }
}

Write-Host "=== Updated $count files ==="
