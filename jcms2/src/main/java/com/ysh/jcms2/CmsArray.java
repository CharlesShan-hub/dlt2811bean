package com.ysh.jcms2;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用数组容器 — elements 是 Pointer 数组，每个指向一个独立分配的 CmsType。
 *
 * C 侧视图:
 *   typedef struct {
 *       void **elements;   // 指向 Memory(ptr0, ptr1, ...) 每个 8 字节
 *       int32_t count;
 *   } cms_array_t;         // sizeof = 16 (ptr8 + int32 4 + padding 4)
 *
 * Java:
 *   CmsArray<CmsBoolean> arr = new CmsArray<>(CmsBoolean.class);
 *   arr.add(new CmsBoolean(true));
 *   arr.add(new CmsBoolean(false));
 *   arr.write();  // 写 count + elements 指针到 nativePtr
 */
public class CmsArray<T extends CmsType> extends CmsType {

    public static final int SIZEOF = 16;

    /** Java 侧维护的元素列表。 */
    public List<T> items = new ArrayList<>();

    /** elements 指向的 native 内存块（Pointer 数组）。 */
    public Pointer elements;

    /** 元素个数。 */
    public int count;

    public CmsArray() {}

    // ==================== 增删 ====================

    public CmsArray<T> add(T item) {
        items.add(item);
        return this;
    }

    public CmsArray<T> addAll(T... items) {
        for (T item : items) this.items.add(item);
        return this;
    }

    public T get(int index) { return items.get(index); }
    public int size() { return items.size(); }

    // ==================== 同步 native ====================

    @Override
    protected int calcNativeSize() { return SIZEOF; }

    @Override
    public void write() {
        this.count = items.size();
        if (count == 0) {
            this.elements = null;
        } else {
            // 分配 Pointer 数组（count × 8 字节）
            Memory ptrs = new Memory(count * 8L);
            for (int i = 0; i < count; i++) {
                CmsType item = items.get(i);
                item.write();
                ptrs.setPointer(i * 8L, item.nativePtr);
            }
            this.elements = ptrs;
        }
        // 写 struct 字段
        nativePtr.setPointer(0, elements);
        nativePtr.setInt(8, count);
    }

    @Override
    public void read() {
        this.elements = nativePtr.getPointer(0);
        this.count = nativePtr.getInt(8);
        // items 由具体子类或使用者在 read 后填充
    }
}
