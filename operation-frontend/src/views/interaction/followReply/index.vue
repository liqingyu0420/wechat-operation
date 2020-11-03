<template>
  <div class="PassivityReply">
    <!-- :gutter="[18, 16]" -->
    <a-row >
      <a-col
        v-for="item in followReplyList"
        :key="item.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="8"
        :xl="6"
      >
        <action-card

          :item-data="item"
          @jumpEditReply="jumpEditReply"
          @enableInteraction="enableInteraction"
        />
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { getFollowReply, enableInteraction } from '@/api/interaction'
import ActionCard from '../components/ActionCard'
export default {
  name: 'PassivityReply',
  components: {
    ActionCard
  },
  created () {
    this.getFollowReply()
  },
  data () {
    return {
      followReplyList: []
    }
  },
  methods: {
    // 开启或者关闭被关注回复
    async enableInteraction ($par) {
      const { enableVal, id } = $par
      const { data, code } = await enableInteraction({ enableVal }, id)
      if (code === 200) {
        console.log(data)
        this.$message.success('修改成功')
        this.followReplyList = this.followReplyList.map(item => {
          if (item.id === id) {
            item.enable = enableVal
          }
          return item
        })
      }
    },
    // 跳转编辑回复页
    jumpEditReply (record) {
      const { accountId, id } = record
      console.log(record)
      this.$router.push({
        path: '/interaction/editReply',
        query: { accountId, id }
      })
    },
    // 获取被关注回复列表
    async getFollowReply () {
      const { data, code } = await getFollowReply()
      if (code === 200) {
        console.log(data)
        this.followReplyList = data
      }
    }
  }
}
</script>
<style scoped lang="less">
.PassivityReply {
  // display: grid;
  // grid-template-columns: auto auto auto auto;
  // grid-template-rows: auto auto auto auto;
}
</style>
