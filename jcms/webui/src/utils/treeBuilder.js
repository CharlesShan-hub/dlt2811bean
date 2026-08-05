/**
 * 将扁平引用列表解析为 DO/SDO 层级树结构
 * 用于 ServerDir 的目录树展示
 */

/**
 * @param {string} nodeName - 逻辑节点名（如 "PROT/PTOC1"）
 * @param {string[]} refs - 扁平引用列表（如 ["PROT/PTOC1.StrVal", "PROT/PTOC1.StrVal.setMag", ...]）
 * @returns {object[]} 树节点数组，每个节点格式：
 *   { name, type: 'do', label, ref, children: null|[], loading, expanded, isLeaf }
 */
export function buildDoTree(nodeName, refs) {
  const prefix = nodeName + '.'
  // 构建嵌套 map：{ "Mod": { "Beh": {}, "Mag": {} } }
  const root = {}
  for (const ref of refs) {
    const relative = ref.startsWith(prefix) ? ref.substring(prefix.length) : ref
    const parts = relative.split('.')
    let current = root
    for (const part of parts) {
      if (!current[part]) current[part] = {}
      current = current[part]
    }
  }
  // 递归转树节点
  function toNodes(obj) {
    return Object.entries(obj).map(([key, children]) => {
      const childKeys = Object.keys(children)
      const hasChildren = childKeys.length > 0
      return {
        name: `${nodeName}/${key}`,
        type: 'do',
        label: key,
        ref: `${nodeName}.${key}`,
        children: hasChildren ? toNodes(children) : null,
        loading: false,
        expanded: false,
        isLeaf: !hasChildren,
      }
    })
  }
  return toNodes(root)
}