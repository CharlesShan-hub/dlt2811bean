import os, re

root = r'd:\project\work\standard\dlt2811bean\cms\jcms\src\main\java\com\ysh\jcms'

for dirpath, _, files in os.walk(root):
    for f in files:
        if not f.endswith('.java'):
            continue
        path = os.path.join(dirpath, f)
        with open(path, 'r', encoding='utf-8') as fh:
            content = fh.read()
        
        # Remove broken @Setter/import rubble
        content = content.replace('import lombok.Setter;', '')
        content = re.sub(r'@Setter\s*\n', '', content)
        
        # Replace '@Getter\n' with '@Getter @Setter\n' (but only if @Accessors follows)
        content = content.replace('@Getter\n@Accessors', '@Getter @Setter\n@Accessors')
        
        # Ensure import lombok.Getter and import lombok.Setter exist
        if 'import lombok.Getter;' in content and 'import lombok.Setter;' not in content:
            content = content.replace('import lombok.Getter;\nimport lombok.experimental.Accessors;',
                                      'import lombok.Getter;\nimport lombok.Setter;\nimport lombok.experimental.Accessors;')
        
        with open(path, 'w', encoding='utf-8') as fh:
            fh.write(content)
        
        if '@Getter @Setter' in content:
            print(f'OK: {path[-50:]}')
