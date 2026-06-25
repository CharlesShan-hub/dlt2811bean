# PowerShell script: migrate all svc encode functions to dynamic allocation
$srcDir = "d:\project\work\standard\dlt2811bean\cms\ccms"
$cFiles = Get-ChildItem -Path "$srcDir\src\svc" -Filter "*.c" -Recurse

Write-Output "=== Updating .c files ==="
foreach ($f in $cFiles) {
    $content = Get-Content $f.FullName -Raw
    if ($content -notmatch 'per_stream_init_write') { continue }
    $original = $content

    # Pattern: int xxx_encode(const xxx_t *pdu, uint8_t *out_buf, int *out_len) {
    #   per_stream_t s;
    #   per_stream_init_write(&s, out_buf, (size_t)*out_len);
    #   int rc = xxx_encode_stream(&s, pdu);
    #   if (rc) return rc;
    #   *out_len = (int)per_stream_bytes_written(&s);
    #   return CMS_OK;
    # }
    $encodePattern = '(?ms)(int \w+_encode\(const \w+_t \*pdu, )uint8_t \*out_buf, int \*out_len\) \{[^}]*\n\s+per_stream_t s;\n\s+per_stream_init_write\(&s, out_buf, \(size_t\)\*out_len\);\n\s+int rc = \w+_encode_stream\(&s, pdu\);\n\s+if \(rc\) return rc;\n\s+\*out_len = \(int\)per_stream_bytes_written\(&s\);\n\s+return CMS_OK;\n\}'

    $replacement = '${1}uint8_t **out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_dynamic(&s, 256);
    int rc = $($2)_encode_stream(&s, pdu);
    if (rc) { free(s.buf); return rc; }
    *out_buf = per_stream_detach(&s, (size_t *)out_len);
    return CMS_OK;
}'

    # Try a different approach - simpler regex
    $content = $content -replace '(?ms)(int \w+_encode\(const \w+_t \*pdu, )uint8_t \*out_buf, int \*out_len\)', '${1}uint8_t **out_buf, int *out_len)'
    
    if ($content -ne $original) {
        Set-Content -Path $f.FullName -Value $content -NoNewline
        Write-Output "  Updated signatures: $($f.Name)"
    }
}

# Now change the function bodies
Write-Output ""
Write-Output "=== Updating function bodies ==="
foreach ($f in $cFiles) {
    $content = Get-Content $f.FullName -Raw
    $original = $content
    
    # Match the 3-line encode body pattern
    $bodyPattern = '(?ms)(int rc = \w+_encode_stream\(&s, pdu\);)\n\s+if \(rc\) return rc;\n\s+\*out_len = \(int\)per_stream_bytes_written\(&s\);'
    $content = $content -replace $bodyPattern, '${1}
    if (rc) { free(s.buf); return rc; }
    *out_buf = per_stream_detach(&s, (size_t *)out_len);'

    # Change per_stream_init_write to per_stream_init_dynamic
    $content = $content -replace 'per_stream_init_write\(&s, out_buf, \(size_t\)\*out_len\)', 'per_stream_init_dynamic(&s, 256)'
    
    if ($content -ne $original) {
        Set-Content -Path $f.FullName -Value $content -NoNewline
        Write-Output "  Updated bodies: $($f.Name)"
    }
}

# Now update .h files
Write-Output ""
Write-Output "=== Updating .h files ==="
$hFiles = Get-ChildItem -Path "$srcDir\include\svc" -Filter "*.h" -Recurse
foreach ($f in $hFiles) {
    $content = Get-Content $f.FullName -Raw
    $original = $content
    $content = $content -replace '(CMS_EXPORT )?int (\w+_encode\(const \w+_t \*pdu, )uint8_t \*out_buf, int \*out_len\)', '${1}int ${2}uint8_t **out_buf, int *out_len)'
    if ($content -ne $original) {
        Set-Content -Path $f.FullName -Value $content -NoNewline
        Write-Output "  Updated headers: $($f.Name)"
    }
}

Write-Output "Done!"
