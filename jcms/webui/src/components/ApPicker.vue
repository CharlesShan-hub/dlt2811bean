<template>
  <div class="ap-picker">
    <div class="field">
      <label class="field-label">AP 来源</label>
      <UiSegmented
        :model-value="source"
        :options="sourceOptions"
        @update:model-value="onSourceChange"
      />
    </div>

    <div class="field">
      <label class="field-label">访问点</label>
      <UiSelect
        v-model="ap"
        :options="apOptions"
        :loading="loadingAps"
        placeholder="请选择 AP"
      />
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import UiSegmented from './ui/UiSegmented.vue'
import UiSelect from './ui/UiSelect.vue'
import { executeJson } from '../api/cms.js'

const props = defineProps({
  /** 当前选中的访问点（IED/AP 引用） */
  modelValue: String,
})

const emit = defineEmits(['update:modelValue'])

const sourceOptions = [
  { value: 'scd', label: '从 SCD 文件读' },
  { value: 'list', label: '从静态列表读' },
]

const source = ref('')
const apOptions = ref([])
const loadingAps = ref(false)

const ap = computed({
  get: () => props.modelValue || '',
  set: (v) => emit('update:modelValue', v),
})

/** 读取当前 AP 来源配置并刷新下拉。 */
async function loadConfig() {
  try {
    const res = await executeJson('ap --cfg --json')
    if (res && typeof res.fromScd === 'boolean') {
      source.value = res.fromScd ? 'scd' : 'list'
    }
  } catch {
    // 配置读取失败时保留默认值
  }
  await refreshAps()
}

/** 按当前来源刷新 AP 下拉选项。 */
async function refreshAps() {
  const prev = props.modelValue
  loadingAps.value = true
  let options = []
  try {
    if (source.value === 'list') {
      // list 模式：ap --cfg --json 返回 {fromScd, defaultAps}
      const res = await executeJson('ap --cfg --json')
      if (res && Array.isArray(res.defaultAps)) {
        options = res.defaultAps
      }
    } else {
      // scd 模式：ap --list --json 返回 {source, accessPoints: ["IED/AP", ...]}
      const res = await executeJson('ap --list --json')
      if (res && Array.isArray(res.accessPoints)) {
        options = res.accessPoints
      }
    }
  } catch {
    // 拉取失败时保持空选项
  }
  apOptions.value = options
  const next = options.includes(prev) ? prev : (options[0] || '')
  if (next !== props.modelValue) {
    emit('update:modelValue', next)
  }
  loadingAps.value = false
}

/** 切换加载方式：赋值 → 保存 → 刷新下拉。 */
async function onSourceChange(s) {
  if (source.value === s) {
    return
  }
  source.value = s
  await executeJson(`ap --cfg --source ${s} --json`)
  await refreshAps()
}

onMounted(loadConfig)
</script>

<style scoped>
.field {
  margin-bottom: 20px;
}

.field-label {
  display: block;
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.field-label::before {
  content: '';
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--red);
  margin-right: 7px;
  vertical-align: middle;
  box-shadow: 0 0 4px rgba(229, 85, 90, 0.6);
}
</style>
