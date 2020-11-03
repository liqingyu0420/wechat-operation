<template>
  <div class="FansAdd">
    <div class="box-search">
      <a-button
        :type="item.type === search.type ? 'primary' : ''"
        v-for="(item,index) in searchTypeList"
        :key="index"
        @click="handleChooseSearch(item.type)">
        {{ item.title }}
      </a-button>
      <range-picker v-if="search.type !== 'M'" :key="Math.random()" :defaultValue="currentTime" @changeDate="changeDate"/>
    </div>
    <div class="box-desc">

      这段时间里，共有 <span>{{ fansAddData.inactiveNum || '--' }}</span> 人新关注，<span>{{ fansAddData.cancelNum || '--' }}</span> 人取关，取关率
      <span>{{ fansAddData.addNum || '--' }}%</span>，净增长
      <span>{{ fansAddData.newNum || '--' }}</span> 人，平均每天增长
      <span>{{ fansAddData.aveNum || '--' }} </span> 人

    </div>
    <div class="data-view-box">
      <div class="data-view-title">粉丝数据趋势</div>
      <div class="data-view-content">
        <vue-chart class="chart" style="width:100%;height:100%;" :autoresize="true" :options="fansAddOptionss"></vue-chart>
      </div>
      <div class="data-view-title table-list-data">详细数据</div>
      <div class="data-table">
        <a-table :columns="columns" :data-source="fansAddData.tableData" />
      </div>
    </div>
  </div>
</template>

<script>
import MixinOperation from '@/mixin'
import { getFansAdd } from '@/api/operation'
import echartsOptions from './echartsOptions'
const { fansAdd } = Object.freeze(echartsOptions)
const searchTypeList = [
          {
            title: '按小时',
            type: 'H'
          },
          {
            title: '按天',
            type: 'D'
          },
          {
            title: '按周',
            type: 'W'
          },
          {
            title: '按月',
            type: 'M'
          }
        ]
const columns = [
  {
    title: '时间',
    dataIndex: 'dateLabel',
    key: 'dateLabel'
  },
  {
    title: '净增人数',
    dataIndex: 'addNum',
    key: 'addNum'
  },
  {
    title: '取关人数',
    dataIndex: 'cancelNum',
    key: 'cancelNum'
  },
  {
    title: '活跃人数',
    dataIndex: 'inactiveNum',
    key: 'inactiveNum'
  },
  {
    title: '新增人数',
    dataIndex: 'newNum',
    key: 'newNum'
  }
]
export default {
  name: 'FansAdd',
  mixins: [MixinOperation.Operation],
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  computed: {
    fansAddOptionss () {
      return JSON.parse(JSON.stringify(this.fansAddOptions))
    }
  },
  watch: {
    accountId () {
      const { search: { type } } = this
      this.handleChooseSearch(type)
    }
  },
  created () {
    const { search: { type } } = this
    this.handleChooseSearch(type)
  },
  data () {
    return {
      columns, // table 配置
      searchTypeList, // 搜索button
      search: {
        type: 'H' // h:按小时,d:按天,w:按周,m:按月
      },
      fansAddData: {},
      fansAddOptions: fansAdd // echarts 配置数据
    }
  },
  methods: {
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
            num = 30
            break
          case 'M':
            num = 30
            break
        }
        this.initCuttentTime(num)
        this.search.type = type
        this.getFansAdd()
    },
    changeDate (dates) {
      console.log(dates, 'dates')
      this.currentTime = dates
      this.getFansAdd()
    },
    // 查询粉丝增长
    async getFansAdd () {
      const { accountId, search: { type }, currentTime: [startDate, endDate] } = this
      const $par = Object.assign({}, { type }, { startDate, endDate })
      const { data, code } = await getFansAdd(accountId, $par)
      console.log(data)
      if (code === 200) {
        this.fansAddData = data

        const { tableData } = data

        const time = [] // echarts x 轴对应下标

        const addNum = [] // 净增人数

        const cancelNum = [] // 取关人数

        const newNum = [] // 新增人数

        tableData.forEach(item => {
          time.push(item.dateLabel)
          addNum.push(item.addNum)
          cancelNum.push(item.cancelNum)
          newNum.push(item.cancelNum)
        })
          this.fansAddOptions.series.data = addNum.reverse()
          // 净增长
          this.fansAddOptions.series[0].data = addNum.reverse()
          // 取消关注
          this.fansAddOptions.series[1].data = cancelNum.reverse()
          // 新增关注
          this.fansAddOptions.series[2].data = newNum.reverse()
          // 時間
          this.fansAddOptions.xAxis.data = time.reverse()
          // console.log(this.fansAddOptions, ' this.fansAddOptions')
      }
    }
  }
}
</script>
<style scoped lang="less">
@import './config-style';
.FansAdd {
   border: 1px solid #d9d9d9;
}

.chart {
  width: 1000px;
  height: 500px;
}
</style>
