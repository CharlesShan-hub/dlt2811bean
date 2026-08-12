// 8.5.3 创建数据集（CreateDataSet）

export default {
  title: '创建数据集 create-dataset (8.5.3)',
  desc: '动态创建新的数据集或追加成员',
  params: [
    { key: 'ds', label: '数据集引用', type: 'text', required: true, placeholder: 'LD/LN.dsName，如 PROT/LLN0.dsNewDs' },
    { key: 'members', label: '成员列表', type: 'text', required: true, placeholder: '如 "PROT/GGIO1.ST.Ind1,ST PROT/GGIO1.MX.AnIn1,MX"' },
    { key: 'after', label: '分页游标 after', type: 'text', required: false, placeholder: '（可选）从该引用之后继续创建' },
  ],
}