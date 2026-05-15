package com.ysh.dlt2811bean.scl2.ref;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SclRefResult {

    private final boolean valid;
    private final String message;
    private final SclRef ref;

    public static SclRefResult valid(SclRef ref) {
        return new SclRefResult(true, null, ref);
    }

    public static SclRefResult invalid(String message) {
        return new SclRefResult(false, message, null);
    }

    public Optional<SclRef> getRef() {
        return Optional.ofNullable(ref);
    }

    public SclRef getRefOrThrow() {
        if (!valid) throw new IllegalArgumentException(message);
        return ref;
    }

    @Override
    public String toString() {
        return valid ? "VALID: " + ref : "INVALID: " + message;
    }
}
