package com.ysh.jcms.core;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic array container — elements is a Pointer array, each pointing to an
 * independently allocated CmsType.
 *
 * C-side view: typedef struct { void **elements; // points to Memory(ptr0,
 * ptr1, ...), 8 bytes each int32_t count; } cms_array_t; // sizeof = 16 (ptr8 +
 * int32 4 + padding 4)
 *
 * Java: CmsArray<CmsBoolean> arr = new CmsArray<>(CmsBoolean.class);
 * arr.add(new CmsBoolean(true)); arr.add(new CmsBoolean(false)); arr.write();
 * // writes count + elements pointer to nativePtr
 */
public class CmsArray<T extends CmsType> extends CmsType {

    public static final int SIZEOF = 16;

    /** List of elements managed on the Java side. */
    public List<T> items = new ArrayList<>();

    /** Native memory block (Pointer array) pointed to by elements. */
    public Pointer elements;

    /** Number of elements. */
    public int count;

    /**
     * Element type for auto-creation during decode. null means manual
     * pre-allocation.
     */
    private Class<T> itemClass;

    /** Number of elements to pre-allocate during decode (adjustable externally). */
    public int allocSize = 1;

    public Class<T> getItemClass() {
        return itemClass;
    }

    public CmsArray() {
    }

    /** Specify element type for auto-creation during decode. */
    public CmsArray(Class<T> itemClass) {
        this.itemClass = itemClass;
    }

    // ==================== CRUD ====================

    public CmsArray<T> add(T item) {
        items.add(item);
        if (allocSize < items.size())
            allocSize = items.size();
        return this;
    }

    @SafeVarargs
    public final CmsArray<T> addAll(T... items) {
        for (T item : items) {
            this.items.add(item);
        }
        if (allocSize < this.items.size())
            allocSize = this.items.size();
        return this;
    }

    public T get(int index) {
        return items.get(index);
    }
    public int size() {
        return items.size();
    }

    // ==================== Native sync ====================

    @Override
    protected int calcNativeSize() {
        return SIZEOF;
    }

    @Override
    public void write() {
        // During decode: pre-allocate elements so the C decoder has valid targets.
        // Only allocate when allocSize > 0 to prevent re-entrant infinite allocation
        // (e.g. CmsData → CmsArray<CmsData> → CmsData → ...).
        if (items.isEmpty() && itemClass != null && allocSize > 0) {
            for (int i = 0; i < allocSize; i++) {
                try {
                    items.add(itemClass.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    break;
                }
            }
        }
        int count = items.size();
        // Allocate enough slots for the C decoder: items.size() is the real count,
        // but allocSize serves as a safety margin so the C code can safely check
        // elements[i] for NULL (triggering CMS_RETRY) instead of reading garbage.
        int slotCount = Math.max(Math.max(count, allocSize), 1);
        Memory ptrs = new Memory(slotCount * 8L);
        for (int i = 0; i < count; i++) {
            CmsType item = items.get(i);
            item.write();
            ptrs.setPointer(i * 8L, item.nativePtr);
        }
        // Zero-fill unused slots so C sees NULL for out-of-range indices
        for (int i = count; i < slotCount; i++) {
            ptrs.setPointer(i * 8L, null);
        }
        this.elements = ptrs;
        // Write struct fields
        nativePtr.setPointer(0, elements);
        nativePtr.setInt(8, count);
    }

    @Override
    public void read() {
        this.elements = nativePtr.getPointer(0);
        this.count = nativePtr.getInt(8);
        // Sync elements: grow items if needed, then assign nativePtr to each
        if (elements != null && count > 0) {
            // Grow items list to match decoded count
            while (items.size() < count && itemClass != null) {
                try {
                    items.add(itemClass.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    break;
                }
            }
            // Trim excess
            while (items.size() > count) {
                items.remove(items.size() - 1);
            }
            for (int i = 0; i < count && i < items.size(); i++) {
                Pointer elemPtr = elements.getPointer(i * 8L);
                if (elemPtr != null) {
                    items.get(i).nativePtr = elemPtr;
                    items.get(i).read();
                }
            }
        }
    }

    @Override
    protected void resize() {
        int c = 0;
        if (nativePtr != null) {
            c = nativePtr.getInt(8); // cms_array_t.count at offset 8
            if (c > 0)
                allocSize = c;
        }
        while (items.size() < allocSize && itemClass != null) {
            try {
                items.add(itemClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                break;
            }
        }
        while (items.size() > allocSize) {
            items.remove(items.size() - 1);
        }
        // 递归 resize items。即使 nativePtr 当前为 null（刚创建），
        // 下一轮 write() 会分配内存，C decoder 就可以用。
        for (T item : items) {
            if (item != null)
                item.resize();
        }
    }
    // ==================== equals / hashCode ====================

    @Override
    public List<? extends CmsType> children() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CmsArray))
            return false;
        CmsArray<?> other = (CmsArray<?>) o;
        if (items.size() != other.items.size())
            return false;
        for (int i = 0; i < items.size(); i++) {
            T thisItem = items.get(i);
            Object otherItem = other.items.get(i);
            if (thisItem == null && otherItem == null)
                continue;
            if (thisItem == null || otherItem == null)
                return false;
            if (!thisItem.equals(otherItem))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 1;
        for (T item : items)
            h = 31 * h + (item != null ? item.hashCode() : 0);
        return h;
    }
}
