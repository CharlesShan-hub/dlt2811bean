# 实现示例

## 连接服务器
```bash
# 连接服务器, 也可以从服务器启动后的地方粘贴
connect 127.0.0.1 8102 65531 1 C_B5041X S1 true;
# 获取连接状态
status
# 测试tcp连接
test
# 清除屏幕
clear
# 断开连接
release
# 断开tcp
abort 4;
# 退出
exit
```

## 8.2 服务层连接
```bash
# 协商，已经在connect后自动执行
negotiate 65531 1;
# 服务层连接
associate C_B5041X S1 true;
```

## 8.3 目录结构获取
```bash
# 获取LDevice目录
server-dir; # 这样会返回所有LDevice名称
server-dir LD0; # 指定开始的位置
# 获取某一个设备下的LDevice
# ld-dir; # 这样会返回所有LN名称
ld-dir LD0; # 指定LDevice名称,不指定则返回所有LN名称
ld-dir LD0 ALMGGIO12; # 指定LN开始的位置
# 获取某一个设备下的LN
ln-dir LD0/ALMGGIO1 DATA_OBJECT; # 数据对象
ln-dir CTRL/LLN0 DATA_SET; # 数据集只有LLN0才有
```

## 8.3 整体数据
```bash
# 获取数据值
get-all-values CTRL/ALMGGIO1 XX; 
# 获取数据定义
get-all-def CTRL/ALMGGIO1 XX; 
# 获取cb (BRCB,GO_CB,LCB,MSV_CB,SGCB,URCB)
get-all-cb CTRL/LLN0 BRCB;
get-all-cb CTRL/LLN0 GO_CB;
get-all-cb CTRL/LLN0 LCB;
get-all-cb CTRL/LLN0 MSV_CB;
get-all-cb CTRL/LLN0 SGCB;
get-all-cb CTRL/LLN0 URCB;
```

# 8.4 单个数据
```bash
#
```