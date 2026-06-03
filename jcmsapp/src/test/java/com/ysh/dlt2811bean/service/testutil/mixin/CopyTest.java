package com.ysh.dlt2811bean.service.testutil.mixin;

import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public interface CopyTest<T extends CmsAsdu<T>> {

    @Test
    default void copyProducesIndependentInstance() {
        T original = createCopyableAsdu();
        T copy = original.copy();

        assertEquals(original.reqId().get(), copy.reqId().get());

        copy.reqId(original.reqId().get() + 1);
        assertNotEquals(original.reqId().get(), copy.reqId().get());
    }

    T createCopyableAsdu();
}
