# 直接运行编译好的测试程序（CMakeLists.txt 未注册 ctest）
Push-Location ccms\build\bin
.\test_per.exe
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
.\test_scalar.exe
if ($LASTEXITCODE -ne 0) { Pop-Location; exit 1 }
Pop-Location
Write-Host "[OK] ccms 测试通过" -ForegroundColor Green
