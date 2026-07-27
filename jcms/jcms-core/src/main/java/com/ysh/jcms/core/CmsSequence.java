package com.ysh.jcms.core;

import com.ysh.jcms.data.InnerBase;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Base class for SEQUENCE types backed directly by an Inner* PDU.
 *
 * <p>Instead of holding separate Cms* fields, subclasses read/write the
 * Inner* instance directly via helper methods. CmsType wrapper objects
 * are lazily created and cached, sharing the same Inner* reference.
 *
 * <p>{@link #syncToInner()} / {@link #syncFromInner()} automatically
 * sync all cached wrappers and presence flags — subclasses generally
 * do NOT need to override them.
 *
 * <pre>{@code
 * public class CmsGetServerDirectoryRequest extends CmsSequence {
 *     public CmsGetServerDirectoryRequest() {
 *         super(new InnerGetServerDirectoryRequestPDU());
 *     }
 *
 *     public int getObjectClass() { return getInt("objectClass"); }
 *     public CmsGetServerDirectoryRequest objectClass(int v) {
 *         setInt("objectClass", v); return this;
 *     }
 *
 *     public CmsObjectReference refAfter() {
 *         return getWrapper("referenceAfter", CmsObjectReference.class);
 *     }
 *     public CmsGetServerDirectoryRequest refAfter(String v) {
 *         setPresent("referenceAfter", v != null);
 *         if (v != null) refAfter().value(v);
 *         return this;
 *     }
 * }
 * }</pre>
 */
public abstract class CmsSequence extends CmsType {

    private Map<String, CmsType> wrapperCache;
    private Set<String> presentFields;

    protected CmsSequence() {
        initCaches();
    }

    protected CmsSequence(InnerBase inner) {
        super(inner);
        initCaches();
    }

    private void initCaches() {
        this.wrapperCache = new HashMap<>();
        this.presentFields = new HashSet<>();
    }

    // ── presence API ─────────────────────────────────────────────────

    /** Mark an optional field as present/absent, updating Inner* {@code _set}. */
    protected void setPresent(String innerFieldName, boolean v) {
        if (v) {
            presentFields.add(innerFieldName);
            Set<String> s = innerSetField();
            if (s != null) s.add(innerFieldName);
        } else {
            presentFields.remove(innerFieldName);
        }
    }

    protected boolean isPresent(String fieldName) {
        return presentFields.contains(fieldName);
    }

    /** Look up Inner* {@code _set} field dynamically (declared on each subclass). */
    @SuppressWarnings("unchecked")
    private Set<String> innerSetField() {
        try {
            Field f = inner.getClass().getField("_set");
            return (Set<String>) f.get(inner);
        } catch (NoSuchFieldException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ── field access — direct to inner, no cache ─────────────────

    protected int getInt(String innerField) {
        try { return inner.getClass().getField(innerField).getInt(inner); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected void setInt(String innerField, int v) {
        try { inner.getClass().getField(innerField).setInt(inner, v); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected String getString(String innerField) {
        try { return (String) inner.getClass().getField(innerField).get(inner); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected void setString(String innerField, String v) {
        try { inner.getClass().getField(innerField).set(inner, v); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected byte[] getBytes(String innerField) {
        try { return (byte[]) inner.getClass().getField(innerField).get(inner); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    protected void setBytes(String innerField, byte[] v) {
        try { inner.getClass().getField(innerField).set(inner, v); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    // ── CmsType wrapper access — lazy, shares inner reference ──────

    /** Get or create a cached CmsType wrapper sharing the inner field. */
    @SuppressWarnings("unchecked")
    protected <T extends CmsType> T getWrapper(String innerField, Class<T> wrapperType) {
        CmsType cached = wrapperCache.get(innerField);
        if (cached != null) return (T) cached;

        try {
            T wrapper = wrapperType.getDeclaredConstructor().newInstance();
            Field innerFieldRef = inner.getClass().getField(innerField);
            wrapper.inner = (InnerBase) innerFieldRef.get(inner);
            wrapper.syncFromInner();
            wrapperCache.put(innerField, wrapper);
            return wrapper;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create wrapper for " + innerField, e);
        }
    }

    // ── automatic sync ───────────────────────────────────────────

    @Override
    public void syncToInner() {
        // push cached wrappers → inner
        for (CmsType w : wrapperCache.values()) {
            w.syncToInner();
        }
        // sync presentFields → inner._set
        Set<String> s = innerSetField();
        if (s != null) s.addAll(presentFields);
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        // sync inner._set → presentFields
        Set<String> s = innerSetField();
        if (s != null) presentFields.addAll(s);
        // sync inner → cached wrappers
        for (CmsType w : wrapperCache.values()) {
            w.syncFromInner();
        }
    }

    // ── decode override — clears cache ──────────────────────────────

    @Override
    public void decode(byte[] data) {
        super.decode(data);
        wrapperCache.clear();
        presentFields.clear();
    }

    // ── equals / hashCode via inner ────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CmsSequence)) return false;
        return inner.equals(((CmsSequence) o).inner);
    }

    @Override
    public int hashCode() {
        return inner.hashCode();
    }

    @Override
    public String toString() {
        return "(" + getClass().getSimpleName() + ")\n" + inner.toString();
    }
}
