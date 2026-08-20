<template>
  <div class="field" :class="{ 'switch-field': isSwitch }">
    <label class="field-label">{{ param.label }}</label>
    <component :is="comp" :param="param" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import FieldText from './fields/FieldText.vue'
import FieldSwitch from './fields/FieldSwitch.vue'
import FieldSelect from './fields/FieldSelect.vue'
import FieldLdSelect from './fields/FieldLdSelect.vue'
import FieldApPicker from './fields/FieldApPicker.vue'
import FieldCascade from './fields/FieldCascade.vue'
import FieldLnRefSelect from './fields/FieldLnRefSelect.vue'
import FieldDatasetSelect from './fields/FieldDatasetSelect.vue'
import FieldDsRef from './fields/FieldDsRef.vue'
import FieldDsMemberAfter from './fields/FieldDsMemberAfter.vue'
import FieldValuesList from './fields/FieldValuesList.vue'
import FieldCbSelect from './fields/FieldCbSelect.vue'
import FieldSgcbNum from './fields/FieldSgcbNum.vue'
import FieldRefsList from './fields/FieldRefsList.vue'

/** 参数 type → 字段组件 的注册表。 */
const FIELDS = {
  text: FieldText,
  number: FieldText,
  switch: FieldSwitch,
  'auto-pull-switch': FieldSwitch,
  select: FieldSelect,
  'ld-select': FieldLdSelect,
  'ap-select': FieldApPicker,
  'ln-cascade': FieldCascade,
  'ln-ref-select': FieldLnRefSelect,
  'dataset-select': FieldDatasetSelect,
  'ds-ref-input': FieldDsRef,
  'ds-member-after': FieldDsMemberAfter,
  'values-list': FieldValuesList,
  'cb-select': FieldCbSelect,
  'sgcb-num': FieldSgcbNum,
  'refs-list': FieldRefsList,
}

const props = defineProps({ param: { type: Object, required: true } })

const comp = computed(() => FIELDS[props.param.type])
const isSwitch = computed(() => ['switch', 'auto-pull-switch'].includes(props.param.type))
</script>

<style scoped>
.field {
  flex: 1;
  min-width: 0;
}

.switch-field {
  display: flex;
  align-items: center;
  justify-content: space-between;
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

.switch-field .field-label {
  margin-bottom: 0;
}
</style>