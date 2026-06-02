package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.ysh.jcms.datatypes.type.AbstractCmsType;

public abstract class AbstractCmsCompound<T extends AbstractCmsCompound<T>> extends AbstractCmsType<T> {

    private Structure nativeStruct;

    protected AbstractCmsCompound(String typeName) {
        super(typeName);
    }

    protected void setNativeStruct(Structure s) {
        this.nativeStruct = s;
    }

    protected Structure getNativeStruct() {
        return nativeStruct;
    }

    protected void write() {
        if (nativeStruct != null) nativeStruct.write();
    }

    protected void read() {
        if (nativeStruct != null) nativeStruct.read();
    }

    protected Pointer getPointer() {
        return nativeStruct != null ? nativeStruct.getPointer() : null;
    }
}
