package com.ysh.dlt2811bean.datatypes.type;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerChoice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCmsChoice<T extends AbstractCmsChoice<T>>
        extends AbstractCmsType<T> {

    private final List<String> alternativeNames = new ArrayList<>();
    protected int selectedIndex;

    protected AbstractCmsChoice(String typeName, int defaultIndex) {
        super(typeName);
        this.selectedIndex = defaultIndex;
    }

    protected void registerAlternative(String name) {
        alternativeNames.add(name);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void select(int index) {
        if (index < 0 || index >= alternativeNames.size()) {
            throw new IllegalArgumentException(
                "CHOICE index " + index + " out of range [0, " + (alternativeNames.size() - 1) + "]");
        }
        this.selectedIndex = index;
    }

    private AbstractCmsType<?> getAlternative(String name) {
        try {
            Field f = getClass().getField(name);
            return (AbstractCmsType<?>) f.get(this);
        } catch (Exception e) {
            throw new RuntimeException("Alternative not accessible: " + name, e);
        }
    }

    @Override
    public void encode(PerOutputStream pos) {
        PerChoice.encode(pos, selectedIndex);
        getAlternative(alternativeNames.get(selectedIndex)).encode(pos);
    }

    @Override
    public T decode(PerInputStream pis) throws Exception {
        selectedIndex = PerChoice.decode(pis);
        getAlternative(alternativeNames.get(selectedIndex)).decode(pis);
        return self();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T copy() {
        try {
            T clone = (T) getClass().getDeclaredConstructor().newInstance();
            clone.selectedIndex = this.selectedIndex;
            for (String name : alternativeNames) {
                AbstractCmsType<?> field = getAlternative(name);
                AbstractCmsType<?> clonedField = (AbstractCmsType<?>) field.copy();
                Field f = getClass().getField(name);
                f.set(clone, clonedField);
            }
            return clone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy " + typeName, e);
        }
    }

    @Override
    public String toString() {
        String name = alternativeNames.get(selectedIndex);
        return "(" + getClass().getSimpleName() + ") " + name + ": " + getAlternative(name);
    }
}