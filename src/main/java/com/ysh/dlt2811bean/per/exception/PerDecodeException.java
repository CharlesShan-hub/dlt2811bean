package com.ysh.dlt2811bean.per.exception;

/**
 * Exception thrown when PER decoding fails.
 *
 * <p>This is raised when input data does not conform to ASN.1 APER encoding rules,
 * including: insufficient data, invalid constraint range, type mismatch, etc.
 */
public class PerDecodeException extends Exception {

    public PerDecodeException(String message) {
        super(message);
    }

    public PerDecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
