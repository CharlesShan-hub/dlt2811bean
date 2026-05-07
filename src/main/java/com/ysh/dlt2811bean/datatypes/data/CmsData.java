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
import com.ysh.dlt2811bean.per.types.PerChoice;

public class CmsData<T extends CmsType<T>> extends AbstractCmsDataUnit<CmsData<T>, T> {

    public CmsData() {
        super("Data", null);
    }

    public CmsData(T value) {
        super("Data", null);
        this.value = value;
    }

    void setValue(T value) {
        this.value = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsData<T> copy() {
        CmsData<T> clone = new CmsData<>();
        if (value != null) {
            clone.setValue((T) ((CmsType<?>) value).copy());
        }
        return clone;
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
            case 1: return new CmsArray<>(CmsData::new).capacity(100);
            case 2: return new CmsStructure().capacity(100);
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
            case 15: return new CmsOctetString().max(255);
            case 16: return new CmsVisibleString().max(255);
            case 17: return new CmsUtf8String().max(255);
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