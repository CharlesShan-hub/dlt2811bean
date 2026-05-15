package com.ysh.dlt2811bean.scl2.ref;

import com.ysh.dlt2811bean.scl2.model.*;

import java.util.List;

public class SclRefValidator {

    private final SclDocument document;

    public SclRefValidator(SclDocument document) {
        this.document = document;
    }

    public SclRefResult validate(String refStr) {
        SclRef ref;
        try {
            ref = SclRefParser.parse(refStr);
        } catch (IllegalArgumentException e) {
            return SclRefResult.invalid("Invalid reference format: " + refStr);
        }

        SclIED ied = findIedForLd(ref.getLdName());
        if (ied == null) {
            return SclRefResult.invalid("No IED found containing LDevice '" + ref.getLdName() + "'");
        }

        SclServer server = findServer(ied);
        if (server == null) {
            return SclRefResult.invalid("IED '" + ied.getName() + "' has no server");
        }

        SclLDevice ld = server.findLDeviceByInst(ref.getLdName());
        if (ld == null) {
            return SclRefResult.invalid("LDevice '" + ref.getLdName() + "' not found in IED '" + ied.getName() + "'");
        }

        SclLN ln = ld.findLnByFullName(ref.getLnName());
        if (ln == null) {
            return SclRefResult.invalid("LN '" + ref.getLnName() + "' not found in LDevice '" + ref.getLdName() + "'");
        }

        if (!ref.hasDo()) {
            return SclRefResult.valid(ref);
        }

        SclDOType doType = resolveDoType(ln);
        if (doType == null) {
            return SclRefResult.invalid("Cannot resolve DO type for LN '" + ref.getLnName() + "'");
        }

        SclDA daDef = doType.findDaByName(ref.getDoName());
        if (daDef == null) {
            return SclRefResult.invalid("DO '" + ref.getDoName() + "' not found in LN type '" + ln.getLnType() + "'");
        }

        if (!ref.hasDa()) {
            return SclRefResult.valid(ref);
        }

        SclDA subDa = findDaInType(daDef, ref.getDaName());
        if (subDa == null) {
            return SclRefResult.invalid("DA '" + ref.getDaName() + "' not found in DO '" + ref.getDoName() + "'");
        }

        return SclRefResult.valid(ref);
    }

    public boolean isValid(String refStr) {
        return validate(refStr).isValid();
    }

    private SclIED findIedForLd(String ldInst) {
        for (SclIED ied : document.getIeds()) {
            for (SclAccessPoint ap : ied.getAccessPoints()) {
                SclServer server = ap.getServer();
                if (server != null && server.findLDeviceByInst(ldInst) != null) {
                    return ied;
                }
            }
        }
        return null;
    }

    private SclServer findServer(SclIED ied) {
        for (SclAccessPoint ap : ied.getAccessPoints()) {
            if (ap.getServer() != null) return ap.getServer();
        }
        return null;
    }

    private SclDOType resolveDoType(SclLN ln) {
        SclDataTypeTemplates templates = document.getDataTypeTemplates();
        if (templates == null) return null;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
        if (lnt == null) return null;
        return null;
    }

    private SclDA findDaInType(SclDA da, String daName) {
        if (da.getName().equals(daName)) return da;
        for (SclDA sub : da.getSubDas()) {
            SclDA found = findDaInType(sub, daName);
            if (found != null) return found;
        }
        return null;
    }

    public List<String> validateAll(List<String> refs) {
        return refs.stream()
            .filter(r -> !isValid(r))
            .toList();
    }
}
