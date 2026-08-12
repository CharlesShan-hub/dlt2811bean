<template>
  <div class="col-right">
    <div v-if="showAsn1" class="split-top" :style="{ height: topHeight + 'px' }">
      <UiCard :title="rightTitle" icon="⛓" fill>
        <template #header>
          <span class="glass ui-card__toggle" title="隐藏 {{ rightTitle }}" @click="emit('update:showAsn1', false)">𝔄</span>
        </template>
        <!-- connect 是便捷封装命令：显示状态图而非 ASN.1 -->
        <StateDiagram v-if="isConnect" :states="connectFlow.states" :edges="connectFlow.edges" :active="activeState" />
        <p v-if="isConnect" class="tip">💡 <code>connect --ap</code> 自动完成上述三步；关联建立后即可使用各服务页面。</p>
        <template v-else>
          <Asn1Code v-if="def.asn1" :code="def.asn1" />
          <p v-else-if="def.desc" class="svc-note">{{ def.desc }}</p>
          <p v-if="def.note" class="svc-note">{{ def.note }}</p>
        </template>
      </UiCard>
    </div>

    <!-- 水平拖拽手柄：仅在 ASN.1 可见时显示 -->
    <div v-if="showAsn1" class="drag-h" @mousedown.prevent="emit('start-h-drag')"></div>

    <CmdResultCard
      :result="result"
      :json-format="jsonFormat"
      :highlighted-cmd="highlightedCmd"
      :highlighted-result-cmd="highlightedResultCmd"
      :formatted-json="formattedJson"
      :display-json="displayJson"
      :json-truncated="jsonTruncated"
      :json-expanded="jsonExpanded"
      :json-total-lines="jsonTotalLines"
      :json-fold-lines="jsonFoldLines"
      :output-lines="outputLines"
      :preview-cmd="previewCmd"
      :result-cmd="result?.cmd ?? ''"
      @update:json-format="emit('update:json-format', $event)"
      @edit="emit('edit', $event)"
      @expand-json="emit('expand-json')"
      @collapse-json="emit('collapse-json')"
    />
  </div>
</template>

<script setup>
import StateDiagram from './StateDiagram.vue'
import Asn1Code from './Asn1Code.vue'
import CmdResultCard from './CmdResultCard.vue'
import UiCard from './ui/UiCard.vue'

defineProps({
  def: Object,
  result: Object,
  showAsn1: Boolean,
  isConnect: Boolean,
  activeState: String,
  rightTitle: String,
  connectFlow: Object,
  topHeight: Number,
  jsonFormat: Boolean,
  formattedJson: String,
  displayJson: String,
  jsonTruncated: Boolean,
  jsonExpanded: Boolean,
  jsonTotalLines: Number,
  jsonFoldLines: Number,
  outputLines: Array,
  highlightedCmd: String,
  highlightedResultCmd: String,
  previewCmd: String,
})

const emit = defineEmits([
  'update:showAsn1',
  'update:json-format',
  'edit',
  'start-h-drag',
  'expand-json',
  'collapse-json',
])
</script>

<style scoped>
.col-right {
  min-height: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-right: 6px;
}

/* 右列上半部分（ASN.1） */
.split-top {
  flex-shrink: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* 水平拖拽手柄 */
.drag-h {
  height: 6px;
  cursor: row-resize;
  background: transparent;
  flex-shrink: 0;
  position: relative;
  z-index: 5;
  transition: background 0.15s;
}
.drag-h:hover,
.drag-h:active {
  background: var(--accent);
}

.ui-card__toggle {
  margin-left: auto;
  cursor: pointer;
  font-size: 14px;
  color: var(--text-secondary);
  transition: all 0.2s;
  user-select: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
  border-radius: 6px;
}
.ui-card__toggle:hover {
  color: var(--text-primary);
}

.tip {
  font-size: 13px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 12px 0;
}

.svc-note {
  font-size: 13px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 12px 0;
}
</style>