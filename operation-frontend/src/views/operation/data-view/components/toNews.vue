<template>
  <div class="toNews">

    <div class="choose-pro-radio">
      <a-radio-group button-style="solid" :default-value="1" @change="e => { currentRadio = e && e.target.value || 1}">
        <a-radio-button :value="1">互动时段</a-radio-button>
        <a-radio-button :value="2">互动类型</a-radio-button>
      </a-radio-group>
    </div>
    <div class="box-search">
      <range-picker :defaultValue="currentTime" @changeDate="changeDate"/>
    </div>
    <div class="data-view-box">
      <div class="data-view-title">{{ currentRadio === 1 ? '收到消息时段分布图' :'互动类型分析' }}</div>
      <div class="data-view-content" v-if="currentRadio=== 1">
        <vue-chart style="width:100%;height:100%;" :autoresize="true" :options="toNewsOptions"></vue-chart>
      </div>
      <div v-if="currentRadio === 1">
        <div class="data-view-title table-list-data">详细数据</div>
        <div class="data-table">
          <a-table :columns="columns" :data-source="toNewsData" />
        </div>
      </div>

      <div class="data-view-content" v-if="currentRadio === 2">
        <vue-chart style="width:100%;height:100%;" :autoresize="true" :options="toNewsOptionsOther"></vue-chart>
      </div>
    </div>

  </div>
</template>

<script>
import MixinOperation from '@/mixin'
import { getToNews } from '@/api/operation'
import echartsOptions from './echartsOptions'
const { toNews, fansPro } = Object.freeze(echartsOptions)
const columns = [
  {
    title: '时间段',
    dataIndex: 'dateLabel',
    key: 'dateLabel'
  },
  {
    title: '关注',
    dataIndex: 'subscribe',
    key: 'subscribe'
  },
  {
    title: '取关',
    dataIndex: 'unSubscribe',
    key: 'unSubscribe'
  },
  {
    title: '扫描二维码',
    dataIndex: 'scan',
    key: 'scan'
  },
  {
    title: '菜单点击',
    dataIndex: 'menu',
    key: 'menu'
  },
  {
    title: '总数',
    dataIndex: 'total',
    key: 'total'
  }
]
const formType = [

  '粉丝消息占比',

  '关注占比',

  '取关占比',

  '扫描二维码占比',

  '点击菜单占比'

]
export default {
  name: 'ToNews',
  mixins: [MixinOperation.Operation],
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  created () {
    this.getToNews()
    this.handleChooseSearch('W')
  },
  watch: {
    accountId () {
      this.handleChooseSearch('W')
    }
  },
  data () {
    return {
      columns,
      toNewsOptions: toNews,
      toNewsOptionsOther: fansPro,
      currentRadio: 1,
      toNewsData: [], // table数据
      formType // 来源类型
    }
  },
  methods: {
     // 时间切换
    changeDate (dates) {
      this.currentTime = dates
      this.getToNews()
    },
     /** 选择模式 2020-05-20 */
      handleChooseSearch (type) {
        let num = 1
        switch (type) {
          case 'H':
            num = 1
            break
          case 'D':
            num = 1
            break
          case 'W':
            num = 7
            break
          case 'M':
            num = 30
            break
        }
        this.initCuttentTime(num)
        // this.search.type = type
        this.getToNews()
      },
    // 获取互动消息数据
    async getToNews (e) {
      const { currentTime: [ startDate, endDate ], accountId } = this

      const { data, code } = await getToNews(accountId, { startDate, endDate })
      if (code === 200) {
        console.log(data)
        this.receiveMsgEcharts(data.list)
        this.formTypeEcharts(data.type)
        this.toNewsData = data.list
      }
    },
    // 收到消息时段分布图
    receiveMsgEcharts (res) {
      // 時間
      const time = []
      // 菜单
      const menu = []
      // 扫码
      const scan = []
      // 关注
      const subscribe = []
      // 取关
      const unSubscribe = []
      // 粉丝消息
      const msg = []
      res.map(item => {
        time.push(item.dateLabel)
        menu.push(item.menu)
        scan.push(item.scan)
        subscribe.push(item.subscribe)
        unSubscribe.push(item.unSubscribe)
        msg.push(item.msg)
      })
      // 粉丝消息
      this.toNewsOptions.series[0].data = msg
      // 关注
      this.toNewsOptions.series[1].data = subscribe
      // 取关
      this.toNewsOptions.series[2].data = unSubscribe
      // 扫描二维码
      this.toNewsOptions.series[3].data = scan
      // 菜单点击
      this.toNewsOptions.series[4].data = menu
      // 時間
      this.toNewsOptions.xAxis[0].data = time
    },
      // 来源类型分布
    formTypeEcharts (res) {
      this.toNewsOptionsOther.series[0].data = Object.keys(res).map(key => {
        return {
          name: this.formType[key],
          value: res[key]
        }
      })
    }
  }

}
</script>
<style scoped lang="less">
@import './config-style';
.toNews {
  border: 1px solid #d9d9d9;

}
</style>
