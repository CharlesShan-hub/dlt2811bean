package com.ysh.dlt2811bean.service.testutil.mixin;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface FromFlagsTest<T extends CmsAsdu<T>> {

    @Test
    default void fromFlagsRequest() {
        assertEquals(MessageType.REQUEST, createFromFlags(false, false).messageType());
    }

    @Test
    default void fromFlagsPositive() {
        assertEquals(MessageType.RESPONSE_POSITIVE, createFromFlags(true, false).messageType());
    }

    @Test
    default void fromFlagsNegative() {
        assertEquals(MessageType.RESPONSE_NEGATIVE, createFromFlags(true, true).messageType());
    }

    T createFromFlags(boolean resp, boolean err);
}
