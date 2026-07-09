if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    $env:JAVA_HOME = [Environment]::GetEnvironmentVariable("JAVA_HOME", "User")
}

if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    Write-Host "[ERROR] JAVA_HOME 未设置或不正确" -ForegroundColor Red; exit 1
}

Push-Location jcms
mvn -q exec:java -pl jcms-app `
    "-Dlogback.configurationFile=jcms-app/src/main/resources/logback-cli.xml" `
    "-Dexec.mainClass=com.ysh.jcms.app.console.CmsClientConsole" `
    "-Dfile.encoding=UTF-8"
Pop-Location
