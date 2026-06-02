package com.ysh.jcms.datatypes.type;

import com.sun.jna.ptr.IntByReference;

public abstract class AbstractCmsType implements CmsType {

    protected final String typeName;
    protected boolean optional = false;
    protected boolean present = true;

    protected AbstractCmsType(String typeName) {
        this.typeName = typeName;
    }

    @SuppressWarnings("unchecked")
    protected <T extends AbstractCmsType> T self() {
        return (T) this;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    // ==================== FFI 编解码辅助 ====================

    @FunctionalInterface
    public interface FfiEncoder {
        int encode(byte[] buf, IntByReference outLen);
    }

    protected byte[] ffiEncode(int bufSize, FfiEncoder encoder) {
        byte[] buf = new byte[bufSize];
        IntByReference outLen = new IntByReference(buf.length);
        encoder.encode(buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    protected byte[] ffiEncode(FfiEncoder encoder) {
        return ffiEncode(16, encoder);
    }
}
