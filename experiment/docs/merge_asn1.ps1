$docsDir = "$PSScriptRoot\cms\experiment\docs"
$outputFile = "$PSScriptRoot\cms\experiment\cms.asn1"

Write-Host "Merging ASN.1 files into $outputFile ..." -ForegroundColor Cyan

# Start with datatypes (the foundation) — keep full module header + definitions
Get-Content "$docsDir\dlt2811b-datatypes.asn" | Out-File -FilePath $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-datatypes.asn" -ForegroundColor Green

# Append protocol layer
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-protocol.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-protocol.asn" -ForegroundColor Green

# Append association
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-association.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-association.asn" -ForegroundColor Green

# Append data services
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-data.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-data.asn" -ForegroundColor Green

# Append control
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-control.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-control.asn" -ForegroundColor Green

# Append report
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-report.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-report.asn" -ForegroundColor Green

# Append dataset
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-dataset.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-dataset.asn" -ForegroundColor Green

# Append directory
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-directory.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-directory.asn" -ForegroundColor Green

# Append setting
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-setting.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-setting.asn" -ForegroundColor Green

# Append file
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-file.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-file.asn" -ForegroundColor Green

# Append goose
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-goose.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-goose.asn" -ForegroundColor Green

# Append rpc
Add-Content -Path $outputFile -Value "`r`n" -Encoding ascii
Get-Content "$docsDir\dlt2811b-rpc.asn" | Add-Content -Path $outputFile -Encoding ascii
Write-Host "  [OK] dlt2811b-rpc.asn" -ForegroundColor Green

Write-Host "Done! Merged file: $outputFile" -ForegroundColor Cyan
Write-Host "Now you need to manually remove the middle END/BEGIN lines." -ForegroundColor Yellow
