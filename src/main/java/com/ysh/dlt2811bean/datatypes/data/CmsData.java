package com.ysh.dlt2811bean.datatypes.data;

import com.ysh.dlt2811bean.datatypes.code.CmsCheck;
import com.ysh.dlt2811bean.datatypes.code.CmsQuality;
import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.compound.CmsBinaryTime;
import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsDbpos;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsTcmd;
import com.ysh.dlt2811bean.datatypes.numeric.*;
import com.ysh.dlt2811bean.datatypes.string.CmsBitString;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.string.CmsUtf8String;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsDataUnit;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

public class CmsData<T extends CmsType<T>> extends AbstractCmsDataUnit<CmsData<T>, T> {

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
        setValue(value);
    }

    @Override
    protected int choiceIndex(Class<?> type) {
        if (type == CmsServiceError.class) return ERROR;
        if (type == CmsArray.class) return ARRAY;
        if (type == CmsStructure.class) return STRUCTURE;
        if (type == CmsBoolean.class) return BOOLEAN;
        if (type == CmsInt8.class) return INT8;
        if (type == CmsInt16.class) return INT16;
        if (type == CmsInt32.class) return INT32;
        if (type == CmsInt64.class) return INT64;
        if (type == CmsInt8U.class) return INT8U;
        if (type == CmsInt16U.class) return INT16U;
        if (type == CmsInt32U.class) return INT32U;
        if (type == CmsInt64U.class) return INT64U;
        if (type == CmsFloat32.class) return FLOAT32;
        if (type == CmsFloat64.class) return FLOAT64;
        if (type == CmsBitString.class) return BIT_STRING;
        if (type == CmsOctetString.class) return OCTET_STRING;
        if (type == CmsVisibleString.class) return VISIBLE_STRING;
        if (type == CmsUtf8String.class) return UNICODE_STRING;
        if (type == CmsUtcTime.class) return UTC_TIME;
        if (type == CmsBinaryTime.class) return BINARY_TIME;
        if (type == CmsQuality.class) return QUALITY;
        if (type == CmsDbpos.class) return DBPOS;
        if (type == CmsTcmd.class) return TCMD;
        if (type == CmsCheck.class) return CHECK;
        throw new IllegalArgumentException("Unknown Data type: " + type);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T createValue(int index) {
        switch (index) {
            case 0: return (T) new CmsServiceError();
            case 1: return (T) new CmsArray<>(CmsData.class);
            case 2: return (T) new CmsStructure();
            case 3: return (T) new CmsBoolean();
            case 4: return (T) new CmsInt8();
            case 5: return (T) new CmsInt16();
            case 6: return (T) new CmsInt32();
            case 7: return (T) new CmsInt64();
            case 8: return (T) new CmsInt8U();
            case 9: return (T) new CmsInt16U();
            case 10: return (T) new CmsInt32U();
            case 11: return (T) new CmsInt64U();
            case 12: return (T) new CmsFloat32();
            case 13: return (T) new CmsFloat64();
            case 14: return (T) new CmsBitString();
            case 15: return (T) new CmsOctetString();
            case 16: return (T) new CmsVisibleString();
            case 17: return (T) new CmsUtf8String();
            case 18: return (T) new CmsUtcTime();
            case 19: return (T) new CmsBinaryTime();
            case 20: return (T) new CmsQuality();
            case 21: return (T) new CmsDbpos();
            case 22: return (T) new CmsTcmd();
            case 23: return (T) new CmsCheck();
            default: throw new IllegalArgumentException("Unknown choice index: " + index);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void read(PerInputStream pis, CmsType<?> value) throws Exception {
        CmsData data = new CmsData();
        data.setValue(value);
        data.decode(pis);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void write(PerOutputStream pos, CmsType<?> value) {
        CmsData data = new CmsData();
        data.setValue(value);
        data.encode(pos);
    }
}
