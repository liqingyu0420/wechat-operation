<template>
  <div class="DataView">
    <!-- 公众号 -->
    <div class="official-accounts tabs">
      <a-row >
        <a-col
          :xs="12"
          :sm="12"
          :md="4"
          :lg="4"
          :xl="4"
          :class="['official-accounts-item', 'cp', index === currentTabsIndex ? 'official-accounts-item-active' : '' ]"
          @click="currentTabs(index)"
          v-for="(item, index) in mulAccountList"
          :key="item.nickName">
          <img class="official-accounts-item-img" :src="item.headImage" alt="">  {{ item.nickName }}
        </a-col>
        <div class="pagination" v-if="accountList.length > 3">
          <a-icon type="left" @click="subAreaIndex"/>
          <a-icon type="right" @click="addAreaIndex"/>
        </div>

      </a-row>

    </div>
    <!-- 数据统计 -->
    <a-row class="statictis" :gutter="[10, 20]">
      <a-col
        :xs="12"
        :sm="12"
        :md="4"
        :lg="4"
        :xl="4"
        v-for="(item , index) in statistic"
        :key="index">
        <a-card hoverable>
          <div
            class="indicator-statistic"
          >
            <div class="indicator-statistic-item-label">
              {{ item.label || '' }}
            </div>
            <div class="indicator-statistic-item-count" :style="{ color: currentAccount[item.rate] < 0 ? 'rgb(207, 19, 34)' : '#52c41a' }">
              {{ currentAccount[item.count] || 0 }}
            </div>
            <div class="indicator-statistic-item-rate">
              {{ currentAccount[item.rate] || 0 }}%
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 粉丝属性 -->
    <div class="fans-property">
      <div class="fans-property-box">
        <div
          :class="['fans-property-box-item', proListIndex === index ? 'fans-property-box-item-active' : '']"
          @click="changeProperty(item, index)"
          v-for="(item, index) in proList"
          :key="index">
          {{ item.title }}
        </div>

      </div>
      <div class="fans-property-line">

      </div>
    </div>

    <!-- 异步请求的地图数据 -->
    <div class="card-content" v-if="accountList.length > 0">
      <component :accountId="accountId" :is="proList[proListIndex].componentsName"></component>
    </div>
  </div>
</template>

<script>
import { getAccount } from '@/api/wxchat'
import { getSingleSummary } from '@/api/operation'
import FansActive from './components/fansActive'
import fansAdd from './components/fansAdd'
import fansLoyal from './components/fansLoyal'
import fansPro from './components/fansPro'
import imageText from './components/imageText'
import toNews from './components/toNews'
const official = [
    {
        img: 'http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJ7PXOg8UQskrEjqSwywYib5UYqjyAdKTr1ickq03BDOg9xtWIk108SsfHzd6PIXERRkVQSj9pXKsmw/132',
        name: '逻辑狗'
    }
]
const proList = [
    {
          title: '粉丝增长',
          componentsName: 'fansAdd'
        },
        {
          title: '粉丝属性',
          componentsName: 'fansPro'
        },
        {
          title: '活跃度',
          componentsName: 'fansActive'
        },
        {
          title: '忠诚度',
          componentsName: 'fansLoyal'
        },
        {
          title: '图文影响力',
          componentsName: 'imageText'
        },
        {
          title: '互动消息',
          componentsName: 'toNews'
    }
]
const statistic = [
    {
        label: '昨日新增',
        count: 'addNum',
        rate: 'addRate'
    },
    {
        label: '昨日取关',
        count: 'cancelNum',
        rate: 'cancelRate'
    },
    {
        label: '昨日净增',
        count: 'newNum',
        rate: 'newRate'
    },
    {
        label: '昨日活跃',
        count: 'inactiveNum',
        rate: 'inactiveRate'
    },
    {
        label: '总粉丝',
        count: 'totalFansNum',
        rate: 'totalFansRate'
    }
]
export default {
  name: 'DataView',
  components: {
    FansActive,
    fansAdd,
    fansLoyal,
    fansPro,
    imageText,
    toNews
  },
  computed: {
    accountId () {
      return this.accountList[this.currentTabsIndex].id
    },
    mulAccountList () {
      return this.accountList.slice(this.areaIndex, this.areaIndex + 4)
    }
  },
  async created () {
    await this.getAccount()
    this.getSingleSummary()
  },
  data () {
      return {
          official,
          accountList: [], // 公众号列表数据
          areaAccount: [], // 部分账号数据
          total: 0, //  公众号数量
          maxAreaIndex: 0, // 公众号选择区域最大值
          areaIndex: 0, // 公众号选择区域
          currentTabsIndex: 0, // 当前选择公众号下表标
          currentAccount: {}, // 当前账户
          statistic,
          proList,
          proListIndex: 0
      }
  },
  methods: {
    subAreaIndex () {
      if (this.areaIndex === 0) {
        return
      }
      this.areaIndex -= 4
    },
    addAreaIndex () {
      if (this.areaIndex > this.total - 5) {
        return
      }
      this.areaIndex += 4
      this.$forceUpdate()
    },
      currentTabs (index) {
        this.currentTabsIndex = index
        this.getSingleSummary()
      },
      // 切换粉丝属性
      changeProperty (item, index) {
        this.proListIndex = index
      },
      // 查询当前公众号数据
      async getSingleSummary () {
       const { data, code } = await getSingleSummary(this.accountId)
        if (code === 200) {
          this.currentAccount = data || {}
        }
      },
      // 获取公众号列表
      async getAccount () {
        const { data, code } = await getAccount()
        if (code === 200) {
          console.log(data)
          this.total = data.length

          this.accountList = data
        }
      }
  }
}
</script>
<style scoped lang="less">
.DataView {
    width: 100%;
    // height: 500px;
    background-color: #fff;
    .official-accounts {
        width: 100%;
        background-color: #f0f2f5;
        border: 1px solid #d9d9d9;
        &-item {
            height: 40px;
            display: flex;
            justify-content: center;
            align-items: center;
            &-img {
                width: 30px;
                height: 30px;
                margin-right: 5px;
            }
        }
        &-item-active {
            background-color: #fff;
            box-sizing: border-box;
        }
        .pagination {
          height: 40px;
          display: flex;
          justify-content: flex-start;
          align-items: center;
          font-size: 18px;
          .anticon-left {
            margin-right: 10px;
          }
        }

    }
    .statictis {
        padding: 20px;
    }
    .fans-property {
      padding-left: 20px;
      &-box {
        display: flex;
        margin-bottom: -1px;
        &-item {
          width: 150px;
          height: 41px;
          text-align: center;
          line-height: 41px;
          border: 1px solid #d9d9d9;

          background: #f7f7f7;
          margin-right: 5px;
          cursor: pointer;
        }
        &-item-active {
          background-color: #fff;
             border-bottom: none;
        }
      }

      &-line {
        width: 100%;
        height: 1px;
        background-color: #d9d9d9;
      }
    }

    .card-content {
      padding: 20px;
    }
}

.pagination {
  position: absolute;
  left: 1120px;
}
</style>
