<template>
  <div>
    <a-card title="" :bordered="false" style="width: 100%">
      <a-result
        v-if="accountId"
        status="success"
        title="恭喜您授权成功微信公众号"
        sub-title="确认成功之后即将同步公众号粉丝数据、标签数据。公众号各类统计型数据将在明天凌晨开始从微信服务器同步 "
      >
        <template #extra>
          <a-card-meta :title="account.nickName" :description="account.principalName">
          </a-card-meta>
          <br/>
          <a-button key="console" type="primary" @click="confirmAccount">
            确认
          </a-button>
          <a-button key="buy" @click="black">
            取消
          </a-button>
        </template>
      </a-result>
      <a-result
        v-else
        status="error"
        title="授权微信公众号失败"
      >
        <template #extra>
          <a-button key="console" type="primary" @click="black">
            返回微信公号列表
          </a-button>
        </template>
      </a-result>
    </a-card>
  </div>
</template>

<script>
import { getAccountDetail, confirmAccount } from '@/api/publicAccount'
export default {
  name: 'Confirm',
  created () {
      const { accountId } = this.$route.query
      if (accountId) {
        this.accountId = accountId
        this.getAccountDetail()
      }
  },
  data () {
      return {
          accountId: '',
          account: {}
      }
  },
  methods: {
     async getAccountDetail () {
         const { data, code } = await getAccountDetail(this.accountId)
         if (code === 200) {
             this.account = data
         }
     },
    //  确认对微信号进行授权
     async confirmAccount () {
        const { data, code } = await confirmAccount({ accountId: this.accountId })
        if (code === 200) {
            console.log(data)
            this.black()
        }
     },
     black () {
       this.$router.replace('/publicAccAdmin')
     }
  }
}
</script>

<style lang="less" scoped>
</style>
