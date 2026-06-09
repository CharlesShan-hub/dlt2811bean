package com.ysh.jcms2.example;

import com.ysh.jcms2.*;

/**
 * 数组演示 — 全指针架构下数组也是存指针数组。
 */
public class ArrayDemo {

    public static void main(String[] args) {

        // ========== 1. boolean 数组 ==========

        System.out.println("=== boolean 数组 ===");

        CmsArray<CmsBoolean> bools = new CmsArray<>();
        bools.add(new CmsBoolean(true));
        bools.add(new CmsBoolean(false));
        bools.add(new CmsBoolean(true));

        bools.write();  // 写 elements(count 个指针) + count

        System.out.println("  count = " + bools.count);
        System.out.println("  elements = " + bools.elements);
        CmsBoolean b0 = new CmsBoolean(); b0.nativePtr = bools.elements.getPointer(0); b0.read();
        CmsBoolean b1 = new CmsBoolean(); b1.nativePtr = bools.elements.getPointer(8); b1.read();
        CmsBoolean b2 = new CmsBoolean(); b2.nativePtr = bools.elements.getPointer(16); b2.read();
        System.out.println("  elements[0] = " + bools.elements.getPointer(0) + " → " + b0.value());
        System.out.println("  elements[1] = " + bools.elements.getPointer(8) + " → " + b1.value());
        System.out.println("  elements[2] = " + bools.elements.getPointer(16) + " → " + b2.value());

        System.out.println("  bools.nativeSize = " + bools.nativeSize);  // 16

        // ========== 2. CmsUint8Array 数组 ==========

        System.out.println("\n=== CmsUint8Array 数组 ===");

        CmsArray<CmsUint8Array> strs = new CmsArray<>();
        strs.add(new CmsUint8Array("hello".getBytes()));
        strs.add(new CmsUint8Array("world".getBytes()));

        strs.write();

        System.out.println("  count = " + strs.count);
        System.out.println("  elements = " + strs.elements);
        CmsUint8Array s0 = new CmsUint8Array(); s0.nativePtr = strs.elements.getPointer(0); s0.read();
        CmsUint8Array s1 = new CmsUint8Array(); s1.nativePtr = strs.elements.getPointer(8); s1.read();
        System.out.println("  elements[0] → " + new String(s0.value()));
        System.out.println("  elements[1] → " + new String(s1.value()));

        // ========== 3. 数组的数组 ==========

        System.out.println("\n=== 数组的数组 ===");

        CmsArray<CmsArray<CmsBoolean>> arrOfArrs = new CmsArray<>();
        CmsArray<CmsBoolean> inner1 = new CmsArray<>();
        inner1.add(new CmsBoolean(true));
        inner1.write();

        CmsArray<CmsBoolean> inner2 = new CmsArray<>();
        inner2.add(new CmsBoolean(false));
        inner2.add(new CmsBoolean(true));
        inner2.write();

        arrOfArrs.add(inner1);
        arrOfArrs.add(inner2);
        arrOfArrs.write();

        CmsArray<CmsBoolean> innerRead0 = new CmsArray<>();
        innerRead0.nativePtr = arrOfArrs.elements.getPointer(0); innerRead0.read();
        CmsArray<CmsBoolean> innerRead1 = new CmsArray<>();
        innerRead1.nativePtr = arrOfArrs.elements.getPointer(8); innerRead1.read();
        System.out.println("  outer count = " + arrOfArrs.count);
        System.out.println("  inner[0].count = " + innerRead0.count);
        System.out.println("  inner[1].count = " + innerRead1.count);
    }
}
