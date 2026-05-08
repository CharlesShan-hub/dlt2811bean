$env:JAVA_HOME="D:\envs\.jdks\ms-21.0.10"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
mvn exec:java@cms-cli -q
