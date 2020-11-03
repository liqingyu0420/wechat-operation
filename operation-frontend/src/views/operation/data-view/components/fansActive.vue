<template>
  <div class="FansActive">
    <div class="box-search">
      <range-picker :defaultValue="currentTime" @changeDate="changeDate"/>
    </div>

    <div class="box-desc">

      这段时间里，平均每天有 <span>{{ fansActiveData.activenNum || '--' }}</span> 个活跃粉丝，平均活跃比例为
      <span>{{ fansActiveData.activenRate || 0 }}%</span>

    </div>
    <div class="data-view-box">
      <div class="data-view-title">粉丝活跃度统计</div>
      <div class="data-view-content">
        <vue-chart style="width:100%;height:100%;" :autoresize="true" :options="fansActiveOptions"></vue-chart>
      </div>
      <div class="data-view-title table-list-data">详细数据</div>
      <div class="data-table">
        <a-table :columns="columns" :data-source="fansActiveData.tableData"></a-table>
      </div>
    </div>
  </div>
</template>

<script>
import MixinOperation from '@/mixin'
import { getFansActive } from '@/api/operation'
import echartsOptions from './echartsOptions'

const { fansActive } = Object.freeze(echartsOptions)
const columns = [
  {
    title: '日期',
    dataIndex: 'statDate',
    key: 'statDate'
  },
  {
    title: '活跃粉丝数',
    dataIndex: 'inactiveNum',
    key: 'inactiveNum'
  },
  {
    title: '七天内互动粉丝数',
    dataIndex: 'sevenNum',
    key: 'sevenNum'
  },
  {
    title: '十五天内互动粉丝数',
    dataIndex: 'fifteenNum',
    key: 'fifteenNum'
  },
  {
    title: '总粉丝数',
    dataIndex: 'totalFansNum',
    key: 'totalFansNum'
  },
  {
    title: '活跃比例',
    dataIndex: 'inactiveRate',
    key: 'inactiveRate'
  }
]

export default {
  name: 'FansActive',
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
    this.handleChooseSearch('W')
  },
  data () {
    return {
      columns,
      defaultValue: '',
      fansActiveData: {}, // 粉丝数据
      fansActiveOptions: fansActive
    }
  },
  methods: {
    // 切换时间
    changeDate (dates) {
      this.currentTime = dates
      this.getFansActive()
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
            num = 7
            break
          case 'M':
            num = 30
            break
        }
      this.initCuttentTime(num)
        // this.search.type = type
        this.getFansActive()
      },
      // 获取粉丝活跃度
      async getFansActive () {
        const { currentTime: [startDate, endDate] } = this
        const { data, code } = await getFansActive(this.accountId, { startDate, endDate })
        if (code === 200) {
          console.log(data)
          // this.fansActiveData = data
          // 時間
          const time = []
          /* 七天互动粉丝数 */
          const sevenDayFsNum = []
          /* 十五天互动粉丝数 */
          const fifteenDayFsNum = []
          data.forEach(item => {
            time.push(item.statDate)
            sevenDayFsNum.push(item.sevenNum)
            fifteenDayFsNum.push(item.fifteenNum)
          })
          // 七天互动粉丝数
          this.fansActiveOptions.series[1].data = sevenDayFsNum
          //  十五天互动粉丝数
          this.fansActiveOptions.series[2].data = fifteenDayFsNum
          // 時間
          this.fansActiveOptions.xAxis[0].data = time

          this.mulTable(data)
          // 平均活跃粉丝与比率
          this.mulArgee(data)
        }
      },
      // 计算平均活跃粉丝与比率
      mulArgee (data) {
        this.fansActiveData.activenNum = data.map(item => item.inactiveNum).reduce((pre, next) => pre + next) / data.length
        this.fansActiveData.activenRate = data.map(item => item.inactiveRate).reduce((pre, next) => pre + next) / data.length
      },
      // 计算表格需要的数据
      mulTable (data) {
      this.fansActiveData.tableData = data.map(item => {
          return {
            statDate: item.statDate,
            inactiveNum: item.inactiveNum,
            sevenNum: item.inactiveNum,
            fifteenNum: item.fifteenNum,
            totalFansNum: item.totalFansNum,
            inactiveRate: item.inactiveRate + '%'
          }
        })
      }
  }
}
</script>
<style scoped lang="less">
@import './config-style';
.FansActive {
  border: 1px solid #d9d9d9;
}
</style>
