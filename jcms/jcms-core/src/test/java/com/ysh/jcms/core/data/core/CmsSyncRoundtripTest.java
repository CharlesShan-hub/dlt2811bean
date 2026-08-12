package com.ysh.jcms.core.data.core;

import com.ysh.jcms.core.data.bitarray.CmsQuality;
import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.data.bitarray.CmsLcbOptFlds;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 共享 {@code _v} 别名一致性回归测试：decode → 修改 → 再 encode → decode 必须保留修改。
 *
 * <p>覆盖两类别名断裂风险：
 * <ul>
 *   <li>{@link CmsSequence} 中的 {@link CmsBits} 字段（encode 方向需显式写回父 _v）</li>
 *   <li>{@link CmsChoice} 的 WRAPPER 变体（decode 后 String 形态不共享 map，修改依赖写回）</li>
 * </ul>
 */
public class CmsSyncRoundtripTest {

    /** CmsSequence 必填 CmsBits 字段：decode 后修改 bit → 再 encode 不丢更新。 */
    @Test
    public void sequenceBits_decodeModifyEncode_keepsUpdate() {
        CmsBrcb a = new CmsBrcb().rptID("rpt01").rptEna(true).datSet("dataset1").confRev(3L)
                .optFlds(new CmsRcbOptFlds().sequence_number(true)).bufTm(5000L).sqNum(100)
                .trgOps(new CmsTriggerConditions().data_change(true)).intgPd(3000L)
                .gi(false).purgeBuf(true).entryID(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        CmsBrcb b = new CmsBrcb();
        b.decode(a.encode());
        // 修改：改回 false + 新增 data_reference
        b.optFlds.sequence_number(false);
        b.optFlds.data_reference(true);
        CmsBrcb c = new CmsBrcb();
        c.decode(b.encode());
        assertFalse(c.optFlds.sequence_number);
        assertTrue(c.optFlds.data_reference);
    }

    /** CmsChoice WRAPPER + CmsScalar 变体：decode 后改值 → 再 encode 不丢。 */
    @Test
    public void choiceWrapperScalar_decodeModifyEncode_keepsUpdate() {
        CmsData a = new CmsData().alt_float32(3.14f);
        CmsData b = new CmsData();
        b.decode(a.encode());
        b.alt_float32.value(9.9f);
        CmsData c = new CmsData();
        c.decode(b.encode());
        assertEquals(9.9f, c.alt_float32.value(), 1e-6f);
    }

    /** CmsChoice WRAPPER + CmsBits 变体：decode 后改 bit → 再 encode 不丢。 */
    @Test
    public void choiceWrapperBits_decodeModifyEncode_keepsUpdate() {
        CmsData a = new CmsData();
        a.choice(CmsData.CHOICE_QUALITY);
        a.alt_quality.validity(CmsQuality.INVALID);
        CmsData b = new CmsData();
        b.decode(a.encode());
        b.alt_quality.validity(0);
        CmsData c = new CmsData();
        c.decode(b.encode());
        assertEquals(0, c.alt_quality.validity());
    }

    /** CmsChoice WRAPPER + 嵌套 SEQUENCE 变体（JER 为 map 形态，走共享分支）：对照组，预期通过。 */
    @Test
    public void choiceWrapperSequence_decodeModifyEncode_keepsUpdate() {
        CmsData a = new CmsData();
        a.choice(CmsData.CHOICE_UTC_TIME);
        a.alt_utc_time.secondsSinceEpoch.value(1000L);
        CmsData b = new CmsData();
        b.decode(a.encode());
        b.alt_utc_time.secondsSinceEpoch.value(9999L);
        CmsData c = new CmsData();
        c.decode(b.encode());
        assertEquals(9999L, c.alt_utc_time.secondsSinceEpoch.value());
    }

    /** CmsSequence optional CmsBits 字段：构造 → encode → decode 不丢字段。 */
    @Test
    public void optionalBits_encodeDecode_roundtrip() {
        CmsLcb a = new CmsLcb().logEna(true).datSet("ds1")
                .trgOps(new CmsTriggerConditions().data_change(true))
                .intgPd(3000L).logRef("log1")
                .optFlds(new CmsLcbOptFlds().bit0(true));
        CmsLcb b = new CmsLcb();
        b.decode(a.encode());
        assertTrue(b.optFlds.bit0);
    }
}
