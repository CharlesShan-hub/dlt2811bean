$configDir = "$PSScriptRoot\config"
if (!(Test-Path $configDir)) {
    New-Item -ItemType Directory -Path $configDir -Force | Out-Null
}

Write-Host "Downloading sample-scd-full.scd ..." -ForegroundColor Yellow
Invoke-WebRequest -Uri "https://raw.githubusercontent.com/gillerman/IEC_61850_SCL_2_Nodeset/master/Example61850TestSCD.xml" -OutFile "$configDir\sample-scd-full.scd"
Write-Host "  OK" -ForegroundColor Green

Write-Host "Downloading sample-scd-relay.scd ..." -ForegroundColor Yellow
Invoke-WebRequest -Uri "https://raw.githubusercontent.com/robidev/iec61850_open_server/master/scd/protection_relay.scd" -OutFile "$configDir\sample-scd-relay.scd"
Write-Host "  OK" -ForegroundColor Green

Write-Host "`nDone! Files saved to: $configDir" -ForegroundColor Green
