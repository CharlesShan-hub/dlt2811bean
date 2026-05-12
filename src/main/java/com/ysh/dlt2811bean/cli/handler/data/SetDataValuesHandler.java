package com.ysh.dlt2811bean.cli.handler.data;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.string.CmsUtf8String;
import com.ysh.dlt2811bean.datatypes.numeric.*;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsSetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsSetDataValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.scl.SclReader;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl.SclDocument;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.SclIED;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetDataValuesHandler extends AbstractServiceHandler {

    public SetDataValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_DATA_VALUES); }
    
    public List<Param> getParams() {
        return List.of(
            new Param("refs", "数据引用 (逗号分隔)", "C1/LPHD1.Proxy.stVal"),
            new Param("value", "要设置的值", "true"),
            Param.fc()
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String refs = values.get("refs");
        String val = values.get("value");
        String fc = values.get("fc");

        String[] refArr = refs.split(",");
        String[] valArr = val.split(",");

        // Load SCL model for type resolution
        SclDocument sclDocument = loadSclDocument();
        SclIED.SclServer server = findFirstServer(sclDocument);
        SclDataTypeTemplates templates = findDataTypeTemplates(sclDocument);

        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST);
        for (int i = 0; i < refArr.length; i++) {
            String ref = refArr[i].trim();
            String v = i < valArr.length ? valArr[i].trim() : valArr[valArr.length - 1].trim();

            // Resolve bType and parse value into the appropriate CmsType
            CmsType<?> typedValue = parseValueByType(ref, v, server, templates);

            CmsSetDataValuesEntry entry = new CmsSetDataValuesEntry()
                .reference(ref)
                .value(typedValue);
            if (!fc.isEmpty()) {
                entry.fc(fc);
            }
            asdu.data.add(entry);
        }

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  All data values set successfully"));
            // Add refs to cachedValues for Tab completion
            java.util.Set<String> cachedValues = ctx.getCachedValues();
            for (String ref : refArr) {
                cachedValues.add(ref.trim());
            }
        } else if (response.getMessageType() == MessageType.RESPONSE_NEGATIVE) {
            CmsSetDataValues resp = (CmsSetDataValues) response.getAsdu();
            List<String> failures = new ArrayList<>();
            for (int i = 0; i < resp.result.size() && i < refArr.length; i++) {
                int errorCode = resp.result.get(i).get();
                if (errorCode != CmsServiceError.NO_ERROR) {
                    failures.add(refArr[i].trim() + " -> error " + errorCode);
                }
            }
            printList("Some or all values failed", failures, item -> CmsColor.red(item));
        }
    }

    /**
     * Loads the SCL document from the config file path.
     */
    private SclDocument loadSclDocument() throws Exception {
        String sclPath = config.getServer().getSclFile();
        return new SclReader().read(sclPath);
    }

    /**
     * Finds the first server from the SCL document.
     */
    private SclIED.SclServer findFirstServer(SclDocument sclDocument) {
        if (sclDocument == null || sclDocument.getIeds() == null) return null;
        for (SclIED ied : sclDocument.getIeds()) {
            if (ied.getAccessPoints() != null) {
                for (SclIED.SclAccessPoint ap : ied.getAccessPoints()) {
                    if (ap.getServer() != null) {
                        return ap.getServer();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds the DataTypeTemplates from the SCL document.
     */
    private SclDataTypeTemplates findDataTypeTemplates(SclDocument sclDocument) {
        if (sclDocument == null) return null;
        return sclDocument.getDataTypeTemplates();
    }

    /**
     * Parses a string value into the appropriate CmsType based on the bType of the reference.
     * Falls back to CmsVisibleString if type resolution fails.
     */
    private CmsType<?> parseValueByType(String ref, String value,
                                         SclIED.SclServer server, SclDataTypeTemplates templates) {
        // Try to resolve bType from SCL templates
        String bType = resolveBType(ref, server, templates);
        if (bType != null) {
            try {
                return createTypedValue(bType, value);
            } catch (Exception e) {
                System.out.println(CmsColor.yellow("  Warning: Failed to parse '" + value + "' as " + bType + ", using string"));
            }
        }
        // Fallback: use visible string
        return new CmsVisibleString(value).max(255);
    }

    /**
     * Resolves the bType for a reference from SCL templates.
     * Supports formats: LD/LN.DO.DA, LD/LN.DO.SDI.BDA, LD/LN.DO (DO-level).
     */
    private String resolveBType(String ref, SclIED.SclServer server, SclDataTypeTemplates templates) {
        if (server == null || templates == null) return null;

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;

        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");

        if (parts.length < 2) return null;

        String lnName = parts[0];
        String doName = parts[1];

        if (parts.length == 4) {
            // SDI.BDA format: e.g. sVC.offset
            String sdiName = parts[2];
            String bdaName = parts[3];
            return SclTypeResolver.resolveSdiBType(server, templates, ldName, lnName, doName, sdiName, bdaName);
        } else if (parts.length == 3) {
            // Direct DA: e.g. stVal
            String daName = parts[2];
            return SclTypeResolver.resolveBType(server, templates, ldName, lnName, doName, daName);
        } else {
            // DO-level (no DA specified): use first DA's type
            var das = SclTypeResolver.listDasFromType(server, templates, ldName, lnName, doName);
            if (das != null && !das.isEmpty()) {
                String daName = das.get(0).getName();
                return SclTypeResolver.resolveBType(server, templates, ldName, lnName, doName, daName);
            }
        }

        return null;
    }

    /**
     * Creates a typed CmsType value from a string based on the bType.
     * Supports all standard DL/T 2811 data types.
     */
    private CmsType<?> createTypedValue(String bType, String value) {
        switch (bType) {
            case "BOOLEAN":
                return new CmsBoolean(Boolean.parseBoolean(value.trim()));
            case "INT8":
                return new CmsInt8(Integer.parseInt(value.trim()));
            case "INT16":
                return new CmsInt16(Integer.parseInt(value.trim()));
            case "INT32":
                return new CmsInt32(Integer.parseInt(value.trim()));
            case "INT64":
                return new CmsInt64(Long.parseLong(value.trim()));
            case "INT8U":
                return new CmsInt8U(Integer.parseInt(value.trim()));
            case "INT16U":
                return new CmsInt16U(Integer.parseInt(value.trim()));
            case "INT32U":
                return new CmsInt32U(Long.parseLong(value.trim()));
            case "INT64U":
                return new CmsInt64U(new java.math.BigInteger(value.trim()));
            case "FLOAT32":
                return new CmsFloat32(Float.parseFloat(value.trim()));
            case "FLOAT64":
                return new CmsFloat64(Double.parseDouble(value.trim()));
            case "VisString255":
            case "VISIBLE STRING":
                return new CmsVisibleString(value).max(255);
            case "Unicode255":
            case "UNICODE STRING":
                return new CmsUtf8String(value).max(255);
            default:
                // For complex types (Quality, Dbpos, Tcmd, Check, Timestamp, Enum, Struct, etc.),
                // fall back to visible string as they require special handling
                return new CmsVisibleString(value).max(255);
        }
    }
}
