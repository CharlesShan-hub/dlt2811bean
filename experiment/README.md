# CMS Experiment — ASN.1 PER Codec & C Code Generator

## 目录结构

```
cmsper/          ← PER 编解码核心库 (C)
cmsgenerator/    ← ASN.1 → C 代码生成器
cmsapp/          ← 应用示例 (roundtrip 测试)
docs/
  cms.asn1       ← CMS ASN.1 定义
```

## 构建顺序

### 1. PER 核心库

```powershell
cd cmsper
.\win_cmsper.ps1
```

### 2. 代码生成器 + 数据类型库

```powershell
cd cmsgenerator
.\win_cmsgen.ps1 -InputAsn ..\docs\cms.asn1
```

### 3. FFI DLL（跨语言调用）

```powershell
cd cmsgenerator
.\win_dll.ps1
```

输出：`cmsgenerator/build/bin/libcmsper_datatypes.dll`

### 4. 应用示例

```powershell
cd cmsapp
.\win_cmsapp.ps1
```

## 运行示例

`win_cmsapp.ps1` 会依次运行五个 roundtrip 测试：

- **sgcb_roundtrip** — 普通数据编解码
- **seq_of_roundtrip** — SEQUENCE OF 嵌套编解码
- **service_roundtrip** — 服务报文编解码 (Associate/Release/Abort)
- **apdu_roundtrip** — APDU 帧编解码 (APCH 4B 帧头 + ASDU)
- **ffi_roundtrip** — FFI 层编解码 (byte[] in/out)

## FFI 接口

`cms_ffi.h` 提供纯 C ABI 接口，各语言通过 FFI 加载 DLL 直接调用：

| 函数 | 说明 |
|------|------|
| `cms_ffi_encode_associate_request` | Associate-Request 编码 |
| `cms_ffi_decode_associate_request` | Associate-Request 解码 |
| `cms_ffi_encode_release_request` | Release-Request 编码 |
| `cms_ffi_decode_release_request` | Release-Request 解码 |
| `cms_ffi_encode_abort` | Abort 编码 |
| `cms_ffi_decode_abort` | Abort 解码 |

所有接口均为 `byte[]` 进 `byte[]` 出，与语言无关。

## APDU 帧格式

APDU = APCH(4B) + ASDU(PER 编码)，与 Java 端 `CmsApdu` 二进制兼容：

```
┌──────────┬────────┬──────────┬─────────────┐
│ CC (1B)  │ SC(1B) │ FL (2B)  │ ASDU (nB)   │
│ Control  │ 服务码  │ 帧长度   │ PER 编码数据 │
│ Code     │        │ (大端)   │             │
└──────────┴────────┴──────────┴─────────────┘
```

CC 字节：`bit7=Next | bit6=Resp | bit5=Err | bit4=bak | bit3~0=PI(0x01)`
