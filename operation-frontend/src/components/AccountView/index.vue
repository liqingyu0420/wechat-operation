<template>
  <div class="AccountView">
    <!--  @click="$refs['account-list'].openModal()" -->
    <div class="AccountView-box cp">
      <a-tooltip placement="top" :title=" accountInfo.nickName || ''">
        <img :style="{border:!! accountInfo.headImage? '': '1px solid #666666'}" class="account-img" :src="accountInfo.headImage || ''" alt="">
      </a-tooltip>

      <!-- <a-icon type="caret-down" /> -->
    </div>

    <account-list ref="account-list" @select="select"/>
  </div>
</template>

<script>
import AccountList from '@/components/AccountList'
import { mapGetters } from 'vuex'
export default {
  name: 'AccountView',
  components: {
      AccountList
  },
  computed: {
    ...mapGetters(['account'])
  },
  props: {
      accountId: {
          type: String,
          default: ''
      }
  },
  watch: {
    accountId: {
      handler () {
         this.accountInfo = this.account.filter(item => {
          return Number(item.id) === Number(this.accountId)
        })[0] || {}
      },
      immediate: true
    }
  },
  data () {
    return {
      accountInfo: {}
    }
  },
  methods: {
      select (account) {
          this.$emit('selectAccount', account)
      }
  }
}
</script>
<style scoped lang="less">
.AccountView {
    .AccountView-box {
        width: 80px;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .account-img {
            width: 40px;
            height: 40px;

            object-fit: cover;
            border-radius: 10px;
        }
    }

}
</style>
