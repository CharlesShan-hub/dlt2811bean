import os, re, sys

root = r'd:\project\work\standard\dlt2811bean\cms\jcms\src\main\java\com\ysh\jcms'

# Types that need ByValue inner class (all concrete CmsType/CmsScalar subclasses)
# We add ByValue to any concrete class extending CmsType or CmsScalar
need_byvalue = {}

for dirpath, _, files in os.walk(root):
    for f in files:
        if not f.endswith('.java'):
            continue
        path = os.path.join(dirpath, f)
        with open(path, 'r', encoding='utf-8') as fh:
            content = fh.read()
        
        # Check if this is a concrete class extending CmsType or CmsScalar
        m = re.search(r'public class (\w+) extends (\w+)', content)
        if not m:
            continue
        cls, parent = m.groups()
        
        # Skip abstract classes and ByValue inner classes
        if 'abstract class' in content or '.ByValue' in cls:
            continue
            
        # Check if parent is in our hierarchy
        if parent in ('CmsType', 'CmsScalar', 'CmsUint8Array') or parent.endswith('CmsType') or parent.endswith('CmsScalar'):
            # Check if ByValue already exists
            if 'class ByValue' in content:
                continue
            
            # Add import if not present
            if 'import com.sun.jna.Structure;' not in content:
                content = content.replace('package ',
                    'import com.sun.jna.Structure;\npackage ', 1)
            
            # Find the class declaration and add ByValue after the last field
            # Simpler: add it right before the last closing brace
            last_brace = content.rfind('}')
            byvalue_cls = '\n    public static class ByValue extends ' + cls + ' implements Structure.ByValue {}\n'
            
            if 'implements Structure.ByValue' not in content:
                content = content[:last_brace] + byvalue_cls + content[last_brace:]
                
            with open(path, 'w', encoding='utf-8') as fh:
                fh.write(content)
            print(f'Added ByValue to: {path[-50:]}')

print('Done')
