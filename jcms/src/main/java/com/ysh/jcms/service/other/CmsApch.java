package com.ysh.jcms.service.other;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsApch extends Structure {
    public byte cc;
    public byte sc;
    public short fl;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("cc", "sc", "fl");
    }
}
