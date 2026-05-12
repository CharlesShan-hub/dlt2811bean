# Remove-Capacity100.ps1
param(
    [Parameter(Mandatory=$true)]
    [string]$RootPath
)

Get-ChildItem -Path $RootPath -File -Recurse -Filter "*.java" | ForEach-Object {
    $file = $_.FullName
    $content = Get-Content -Path $file -Raw
    if ($content -match '\.capacity\(100\);') {
        $newContent = $content -replace '\.capacity\(100\);', ';'
        Set-Content -Path $file -Value $newContent -NoNewline
        Write-Host "Updated: $file"
    }
}