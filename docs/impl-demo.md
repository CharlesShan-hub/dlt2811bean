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

## 8.5 数据集的读写
```bash
# 查看数据集字段dir: 这里边会给出DO和FC，通过FC就知道是哪个DO下的哪个DA
get-dataset-dir MEAS/LLN0.dsAin1;
# 查看数据集里边的值
get-dataset-values MEAS/LLN0.dsAin1;
# 这里可以看到很多字段是error，这是因为没有初始值，可以通过cache查看
cache.MEAS.LLN0.DATA_SET.dsAin1.12.DO;

# 数据集写入
set-dataset-values MEAS/LLN0.dsAin1 Bay1_P1; # 从头写，一个值
set-dataset-values MEAS/LLN0.dsAin1 Bay1_P1,Bay1_Q1,Bay1_S1; # 从头写，多个值
set-dataset-values MEAS/LLN0.dsAin1 Bay1_Q2,Bay1_S2 MEAS/MMXU1.TotW; # 从第一个后边写，多个值
set-dataset-values MEAS/LLN0.dsAin1 Bay1_Q3 --referenceAfter MEAS/MMXU1.TotW; # 可以指定参数
```

## 8.6 数据集的创建与删除
```bash
# 1. 创建新数据集 myTestDs（应该成功）
create-dataset --dsRef MEAS/LLN0.myTestDs --ref MEAS/LLN0.AmpSv.instMag.i --fc MX;
# 2. 验证创建成功
get-dataset-dir MEAS/LLN0.myTestDs;
# 3. 重复创建同名数据集（应该成功，因为会先删旧的再建新的）
create-dataset --dsRef MEAS/LLN0.myTestDs --ref MEAS/LLN0.AmpSv.instMag.q --fc MX;
# 4. 验证被覆盖了（只有 1 个成员 q，没有 i）
get-dataset-dir MEAS/LLN0.myTestDs;
# 5. 删除数据集 myTestDs（应该成功）
delete-dataset MEAS/LLN0.myTestDs;
# 6. 验证删除成功
get-dataset-dir MEAS/LLN0.myTestDs;
# 7. 删除预定义数据集 dsAin1（应该失败）
delete-dataset MEAS/LLN0.dsAin1;
# 8. release 后重新 connect
release;
connect 127.0.0.1 8102 65531 1 C_B5041X S1 true;
# 9. 再次创建同名数据集 myTestDs（应该成功，因为 release 时已删除）
create-dataset --dsRef MEAS/LLN0.myTestDs --ref MEAS/LLN0.AmpSv.instMag.i --fc MX;
```

# sg

```bash
# 先查看当前状态（actSG=1）
get-sgcb-values MEAS/LLN0.SGCB;
# 切换到 SG 2（应该成功，因为 numOfSG=1 所以会失败）
select-active-sg MEAS/LLN0.SGCB 2;
# 切换到 SG 1（应该成功）
select-active-sg MEAS/LLN0.SGCB 1;
# 验证 actSG 已变为 1
get-sgcb-values MEAS/LLN0.SGCB;
```

```bash
select-edit-sg MEAS/LLN0.SGCB 1      # 开始编辑，cnfEdit=false
set-edit-sg-value MEAS/LLN0.SGCB 100  # 设置值
set-edit-sg-value MEAS/LLN0.SGCB 200  # 还可以再改
get-edit-sg-value MEAS/LLN0.SGCB SG   # 查看当前编辑的值
confirm-edit-sg MEAS/LLN0.SGCB        # 确认编辑完成，cnfEdit=true
select-active-sg MEAS/LLN0.SGCB 1     # 激活
```

# 报告

```bash
# brcb
ln-dir LD0/LLN0 BRCB; # 这里可以看到有三个BRCB块
get-brcb-values  LD0/LLN0.brcbAlarm;
get-brcb-values  LD0/LLN0.brcbWarning;
get-brcb-values  LD0/LLN0.brcbCommState;

# urcb
ln-dir MEAS/LLN0 URCB; # 也是有三个urcb块
get-urcb-values MEAS/LLN0.urcbAinA;
get-urcb-values MEAS/LLN0.urcbAinB;
get-urcb-values MEAS/LLN0.urcbAinD;
```

```bash
# 设置 rptEna=true
set-brcb-values MEAS/LLN0.brcbWarning rptEna=true;
# 确认已变更
get-brcb-values MEAS/LLN0.brcbWarning;
# 设置 rptEna=false
set-brcb-values MEAS/LLN0.brcbWarning rptEna=false;
# 确认
get-brcb-values MEAS/LLN0.brcbWarning;
```

```bash
# 报告设置
get-brcb-values MEAS/LLN0.brcbWarning;
set-brcb-values MEAS/LLN0.brcbWarning --rptEna true --rptID myRptID --datSet myDataSet --optFlds 1023 --bufTm 2000 --trgOps 7 --intgPd 10000 --gi true --purgeBuf false;
get-brcb-values MEAS/LLN0.brcbWarning;
```