package com.ysh.jcms2.data.string;

/*
 * BitString ::= BIT STRING
 *
 * This type is an alias for CmsUint8Array { uint8_t* value; int32_t len; }.
 * Use CmsUint8Array directly — this file is kept only for ASN.1 type-name
 * documentation.
 *
 * NOTE: For BitString, len stores the number of bits (nbits), not bytes.
 */
public class CmsBitString {
}
