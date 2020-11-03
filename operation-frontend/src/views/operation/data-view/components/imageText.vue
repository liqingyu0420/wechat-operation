<template>
  <div class="imageText">
    <div class="box-search">
      <a-button
        :type="item.type === search.type ? 'primary' : ''"
        v-for="(item,index) in searchTypeList"
        :key="index"
        @click="handleChooseSearch(item.type)">
        {{ item.title }}
      </a-button>
      <range-picker :key="Math.random()" :defaultValue="currentTime" @changeDate="changeDate"/>
    </div>
    <div class="box-desc">
      这段时间里，共有 <span>{{ fansImgText.totalDispatchCount || '--' }}</span> 篇文章，阅读人数 <span>{{ fansImgText.totalReadCount || '--' }}</span>，分享人数
      <span>{{ fansImgText.totalShareCount || '--' }}</span>，数据不去重
    </div>
    <div class="data-view-box">
      <div class="data-view-title">粉丝数据趋势</div>
      <div class="data-view-content">
        <vue-chart style="width:100%;height:100%;" :autoresize="true" :options="fansImgTextOptions"></vue-chart>
      </div>
      <div class="choose-pro-radio">
        <!-- v-model="showType" -->
        <a-button :type="showType === 1 ? 'primary' : ''" @click="showType = 1"> 数据总览 </a-button>
        <a-button v-if="detailList.length > 0" :type="showType !== 1 ? 'primary' : ''" @click="showType = 2">{{ detailList[0].statDate }} 数据详情 </a-button>
        <!-- <el-radio-group size="small" >
          <el-radio-button :label="1">数据总览</el-radio-button>
          <el-radio-button v-if="info['date']" :label="2">{{ info['date'] }}数据详情</el-radio-button>
        </el-radio-group> -->
      </div>
      <!--      <div class="data-view-title table-list-data">详细数据</div>-->
      <!-- v-if="showType === 1" -->
      <div class="data-table">
        <a-table :columns="columns" :data-source="fansImgText.tableData" v-show="showType === 1">
          <template slot="action" slot-scope="text, record">
            <a @click="lookDetail(record)">查看详情</a>
          </template>
        </a-table>
        <a-table :columns="detailColumns" :data-source="detailList" v-show="showType !== 1">
        </a-table>
      </div>
    </div>
  </div>
</template>

<script>
import MixinOperation from '@/mixin'
import { getArticlesInfluence } from '@/api/operation'
import echartsOptions from './echartsOptions'
const { imgText } = Object.freeze(echartsOptions)
const columns = [
  {
    title: '时间',
    dataIndex: 'statDate',
    key: 'statDate'
  },
  {
    title: '发文篇数',
    dataIndex: 'dispatchCount',
    key: 'dispatchCount'
  },
  {
    title: '送达人数',
    dataIndex: 'deliveryCount',
    key: 'deliveryCount'
  },
  {
    title: '阅读人数',
    dataIndex: 'readCount',
    key: 'readCount'
  },
  {
    title: '分享人数/次数',
    dataIndex: 'shareCount',
    key: 'shareCount'
  },
  {
    title: '原文阅读人数/次数',
    dataIndex: 'originReadCount',
    key: 'originReadCount'
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]
const detailColumns = [
  {
    title: '标题',
    dataIndex: 'title',
    key: 'title'
  },
  {
    title: '阅读人数/次数',
    dataIndex: 'readCount',
    key: 'readCount'
  },
  {
    title: '分享人数/次数',
    dataIndex: 'shareCount',
    key: 'shareCount'
  },
  {
    title: '原文阅读人数/次数',
    dataIndex: 'originReadCount',
    key: 'originReadCount'
  }
]
export default {
  name: 'ImageText',
  mixins: [MixinOperation.Operation],

  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  watch: {
    accountId () {
      this.handleChooseSearch('W')
    }
  },
  created () {
    if (this.accountId) {
      this.handleChooseSearch('W')
    }
  },
  data () {
    return {
      detailColumns,
      columns,

      searchTypeList: [
        {
          title: '近一周',
          type: 'W'
        },
        {
          title: '近30天',
          type: 'M'
        }
      ],
      search: {
        type: 'W' // h:按小时,d:按天,w:按周,m:按月
      },
      fansImgText: {},
      fansImgTextOptions: JSON.parse(JSON.stringify(imgText)),
      showType: 1, // 1 数据总览 2 数据详情
      detailList: []
    }
  },
  methods: {
    // 查看详情
    lookDetail (record) {
      console.log(record)
      this.showType = 2
      this.detailList = record.list.map(item => {
        return {
          statDate: item.statDate,
          title: item.title,
          readCount: item.intPageReadUser + '/' + item.intPageReadCount,
          originReadCount: item.intPageReadUser + '/' + item.intPageReadCount,
          shareCount: item.shareUser + '/' + item.shareCount
        }
      })
    },
    // 切换时间
    changeDate (dates) {
      this.currentTime = dates
      this.getArticlesInfluence()
    },

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
          num = 8
          break
        case 'M':
          num = 30
          break
      }
      const end = new Date()
      end.setTime(end.getTime() - 3600 * 1000 * 24)
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * num)
      this.currentTime = [`${start.getFullYear()}-${this.dealTime(start.getMonth() + 1)}-${this.dealTime(start.getDate())}`, `${end.getFullYear()}-${this.dealTime(end.getMonth() + 1)}-${this.dealTime(end.getDate())}`]
      this.search.type = type
      this.getArticlesInfluence()
    },
    // 查询单个公众号--图文影响力
    async getArticlesInfluence () {
      const [startDate, endDate] = this.currentTime
     const { data, code } = await getArticlesInfluence(this.accountId, { startDate, endDate })
     if (code === 200) {
       if (data.length === 0) {
        this.fansImgText = {}
        this.detailList = []
        this.fansImgTextOptions = this.$options.data().fansImgTextOptions
        // this.fansImgTextOptions = {}
         return
       }
       console.log(data)
       this.mulToalData(data)
       this.mulTable(data)
       this.mulEcharts(data)
     }
    },
    // 填充echarts数据
    mulEcharts (data) {
         // 時間
          const time = []
          /* 阅读人数 */
          const readUesr = []
          /* 阅读次数 */
          const readCount = []
          /* 分享人数 */
          const shareUser = []
          /* 分享次数 */
          const shareCount = []
          /* 阅读原文人数 */
          const originReadUesr = []
          /* 阅读原文次数 */
          const originReadCount = []

          if (data) {
            data.map(da => {
              time.push(da.statDate)
              readUesr.push(da.intPageReadUser)
              readCount.push(da.intPageReadCount)
              shareUser.push(da.shareUser)
              shareCount.push(da.shareCount)
              originReadUesr.push(da.oriPageReadUser)
              originReadCount.push(da.oriPageReadCount)
            })
          }
          // 阅读人数
          this.fansImgTextOptions.series[0].data = readUesr
          // 阅读次数
          this.fansImgTextOptions.series[1].data = readCount
          // 分享人数
          this.fansImgTextOptions.series[2].data = shareUser
          // 分享次数
          this.fansImgTextOptions.series[3].data = shareCount
          // 阅读原文人数
          this.fansImgTextOptions.series[4].data = originReadUesr
          // 阅读原文次数
          this.fansImgTextOptions.series[5].data = originReadCount

          // 時間
          this.fansImgTextOptions.xAxis.data = time
    },
    // 计算数据总和
    mulToalData (data) {
      this.$set(this.fansImgText, 'totalDispatchCount', data.map(item => item.size).reduce((pre, next) => pre + next))
      this.fansImgText.totalDispatchCount = data.map(item => item.size).reduce((pre, next) => pre + next)
      this.fansImgText.totalReadCount = data.map(item => item.targetUser).reduce((pre, next) => pre + next)
      this.fansImgText.totalShareCount = data.map(item => item.shareCount).reduce((pre, next) => pre + next)
    },
    // 计算表格需要的数据
    mulTable (data) {
      this.fansImgText.tableData = data.map(item => {
        return {
          statDate: item.statDate,
          dispatchCount: item.size,
          readCount: item.intPageReadUser + '/' + item.intPageReadCount,
          deliveryCount: item.targetUser,
          originReadCount: item.intPageReadUser + '/' + item.intPageReadCount,
          shareCount: item.shareUser + '/' + item.shareCount,
          list: item.list // 查看详情时使用
        }
      })
    }
  }
}
</script>
<style scoped lang="less">
@import './config-style';
.imageText {
   border: 1px solid #d9d9d9;
   /deep/ .ant-btn {
      margin-right: 10px;
   }
}
</style>
