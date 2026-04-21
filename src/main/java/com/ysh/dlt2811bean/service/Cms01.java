package com.ysh.dlt2811bean.service;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import com.ysh.dlt2811bean.utils.per.types.PerVisibleString;
import com.ysh.dlt2811bean.utils.per.types.PerOctetString;

/**
 * CMS 服务码 01 — Associate（关联请求）。
 *
 * <p>ASDU 字段布局（PER 编码，按顺序）：
 * <pre>
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID           OCTET STRING (SIZE(2))          │  2 字节请求序号
 * │ ProtocolVersion INTEGER (0..255)                │  协议版本号
 * │ ApduSize        INTEGER (0..65535)              │  本端最大 APDU 长度
 * │ AsduSize        INTEGER (0..65535)              │  本端最大 ASDU 长度
 * │ ServerName      VisibleString (SIZE(0..255))    │  本端服务器名称
 * └─────────────────────────────────────────────────┘
 * </pre>
 *
 * <p>⚠️ 字段定义及约束范围是占位值，需对照 GB/T 45906.3 标准正文修正。
 */
public class Cms01 extends CmsService {

    public Cms01() {
        super(1);
    }

    // ==================== 字段 ====================

    private int reqId;
    private int protocolVersion;
    private int apduSize;
    private int asduSize;
    private String serverName;

    // ==================== 编码 ====================

    @Override
    protected byte[] encodeAsdu() {
        PerOutputStream pos = new PerOutputStream();

        PerOctetString.encodeFixedSize(pos, intToBytes2(reqId), 2);
        PerInteger.encode(pos, protocolVersion, 0, 255);
        PerInteger.encode(pos, apduSize, 0, 65535);
        PerInteger.encode(pos, asduSize, 0, 65535);
        PerVisibleString.encodeConstrained(pos, serverName != null ? serverName : "", 0, 255);

        return pos.toByteArray();
    }

    // ==================== 解码 ====================

    @Override
    protected void decodeAsdu(PerInputStream pis) throws PerDecodeException {
        byte[] reqIdBytes = PerOctetString.decodeFixedSize(pis, 2);
        this.reqId = bytes2ToInt(reqIdBytes);
        this.protocolVersion = (int) PerInteger.decode(pis, 0, 255);
        this.apduSize = (int) PerInteger.decode(pis, 0, 65535);
        this.asduSize = (int) PerInteger.decode(pis, 0, 65535);
        this.serverName = PerVisibleString.decodeConstrained(pis, 0, 255);
    }

    // ==================== Getter / Setter ====================

    public int getReqId() { return reqId; }
    public Cms01 setReqId(int reqId) { this.reqId = reqId; return this; }

    public int getProtocolVersion() { return protocolVersion; }
    public Cms01 setProtocolVersion(int protocolVersion) { this.protocolVersion = protocolVersion; return this; }

    public int getApduSize() { return apduSize; }
    public Cms01 setApduSize(int apduSize) { this.apduSize = apduSize; return this; }

    public int getAsduSize() { return asduSize; }
    public Cms01 setAsduSize(int asduSize) { this.asduSize = asduSize; return this; }

    public String getServerName() { return serverName; }
    public Cms01 setServerName(String serverName) { this.serverName = serverName; return this; }

    @Override
    public String toString() {
        return "Cms01{reqId=" + reqId
            + ", protocolVersion=" + protocolVersion
            + ", apduSize=" + apduSize
            + ", asduSize=" + asduSize
            + ", serverName='" + serverName + "'}";
    }
}
