<template>
  <div class="UserAdmin">
    <a-card>

      <div class="UserAdmin-header">
        <a-button @click="visabled = true" type="primary">新增</a-button>
      </div>

      <a-table :columns="columns" :data-source="userList" :loading="loading">
        <template slot="state" slot-scope="text, record">
          {{ record.state ? '使用中' : '冻结' }}
        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="authOpration(record)"> {{ record.state ? '冻结' : '使用' }}</a>
          <a-divider type="vertical" />
          <a>Delete</a>
          <a-divider type="vertical" />
          <a class="ant-dropdown-link"> More actions <a-icon type="down" /> </a>
        </span>
      </a-table>
    </a-card>
    <a-modal width="600px" :loading="loading" v-model="visabled" title="子账户管理" @ok="handleOk">
      <a-form :form="form" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }" @submit="handleSubmit">
        <a-form-item label="昵称">
          <a-input
            v-decorator="[
              'nickName',
              {rules: [{ required: true, message: '请输入昵称' }], validateTrigger: 'blur'}
            ]"/>
        </a-form-item>
        <a-form-item label="登录账号">
          <a-input
            :addon-before="authSubPre"
            placeholder="输入字母等后缀 最多十位"
            :maxLength="10"
            v-decorator="[
              'userCode',
              {rules: [{ required: true, message: '请输入登录账号'}], validateTrigger: 'blur'}
            ]"
          />
        </a-form-item>
        <a-form-item label="登录密码">
          <a-input-password
            size="large"
            placeholder="请输入密码"
            v-decorator="[
              'password',
              {rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}
            ]"
          >
            <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
          </a-input-password></a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import md5 from 'md5'
import { getAuthList, getAuthSubPre, postAuth, authOpration } from '@/api/user'
const columns = [
  {
    dataIndex: 'nickName',
    key: 'nickName',
    title: '登录名称'
  },
  {
    dataIndex: 'createTime',
    key: 'createTime',
    title: '创建时间'
  },
  {
    dataIndex: 'state',
    key: 'state',
    title: '使用状态',
    scopedSlots: { customRender: 'state' }
  },
  {
    title: 'Action',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]

export default {
  name: 'UserAdmin',
  created () {
      this.getAuthList()
      this.getAuthSubPre()
  },
  data () {
      return {
        columns,
        visabled: false,
        form: this.$form.createForm(this, { name: 'coordinated' }),
        userList: [],
        authSubPre: '',
        loading: false
      }
  },
  methods: {
    // 子账号冻结/解冻
    async authOpration (record) {
        console.log(record)
        this.loading = true
      const { data, code } = await authOpration(record.id)
      if (code === 200) {
        console.log(data)
        await this.getAuthList()
        this.$message.success('操作成功')
      }
      this.loading = false
    },
    //   handleOk
    handleOk () {
        this.handleSubmit()
    },

    // 提交表单
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values)
          const authParams = { ...values }
          authParams.password = md5(authParams.password)
          authParams.userCode = this.authSubPre + authParams.userCode
          this.postAuth(authParams)
        }
      })
    },
    // 设置子账号
    async postAuth (authParams) {
        this.loading = true
      const { data, code } = await postAuth(authParams)
      if (code === 200) {
          console.log(data)
          this.$message.success('添加成功')
          this.getAuthList()
      }
      this.loading = false
      this.visabled = false
    },
    // 获取子账号前缀
    async getAuthSubPre () {
      const { data, code } = await getAuthSubPre()
      if (code === 200) {
          console.log(data)
          this.authSubPre = data
      }
    },
    async getAuthList () {
      const { data, code } = await getAuthList()
      if (code === 200) {
          console.log(data)
          this.userList = data
      }
    }
  }
}
</script>
<style scoped lang="less">
.UserAdmin {
    .UserAdmin-header {
        margin-bottom: 10px;
    }
}
</style>
