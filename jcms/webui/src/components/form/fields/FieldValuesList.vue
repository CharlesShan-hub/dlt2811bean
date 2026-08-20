<template>
  <ValuesListEditor :model-value="value" :members="ctx.dsMemberOptions" @update:model-value="value = $event" />
</template>

<script setup>
import { computed } from 'vue'
import ValuesListEditor from '../../ValuesListEditor.vue'
import { useFormCtx } from '../formContext.js'

const props = defineProps({ param: { type: Object, required: true } })
const ctx = useFormCtx()

/** values-list 是数组，ValueListEditor 已内置值编辑弹窗。 */
const value = computed({
  get: () => ctx.form[props.param.key],
  set: (v) => (ctx.form[props.param.key] = v),
})
</script>