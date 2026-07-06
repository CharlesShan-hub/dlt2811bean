@echo off
setlocal
set "CMAKE_GENERATOR=MinGW Makefiles"
cmake .. -DCMAKE_EXPORT_COMPILE_COMMANDS=ON
exit /b %errorlevel%
