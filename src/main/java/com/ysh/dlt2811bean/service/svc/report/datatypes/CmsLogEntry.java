package com.ysh.dlt2811bean.service.svc.report.datatypes;

/**
 * LogEntry structure definition for logging services.
 *
 * Corresponds to Table 51 in GB/T 45906.3-2025: LogEntry parameters.
 *
 * Category: Logging service
 *
 * The LogEntry structure represents a single log record containing the entry time,
 * unique identifier, and one or more data elements. Each data element in the
 * entryData sequence references an object attribute with its functional constraint
 * and value.
 *
 * The entryData field uses reference indexing, where 'reference' is the object
 * reference name and 'fc' is the functional constraint.
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * LogEntry:: = SEQUENCE {
 *   timeOfEntry    [0] IMPLICIT EntryTime,
 *   entryID        [1] IMPLICIT EntryID,
 *   entryData      [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint,
 *     value         [2] IMPLICIT Data,
 *     reason        [3] IMPLICIT ReasonCode
 *   }
 * }
 * </pre>
 */
public class CmsLogEntry {
}
