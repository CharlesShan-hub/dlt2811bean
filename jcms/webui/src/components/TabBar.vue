<template>
  <div class="tab-bar" ref="tabBarRef" @contextmenu.prevent>
    <div class="tab-scroll" ref="scrollRef">
      <div
        v-for="(tab, index) in tabs"
        :key="tab.id"
        class="tab-item"
        :class="{
          active: tab.id === activeId,
          pinned: tab.pinned,
          'drag-over-left': dragOverIndex === index && dragOverSide === 'left',
          'drag-over-right': dragOverIndex === index && dragOverSide === 'right',
        }"
        :draggable="true"
        @click="$emit('switch', tab.id)"
        @mousedown.middle="tryClose(tab)"
        @contextmenu.prevent="openContextMenu($event, tab, index)"
        @dragstart="onDragStart($event, index)"
        @dragover="onDragOver($event, index)"
        @dragleave="onDragLeave"
        @dragend="onDragEnd"
        @drop="onDrop($event, index)"
      >
        <!-- Pin 图标 -->
        <span v-if="tab.pinned" class="pin-icon" title="已固定">📌</span>
        <span class="tab-label">{{ tab.title }}</span>
        <span
          v-if="!tab.pinned"
          class="tab-close"
          title="关闭"
          @click.stop="tryClose(tab)"
        >✕</span>
      </div>
    </div>

    <!-- 右键菜单 -->
    <div
      v-if="contextMenu.visible"
      class="context-menu"
      :style="{ left: contextMenu.x + 'px', top: contextMenu.y + 'px' }"
      ref="menuRef"
    >
      <div class="ctx-item" @click="closeLeft">关闭左边</div>
      <div class="ctx-item" @click="closeRight">关闭右边</div>
      <div class="ctx-item" @click="closeOthers">关闭其他</div>
      <div class="ctx-item" @click="closeAll">关闭全部</div>
      <div class="ctx-divider"></div>
      <div class="ctx-item" @click="togglePin">
        {{ contextMenu.tab?.pinned ? '取消固定' : '固定标签' }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  tabs: Array,
  activeId: String,
})

const emit = defineEmits(['switch', 'close', 'close-left', 'close-right', 'close-others', 'close-all', 'toggle-pin', 'reorder'])

const tabBarRef = ref(null)
const scrollRef = ref(null)
const menuRef = ref(null)

// ── 右键菜单 ──
const contextMenu = reactive({
  visible: false,
  x: 0,
  y: 0,
  tab: null,
  index: -1,
})

function openContextMenu(e, tab, index) {
  contextMenu.visible = true
  contextMenu.x = e.clientX
  contextMenu.y = e.clientY
  contextMenu.tab = tab
  contextMenu.index = index
}

function closeContextMenu() {
  contextMenu.visible = false
  contextMenu.tab = null
  contextMenu.index = -1
}

function closeLeft() {
  emit('close-left', contextMenu.index)
  closeContextMenu()
}

function closeRight() {
  emit('close-right', contextMenu.index)
  closeContextMenu()
}

function closeOthers() {
  emit('close-others', contextMenu.index)
  closeContextMenu()
}

function closeAll() {
  emit('close-all')
  closeContextMenu()
}

function togglePin() {
  if (contextMenu.tab) {
    emit('toggle-pin', contextMenu.tab.id)
  }
  closeContextMenu()
}

// 点击菜单外部关闭
function onWindowClick(e) {
  if (contextMenu.visible && menuRef.value && !menuRef.value.contains(e.target)) {
    closeContextMenu()
  }
}

onMounted(() => {
  window.addEventListener('click', onWindowClick)
  window.addEventListener('resize', closeContextMenu)
})

onUnmounted(() => {
  window.removeEventListener('click', onWindowClick)
  window.removeEventListener('resize', closeContextMenu)
})

// ── 关闭（pin 保护） ──
function tryClose(tab) {
  if (tab.pinned) return
  emit('close', tab.id)
}

// ── 拖动排序 ──
const dragIndex = ref(-1)
const dragOverIndex = ref(-1)
const dragOverSide = ref('')

function onDragStart(e, index) {
  dragIndex.value = index
  e.dataTransfer.effectAllowed = 'move'
  e.dataTransfer.setData('text/plain', String(index))
  e.target.style.opacity = '0.5'
}

function onDragOver(e, index) {
  e.preventDefault()
  e.dataTransfer.dropEffect = 'move'
  if (dragIndex.value === index) return

  const rect = e.currentTarget.getBoundingClientRect()
  const mid = rect.left + rect.width / 2
  dragOverIndex.value = index
  dragOverSide.value = e.clientX < mid ? 'left' : 'right'
}

function onDragLeave() {
  dragOverIndex.value = -1
  dragOverSide.value = ''
}

function onDragEnd(e) {
  e.target.style.opacity = ''
  dragIndex.value = -1
  dragOverIndex.value = -1
  dragOverSide.value = ''
}

function onDrop(e, index) {
  e.preventDefault()
  e.target.style.opacity = ''
  const from = dragIndex.value
  if (from === -1 || from === index) {
    dragIndex.value = -1
    dragOverIndex.value = -1
    dragOverSide.value = ''
    return
  }
  emit('reorder', { from, to: index })
  dragIndex.value = -1
  dragOverIndex.value = -1
  dragOverSide.value = ''
}
</script>

<style scoped>
.tab-bar {
  display: flex;
  align-items: center;
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border);
  flex-shrink: 0;
  overflow-x: auto;
  scrollbar-width: thin;
  position: relative;
  z-index: 10;
}

.tab-bar::-webkit-scrollbar {
  height: 3px;
}

.tab-bar::-webkit-scrollbar-thumb {
  background: var(--border);
  border-radius: 2px;
}

.tab-scroll {
  display: flex;
  align-items: stretch;
  min-height: 34px;
}

.tab-item {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 7px 14px;
  font-size: 12px;
  color: var(--text-secondary);
  cursor: pointer;
  white-space: nowrap;
  border-right: 1px solid var(--border);
  transition: background 0.12s, color 0.12s, border-color 0.12s;
  user-select: none;
  position: relative;
}

.tab-item:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.tab-item.active {
  color: var(--accent);
  background: var(--bg-primary);
  border-bottom: 2px solid var(--accent);
  margin-bottom: -1px;
}

.tab-item.pinned {
  padding-right: 14px;
}

/* 拖拽插入指示器 */
.tab-item.drag-over-left::before {
  content: '';
  position: absolute;
  left: 0;
  top: 4px;
  bottom: 4px;
  width: 2px;
  background: var(--accent);
  border-radius: 1px;
  box-shadow: 0 0 6px var(--accent);
}

.tab-item.drag-over-right::after {
  content: '';
  position: absolute;
  right: -1px;
  top: 4px;
  bottom: 4px;
  width: 2px;
  background: var(--accent);
  border-radius: 1px;
  box-shadow: 0 0 6px var(--accent);
}

/* 固定图标 */
.pin-icon {
  font-size: 11px;
  line-height: 1;
  opacity: 0.7;
  flex-shrink: 0;
}

.tab-label {
  font-size: 12px;
  font-weight: 500;
}

.tab-close {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  font-size: 10px;
  border-radius: 3px;
  color: var(--text-muted);
  transition: background 0.1s, color 0.1s;
  flex-shrink: 0;
}

.tab-close:hover {
  background: var(--red-bg);
  color: var(--red);
}

/* ── 右键菜单 ── */
.context-menu {
  position: fixed;
  z-index: 9999;
  min-width: 140px;
  background: #1e1f2e;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 4px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5), 0 0 0 1px rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(12px);
}

.ctx-item {
  padding: 7px 14px;
  font-size: 12px;
  color: var(--text-secondary);
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.1s, color 0.1s;
}

.ctx-item:hover {
  background: var(--accent-muted);
  color: var(--text-primary);
}

.ctx-divider {
  height: 1px;
  margin: 4px 8px;
  background: rgba(255, 255, 255, 0.08);
}
</style>