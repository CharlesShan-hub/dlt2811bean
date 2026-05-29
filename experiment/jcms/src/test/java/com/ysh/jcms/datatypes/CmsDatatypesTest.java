package com.ysh.jcms.datatypes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CmsDatatypesTest {

    @Test
    void booleanRoundtrip() {
        CmsBoolean original = new CmsBoolean(true);
        byte[] encoded = original.encode();
        assertNotNull(encoded);
        assertTrue(encoded.length > 0);
        CmsBoolean decoded = CmsBoolean.decode(encoded);
        assertTrue(decoded.isValue());

        CmsBoolean originalFalse = new CmsBoolean(false);
        byte[] encodedFalse = originalFalse.encode();
        CmsBoolean decodedFalse = CmsBoolean.decode(encodedFalse);
        assertFalse(decodedFalse.isValue());
    }

    @Test
    void int8Roundtrip() {
        CmsInteger original = new CmsInteger((byte) -42);
        byte[] encoded = original.encodeInt8();
        CmsInteger decoded = CmsInteger.decodeInt8(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void int8URoundtrip() {
        CmsInteger original = new CmsInteger((short) 200);
        byte[] encoded = original.encodeInt8U();
        CmsInteger decoded = CmsInteger.decodeInt8U(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void int16Roundtrip() {
        CmsInteger original = new CmsInteger((short) -12345);
        byte[] encoded = original.encodeInt16();
        CmsInteger decoded = CmsInteger.decodeInt16(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void int16URoundtrip() {
        CmsInteger original = new CmsInteger(60000);
        byte[] encoded = original.encodeInt16U();
        CmsInteger decoded = CmsInteger.decodeInt16U(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void int32Roundtrip() {
        CmsInteger original = new CmsInteger(-2000000);
        byte[] encoded = original.encodeInt32();
        CmsInteger decoded = CmsInteger.decodeInt32(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void int32URoundtrip() {
        CmsInteger original = new CmsInteger(3000000000L);
        byte[] encoded = original.encodeInt32U();
        CmsInteger decoded = CmsInteger.decodeInt32U(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void int64Roundtrip() {
        CmsInteger original = new CmsInteger(-9000000000000L);
        byte[] encoded = original.encodeInt64();
        CmsInteger decoded = CmsInteger.decodeInt64(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void int64URoundtrip() {
        CmsInteger original = new CmsInteger(18000000000000L);
        byte[] encoded = original.encodeInt64U();
        CmsInteger decoded = CmsInteger.decodeInt64U(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void float32Roundtrip() {
        CmsFloat original = new CmsFloat(3.14159f);
        byte[] encoded = original.encodeFloat32();
        CmsFloat decoded = CmsFloat.decodeFloat32(encoded);
        assertEquals(original.getValue(), decoded.getValue(), 1e-6);
    }

    @Test
    void float64Roundtrip() {
        CmsFloat original = new CmsFloat(2.718281828459045);
        byte[] encoded = original.encodeFloat64();
        CmsFloat decoded = CmsFloat.decodeFloat64(encoded);
        assertEquals(original.getValue(), decoded.getValue(), 1e-15);
    }

    @Test
    void visibleStringRoundtrip() {
        CmsString original = new CmsString("HelloCMS");
        byte[] encoded = original.encodeVisibleString();
        CmsString decoded = CmsString.decodeVisibleString(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void utf8StringRoundtrip() {
        CmsString original = new CmsString("UTF-8测试");
        byte[] encoded = original.encodeUTF8String();
        CmsString decoded = CmsString.decodeUTF8String(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void octetStringRoundtrip() {
        byte[] data = {0x01, 0x02, 0x03, 0x04, (byte) 0xFF};
        CmsOctetString original = new CmsOctetString(data);
        byte[] encoded = original.encode();
        CmsOctetString decoded = CmsOctetString.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void objectNameRoundtrip() {
        CmsString original = new CmsString("MyObject");
        byte[] encoded = original.encodeObjectName();
        CmsString decoded = CmsString.decodeObjectName(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void objectReferenceRoundtrip() {
        CmsString original = new CmsString("MyReference");
        byte[] encoded = original.encodeObjectReference();
        CmsString decoded = CmsString.decodeObjectReference(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void fcRoundtrip() {
        byte[] fcData = {(byte) 0xAB, (byte) 0xCD};
        CmsFC original = new CmsFC(fcData);
        byte[] encoded = original.encode();
        CmsFC decoded = CmsFC.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void subReferenceRoundtrip() {
        CmsString original = new CmsString("SubRef1");
        byte[] encoded = original.encodeSubReference();
        CmsString decoded = CmsString.decodeSubReference(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void entryIDRoundtrip() {
        byte[] id = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        CmsEntryID original = new CmsEntryID(id);
        byte[] encoded = original.encode();
        CmsEntryID decoded = CmsEntryID.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void entryIDInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> new CmsEntryID(new byte[]{0x01, 0x02}));
    }

    @Test
    void bitStringRoundtrip() {
        byte[] bits = {(byte) 0xAB, (byte) 0xCD};
        CmsBitString original = new CmsBitString(bits);
        byte[] encoded = original.encode();
        CmsBitString decoded = CmsBitString.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void binaryTimeRoundtrip() {
        CmsBinaryTime original = new CmsBinaryTime(10, 30, 45, 500);
        byte[] encoded = original.encode();
        CmsBinaryTime decoded = CmsBinaryTime.decode(encoded);
        assertEquals(original.getHour(), decoded.getHour());
        assertEquals(original.getMinute(), decoded.getMinute());
        assertEquals(original.getSecond(), decoded.getSecond());
        assertEquals(original.getMillisecond(), decoded.getMillisecond());
    }

    @Test
    void utcTimeRoundtrip() {
        CmsUtcTime original = new CmsUtcTime(1700000000000L);
        byte[] encoded = original.encode();
        CmsUtcTime decoded = CmsUtcTime.decode(encoded);
        assertEquals(original.getSecondsSinceEpoch(), decoded.getSecondsSinceEpoch());
    }

    @Test
    void timeStampRoundtrip() {
        CmsTimeStamp original = new CmsTimeStamp(1700000000L, 456L);
        byte[] encoded = original.encode();
        CmsTimeStamp decoded = CmsTimeStamp.decode(encoded);
        assertEquals(original.getSecondsSinceEpoch(), decoded.getSecondsSinceEpoch());
        assertEquals(original.getFractional(), decoded.getFractional());
    }

    @Test
    void originatorRoundtrip() {
        byte[] ident = {0x01, 0x02, 0x03};
        CmsOriginator original = new CmsOriginator(1, ident);
        byte[] encoded = original.encode();
        CmsOriginator decoded = CmsOriginator.decode(encoded);
        assertEquals(original.getOrCat(), decoded.getOrCat());
        assertArrayEquals(original.getOrIdent(), decoded.getOrIdent());
    }

    @Test
    void phyComAddrRoundtrip() {
        byte[] addr = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        CmsPhyComAddr original = new CmsPhyComAddr(addr);
        byte[] encoded = original.encode();
        CmsPhyComAddr decoded = CmsPhyComAddr.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void phyComAddrInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> new CmsPhyComAddr(new byte[]{0x01}));
    }

    @Test
    void qualityRoundtrip() {
        byte[] q = {(byte) 0xFF, 0x1F};
        CmsQuality original = new CmsQuality(q);
        byte[] encoded = original.encode();
        CmsQuality decoded = CmsQuality.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void dbposRoundtrip() {
        CmsDbpos original = new CmsDbpos(2);
        byte[] encoded = original.encode();
        CmsDbpos decoded = CmsDbpos.decode(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void tcmdRoundtrip() {
        CmsTcmd original = new CmsTcmd(1);
        byte[] encoded = original.encode();
        CmsTcmd decoded = CmsTcmd.decode(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void checkRoundtrip() {
        byte[] c = {(byte) 0xAA, 0x55};
        CmsCheck original = new CmsCheck(c);
        byte[] encoded = original.encode();
        CmsCheck decoded = CmsCheck.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void lcbOptFldsRoundtrip() {
        byte[] v = {0x2A};
        CmsLcbOptFlds original = new CmsLcbOptFlds(v);
        byte[] encoded = original.encode();
        CmsLcbOptFlds decoded = CmsLcbOptFlds.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void msvcbOptFldsRoundtrip() {
        byte[] v = {(byte) 0xAB};
        CmsMsvcbOptFlds original = new CmsMsvcbOptFlds(v);
        byte[] encoded = original.encode();
        CmsMsvcbOptFlds decoded = CmsMsvcbOptFlds.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void rcbOptFldsRoundtrip() {
        byte[] v = {(byte) 0xAB, 0x0F};
        CmsRcbOptFlds original = new CmsRcbOptFlds(v);
        byte[] encoded = original.encode();
        CmsRcbOptFlds decoded = CmsRcbOptFlds.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void reasonCodeRoundtrip() {
        byte[] v = {0x15};
        CmsReasonCode original = new CmsReasonCode(v);
        byte[] encoded = original.encode();
        CmsReasonCode decoded = CmsReasonCode.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void timeQualityRoundtrip() {
        byte[] v = {(byte) 0xFF};
        CmsTimeQuality original = new CmsTimeQuality(v);
        byte[] encoded = original.encode();
        CmsTimeQuality decoded = CmsTimeQuality.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void triggerConditionsRoundtrip() {
        byte[] v = {0x07};
        CmsTriggerConditions original = new CmsTriggerConditions(v);
        byte[] encoded = original.encode();
        CmsTriggerConditions decoded = CmsTriggerConditions.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void packedListRoundtrip() {
        byte[] v = {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC};
        CmsPackedList original = new CmsPackedList(v);
        byte[] encoded = original.encode();
        CmsPackedList decoded = CmsPackedList.decode(encoded);
        assertArrayEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void serviceErrorRoundtrip() {
        CmsServiceError original = new CmsServiceError(3);
        byte[] encoded = original.encode();
        CmsServiceError decoded = CmsServiceError.decode(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void addCauseRoundtrip() {
        CmsAddCause original = new CmsAddCause(1);
        byte[] encoded = original.encode();
        CmsAddCause decoded = CmsAddCause.decode(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void orCatRoundtrip() {
        CmsOrCat original = new CmsOrCat(2);
        byte[] encoded = original.encode();
        CmsOrCat decoded = CmsOrCat.decode(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void smpModRoundtrip() {
        CmsSmpMod original = new CmsSmpMod(0);
        byte[] encoded = original.encode();
        CmsSmpMod decoded = CmsSmpMod.decode(encoded);
        assertEquals(original.getValue(), decoded.getValue());
    }

    @Test
    void dataRoundtrip() {
        CmsData original = new CmsData(3, 1, 0.0, null, null);
        byte[] encoded = original.encode();
        CmsData decoded = CmsData.decode(encoded);
        assertEquals(original.getChoice(), decoded.getChoice());
        assertEquals(original.getIntVal(), decoded.getIntVal());
    }

    @Test
    void dataWithStringRoundtrip() {
        CmsData original = new CmsData(16, 0, 0.0, "testString", null);
        byte[] encoded = original.encode();
        CmsData decoded = CmsData.decode(encoded);
        assertEquals(original.getChoice(), decoded.getChoice());
        assertEquals(original.getStrVal(), decoded.getStrVal());
    }

    @Test
    void dataWithBytesRoundtrip() {
        byte[] bytes = {0x01, 0x02, 0x03};
        CmsData original = new CmsData(15, 0, 0.0, null, bytes);
        byte[] encoded = original.encode();
        CmsData decoded = CmsData.decode(encoded);
        assertEquals(original.getChoice(), decoded.getChoice());
        assertArrayEquals(original.getBytesVal(), decoded.getBytesVal());
    }

    @Test
    void dataDefinitionRoundtrip() {
        byte[] fc = {0x00, 0x01};
        CmsDataDefinition original = new CmsDataDefinition(
            "tempSensor", "int32", fc,
            6, 25, 0.0, null, null
        );
        byte[] encoded = original.encode();
        CmsDataDefinition decoded = CmsDataDefinition.decode(encoded);
        assertEquals(original.getDataName(), decoded.getDataName());
        assertEquals(original.getDataType(), decoded.getDataType());
        assertArrayEquals(original.getFc(), decoded.getFc());
        assertEquals(original.getDataChoice(), decoded.getDataChoice());
        assertEquals(original.getDataInt(), decoded.getDataInt());
    }
}
