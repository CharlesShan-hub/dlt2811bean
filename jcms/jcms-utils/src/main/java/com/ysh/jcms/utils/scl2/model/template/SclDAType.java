package com.ysh.jcms.utils.scl2.model.template;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

import com.ysh.jcms.utils.scl2.model.template.SclProtNs;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclDAType {

    private String id;
    private String desc;
    private String iedType;

    private List<SclBDA> bdas = new ArrayList<>();
    private final List<SclProtNs> protNs = new ArrayList<>();

    public SclDAType addBda(SclBDA bda) {
        bdas.add(bda);
        return this;
    }

    public SclDAType addProtNs(SclProtNs p) {
        protNs.add(p);
        return this;
    }

    public SclBDA findBdaByName(String name) {
        for (SclBDA b : bdas) {
            if (b.name().equals(name)) {
                return b;
            }
        }
        return null;
    }
}
