param(
    [Parameter(ValueFromRemainingArguments=$true)]
    [string[]]$args
)

$hostAddr = "http://127.0.0.1"
$port = 8080
$argIndex = 0

if ($args.Count -eq 0) {
    Write-Host 'Usage: ./cms <command> [args...]' -ForegroundColor Yellow
    Write-Host '       ./cms --status' -ForegroundColor Yellow
    Write-Host '       ./cms --port <port> <command> [args...]' -ForegroundColor Yellow
    exit 1
}

if ($args[0] -eq '--port' -and $args.Count -gt 1) {
    $port = $args[1]
    $argIndex = 2
}

if ($args[$argIndex] -eq '--status') {
    $url = "${hostAddr}:${port}/api/status"
    try {
        $response = Invoke-WebRequest -Uri $url -Method GET -TimeoutSec 10 -UseBasicParsing
        Write-Host $response.Content
    } catch {
        Write-Error "Cannot connect to CMS CLI API server at ${hostAddr}:${port}"
        Write-Error "Make sure win_cli is running."
        exit 1
    }
    exit 0
}

$cmdParts = $args[$argIndex..$args.Count]
$cmdLine = ''
foreach ($part in $cmdParts) {
    if ($cmdLine -ne '') { $cmdLine += ' ' }
    if ($part -match '[\s"]') {
        $cmdLine += '"' + $part.Replace('"', '\"') + '"'
    } else {
        $cmdLine += $part
    }
}

$url = "${hostAddr}:${port}/api/execute"
try {
    $body = @{ cmd = $cmdLine }
    $response = Invoke-WebRequest -Uri $url -Method POST -Body $body -TimeoutSec 30 -UseBasicParsing
    Write-Host $response.Content
} catch {
    Write-Error "Cannot connect to CMS CLI API server at ${hostAddr}:${port}"
    Write-Error "Make sure win_cli is running."
    exit 1
}
