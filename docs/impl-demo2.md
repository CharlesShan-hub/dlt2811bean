# 实现示例（./cms 远程客户端版）

## 连接服务器

```bash
# 先启动交互式 CLI（终端 1）
./win_cli

# 在另一个终端（终端 2）执行以下命令

# 连接服务器（只做 TCP 连接）
./cms connect 127.0.0.1 8102

# 协商（可选，connect 时传 asduSize 会自动协商）
./cms negotiate 65531 1

# 服务层连接
./cms associate C_B5041X S1 true

# 获取连接状态
./cms status

# 测试 tcp 连接
./cms test

# 清除屏幕
./cms clear

# 断开服务层连接
./cms release

# 断开 TCP
./cms abort 4
```

## 目录结构获取

```bash
# 获取所有 LDevice 目录
./cms server-dir

# 从指定位置开始获取 LDevice 目录
./cms server-dir LD0

# 获取某个 LDevice 下的所有 LN
./cms ld-dir LD0

# 从指定 LN 开始获取
./cms ld-dir LD0 LTSM6

# 获取 LN 下的数据对象
./cms ln-dir LD0/ALMGGIO1 DATA_OBJECT

# 数据集只有 LLN0 才有
./cms ln-dir CTRL/LLN0 DATA_SET

# 无数据的 LN
./cms ln-dir LD0/ALMGGIO12 DATA_SET
```

## 整体数据

```bash
# 获取数据值
./cms get-all-values CTRL/ALMGGIO1 XX

# LLN0 下会有很多数据
./cms get-all-values LD0/LLN0 XX

# 选择指定位置之后的数据
./cms get-all-values LD0/LLN0 XX YCDeadZone.dU

# 数值类型的数据
./cms get-all-values LD0/RSYN1 XX

# 获取数据定义
./cms get-all-def CTRL/ALMGGIO1 XX

# 获取控制块
./cms get-all-cb CTRL/LLN0 BRCB
./cms get-all-cb CTRL/LLN0 GO_CB
./cms get-all-cb CTRL/LLN0 LCB
./cms get-all-cb CTRL/LLN0 MSV_CB
./cms get-all-cb CTRL/LLN0 SGCB
./cms get-all-cb CTRL/LLN0 URCB
```

## 单个数据

```bash
# 获取数据值
./cms get-data-values CTRL/ALMGGIO1.Mod.ctlModel XX
./cms get-data-values CTRL/ALMGGIO1.Mod.dU XX
./cms get-data-values CTRL/ALMGGIO1.Alm1.dU XX

# 用逗号分割多个数据值
./cms get-data-values LD0/RSYN1.SynCatmms.minVal,LD0/RSYN1.SynCatmms.maxVal XX

# 设置数据值（服务器重启后会恢复默认值）
./cms set-data-values LD0/RSYN1.SynCatmms.minVal,LD0/RSYN1.SynCatmms.maxVal 0,359 XX

# 验证修改
./cms get-data-values LD0/RSYN1.SynCatmms.minVal,LD0/RSYN1.SynCatmms.maxVal XX

# 查看某个 DO 下的所有 DA
./cms cache LD0/RSYN1.DATA_OBJECT.SynCatmms

# 获取 DA 目录
./cms get-data-dir LD0/RSYN1.SynCatmms

# 选择指定位置之后的 DA
./cms get-data-dir LD0/RSYN1.SynCatmms cdcNs

# 查看某个 DO 下的所有 DA 的定义
./cms get-data-def LD0/RSYN1.SynCatmms XX
./cms get-data-def LD0/RSYN1.Beh XX
```

## 数据集的读写

```bash
# 查看数据集字段（会给出 DO 和 FC）
./cms get-dataset-dir MEAS/LLN0.dsAin1

# 查看数据集里边的值
./cms get-dataset-values MEAS/LLN0.dsAin1

# 通过 cache 查看字段详情
./cms cache MEAS.LLN0.DATA_SET.dsAin1.12.DO

# 数据集写入（从头写，一个值）
./cms set-dataset-values MEAS/LLN0.dsAin1 Bay1_P1

# 从头写，多个值
./cms set-dataset-values MEAS/LLN0.dsAin1 Bay1_P1,Bay1_Q1,Bay1_S1

# 从指定位置后写，多个值
./cms set-dataset-values MEAS/LLN0.dsAin1 Bay1_Q2,Bay1_S2 MEAS/MMXU1.TotW

# 指定参数写入
./cms set-dataset-values MEAS/LLN0.dsAin1 Bay1_Q3 --referenceAfter MEAS/MMXU1.TotW
```

## 数据集的创建与删除

```bash
# 创建新数据集 myTestDs
./cms create-dataset --dsRef MEAS/LLN0.myTestDs --ref MEAS/LLN0.AmpSv.instMag.i --fc MX

# 验证创建成功
./cms get-dataset-dir MEAS/LLN0.myTestDs

# 重复创建同名数据集（会先删旧的再建新的）
./cms create-dataset --dsRef MEAS/LLN0.myTestDs --ref MEAS/LLN0.AmpSv.instMag.q --fc MX

# 验证被覆盖（只有 1 个成员 q）
./cms get-dataset-dir MEAS/LLN0.myTestDs

# 删除数据集
./cms delete-dataset MEAS/LLN0.myTestDs

# 验证删除成功
./cms get-dataset-dir MEAS/LLN0.myTestDs

# 删除预定义数据集（应该失败）
./cms delete-dataset MEAS/LLN0.dsAin1

# release 后重新 connect
./cms release
./cms connect 127.0.0.1 8102 65531 1 C_B5041X S1 true

# 再次创建同名数据集
./cms create-dataset --dsRef MEAS/LLN0.myTestDs --ref MEAS/LLN0.AmpSv.instMag.i --fc MX
```

## 定值组（SG）

```bash
# 查看当前定值组状态
./cms get-sgcb-values MEAS/LLN0.SGCB

# 切换到 SG 2（numOfSG=1 时会失败）
./cms select-active-sg MEAS/LLN0.SGCB 2

# 切换到 SG 1
./cms select-active-sg MEAS/LLN0.SGCB 1

# 验证 actSG 已变为 1
./cms get-sgcb-values MEAS/LLN0.SGCB
```

```bash
# 查看 LLN0 下有哪些数据对象
./cms ln-dir MEAS/LLN0

# 开始编辑第 1 组
./cms select-edit-sg MEAS/LLN0.SGCB 1

# 修改定值
./cms set-edit-sg-value MEAS/LLN0.SGXXX.xxx 30

# 查看当前编辑的值
./cms set-edit-sg-value MEAS/LLN0.SGXXX.xxx SG

# 确认编辑完成，写入 SCL 模型
./cms confirm-edit-sg MEAS/LLN0.SGCB

# 验证修改
./cms get-data-values MEAS/LLN0.SGXXX.xxx SG

# 激活第 1 组
./cms select-active-sg MEAS/LLN0.SGCB 1

# 再次验证
./cms get-data-values MEAS/LLN0.SGXXX.xxx SG

# 断开重连后验证（服务器重启会恢复默认值）
./cms release
./cms connect 127.0.0.1 8102 65531 1 C_B5041X S1 true
./cms get-data-values MEAS/LLN0.SGXXX.xxx SG
```

## 报告

```bash
# 查看 BRCB 块
./cms ln-dir LD0/LLN0 BRCB

# 获取 BRCB 值
./cms get-brcb-values LD0/LLN0.brcbAlarm
./cms get-brcb-values LD0/LLN0.brcbWarning
./cms get-brcb-values LD0/LLN0.brcbCommState

# 查看 URCB 块
./cms ln-dir MEAS/LLN0 URCB

# 获取 URCB 值
./cms get-urcb-values MEAS/LLN0.urcbAinA
./cms get-urcb-values MEAS/LLN0.urcbAinB
./cms get-urcb-values MEAS/LLN0.urcbAinD
```

```bash
# 设置 rptEna=true
./cms set-brcb-values MEAS/LLN0.brcbWarning rptEna=true

# 确认已变更
./cms get-brcb-values MEAS/LLN0.brcbWarning

# 设置 rptEna=false
./cms set-brcb-values MEAS/LLN0.brcbWarning rptEna=false

# 确认
./cms get-brcb-values MEAS/LLN0.brcbWarning
```

```bash
# 批量设置报告参数
./cms set-brcb-values MEAS/LLN0.brcbWarning --rptEna true --rptID myRptID --datSet myDataSet --optFlds 1023 --bufTm 2000 --trgOps 7 --intgPd 10000 --gi true --purgeBuf false

# 确认所有参数
./cms get-brcb-values MEAS/LLN0.brcbWarning
```
