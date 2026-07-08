package com.ysh.jcms.utils.scl2.ref;

public class SclRefResult {

    private final boolean valid;
    private final String message;
    private final SclRef ref;

    private SclRefResult(boolean valid, String message, SclRef ref) {
        this.valid = valid;
        this.message = message;
        this.ref = ref;
    }

    public static SclRefResult valid(SclRef ref) {
        return new SclRefResult(true, null, ref);
    }

    public static SclRefResult invalid(String message) {
        return new SclRefResult(false, message, null);
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

    public SclRef getRef() {
        return ref;
    }
}
