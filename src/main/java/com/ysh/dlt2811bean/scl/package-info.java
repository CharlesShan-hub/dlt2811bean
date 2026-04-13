/**
 * SCL（变电站配置语言）解析器：ICD/CID/SCD/IID 文件 → 内存信息模型。
 * <p>复用 iec61850bean 的 SclParser 实现。
 * 只依赖 core.model，不依赖 CMS 协议层。
 */
package com.ysh.dlt2811bean.scl;
