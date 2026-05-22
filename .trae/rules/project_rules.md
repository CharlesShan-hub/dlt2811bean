---
alwaysApply: false
---
# DL/T 2811-2024 CMS 协议 Java 实现 - 项目规则

## 项目概述
基于 DL/T 2811-2024 标准的 CMS（变电站通信）协议 Java 实现。包含服务器端和客户端，支持交互式 CLI 和 HTTP API 远程控制。
对于windows我使用的是本地的指定版本的java：
$env:JAVA_HOME="D:\envs\.jdks\ms-21.0.10"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"

## 构建与运行
- **构建**: `mvn compile -q`（跳过测试）
- **启动服务器**: `.\win_server.ps1`（Windows）/ `./server.sh`（Linux/Mac）
- **启动交互式 CLI**: `.\win_cli.ps1`（Windows）/ `./cli.sh`（Linux/Mac）
- **远程 CLI**: `.\cms.ps1 <command>`（Windows）/ `./cms.sh <command>`（Linux/Mac，通过 HTTP API 发送命令）

## 配置说明
- **application.yaml** 中 `server.sclFiles` 支持列表，程序会依次检查第一个存在的文件
- 也兼容旧版单字符串写法 `server.sclFile: path/to/file.scd`
- 通过 `CmsConfig.Server.getResolvedSclFile()` 获取第一个实际存在的文件路径
