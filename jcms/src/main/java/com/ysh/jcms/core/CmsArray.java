package com.ysh.jcms.core;

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

    /** 预分配的元素类型（用于 decode 时自动创建）。传 null 则需手动预分配。 */
    private Class<T> itemClass;

    /** decode 时自动预分配的元素数量（默认 128，外部可按需修改）。 */
    public int allocSize = 128;

    public CmsArray() {}

    /** 指定元素类型，decode 时可 auto-create 元素。 */
    public CmsArray(Class<T> itemClass) {
        this.itemClass = itemClass;
    }

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
        // decode 场景：items 为空但有 itemClass，自动预分配元素
        // 这样 C 侧 decode 时 elements[i] 就有有效的写入目标
        if (items.isEmpty() && itemClass != null) {
            for (int i = 0; i < allocSize; i++) {
                try {
                    items.add(itemClass.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    break;
                }
            }
        }
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
        // 同步元素：用 C 解码后的指针覆盖 Java 预分配的指针
        if (elements != null && count > 0) {
            // 修整 items 到实际 decode 的个数
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
}
