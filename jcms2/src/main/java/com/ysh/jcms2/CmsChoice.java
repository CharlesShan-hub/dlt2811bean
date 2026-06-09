package com.ysh.jcms2;

import com.sun.jna.Pointer;
import java.util.List;

/**
 * CHOICE 基类 — choice (int32 inline) + 每个备选一个指针。
 *
 * native 布局:
 *   [choice: 4 字节] [ptr_alt0: 8 字节] [ptr_alt1: 8 字节] ...
 *    ↑ inline         ↑ 指向 alt0 的 nativePtr    ↑ 指向 alt1 的 nativePtr
 *
 * calcNativeSize() 固定 = 4 + alternatives().size() × 8
 * write()/read() 也固定，子类只需提供 alternatives()。
 */
public abstract class CmsChoice extends CmsType {

    protected int choice;

    protected CmsChoice() {
        // 基类构造器会调 calcNativeSize()，那时 alternatives() 可用
    }

    /** 备选列表（按 ASN.1 定义的顺序），每个备选是 CmsType。 */
    public abstract List<? extends CmsType> alternatives();

    @Override
    protected int calcNativeSize() {
        return 4 + alternatives().size() * 8;
    }

    @Override
    public void write() {
        int ofs = 0;
        nativePtr.setInt(ofs, choice); ofs += 4;
        List<? extends CmsType> alts = alternatives();
        for (int i = 0; i < alts.size(); i++) {
            CmsType alt = alts.get(i);
            alt.write();
            nativePtr.setPointer(ofs, alt.nativePtr);
            ofs += 8;
        }
    }

    @Override
    public void read() {
        int ofs = 0;
        choice = nativePtr.getInt(ofs); ofs += 4;
        List<? extends CmsType> alts = alternatives();
        for (int i = 0; i < alts.size(); i++) {
            Pointer ptr = nativePtr.getPointer(ofs);
            if (ptr != null) {
                alts.get(i).nativePtr = ptr;
                alts.get(i).read();
            }
            ofs += 8;
        }
    }

    // ==================== 选择器 ====================

    public int choice() { return choice; }
    public CmsChoice choice(int c) { this.choice = c; write(); return this; }
}
