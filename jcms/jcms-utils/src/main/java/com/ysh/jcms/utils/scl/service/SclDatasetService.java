package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;

/**
 * 数据集（DataSet）相关的 SCL 操作服务。
 * <p>
 * 收编
 * GetDataSetDirectory、GetDataSetValues、SetDataSetValues、CreateDataSet、DeleteDataSet
 * handler 中的重复 SCL 解析逻辑。
 */
public class SclDatasetService {

    /** 数据集引用解析结果。 */
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
     * 解析数据集引用字符串 {@code "LD0/LLN0.dsName"}，返回 LD/LN/DataSet 三元组。
     *
     * @param ied
     *            IED 模型
     * @param ref
     *            引用字符串，格式为 {@code ldInst/lnName.dsName}
     * @return 解析结果，若任一环节失败则返回 {@code null}
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
     * 解析数据集引用字符串，仅返回 LN（DataSet 可能不存在，用于创建场景）。
     *
     * @param ied
     *            IED 模型
     * @param ref
     *            引用字符串，格式为 {@code ldInst/lnName.dsName}
     * @return LN，若任一环节失败则返回 {@code null}
     */
    public static SclLN resolveLn(SclIED ied, String ref) {
        if (ref == null || !SclRefParser.isValid(ref))
            return null;

        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();

        SclLDevice device = resolveLd(ied, ldName);
        if (device == null)
            return null;

        return resolveLn(device, lnName);
    }

    /**
     * 从引用字符串中提取 dsName。
     *
     * @param ref
     *            引用字符串，格式为 {@code ldInst/lnName.dsName}
     * @return dsName，若解析失败则返回 {@code null}
     */
    public static String extractDsName(String ref) {
        if (ref == null || !SclRefParser.isValid(ref))
            return null;
        return SclRefParser.parse(ref).doName();
    }

    /**
     * 将引用字符串解析为 FCDA。
     *
     * @param ied
     *            IED 模型
     * @param ref
     *            成员引用，格式为 {@code ldInst/lnClass.lnInst/doName.daName}
     * @return FCDA，若解析失败则返回 {@code null}
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

    private static SclLN resolveLn(SclLDevice device, String lnName) {
        return device.findLnByFullName(lnName);
    }
}
