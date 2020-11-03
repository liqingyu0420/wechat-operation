<template>
  <div class="FansLoyal">
    <div class="box-search">
      <a-date-picker format="YYYY/MM/DD" :default-value="currentTime" @change="onChange" />
    </div>
    <div class="choose-pro-radio">
      <a-radio-group :default-value="1" button-style="solid" v-model="showType">
        <a-radio-button :value="1">
          显示百分比
        </a-radio-button>
        <a-radio-button :value="2">
          显示关注数
        </a-radio-button>
      </a-radio-group>
    </div>
    <div class="data-view-box">
      <div class="stash-table">
        <div class="loyalty-table">
          <table border="0" width="100%" cellspacing="0">
            <tr align="center" class="table-head">
              <td style="border-right: 1px solid rgb(233, 233, 233); width: 111px;">日期</td>
              <td>当天关注粉丝</td>
              <td>当天留存</td>
              <td>2天留存</td>
              <td>3天留存</td>
              <td>4天留存</td>
              <td>5天留存</td>
              <td>6天留存</td>
              <td>7天留存</td>
            </tr>
            <tr align="center" class="table-body" v-for="(item,index) in list" :key="index">
              <td style="border-right: 1px solid rgb(233, 233, 233); color: rgb(26, 26, 26); width: 111px;">
                {{ item.statDate }}
              </td>
              <td v-for="(tdItem,tdIndex) in item.keepNum" :key="tdIndex">
                <!-- | dealNum(tdIndex,showType)  -->
                <span :class="{'sec-color':tdIndex === 2 || tdIndex === 7}" v-if=" tdIndex > tdItem.show">--</span>
                <span :class="{'sec-color':tdIndex === 2 || tdIndex === 7}" v-else>{{ tdItem.value | dealNum( item.keepNum[0].value, tdIndex, showType) }}</span>
              </td>
            </tr>
            <tr>
              <td
                class="blank-box"
                style="border-right: 1px solid rgb(233, 233, 233); color: rgb(26, 26, 26); width: 111px;"></td>
              <td class="blank-box"></td>
              <td class="blank-box"></td>
              <td class="blank-box"></td>
              <td class="blank-box"></td>
              <td class="blank-box"></td>
              <td class="blank-box"></td>
              <td class="blank-box"></td>
              <td class="blank-box"></td>
            </tr>
          </table>
        </div>
        <div class="loyalty-table co-las">
          <table border="0" width="100%" cellspacing="0">
            <tr align="center" class="table-body tot-cl">
              <td style="width: 111px;"></td>
              <td>平均留存率</td>
              <td v-for="(item,index) in agree" :key="index">
                <span :class="{'sec-color':index === 1 || index === 6}">{{ item | filtersInfinity }}</span>
              </td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getFansLoyal } from '@/api/operation'
export default {
  name: 'FansLoyal',
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  filters: {
    dealNum (item, currentValue, index, showType) {
      console.log(item, currentValue, index)
      if (showType === 2) {
        if (item === 0) return item
        if (currentValue === 0) return '--'
          return index === 0 ? item : 100 * (item / currentValue).toFixed(2) + '%'
      } else {
        return item
      }
    },
    filtersInfinity (value) {
      console.log(value)
      if (value === 'Infinity%') {
        return '--'
      } else {
         return value
       }
    }
  },
  watch: {
    accountId () {
      this.setTime()
    }
  },
  created () {
    if (this.accountId) {
      this.setTime()
    }
  },
  data () {
    return {
      currentTime: '', // 查询的时间
      showType: 1,
      list: [],
      agree: []
    }
  },
  methods: {

    // 时间改变
    onChange (date, dateString) {
      this.currentTime = dateString.replace(/\//g, '-')
      this.getFansLoyal()
    },
    setTime () {
      const time = new Date()
      time.setTime(time.getTime() - 3600 * 1000 * 24)
      this.currentTime = `${time.getFullYear()}-${this.dealTime(time.getMonth() + 1)}-${this.dealTime(time.getDate())}`
      this.getFansLoyal()
    },
    dealTime (time) {
      return time < 10 ? `0${time}` : time
    },
    // 获取粉丝忠诚度
    async getFansLoyal () {
      const { data, code } = await getFansLoyal(this.accountId, { date: this.currentTime })
      if (code === 200) {
        console.log(data)
        this.mulKeepNum(data)
        this.mulAgreeRate()
      }
    },
    // 算取留存数
    mulKeepNum (data) {
      const deepData = JSON.parse(JSON.stringify(data))
      this.list = deepData.map(item => {
        const statDate = item.statDate
        this.$delete(item, 'statDate')
        const keepNum = []
        const nowNum = item.nowNum

        this.$delete(item, 'nowNum')
        Object.keys(item).forEach((key, index) => {
          const val = Object.create(null)
          val['value'] = item[key]
          val['show'] = this.dateOperation(statDate)
          keepNum.push(val)
        })
        keepNum.unshift({
          value: nowNum,
          show: this.dateOperation(statDate)
        })

        return {
          statDate: statDate,
          keepNum: keepNum
        }
      })
    },
    // 算取平均留存率
    mulAgreeRate (data) {
      //  this.agree =
     const row = this.list.map(item => {
        const curDay = item.keepNum[0].value
        console.log(curDay)
        console.log(item.keepNum.slice(1, 8))
        const res = item.keepNum.slice(1, 8).map(ele => {
          return ele.value === 0 ? ele.value : 100 * (ele.value / curDay)
        })
        // .reduce((pre, next) => pre + next)
        return res
      })
      console.log(row)
      const cacleObj = {}
      row.map(item => {
        item.map((ele, index) => {
          if (cacleObj.hasOwnProperty(index)) {
            cacleObj[index] = cacleObj[index] + ele
          } else {
             cacleObj[index] = ele
          }
        })
      })
      const nums = [0, 1, 2, 3, 4, 5, 6]
      console.log(cacleObj)
      this.agree = nums.map(num => {
        return (cacleObj[num] / row.length).toFixed(2) + '%'
      })
    },
    // 时间操作者
    dateOperation (statDate) {
      const time = new Date()
      const currentTime = time.getFullYear() + '/' + (time.getMonth() + 1) + '/' + time.getDate()
      const res = new Date(currentTime).getTime()
      const queryTime = new Date(statDate.replace(/-/g, '/')).getTime()
      const day = 3600 * 24 * 1000
      return (res - queryTime) / day
    }
  }

}
</script>
<style scoped lang="less">
@import './config-style';
.FansLoyal {
    border: 1px solid #d9d9d9;
}
</style>
