/**
 * 可拖拽分割面板 composable
 * 管理 CommandDebug 左右列（垂直拖拽）和右列上下区域（水平拖拽）的分割宽度
 */
import { ref, watch, toRef } from 'vue'
import { debugShared } from '../stores/debugShared.js'

export function useSplitPane() {
  const gridRef = ref(null)
  const leftColWidth = toRef(debugShared, 'leftColWidth')
  const topHeight = toRef(debugShared, 'topHeight')
  const showAsn1 = toRef(debugShared, 'showAsn1')

  /** 左栏默认宽度（拖拽后双击手柄还原） */
  const DEFAULT_LEFT = 380

  // ASN.1 显示状态持久化
  watch(showAsn1, (v) => localStorage.setItem('cms-show-asn1', v ? '1' : '0'))

  let dragging = null

  function startVDrag(e) {
    dragging = { type: 'v', startX: e.clientX, startW: leftColWidth.value }
    document.addEventListener('mousemove', onDragMove)
    document.addEventListener('mouseup', stopDrag)
    document.body.style.cursor = 'col-resize'
    document.body.style.userSelect = 'none'
  }

  function startHDrag(e) {
    dragging = { type: 'h', startY: e.clientY, startH: topHeight.value }
    document.addEventListener('mousemove', onDragMove)
    document.addEventListener('mouseup', stopDrag)
    document.body.style.cursor = 'row-resize'
    document.body.style.userSelect = 'none'
  }

  function onDragMove(e) {
    if (!dragging) return
    if (dragging.type === 'v') {
      const dx = e.clientX - dragging.startX
      leftColWidth.value = Math.max(200, Math.min(800, dragging.startW + dx))
    } else {
      const dy = e.clientY - dragging.startY
      topHeight.value = Math.max(100, Math.min(2000, dragging.startH + dy))
    }
  }

  function stopDrag() {
    dragging = null
    document.removeEventListener('mousemove', onDragMove)
    document.removeEventListener('mouseup', stopDrag)
    document.body.style.cursor = ''
    document.body.style.userSelect = ''
  }

  /** 双击手柄：还原左栏默认宽度 */
  function resetSplit() {
    leftColWidth.value = DEFAULT_LEFT
  }

  return {
    gridRef,
    leftColWidth,
    topHeight,
    showAsn1,
    startVDrag,
    startHDrag,
    resetSplit,
  }
}