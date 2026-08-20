<template>
  <UiSelect
    :model-value="value"
    :options="ctx.datasetOptions"
    placeholder="请先选择逻辑节点"
    empty-label="（不选）"
    @update:model-value="value = $event"
  />
</template>

<script setup>
import { computed } from 'vue'
import UiSelect from '../../ui/UiSelect.vue'
import { useFormCtx } from '../formContext.js'

const props = defineProps({ param: { type: Object, required: true } })
const ctx = useFormCtx()

const value = computed({
  get: () => ctx.form[props.param.key],
  set: (v) => (ctx.form[props.param.key] = v),
})
</script>