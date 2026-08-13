package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;

/**
 * SCL operation service for data sets (DataSet).
 * <p>
 * Consolidates the duplicated SCL parsing logic in the
 * GetDataSetDirectory、GetDataSetValues、SetDataSetValues、CreateDataSet、DeleteDataSet
 * handlers.
 */
public class SclDatasetService {

    /** Data set reference resolution result. */
    public static class DataSetResolution {
        public final SclLDevice device;
        public final SclLN ln;
        public final SclDataSet dataSet;

        public DataSetResolution(SclLDevice device, SclLN ln, SclDataSet dataSet) {
            this.device = device;
            this.ln = ln;
            this.dataSet = dataSet;
        }
    }

    /**
     * Resolves a data set reference string {@code "LD0/LLN0.dsName"} and returns the LD/LN/DataSet triple.
     * <p>
     * Looks up within the scope of the given AP.
     *
     * @param ied
     *            IED model
     * @param ap
     *            the currently associated access point
     * @param ref
     *            reference string, format {@code ldInst/lnName.dsName}
     * @return resolution result, or {@code null} if any step fails
     */
    public static DataSetResolution resolveDataSet(SclIED ied, SclAccessPoint ap, String ref) {
        if (ref == null || !SclRefParser.isValid(ref))
            return null;

        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String dsName = sclRef.doName();
        if (dsName == null)
            return null;

        SclLDevice device = resolveLd(ap, ldName);
        if (device == null)
            return null;

        SclLN ln = resolveLn(device, lnName);
        if (ln == null)
            return null;

        SclDataSet dataSet = ln.findDataSetByName(dsName);
        if (dataSet == null)
            return null;

        return new DataSetResolution(device, ln, dataSet);
    }

    /**
     * Resolves a data set reference string {@code "LD0/LLN0.dsName"} and returns the LD/LN/DataSet triple.
     *
     * @param ied
     *            IED model
     * @param ref
     *            reference string, format {@code ldInst/lnName.dsName}
     * @return resolution result, or {@code null} if any step fails
     */
    public static DataSetResolution resolveDataSet(SclIED ied, String ref) {
        if (ref == null || !SclRefParser.isValid(ref))
            return null;

        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String dsName = sclRef.doName();
        if (dsName == null)
            return null;

        SclLDevice device = resolveLd(ied, ldName);
        if (device == null)
            return null;

        SclLN ln = resolveLn(device, lnName);
        if (ln == null)
            return null;

        SclDataSet dataSet = ln.findDataSetByName(dsName);
        if (dataSet == null)
            return null;

        return new DataSetResolution(device, ln, dataSet);
    }

    /**
     * Resolves a data set reference string and returns only the LN (the DataSet may not exist; used for creation scenarios).
     * <p>
     * Looks up within the scope of the given AP.
     *
     * @param ied
     *            IED model
     * @param ap
     *            the currently associated access point
     * @param ref
     *            reference string, format {@code ldInst/lnName.dsName}
     * @return LN, or {@code null} if any step fails
     */
    public static SclLN resolveLn(SclIED ied, SclAccessPoint ap, String ref) {
        if (ref == null || !SclRefParser.isValid(ref))
            return null;

        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();

        SclLDevice device = resolveLd(ap, ldName);
        if (device == null)
            return null;

        return resolveLn(device, lnName);
    }

    /**
     * Extracts the dsName from a reference string.
     *
     * @param ref
     *            reference string, format {@code ldInst/lnName.dsName}
     * @return dsName, or {@code null} if parsing fails
     */
    public static String extractDsName(String ref) {
        if (ref == null || !SclRefParser.isValid(ref))
            return null;
        return SclRefParser.parse(ref).doName();
    }

    /**
     * Parses a reference string into an FCDA.
     * <p>
     * Looks up within the scope of the given AP.
     *
     * @param ied
     *            IED model
     * @param ap
     *            the currently associated access point
     * @param ref
     *            member reference, format {@code ldInst/lnClass.lnInst/doName.daName}
     * @return FCDA, or {@code null} if parsing fails
     */
    public static SclFCDA parseRefToFcda(SclIED ied, SclAccessPoint ap, String ref) {
        if (ref == null || ref.isEmpty() || !SclRefParser.isValid(ref))
            return null;

        SclRef sclRef = SclRefParser.parse(ref);
        SclLDevice device = resolveLd(ap, sclRef.ldInst());
        if (device == null)
            return null;

        SclLN ln = resolveLn(device, sclRef.lnName());
        if (ln == null)
            return null;

        SclFCDA fcda = new SclFCDA();
        fcda.ldInst(sclRef.ldInst());
        fcda.lnClass(ln.lnClass());
        fcda.lnInst(ln.inst());
        fcda.prefix(ln.prefix() != null ? ln.prefix() : "");
        fcda.doName(sclRef.doName());
        fcda.daName(sclRef.daName());

        return fcda;
    }

    /**
     * Parses a reference string into an FCDA.
     *
     * @param ied
     *            IED model
     * @param ref
     *            member reference, format {@code ldInst/lnClass.lnInst/doName.daName}
     * @return FCDA, or {@code null} if parsing fails
     */
    public static SclFCDA parseRefToFcda(SclIED ied, String ref) {
        if (ref == null || ref.isEmpty() || !SclRefParser.isValid(ref))
            return null;

        SclRef sclRef = SclRefParser.parse(ref);
        SclLDevice device = resolveLd(ied, sclRef.ldInst());
        if (device == null)
            return null;

        SclLN ln = resolveLn(device, sclRef.lnName());
        if (ln == null)
            return null;

        SclFCDA fcda = new SclFCDA();
        fcda.ldInst(sclRef.ldInst());
        fcda.lnClass(ln.lnClass());
        fcda.lnInst(ln.inst());
        fcda.prefix(ln.prefix() != null ? ln.prefix() : "");
        fcda.doName(sclRef.doName());
        fcda.daName(sclRef.daName());

        return fcda;
    }

    private static SclLDevice resolveLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }

    private static SclLDevice resolveLd(SclAccessPoint ap, String ldName) {
        return Navigator.findLd(ap, ldName);
    }

    private static SclLN resolveLn(SclLDevice device, String lnName) {
        return device.findLnByFullName(lnName);
    }
}
