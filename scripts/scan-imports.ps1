<#
.SYNOPSIS
    Scan all *.java files under a folder for lines containing a given import
    substring. READ-ONLY: never modifies any file.

.DESCRIPTION
    Recursively walks the given folder, finds every line whose trimmed text
    contains the search string, and prints:

        <absolute path>:<line number>: <line text>

    Matching is literal (case-insensitive) — the search string is treated as
    plain text, not a regular expression.

.PARAMETER Path
    Root folder to scan recursively.

.PARAMETER Pattern
    Literal text to search for in each line. Default: "com.ysh.jcms.info."

.PARAMETER OutputFile
    Optional output file (UTF-8). If omitted, results go to the console.

.EXAMPLE
    powershell -File .\scan-imports.ps1 -Path ..\jcms -Pattern "com.ysh.jcms.info."

.EXAMPLE
    powershell -File .\scan-imports.ps1 -Path ..\jcms -Pattern "com.ysh.jcms.data.Inner" -OutputFile out.txt
#>
param(
    [Parameter(Mandatory = $true)]
    [string]$Path,

    [string]$Pattern = "com.ysh.jcms.info.",

    [string]$OutputFile
)

if (-not (Test-Path -Path $Path -PathType Container)) {
    Write-Error "Folder not found: $Path"
    exit 1
}

$files = Get-ChildItem -Path $Path -Recurse -Filter *.java -File
if ($files.Count -eq 0) {
    Write-Host "No *.java files found under $Path"
    exit 0
}

$results = foreach ($file in $files) {
    $lineNo = 0
    foreach ($line in [System.IO.File]::ReadLines($file.FullName)) {
        $lineNo++
        if ($line.IndexOf($Pattern, [System.StringComparison]::OrdinalIgnoreCase) -ge 0) {
            "{0}:{1}: {2}" -f $file.FullName, $lineNo, $line.Trim()
        }
    }
}

if ($OutputFile) {
    $results | Set-Content -Path $OutputFile -Encoding UTF8
    Write-Host "[OK] $($results.Count) matching line(s) written to $OutputFile"
} else {
    $results
    Write-Host ""
    Write-Host "Total: $($results.Count) matching line(s) in $($files.Count) java file(s) scanned under $Path"
}
