<template>
  <SvcPage section="8.6" title="定值组" chip="Setting Group" desc="选择激活/编辑定值组、读写定值、确认提交">
    <!-- 工作流状态图 -->
    <UiCard title="工作流" icon="⚙">
      <StateDiagram :states="sgFlow.states" :edges="sgFlow.edges" />
      <p class="tip">
        <code>set-edit-sg</code> / <code>get-edit-sg</code> 可在「编辑中」状态下多次调用；
        <code>sgcb-vals</code> 可在任意状态下读取 SGCB 属性。
      </p>
    </UiCard>

    <!-- 完整操作示例 -->
    <UiCard title="完整工作流示例" icon="📋">
      <div class="workflow-example">
        <p class="step-label">1. 连接并选择编辑组</p>
        <pre class="cmd-block">cms connect --ap P_B5041A/S1
cms select-edit-sg --ref "PROT/DeZonePTOC1.SG1" --num 1</pre>
        <p class="step-label">2. 写入定值</p>
        <pre class="cmd-block">cms set-edit-sg --refs "PROT/OCPTOC2.StrVal" --values "100" --type int32</pre>
        <p class="step-label">3. 读取验证</p>
        <pre class="cmd-block">cms get-edit-sg --refs "PROT/OCPTOC2.StrVal"</pre>
        <p class="step-label">4. 提交生效</p>
        <pre class="cmd-block">cms confirm-edit-sg --ref "PROT/DeZonePTOC1.SG1"</pre>
        <p class="step-label">5. 激活定值组</p>
        <pre class="cmd-block">cms select-active-sg --ref "PROT/DeZonePTOC1.SG1" --num 1</pre>
      </div>
    </UiCard>
  </SvcPage>
</template>

<script setup>
import SvcPage from '../components/SvcPage.vue'
import StateDiagram from '../components/StateDiagram.vue'
import UiCard from '../components/ui/UiCard.vue'
import { SG_FLOW } from '../cmddefs/sg/flow.js'

const sgFlow = SG_FLOW
</script>

<style scoped>
.tip {
  margin-top: 12px;
  font-size: 13px;
  color: var(--text-secondary);
  line-height: 1.6;
}
.tip code {
  font-size: 12px;
  background: rgba(91, 141, 239, 0.12);
  padding: 1px 6px;
  border-radius: 4px;
  color: var(--accent);
}

.workflow-example {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.step-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.cmd-block {
  margin: 0 0 4px 0;
  padding: 10px 14px;
  background: rgba(0, 0, 0, 0.06);
  border-radius: 8px;
  font-size: 13px;
  font-family: 'Cascadia Code', 'Fira Code', 'Consolas', monospace;
  line-height: 1.7;
  overflow-x: auto;
  white-space: pre;
  color: var(--text-primary);
}
</style>