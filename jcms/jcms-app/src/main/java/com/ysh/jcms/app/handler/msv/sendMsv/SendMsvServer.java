package com.ysh.jcms.app.handler.msv.sendMsv;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.svc.msv.CmsSendMsvMessage;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SendMSVMessage server handler — unconfirmed service.
 *
 * <p>Clients send multicast sampled value messages to the server via TCP.
 * The server receives the SV data through this CMS service and should
 * subsequently forward it as a real L2 multicast (IEEE 802.1Q / IEC 61850-9-2)
 * on the process bus.
 *
 * <p>This is an <b>unconfirmed</b> service — no Response or Error PDU
 * is returned to the client.  See DL/T 2811 §8.10.1.
 *
 * <p><b>TODO — 后续开发计划:</b></p>
 * <ul>
 *   <li>将采样值数据 (sample) 封装为 IEC 61850-9-2 LE 格式的以太网帧</li>
 *   <li>配置目标组播 MAC 地址 (MSVCB.dstAddress) 和 VLAN 优先级</li>
 *   <li>根据 smpRate 和 smpMod 控制 SV 帧的发送时序</li>
 *   <li>支持多路 MSVCB 同时发布，每个 MSVCB 一个发送线程/定时器</li>
 *   <li>处理 confRev 变化时的同步逻辑</li>
 * </ul>
 */
public class SendMsvServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SendMsvServer.class);

    public SendMsvServer() {
        super(ServiceName.SEND_MSV_MESSAGE, CmsSendMsvMessage.class, null);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSendMsvMessage req = (CmsSendMsvMessage) decoded;
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSendMsvMessage req = (CmsSendMsvMessage) rawReq;
        int reqId = req.reqId.value();
        String msvId = str(req.msvId);
        int smpCnt = req.smpCnt.value();
        int sampleCount = req.sample.count;

        log.info("SendMSVMessage from {}: reqId={}, msvID={}, smpCnt={}, {} samples",
            session.getSessionId(), reqId, msvId, smpCnt, sampleCount);

        // TODO: Publish SV data via L2 multicast (see class-level docs).
        // Currently only logs the received SV message without forwarding.

        // Unconfirmed service — no response
        return noResponse();
    }
}
