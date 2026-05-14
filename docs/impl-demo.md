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
ld-dir LD0 LTSM6; # 指定LN开始的位置
# 获取某一个设备下的LN
ln-dir LD0/ALMGGIO1 DATA_OBJECT; # 数据对象
ln-dir CTRL/LLN0 DATA_SET; # 数据集只有LLN0才有
ln-dir LD0/ALMGGIO12 DATA_SET; # 这个就是无数据
```

## 8.3 整体数据
```bash
# 获取数据值
get-all-values CTRL/ALMGGIO1 XX; 
# 获取数据值
get-all-values LD0/LLN0 XX; # 这里会有很多
get-all-values LD0/LLN0 XX YCDeadZone.dU; # 选择指定位置之后的
# 获取数据值
get-all-values LD0/RSYN1 XX; # 这里边有数值类型的
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
# 获取数据值
get-data-values CTRL/ALMGGIO1.Mod.ctlModel XX; # (CmsVisibleString) status-only
get-data-values CTRL/ALMGGIO1.Mod.dU XX; # (CmsUtf8String) Mode
get-data-values CTRL/ALMGGIO1.Alm1.dU XX; # (CmsUtf8String) 同期电压异常
get-data-values LD0/RSYN1.SynCatmms.minVal,LD0/RSYN1.SynCatmms.maxVal XX; # 0,360: 用逗号分割多个数据值
# 设置数据值
set-data-values LD0/RSYN1.SynCatmms.minVal,LD0/RSYN1.SynCatmms.maxVal 0,359 XX; # 设置数据值
get-data-values LD0/RSYN1.SynCatmms.minVal,LD0/RSYN1.SynCatmms.maxVal XX; # 可以看到已经变成0,359了（服务器重启后会恢复默认值）
# 获取某个DO下的所有DA
cache.LD0.RSYN1.DATA_OBJECT.SynCatmms; # 可以先去cache里边看一下完整的结构
get-data-dir LD0/RSYN1.SynCatmms;
get-data-dir LD0/RSYN1.SynCatmms cdcNs; # 选择指定位置之后的
# 查看某个DO下的所有DA的定义
get-data-def LD0/RSYN1.SynCatmms XX;
get-data-def LD0/RSYN1.Beh XX;
```

## 8.5 数据集
```bash

```