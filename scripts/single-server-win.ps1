$env:JAVA_HOME = "$env:USERPROFILE\scoop\apps\corretto8-jdk\current"
$env:Path = "$env:USERPROFILE\scoop\shims;$env:Path"

Push-Location jcms
mvn -q exec:java -pl jcms-app `
    "-Dexec.mainClass=com.ysh.jcms.app.console.CmsServerConsole" `
    "-Dexec.args=18780" `
    "-Dfile.encoding=UTF-8"
Pop-Location
