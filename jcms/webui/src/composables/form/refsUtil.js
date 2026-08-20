/** refs-list 行的默认构造（级联 / 普通两种），供 initForm 与 addRefs 共用。 */

export function cascadeRow() {
  return { ld: '', ln: '', do: '', sdo: '', da: '', fc: '', value: '', type: 'visible-string' }
}

export function plainRow() {
  return ''
}