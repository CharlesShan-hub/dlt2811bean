package com.ysh.jcms.datatypes.type;

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
}
