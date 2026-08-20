import { inject } from 'vue'

/** 表单上下文注入 key：CommandForm 提供，字段组件据此取 options/handlers。 */
export const FORM_CTX = Symbol('cms-form-ctx')

/** 便捷取上下文。 */
export function useFormCtx() {
  return inject(FORM_CTX)
}