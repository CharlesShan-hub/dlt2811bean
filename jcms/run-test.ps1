# run-test.ps1 — 逐个模块安装 + 测试
# 先 install 到本地仓库（跳过测试避免 fork 崩溃）
# 最后单独跑 jcms-app 的测试

$ErrorActionPreference = "Stop"
$JCMS = $PSScriptRoot
$orig = Get-Location

function cleanup { Set-Location $orig }
trap { cleanup; break }

Write-Host "===== 1. 清理本地仓库缓存 =====" -ForegroundColor Cyan
foreach ($m in "jcms-core", "jcms-utils", "jcms-app") {
    $dir = "$env:USERPROFILE\.m2\repository\com\ysh\$m\1.0.0-SNAPSHOT"
    if (Test-Path $dir) { Remove-Item "$dir\*" -Recurse -Force -ErrorAction SilentlyContinue }
}
Get-ChildItem "$JCMS\jcms-core", "$JCMS\jcms-utils", "$JCMS\jcms-app" -Directory -ErrorAction SilentlyContinue `
    | ForEach-Object { Remove-Item "$_\target" -Recurse -Force -ErrorAction SilentlyContinue }

Write-Host "===== 2. install parent POM =====" -ForegroundColor Cyan
Set-Location "$JCMS"
$out = mvn install -N --batch-mode 2>&1
$out | Select-String -Pattern "BUILD"
$exit = $LASTEXITCODE
if ($exit -ne 0) { Write-Output $out; throw "parent POM install failed" }

Write-Host "===== 3. install jcms-core (跳过测试) =====" -ForegroundColor Cyan
Set-Location "$JCMS\jcms-core"
$out = mvn install -DskipTests --batch-mode 2>&1
$out | Select-String -Pattern "BUILD"
$exit = $LASTEXITCODE
if ($exit -ne 0) { Write-Output $out; throw "jcms-core install failed" }

Write-Host "===== 4. install jcms-utils (跳过测试) =====" -ForegroundColor Cyan
Set-Location "$JCMS\jcms-utils"
$out = mvn install -DskipTests --batch-mode 2>&1
$out | Select-String -Pattern "BUILD"
$exit = $LASTEXITCODE
if ($exit -ne 0) { Write-Output $out; throw "jcms-utils install failed" }

Write-Host "===== 5. 测试 jcms-app =====" -ForegroundColor Cyan
Set-Location "$JCMS\jcms-app"
mvn test --batch-mode 2>&1
$exit = $LASTEXITCODE
Set-Location $orig

if ($exit -eq 0) {
    Write-Host ""
    Write-Host "===== 全部通过! =====" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "===== 有测试失败 =====" -ForegroundColor Red
}
exit $exit
