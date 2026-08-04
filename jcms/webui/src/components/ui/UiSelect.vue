<template>
  <div ref="wrapEl" class="ui-select">
    <button type="button" class="ui-select__trigger" :class="{ disabled }" :disabled="disabled" @click="toggle">
      <span class="ui-select__value" :class="{ placeholder: !currentLabel }">
        <span v-if="currentColor" class="ui-select__dot" :style="{ background: currentColor }"></span>
        {{ currentLabel || (loading ? '加载中...' : placeholder) }}
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
          :key="optKey(opt)"
          class="ui-select__option"
          :class="{ selected: optValue(opt) === modelValue }"
          @click="choose(opt)"
        >
          <span v-if="optColor(opt)" class="ui-select__dot" :style="{ background: optColor(opt) }"></span>
          {{ opt === '' ? emptyLabel : optLabel(opt) }}
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onBeforeUnmount } from 'vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  /** 字符串选项列表，或对象选项 { value, label, color }（color 为可选圆点颜色） */
  options: { type: Array, default: () => [] },
  placeholder: { type: String, default: '请选择' },
  /** 空选项（''）显示的标签，用于表达"不选" */
  emptyLabel: { type: String, default: '' },
  loading: Boolean,
  /** 禁用：仅展示当前选中项，不可打开选择（如协议固定值的参数） */
  disabled: Boolean,
})

const emit = defineEmits(['update:modelValue'])

const open = ref(false)
const wrapEl = ref(null)
const menuEl = ref(null)
const menuStyle = ref({})

/** 选项值：对象取 value 字段，字符串取自身。 */
function optValue(opt) {
  return typeof opt === 'object' && opt !== null ? opt.value : opt
}
function optLabel(opt) {
  return typeof opt === 'object' && opt !== null ? opt.label : opt
}
function optColor(opt) {
  return typeof opt === 'object' && opt !== null ? opt.color : ''
}
function optKey(opt) {
  return typeof opt === 'object' && opt !== null ? opt.value : opt
}

/** 当前选中项（用于触发按钮上显示彩色圆点 + 标签）。 */
const currentOpt = computed(() => props.options.find((o) => optValue(o) === props.modelValue))
const currentLabel = computed(() => {
  if (!props.modelValue) return ''
  const o = currentOpt.value
  return o === undefined ? String(props.modelValue) : optLabel(o)
})
const currentColor = computed(() => {
  const o = currentOpt.value
  return o !== undefined ? optColor(o) : ''
})

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
  emit('update:modelValue', optValue(opt))
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

.ui-select__trigger.disabled,
.ui-select__trigger:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.ui-select__trigger.disabled:hover,
.ui-select__trigger:disabled:hover {
  border-color: var(--border);
}

.ui-select__value {
  display: flex;
  align-items: center;
  gap: 6px;
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
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 13px;
  color: var(--text-primary);
  cursor: pointer;
  transition: background 0.12s;
}

.ui-select__dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
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
