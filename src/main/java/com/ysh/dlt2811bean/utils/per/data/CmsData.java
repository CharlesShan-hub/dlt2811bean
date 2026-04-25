package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerChoice;

public class CmsData<T extends CmsType<T>> extends AbstractCmsScalar<CmsData<T>, T> {

    public static final int ERROR = 0;
    public static final int ARRAY = 1;
    public static final int STRUCTURE = 2;
    public static final int BOOLEAN = 3;
    public static final int INT8 = 4;
    public static final int INT16 = 5;
    public static final int INT32 = 6;
    public static final int INT64 = 7;
    public static final int INT8U = 8;
    public static final int INT16U = 9;
    public static final int INT32U = 10;
    public static final int INT64U = 11;
    public static final int FLOAT32 = 12;
    public static final int FLOAT64 = 13;
    public static final int BIT_STRING = 14;
    public static final int OCTET_STRING = 15;
    public static final int VISIBLE_STRING = 16;
    public static final int UNICODE_STRING = 17;
    public static final int UTC_TIME = 18;
    public static final int BINARY_TIME = 19;
    public static final int QUALITY = 20;
    public static final int DBPOS = 21;
    public static final int TCMD = 22;
    public static final int CHECK = 23;

    public CmsData() {
        super("Data", null);
    }

    public CmsData(T value) {
        super("Data", null);
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    void setValue(T value) {
        this.value = value;
    }

    @Override
    public void encode(PerOutputStream pos) {
        if (value == null) {
            throw new IllegalStateException("value must be set before encode");
        }
        PerChoice.encode(pos, choiceIndex(value.getClass()));
        value.encode(pos);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsData<T> decode(PerInputStream pis) throws Exception {
        int idx = PerChoice.decode(pis);
        if (value == null) {
            value = (T) createValue(idx);
        }
        value.decode(pis);
        return this;
    }

    private static int choiceIndex(Class<?> type) {
        if (type == CmsServiceError.class) return 0;
        if (type == CmsArray.class) return 1;
        if (type == CmsStructure.class) return 2;
        if (type == CmsBoolean.class) return 3;
        if (type == CmsInt8.class) return 4;
        if (type == CmsInt16.class) return 5;
        if (type == CmsInt32.class) return 6;
        if (type == CmsInt64.class) return 7;
        if (type == CmsInt8U.class) return 8;
        if (type == CmsInt16U.class) return 9;
        if (type == CmsInt32U.class) return 10;
        if (type == CmsInt64U.class) return 11;
        if (type == CmsFloat32.class) return 12;
        if (type == CmsFloat64.class) return 13;
        if (type == CmsBitString.class) return 14;
        if (type == CmsOctetString.class) return 15;
        if (type == CmsVisibleString.class) return 16;
        if (type == CmsUtf8String.class) return 17;
        if (type == CmsUtcTime.class) return 18;
        if (type == CmsBinaryTime.class) return 19;
        if (type == CmsQuality.class) return 20;
        if (type == CmsDbpos.class) return 21;
        if (type == CmsTcmd.class) return 22;
        if (type == CmsCheck.class) return 23;
        throw new IllegalArgumentException("Unknown Data type: " + type);
    }

    private static CmsType<?> createValue(int index) {
        switch (index) {
            case 0: return new CmsServiceError();
            case 1: return new CmsArray<>(CmsData.class);
            case 2: return new CmsStructure();
            case 3: return new CmsBoolean();
            case 4: return new CmsInt8();
            case 5: return new CmsInt16();
            case 6: return new CmsInt32();
            case 7: return new CmsInt64();
            case 8: return new CmsInt8U();
            case 9: return new CmsInt16U();
            case 10: return new CmsInt32U();
            case 11: return new CmsInt64U();
            case 12: return new CmsFloat32();
            case 13: return new CmsFloat64();
            case 14: return new CmsBitString();
            case 15: return new CmsOctetString();
            case 16: return new CmsVisibleString();
            case 17: return new CmsUtf8String();
            case 18: return new CmsUtcTime();
            case 19: return new CmsBinaryTime();
            case 20: return new CmsQuality();
            case 21: return new CmsDbpos();
            case 22: return new CmsTcmd();
            case 23: return new CmsCheck();
            default: throw new IllegalArgumentException("Unknown choice index: " + index);
        }
    }

    @Override
    public String toString() {
        if (value == null) {
            return "Data[unset]";
        }
        return "Data[" + choiceIndex(value.getClass()) + "]=" + value;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void read(PerInputStream pis, CmsType<?> value) throws Exception {
        int idx = choiceIndex(value.getClass());
        CmsData data = new CmsData();
        data.setValue((CmsType) value);
        data.decode(pis);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void write(PerOutputStream pos, CmsType<?> value) {
        int idx = choiceIndex(value.getClass());
        CmsData data = new CmsData();
        data.setValue((CmsType) value);
        data.encode(pos);
    }
}