package com.ysh.dlt2811bean.scl.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an SCL History item ({@code <Hitem>}) element.
 *
 * <pre>
 * {@code
 * <Hitem version="1" revision="A" when="2024-01-01T00:00:00"
 *        who="admin" what="created" why="initial"/>
 * }
 * </pre>
 */
@Data
@NoArgsConstructor
public class SclHitem {

    private String version;
    private String revision;
    private String when;
    private String who;
    private String what;
    private String why;
}
