param(
    [string]$scdFile = "config/sample-scd-full.scd"
)

$env:JAVA_HOME="D:\envs\.jdks\ms-21.0.10"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Compiling..."
mvn compile -q
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "Using SCD file: $scdFile"
mvn test "-Dcms.server.sclFile=$scdFile"
