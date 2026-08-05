import { reactive } from 'vue'

/** 跨标签页共享的调试视图状态 */
export const debugShared = reactive({
  showAsn1: localStorage.getItem('cms-show-asn1') !== '0',
  leftColWidth: 380,
  topHeight: 300,
})

/** 持久化 showAsn1 到 localStorage */
export function setShowAsn1(v) {
  debugShared.showAsn1 = v
  localStorage.setItem('cms-show-asn1', v ? '1' : '0')
}