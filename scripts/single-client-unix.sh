#!/bin/sh
cd jcms && mvn -q exec:java -pl jcms-app \
    -Dlogback.configurationFile=jcms-app/src/main/resources/logback-cli.xml \
    -Dexec.mainClass=com.ysh.jcms.app.console.CmsClientConsole \
    -Dfile.encoding=UTF-8
