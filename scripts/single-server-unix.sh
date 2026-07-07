#!/bin/sh
cd jcms && mvn -q exec:java -pl jcms-app \
    -Dexec.mainClass=com.ysh.jcms.app.console.CmsServerConsole \
    -Dexec.args=18780 \
    -Dfile.encoding=UTF-8
