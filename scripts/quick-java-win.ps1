if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    $env:JAVA_HOME = [Environment]::GetEnvironmentVariable("JAVA_HOME", "User")
    if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
        Write-Host "[ERROR] JAVA_HOME 未设置或不正确" -ForegroundColor Red
        exit 1
    }
}

Push-Location jcms
mvn com.diffplug.spotless:spotless-maven-plugin:2.13.0:apply -q
mvn install -DskipTests -q
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] Java 全量编译完成" -ForegroundColor Green
