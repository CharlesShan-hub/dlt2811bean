package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

/**
 * 解析 LN 下的控制块：ReportControl / LogControl / GSEControl / SampledValueControl。
 */
public class SclControlBlockParser {

    private SclControlBlockParser() {
    }

    public static SclReportControl parseReportControl(XMLStreamReader reader) throws XMLStreamException {
        SclReportControl rc = new SclReportControl();
        rc.name(getAttr(reader, "name"));
        rc.desc(getAttr(reader, "desc"));
        rc.rptID(getAttr(reader, "rptID"));
        rc.datSet(getAttr(reader, "datSet"));
        rc.confRev(getAttr(reader, "confRev"));
        rc.buffered(getAttr(reader, "buffered"));
        rc.bufTime(getAttr(reader, "bufTime"));
        rc.intgPd(getAttr(reader, "intgPd"));
        rc.indexed(getAttr(reader, "indexed"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "TrgOps" :
                    case "OptFields" :
                    case "RptEnabled" :
                        skipElement(reader);
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return rc;
    }

    public static SclLogControl parseLogControl(XMLStreamReader reader) throws XMLStreamException {
        SclLogControl lc = new SclLogControl();
        lc.name(getAttr(reader, "name"));
        lc.desc(getAttr(reader, "desc"));
        lc.logName(getAttr(reader, "logName"));
        lc.datSet(getAttr(reader, "datSet"));
        lc.confRev(getAttr(reader, "confRev"));
        lc.intgPd(getAttr(reader, "intgPd"));
        lc.logEna(getAttr(reader, "logEna"));
        lc.reasonCode(getAttr(reader, "reasonCode"));
        skipElement(reader);
        return lc;
    }

    public static SclGSEControl parseGSEControl(XMLStreamReader reader) throws XMLStreamException {
        SclGSEControl gse = new SclGSEControl();
        gse.name(getAttr(reader, "name"));
        gse.desc(getAttr(reader, "desc"));
        gse.appID(getAttr(reader, "appID"));
        gse.datSet(getAttr(reader, "datSet"));
        gse.confRev(getAttr(reader, "confRev"));
        gse.fixedOffs(getAttr(reader, "fixedOffs"));
        gse.type(getAttr(reader, "type"));
        gse.securityEnable(getAttr(reader, "securityEnable"));
        skipElement(reader);
        return gse;
    }

    public static SclSampledValueControl parseSampledValueControl(XMLStreamReader reader) throws XMLStreamException {
        SclSampledValueControl sv = new SclSampledValueControl();
        sv.name(getAttr(reader, "name"));
        sv.desc(getAttr(reader, "desc"));
        sv.svID(getAttr(reader, "smvID"));
        sv.datSet(getAttr(reader, "datSet"));
        sv.confRev(getAttr(reader, "confRev"));
        sv.smpRate(getAttr(reader, "smpRate"));
        sv.nofASDU(getAttr(reader, "nofASDU"));
        sv.multicast(getAttr(reader, "multicast"));
        sv.securityEnable(getAttr(reader, "securityEnable"));
        skipElement(reader);
        return sv;
    }
}
