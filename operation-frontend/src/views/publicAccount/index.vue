<template>
  <div class="PublicAccount">

    <a-card>
      <div class="add-punlicAccount">
        <a-button
          @click="getAccountPreAuth"
          type="primary"
          icon="plus" >添加公众号</a-button>
      </div>
      <a-table :columns="columns" :data-source="publicAccountData">
        <span slot="publicAccount" slot-scope="text, record">
          <img :src="record.headImage" alt="">
          {{ record.nickName }}
        </span >
        <span slot="accType" slot-scope="text, record">
          {{ record.serviceTypeInfo.id === 2? '服务号' : '订阅号' }}
        </span >
        <span slot="verifyType" slot-scope="text, record">
          {{ record.verifyTypeInfo }}
        </span >
        <span slot="action" slot-scope="text, record">
          <a-popconfirm
            title="确定要删除吗？"
            ok-text="Yes"
            cancel-text="No"
            @confirm="deleteAccount(record.id)"
          >
            <a>删除</a>
          </a-popconfirm>
        </span >
      </a-table>
    </a-card>
  </div>
</template>

<script>
import { getAccount, deleteAccount, getAccountPreAuth } from '@/api/publicAccount'
const verifyTypeInfo = ['未认证', '微信认证', '新浪微博认证', '腾讯微博认证', '已资质认证通过但还未通过名称认证', '已资质认证通过、还未通过名称认证，但通过了新浪微博认证', '已资质认证通过、还未通过名称认证，但通过了腾讯微博认证']
const columns = [
  {
    title: '公众号',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'publicAccount' }
  },
  {
    title: '账号类型',
    dataIndex: 'serviceTypeInfo',
    key: 'serviceTypeInfo',
    scopedSlots: { customRender: 'accType' }
  },
  {
    title: '认证状态',
    dataIndex: 'verifyTypeInfo',
    key: 'verifyTypeInfo',
    scopedSlots: { customRender: 'verifyType' }
  },
  {
    title: '粉丝数',
    dataIndex: 'fansNum',
    key: 'fansNum'
  },
  {
    title: '互动粉丝数',
    dataIndex: 'interactFansNum',
    key: 'interactFansNum'
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  name: 'PublicAccount',
  created () {
    this.getAccount()
  },
  data () {
    return {
      columns,
      publicAccountData: [],
      verifyTypeInfo
    }
  },
  methods: {

    // 获取预授权码地址，微信认证公众号
    async getAccountPreAuth () {
      const { data, code } = await getAccountPreAuth()
      if (code === 200) {
        console.log(data)
        window.open(data)
      }
    },
    // 删除微信公众号
    async deleteAccount () {
     const { code } = await deleteAccount()
     if (code === 200) {
       this.$message('删除成功')
       this.getAccount()
     }
    },
    // 查询公众号列表
    async getAccount () {
      const { data, code } = await getAccount()
      if (code === 200) {
        console.log(data)
        this.publicAccountData = data.map(item => {
          item.serviceTypeInfo = JSON.parse(item.serviceTypeInfo)
          item.verifyTypeInfo = this.verifyTypeInfo[JSON.parse(item.verifyTypeInfo).id -1 ]
          return item
        })
      }
    }
  }
}
</script>
<style scoped lang="less">
.PublicAccount {
  .add-punlicAccount {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-bottom: 20px;
  }
  img {
    width: 30px;
    height: 30px;
    object-fit: cover;
  }
}
</style>
