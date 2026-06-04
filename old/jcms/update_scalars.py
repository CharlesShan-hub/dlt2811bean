"""Batch-update scalar subclasses: add ffiDecode/perDecode, rename decode→from."""
import os, re

BASE = "/Users/charles/workspace/project/dlt2811bean/jcms/src/main/java/com/ysh/jcms"

FILES = [
    f"{BASE}/datatypes/numeric/CmsInt8.java",
    f"{BASE}/datatypes/numeric/CmsInt8U.java",
    f"{BASE}/datatypes/numeric/CmsInt16.java",
    f"{BASE}/datatypes/numeric/CmsInt16U.java",
    f"{BASE}/datatypes/numeric/CmsInt24U.java",
    f"{BASE}/datatypes/numeric/CmsInt32.java",
    f"{BASE}/datatypes/numeric/CmsInt32U.java",
    f"{BASE}/datatypes/numeric/CmsInt64.java",
    f"{BASE}/datatypes/numeric/CmsInt64U.java",
    f"{BASE}/datatypes/numeric/CmsFloat32.java",
    f"{BASE}/datatypes/numeric/CmsFloat64.java",
    f"{BASE}/datatypes/numeric/CmsBoolean.java",          # already done via Edit
    f"{BASE}/datatypes/enumerated/CmsSmpMod.java",
    f"{BASE}/datatypes/enumerated/CmsAddCause.java",
    f"{BASE}/datatypes/enumerated/CmsTcmd.java",
    f"{BASE}/datatypes/enumerated/CmsDbpos.java",
    f"{BASE}/datatypes/enumerated/CmsServiceError.java",
    f"{BASE}/datatypes/enumerated/CmsOrCat.java",
    f"{BASE}/datatypes/code/CmsCheck.java",
    f"{BASE}/datatypes/code/CmsTriggerConditions.java",
    f"{BASE}/datatypes/code/CmsReasonCode.java",
    f"{BASE}/datatypes/code/CmsRcbOptFlds.java",
    f"{BASE}/datatypes/code/CmsLcbOptFlds.java",
    f"{BASE}/datatypes/code/CmsMsvcbOptFlds.java",
    f"{BASE}/datatypes/code/CmsQuality.java",
    f"{BASE}/datatypes/string/CmsEntryID.java",
    f"{BASE}/datatypes/string/CmsFC.java",
    f"{BASE}/datatypes/string/CmsObjectName.java",
    f"{BASE}/datatypes/string/CmsObjectReference.java",
    f"{BASE}/datatypes/string/CmsSubReference.java",
    f"{BASE}/datatypes/packed/CmsPackedList.java",
    f"{BASE}/services/connect/CmsAssociationId.java",
    f"{BASE}/services/connect/CmsServerAccessPointReference.java",
    f"{BASE}/services/connect/CmsAbortReason.java",
]

# Read files
contents = {}
for path in FILES:
    if not os.path.exists(path):
        print(f"MISS {os.path.basename(path)}")
        continue
    with open(path) as f:
        content = f.read()
    # Check if already has from()
    if "public static " + os.path.basename(path).replace(".java","") + " from(byte[]" in content:
        # Already done
        print(f"DONE {os.path.basename(path)}")
        continue
    contents[path] = content

# Process each file
for path, content in list(contents.items()):
    class_name = os.path.basename(path).replace(".java", "")
    
    # Find the decode method pattern
    # Match: public static Xxx decode(byte[] data) { ... }
    # Use a careful regex that captures the whole method body
    pattern = r'(    public static ' + re.escape(class_name) + r' decode\(byte\[\] (?:data|encoded)\) \{(.*?)\n    \})'
    m = re.search(pattern, content, re.DOTALL)
    if not m:
        print(f"SKIP {class_name}: can't find decode method")
        continue
    
    full_method = m.group(1)
    body = m.group(2)
    
    var_name = "data" if "byte[] data)" in full_method else "encoded"
    
    # Determine type patterns
    has_ffi = "CmsFFIDatatypes.isAvailable()" in body
    is_coded_enum = "fromPerBytes" in body
    is_float = class_name in ("CmsFloat32", "CmsFloat64")
    is_bool = class_name == "CmsBoolean"
    is_byte_str = class_name in ("CmsEntryID",)
    is_fixed_str = class_name in ("CmsFC",)
    is_ffi_only = class_name == "CmsPackedList"
    
    if is_bool:
        # Already handled by Edit tool
        continue
    
    if is_ffi_only:
        # CmsPackedList — only FFI, no PER fallback
        ffi_body = body.strip()
        # Remove leading "byte[] valBuf = new byte[65536];" and the return
        # Extract just the decode logic
        per_block = "throw new UnsupportedOperationException(\"" + class_name + " has no Java PER decode fallback\");"
        ffi_getter = body.strip()
        # Create ffiDecode from the body
        lines = ffi_getter.split("\n")
        indent = "        "
        ffi_lines = []
        for line in lines:
            stripped = line.strip()
            if stripped.startswith("byte[] valBuf"):
                ffi_lines.append(f"byte[] valBuf = new byte[65536];")
            elif stripped.startswith("IntByReference"):
                ffi_lines.append(stripped)
            elif stripped.startswith("CmsFFIDatatypes"):
                ffi_lines.append(stripped)
            elif stripped.startswith("byte[] result"):
                ffi_lines.append(stripped)
            elif stripped.startswith("System.arraycopy"):
                ffi_lines.append(stripped.replace("return new " + class_name, "this.value = new byte[valLen.getValue()]; System.arraycopy"))
            elif stripped.startswith("return new"):
                ffi_lines.append("this.value = result;")
            elif stripped:
                ffi_lines.append(stripped)
        
        ffi_code = "\n".join(indent + l for l in ffi_lines)
        
        new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{{ffi_code}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        {per_block}
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
    elif has_ffi:
        if is_coded_enum:
            # Coded enum pattern: byte[] val = new byte[N]; ...decode(data, data.length, val); return new Xxx(fromPerBytes(val, N));
            # PER: return new Xxx(fromPerBytes(PerBitString.decodeFixedSizeBytes(new PerInputStream(data), N), N));
            
            # Extract FFI body
            ffi_m = re.search(r'if \(CmsFFIDatatypes\.isAvailable\(\)\) \{(.*?)\n        \}', body, re.DOTALL)
            if not ffi_m:
                print(f"SKIP {class_name}: can't parse FFI block")
                continue
            ffi_body = ffi_m.group(1).strip()
            
            # Extract PER body (after FFI block)
            per_m = re.search(r'if \(CmsFFIDatatypes\.isAvailable\(\)\) \{.*?\n        \}\n(.*?)\n        return', body, re.DOTALL)
            if not per_m:
                print(f"SKIP {class_name}: can't parse PER block")
                continue
            per_body = per_m.group(1).strip()
            
            # Transform FFI body to ffiDecode
            ffi_decode_val = re.search(r'byte\[\] (\w+) = new byte\[\d+\];', ffi_body)
            ffi_decode_call = re.search(r'CmsFFIDatatypes\.\w+\.INSTANCE\.cms_\w+_decode\(.*?\);', ffi_body)
            ffi_return = re.search(r'return new \w+\(fromPerBytes\((\w+), (\d+)\)\);', ffi_body)
            
            if ffi_decode_val and ffi_return:
                val_name = ffi_decode_val.group(1)
                size_val = ffi_return.group(2)
                ffi_new = f"""        byte[] {val_name} = new byte[{(int(ffi_return.group(2)) + 7) // 8}];
        {ffi_decode_call.group(0)}
        this.value = fromPerBytes({val_name}, {size_val});"""
            else:
                print(f"SKIP {class_name}: can't parse coded enum FFI")
                continue
            
            # Transform PER body
            per_return = re.search(r'return new \w+\(fromPerBytes\(PerBitString\.decodeFixedSizeBytes\(new PerInputStream\(data\), (\d+)\), \1\)\);', per_body)
            if per_return:
                size_val = per_return.group(1)
                per_new = f"this.value = fromPerBytes(PerBitString.decodeFixedSizeBytes(pis, {size_val}), {size_val});"
            else:
                print(f"SKIP {class_name}: can't parse coded enum PER")
                continue
            
            new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{
{ffi_new}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        {per_new}
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
        elif is_float:
            # Float pattern
            if class_name == "CmsFloat32":
                ffi_new = """        float[] v = new float[1];
        CmsFFIDatatypes.Holder.INSTANCE.cms_float32_decode(data, data.length, v);
        this.value = v[0];"""
                per_new = "this.value = Float.intBitsToFloat((int) new PerInputStream(data).readSignedInteger(4));"
            else:
                ffi_new = """        double[] v = new double[1];
        CmsFFIDatatypes.Holder.INSTANCE.cms_float64_decode(data, data.length, v);
        this.value = v[0];"""
                per_new = "this.value = Double.longBitsToDouble(new PerInputStream(data).readSignedInteger(8));"
            
            new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{
{ffi_new}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        {per_new}
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
        elif is_byte_str:
            # CmsEntryID pattern
            ffi_new = """        byte[] val = new byte[8];
        CmsFFIDatatypes.Holder.INSTANCE.cms_entry_id_decode(data, data.length, val);
        this.value = val;
        this.present = true;"""
            per_new = "this.value = PerOctetString.decodeFixedSize(pis, 8);\n        this.present = true;"
            new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{
{ffi_new}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        {per_new}
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
        elif is_fixed_str:
            # CmsFC pattern
            ffi_new = """        byte[] strBuf = new byte[2];
        CmsFFIDatatypes.Holder.INSTANCE.cms_fc_decode(data, data.length, strBuf);
        this.value = new String(strBuf, StandardCharsets.US_ASCII);
        this.present = true;"""
            per_new = "this.value = PerVisibleString.decodeFixedSize(pis, 2);\n        this.present = true;"
            new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{
{ffi_new}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        {per_new}
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
        else:
            # Standard numeric/enumerated/string pattern
            # Determine the FFI variable type
            if "LongByReference" in body:
                var_type = "LongByReference"
                ffi_val_getter = "v.getValue()"
            elif "ByteByReference" in body:
                var_type = "ByteByReference"
                ffi_val_getter = "(int) v.getValue()"
            else:
                var_type = "IntByReference"
                ffi_val_getter = "v.getValue()"
            
            # Check the return line for casting
            ffi_return_line = re.search(r'return new \w+\((.*)\);\s*$', body, re.MULTILINE)
            if ffi_return_line:
                ret_val = ffi_return_line.group(1)
            else:
                ret_val = ffi_val_getter
            
            # For CmsObjectName/CmsObjectReference/CmsSubReference (string types)
            if class_name in ("CmsObjectName", "CmsObjectReference", "CmsSubReference"):
                # String types with complex decode
                ffi_new = """        byte[] strBuf = new byte[128];
        IntByReference strLen = new IntByReference(64);
        CmsFFIDatatypes.Holder.INSTANCE.cms_""" + class_name[3].lower() + class_name[4:] + """_decode(data, data.length, strBuf, strLen);
        this.value = new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII);
        this.present = true;"""
                per_new = "this.value = PerVisibleString.decodeConstrained(pis, 0, 64);\n        this.present = true;"
            elif class_name == "CmsInt8":
                ffi_new = f"""        ByteByReference v = new ByteByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_int8_decode(data, data.length, v);
        this.value = (int) v.getValue();"""
                per_new = "this.value = (int) PerInteger.decode(pis, MIN, MAX);"
            elif class_name == "CmsInt64":
                ffi_new = f"""        LongByReference v = new LongByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_int64_decode(data, data.length, v);
        this.value = v.getValue();"""
                per_new = "this.value = PerInteger.decodeUnconstrained(pis);"
            elif class_name == "CmsInt64U":
                ffi_new = f"""        LongByReference v = new LongByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_int64u_decode(data, data.length, v);
        this.value = unsignedLongToBigInteger(v.getValue());"""
                per_new = "this.value = unsignedLongToBigInteger(PerInteger.decodeUnconstrained(pis));"
            elif class_name == "CmsInt32U":
                # Long value with IntByReference
                ffi_new = """        LongByReference v = new LongByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_int32u_decode(data, data.length, v);
        this.value = v.getValue();"""
                per_new = "this.value = PerInteger.decode(pis, MIN, MAX);"
            else:
                ffi_new = f"""        {var_type} v = new {var_type}();
        CmsFFIDatatypes.Holder.INSTANCE.cms_{class_name[3].lower()}{class_name[4:]}_decode(data, data.length, v);
        this.value = {ret_val};"""
                
                # For enumerated with fixed upper bound
                if class_name in ("CmsSmpMod", "CmsAddCause", "CmsTcmd", "CmsDbpos", "CmsServiceError"):
                    # These use size field as upper bound
                    per_new = "this.value = (int) PerInteger.decode(pis, 0, size - 1);"
                elif class_name == "CmsOrCat":
                    per_new = "this.value = (int) PerInteger.decode(pis, 0, 3);"
                elif class_name in ("CmsInt8U", "CmsInt16", "CmsInt16U", "CmsInt24U", "CmsInt32", "CmsInt8"):
                    per_new = "this.value = (int) PerInteger.decode(pis, MIN, MAX);"
                elif class_name in ("CmsInt64U",):
                    per_new = "this.value = unsignedLongToBigInteger(PerInteger.decodeUnconstrained(pis));"
                else:
                    per_new = "this.value = (" + ret_val.replace("v.getValue()", "PerInteger.decode(pis, MIN, MAX)") + ");"
            
            new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{
{ffi_new}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        {per_new}
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
    else:
        # No FFI check — FFI-only files like CmsPackedList
        if class_name == "CmsPackedList":
            ffi_call = body.strip()
            ffi_new = ffi_call.replace("return new " + class_name + "(result);", "this.value = result;")
            if "this.value" not in ffi_new:
                # Try another pattern
                ffi_new = ffi_call.replace("return new " + class_name + "(result)", "this.value = result")
            new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{
        {ffi_new}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        throw new UnsupportedOperationException("{class_name} has no Java PER decode fallback");
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
        elif class_name == "CmsOrCat":
            ffi_new = """        IntByReference v = new IntByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_or_cat_decode(data, data.length, v);
        this.value = v.getValue();"""
            new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{
{ffi_new}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        throw new UnsupportedOperationException("{class_name} has no Java PER decode fallback");
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
        elif class_name == "CmsServerAccessPointReference":
            ffi_new = """        byte[] strBuf = new byte[256];
        IntByReference strLen = new IntByReference(64);
        CmsFFIDatatypes.Holder.INSTANCE.cms_server_access_point_reference_decode(data, data.length, strBuf, strLen);
        this.value = new String(strBuf, 0, strLen.getValue(), StandardCharsets.US_ASCII);
        this.present = true;"""
            new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{
{ffi_new}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        this.value = PerVisibleString.decodeConstrained(pis, 0, 64);
        this.present = true;
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
        elif class_name == "CmsAssociationId":
            ffi_new = """        CmsOctetString os = CmsOctetString.decode(data, Mode.VARIABLE, MAX_LEN);
        this.value = os.get();
        this.present = true;"""
            new_methods = f"""    @Override
    protected void ffiDecode(byte[] data) {{
{ffi_new}
    }}

    @Override
    protected void perDecode(PerInputStream pis) {{
        this.value = PerOctetString.decodeConstrained(pis, 0, MAX_LEN);
        this.present = true;
    }}

    public static {class_name} from(byte[] data) {{
        return new {class_name}().decode(data);
    }}"""
        else:
            print(f"SKIP {class_name}: unknown non-FFI pattern")
            continue
    
    content_new = content.replace(full_method, new_methods)
    contents[path] = content_new
    print(f"OK   {class_name}")

# Write files
for path, content in contents.items():
    with open(path, 'w') as f:
        f.write(content)

print("Done!")
