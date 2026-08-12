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
                    case "ConfDataSet" :
                        services.confDataSetMax(intAttr(reader, "max"));
                        services.confDataSetMaxAttributes(intAttr(reader, "maxAttributes"));
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
                    case "GOOSE" :
                        services.gooseMax(intAttr(reader, "max"));
                        skipElement(reader);
                        break;
                    case "GSSE" :
                        services.gsseMax(intAttr(reader, "max"));
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
                    case "SMVSettings" :
                    case "SettingGroups" :
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
        return services;
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
