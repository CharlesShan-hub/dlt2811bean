<template>
  <div ref="wrapEl" class="ui-select">
    <button type="button" class="ui-select__trigger" @click="toggle">
      <span class="ui-select__value" :class="{ placeholder: !modelValue }">
        {{ modelValue || (loading ? '加载中...' : placeholder) }}
      </span>
      <span class="ui-select__arrow" :class="{ open }">▾</span>
    </button>

    <transition name="ui-select">
      <div v-if="open" ref="menuEl" class="ui-select__menu" :style="menuStyle">
        <div v-if="options.length === 0" class="ui-select__empty">
          {{ loading ? '加载中...' : '暂无选项' }}
        </div>
        <div
          v-for="opt in options"
          :key="opt"
          class="ui-select__option"
          :class="{ selected: opt === modelValue }"
          @click="choose(opt)"
        >
          {{ opt }}
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount } from 'vue'

defineProps({
  modelValue: { type: String, default: '' },
  /** 字符串选项列表 */
  options: { type: Array, default: () => [] },
  placeholder: { type: String, default: '请选择' },
  loading: Boolean,
})

const emit = defineEmits(['update:modelValue'])

const open = ref(false)
const wrapEl = ref(null)
const menuEl = ref(null)
const menuStyle = ref({})

function toggle() {
  open.value ? close() : openMenu()
}

/** 打开浮层：按触发按钮位置 fixed 定位，避免被滚动容器裁剪。 */
function openMenu() {
  const r = wrapEl.value.getBoundingClientRect()
  menuStyle.value = { top: `${r.bottom + 4}px`, left: `${r.left}px`, width: `${r.width}px` }
  open.value = true
  document.addEventListener('click', onDocClick)
  document.addEventListener('scroll', onDocScroll, true)
}

function close() {
  open.value = false
  document.removeEventListener('click', onDocClick)
  document.removeEventListener('scroll', onDocScroll, true)
}

function onDocClick(e) {
  if (wrapEl.value && !wrapEl.value.contains(e.target)) {
    close()
  }
}

function onDocScroll(e) {
  // 忽略下拉浮层自身的滚动（max-height + overflow-y），否则一滚就关闭
  if (menuEl.value && menuEl.value.contains(e.target)) {
    return
  }
  if (open.value) {
    close()
  }
}

function choose(opt) {
  emit('update:modelValue', opt)
  close()
}

onBeforeUnmount(close)
</script>

<style scoped>
.ui-select {
  position: relative;
}

.ui-select__trigger {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  box-sizing: border-box;
  background: var(--bg-primary);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 10px 12px;
  color: var(--text-primary);
  font-size: 13px;
  cursor: pointer;
  transition: border-color 0.15s;
}

.ui-select__trigger:hover {
  border-color: var(--text-muted);
}

.ui-select__value {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ui-select__value.placeholder {
  color: var(--text-muted);
}

.ui-select__arrow {
  color: var(--text-muted);
  font-size: 12px;
  flex-shrink: 0;
  transition: transform 0.2s;
}

.ui-select__arrow.open {
  transform: rotate(180deg);
}

.ui-select__menu {
  position: fixed;
  z-index: 1000;
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 4px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  max-height: 240px;
  overflow-y: auto;
}

.ui-select__option {
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 13px;
  color: var(--text-primary);
  cursor: pointer;
  transition: background 0.12s;
}

.ui-select__option:hover {
  background: var(--bg-hover);
}

.ui-select__option.selected {
  background: var(--accent-muted);
  color: var(--accent);
}

.ui-select__empty {
  padding: 12px;
  font-size: 12px;
  color: var(--text-muted);
  text-align: center;
}

.ui-select-enter-active,
.ui-select-leave-active {
  transition: opacity 0.12s;
}

.ui-select-enter-from,
.ui-select-leave-to {
  opacity: 0;
}
</style>
