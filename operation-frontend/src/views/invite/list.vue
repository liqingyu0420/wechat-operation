<template>
  <div class="InviteAgent">
    <a-card>

      <a-row class="invite-btn">
        <a-col :md="12" :sm="24">
          <a-button type="primary" @click="visible = true"> <a-icon type="plus"></a-icon>新增代理商</a-button>
        </a-col>
      </a-row>

      <a-table
        :columns="columns"
        :data-source="inviteList"
        :pagination="pagination"
      >
        <span slot="gender" slot-scope="gender">{{ gender === 1? '男' : '女' }} </span>
        <span slot="region" slot-scope="region">{{ region.map(item => item.name).join('') }} </span>
        <span slot="action" slot-scope="text, record">
          <a @click="editInvite(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="getInviteCode(record)">开园邀请码</a>
          <a-divider type="vertical" />

          <a-popconfirm
            title="确认删除此条数据？"
            @confirm="deleteInvite(record)"
            okText="Yes"
            cancelText="No">
            <a href="javascript:;">删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </a-card>
    <a-modal destroyOnClose v-model="visible" title="代理商信息" @ok="handleOk">
      <Add ref="add" @success="success"/>
    </a-modal>

    <a-modal
      destroyOnClose
      v-model="inviteVisible"
      title="开园邀请码"
      @ok="() => {inviteVisible = false, inviteCode = ''}"
      @cancel="() => {inviteVisible = false, inviteCode = ''}"
    >

      <img :src="inviteCode" alt="">
    </a-modal>

  </div>
</template>

<script>
import { getInvite, deleteInvite, getInviteCode } from '@/api/agent.js'
import Add from './add.vue'
const columns = [

  {
    title: '姓名',
    dataIndex: 'name'
  },
  {
    title: '性别',
    dataIndex: 'gender',
    scopedSlots: { customRender: 'gender' }
  },
  {
    title: '手机号',
    dataIndex: 'phone'
  },
  {
    title: '所在地区',
    dataIndex: 'region',
    scopedSlots: { customRender: 'region' }
  },
  {
    title: '详细地址',
    dataIndex: 'address'
  },
  {
    title: '申请时间',
    dataIndex: 'createTime'
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  name: 'InviteAgent',
  components: {
    Add
  },
  created () {
    this.getInvite()
  },
  data () {
    return {
      inviteVisible: false, // 入园邀请码弹窗
      inviteCode: '', // 邀请码
      visible: false,
      inviteList: [],
      pagination: {
        showSizeChanger: true,
        showSizeChange: (current, pageSize) => { this.pageSize = pageSize },
        pageSizeOptions: ['1', '5', '10', '20', '30', '40', '50'],
        pageSize: 10,
        total: 0,
        showTotal: (total) => { return `共 ${total} 条` }
      },
      columns
    }
  },
  methods: {
    // 打开邀请码
    async getInviteCode (record) {
      const inviteCode = await getInviteCode({ agentId: record.id })
      console.log(inviteCode)
      this.inviteVisible = true

      const blob = new Blob([inviteCode], { type: 'imageType' })
      const imageUrl = (window.URL || window.webkitURL).createObjectURL(blob)
      this.inviteCode = imageUrl
    },
    // 新增或修改成功
    success () {
      this.visible = false
      this.getInvite()
    },
    // 编辑表单
    editInvite (record) {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.add.getInviteDetail(record.id)
      })
    },
    // 确定提交表单
    handleOk () {
      this.$refs.add.handleSubmit()
    },
    // 删除邀请商
    async deleteInvite (record) {
      const { status } = await deleteInvite(record.id)
      if (status === 200) {
        this.$message.success('删除成功')
        this.getInvite()
      }
    },
    // 获取列表数据
    async getInvite () {
      console.log('hello')
      const { data, status } = await getInvite(this.queryParam)
      console.log(data, status)
      if (status === 200) {
        const { entityList } = data
        this.inviteList = entityList
      }
    }
  }
}
</script>
<style scoped lang="less">
.headImg {
  width: 40px;
  height: 40px;
  display: block;
  border-radius: 50%;
}

.invite-btn {
  margin-bottom: 20px;
}
</style>
