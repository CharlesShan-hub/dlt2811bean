<#
.SYNOPSIS
    Replace a literal string in all matching text files under a folder.
    PREVIEW by default; use -Apply to write changes back.

.DESCRIPTION
    Recursively walks -Path, and in every file whose name matches -FileFilter,
    replaces every occurrence of -Old with -New. Plain text by default;
    pass -Regex to treat -Old as a .NET regular expression.

    Without -Apply only prints what WOULD change:
        <file>:<line>: <old line>  ==>  <new line>
    With -Apply rewrites the files in place (UTF-8, no BOM; CRLF preserved)
    and prints the same report. Lines not matching are untouched.

    BACK UP YOUR FILES BEFORE USING -Apply.

.PARAMETER Path
    Root folder to scan recursively.

.PARAMETER Old
    String to find (case-sensitive). Plain text unless -Regex is given.

.PARAMETER New
    Replacement string. With -Regex, supports $1-style backreferences.

.PARAMETER Regex
    Treat -Old as a .NET regular expression (e.g. "\bServiceName\b" for
    word-boundary matching, which avoids hitting "getServiceName").

.PARAMETER FileFilter
    File name filter, default "*.java".

.PARAMETER Apply
    Actually write the changes. Without it, preview only.

.PARAMETER OutputFile
    Optional file to save the change report to (UTF-8).

.EXAMPLE
    # Preview only
    powershell -File .\replace-in-files.ps1 -Path ..\jcms -Old "com.ysh.jcms.data.Cms" -New "com.ysh.jcms.core.data.Cms"

.EXAMPLE
    # Write changes
    powershell -File .\replace-in-files.ps1 -Path ..\jcms -Old "com.ysh.jcms.data.Cms" -New "com.ysh.jcms.core.data.Cms" -Apply
#>
param(
    [Parameter(Mandatory = $true)]
    [string]$Path,

    [Parameter(Mandatory = $true)]
    [string]$Old,

    [Parameter(Mandatory = $true)]
    [string]$New,

    [string]$FileFilter = "*.java",

    [switch]$Regex,

    [switch]$Apply,

    [string]$OutputFile
)

if (-not (Test-Path -Path $Path -PathType Container)) {
    Write-Error "Folder not found: $Path"
    exit 1
}
if ([string]::IsNullOrEmpty($Old)) {
    Write-Error "-Old must not be empty"
    exit 1
}

$files = Get-ChildItem -Path $Path -Recurse -Filter $FileFilter -File
$report = [System.Collections.Generic.List[string]]::new()
$changedFiles = 0
$changedLines = 0
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

foreach ($file in $files) {
    $lines = [System.Collections.Generic.List[string]]::new()
    $fileChanged = $false
    $lineNo = 0
    foreach ($line in [System.IO.File]::ReadLines($file.FullName)) {
        $lineNo++
        if ($Regex) {
            if (-not [regex]::IsMatch($line, $Old)) {
                $lines.Add($line)
                continue
            }
            $newLine = [regex]::Replace($line, $Old, $New)
        } elseif ($line.Contains($Old)) {
            $newLine = $line.Replace($Old, $New)
            $report.Add(("{0}:{1}: {2}  ==>  {3}" -f $file.FullName, $lineNo, $line.Trim(), $newLine.Trim()))
            $lines.Add($newLine)
            $fileChanged = $true
            $changedLines++
        } else {
            $lines.Add($line)
        }
    }
    if ($fileChanged) {
        $changedFiles++
        if ($Apply) {
            [System.IO.File]::WriteAllLines($file.FullName, $lines, $utf8NoBom)
        }
    }
}

if ($OutputFile) {
    $report | Set-Content -Path $OutputFile -Encoding UTF8
    Write-Host "[OK] change report written to $OutputFile"
} else {
    $report
}
Write-Host ""
if ($Apply) {
    Write-Host "[Applied] $changedLines line(s) changed in $changedFiles file(s) under $Path"
} else {
    Write-Host "[Preview] $changedLines line(s) in $changedFiles file(s) would change. Re-run with -Apply to write."
}
