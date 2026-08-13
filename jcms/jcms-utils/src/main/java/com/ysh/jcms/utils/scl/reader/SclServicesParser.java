package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.model.ied.SclGSESettings;
import com.ysh.jcms.utils.scl.model.ied.SclReportSettings;
import com.ysh.jcms.utils.scl.model.ied.SclServices;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

/** 解析 IED 下的 {@code <Services>} 及其子元素（ReportSettings / GSESettings）。 */
public class SclServicesParser {

    private SclServicesParser() {
    }

    public static SclServices parseServices(XMLStreamReader reader) throws XMLStreamException {
        SclServices services = new SclServices();
        services.nameLength(intAttr(reader, "nameLength"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "DynAssociation" :
                        services.dynAssociation(true);
                        skipElement(reader);
                        break;
                    case "GetDirectory" :
                        services.getDirectory(true);
                        skipElement(reader);
                        break;
                    case "GetDataObjectDefinition" :
                        services.getDataObjectDefinition(true);
                        skipElement(reader);
                        break;
                    case "GetDataSetValue" :
                        services.getDataSetValue(true);
                        skipElement(reader);
                        break;
                    case "DataSetDirectory" :
                        services.dataSetDirectory(true);
                        skipElement(reader);
                        break;
                    case "ReadWrite" :
                        services.readWrite(true);
                        skipElement(reader);
                        break;
                    case "FileHandling" :
                        services.fileHandling(true);
                        skipElement(reader);
                        break;
                    case "GetCBValues" :
                        services.getCBValues(true);
                        skipElement(reader);
                        break;
                    case "GSEDir" :
                        services.gSEDir(true);
                        skipElement(reader);
                        break;
                    case "TimerActivatedControl" :
                        services.timerActivatedControl(true);
                        skipElement(reader);
                        break;
                    case "DataObjectDirectory" :
                        services.dataObjectDirectory(true);
                        skipElement(reader);
                        break;
                    case "SetDataSetValue" :
                        services.setDataSetValue(true);
                        skipElement(reader);
                        break;
                    case "ConfLdName" :
                        services.confLdName(true);
                        skipElement(reader);
                        break;
                    case "ConfDataSet" :
                        services.confDataSetMax(intAttr(reader, "max"));
                        services.confDataSetMaxAttributes(intAttr(reader, "maxAttributes"));
                        skipElement(reader);
                        break;
                    case "DynDataSet" :
                        services.dynDataSetMax(intAttr(reader, "max"));
                        services.dynDataSetMaxAttributes(intAttr(reader, "maxAttributes"));
                        skipElement(reader);
                        break;
                    case "ConfReportControl" :
                        services.confReportControlMax(intAttr(reader, "max"));
                        skipElement(reader);
                        break;
                    case "ConfLogControl" :
                        services.confLogControlMax(intAttr(reader, "max"));
                        skipElement(reader);
                        break;
                    case "ConfSigRef" :
                        services.confSigRefMax(intAttr(reader, "max"));
                        skipElement(reader);
                        break;
                    case "GOOSE" :
                        services.gooseMax(intAttr(reader, "max"));
                        services.gooseFixedOffs(boolAttr(reader, "fixedOffs"));
                        skipElement(reader);
                        break;
                    case "GSSE" :
                        services.gsseMax(intAttr(reader, "max"));
                        skipElement(reader);
                        break;
                    case "SMVsc" :
                        services.smvscMax(intAttr(reader, "max"));
                        services.smvscDelivery(getAttr(reader, "delivery"));
                        skipElement(reader);
                        break;
                    case "SupSubscription" :
                        services.supSubscriptionMaxGo(intAttr(reader, "maxGo"));
                        services.supSubscriptionMaxSv(intAttr(reader, "maxSv"));
                        skipElement(reader);
                        break;
                    case "ValueHandling" :
                        services.valueHandlingSetToRO(boolAttr(reader, "setToRO"));
                        skipElement(reader);
                        break;
                    case "RedProt" :
                        services.redProtHsr(boolAttr(reader, "hsr"));
                        services.redProtPrp(boolAttr(reader, "prp"));
                        services.redProtRstp(boolAttr(reader, "rstp"));
                        skipElement(reader);
                        break;
                    case "TimeSyncProt" :
                        services.timeSyncProtSntp(boolAttr(reader, "sntp"));
                        services.timeSyncProtC37_238(boolAttr(reader, "c37_238"));
                        services.timeSyncProtOther(boolAttr(reader, "other"));
                        skipElement(reader);
                        break;
                    case "CommProt" :
                        services.commProtIpv6(boolAttr(reader, "ipv6"));
                        skipElement(reader);
                        break;
                    case "ClientServices" :
                        services.clientServicesGoose(boolAttr(reader, "goose"));
                        services.clientServicesGsse(boolAttr(reader, "gsse"));
                        services.clientServicesBufReport(boolAttr(reader, "bufReport"));
                        services.clientServicesUnbufReport(boolAttr(reader, "unbufReport"));
                        services.clientServicesReadLog(boolAttr(reader, "readLog"));
                        services.clientServicesSv(boolAttr(reader, "sv"));
                        services.clientServicesSupportsLdName(boolAttr(reader, "supportsLdName"));
                        skipElement(reader);
                        break;
                    case "ConfLNs" :
                        services.confLNsFixPrefix(boolAttr(reader, "fixPrefix"));
                        services.confLNsFixLnInst(boolAttr(reader, "fixLnInst"));
                        skipElement(reader);
                        break;
                    case "ReportSettings" :
                        services.reportSettings(parseReportSettings(reader));
                        break;
                    case "GSESettings" :
                        services.gseSettings(parseGseSettings(reader));
                        break;
                    case "LogSettings" :
                        services.logSettingsLogEna(getAttr(reader, "logEna"));
                        services.logSettingsTrgOps(getAttr(reader, "trgOps"));
                        services.logSettingsIntgPd(getAttr(reader, "intgPd"));
                        skipElement(reader);
                        break;
                    case "SMVSettings" :
                        parseSmvSettings(reader, services);
                        break;
                    case "SettingGroups" :
                        parseSettingGroups(reader, services);
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return services;
    }

    /** SMVSettings：收第一个 SmpRate 子元素文本（其余子元素跳过）。 */
    private static void parseSmvSettings(XMLStreamReader reader, SclServices services) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("SmpRate".equals(reader.getLocalName())) {
                    services.smvSettingsSmpRate(parseSimpleElementText(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
    }

    /** SettingGroups：收 SGEdit / ConfSG 子元素存在性（其 resvTms 等深属性不展开）。 */
    private static void parseSettingGroups(XMLStreamReader reader, SclServices services) throws XMLStreamException {
        services.settingGroups(true);
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "SGEdit" :
                        services.sgEdit(true);
                        skipElement(reader);
                        break;
                    case "ConfSG" :
                        services.confSG(true);
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
    }

    private static SclReportSettings parseReportSettings(XMLStreamReader reader) throws XMLStreamException {
        SclReportSettings rs = new SclReportSettings();
        rs.bufTime(getAttr(reader, "bufTime"));
        rs.cbName(getAttr(reader, "cbName"));
        rs.rptID(getAttr(reader, "rptID"));
        rs.datSet(getAttr(reader, "datSet"));
        rs.intgPd(getAttr(reader, "intgPd"));
        rs.optFields(getAttr(reader, "optFields"));
        skipElement(reader);
        return rs;
    }

    private static SclGSESettings parseGseSettings(XMLStreamReader reader) throws XMLStreamException {
        SclGSESettings gs = new SclGSESettings();
        gs.appID(getAttr(reader, "appID"));
        gs.cbName(getAttr(reader, "cbName"));
        gs.datSet(getAttr(reader, "datSet"));
        skipElement(reader);
        return gs;
    }
}
