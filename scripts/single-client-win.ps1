$env:JAVA_HOME = "$env:USERPROFILE\scoop\apps\corretto8-jdk\current"
$env:Path = "$env:USERPROFILE\scoop\shims;$env:Path"

Push-Location jcms
mvn -q exec:java -pl jcms-app `
    "-Dlogback.configurationFile=jcms-app/src/main/resources/logback-cli.xml" `
    "-Dexec.mainClass=com.ysh.jcms.app.console.CmsClientConsole" `
    "-Dfile.encoding=UTF-8"
Pop-Location
