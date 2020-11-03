<template>
  <div class="Statistic">
    <!-- 指标总览 -->
    <div class="indicator">
      <div class="indicator-label">
        指标总览<span>(所有认证号昨日指标)</span>
      </div>
      <div class="indicator-statistic">
        <a-row>
          <a-col
            class="indicator-statistic"
            :xs="12"
            :sm="12"
            :md="4"
            :lg="4"
            :xl="4"
            v-for="(item, index) in statistic"
            :key="index">
            <div class="indicator-statistic-item-label">
              {{ item.label }}
            </div>
            <div class="indicator-statistic-item-count">
              <count-to :start-val="0" :end-val="statisticData[item.count] || 0" :duration="2000"/>
              <a-icon v-if="statisticData[item.rate] < 0" type="arrow-down" class="arrow-up" :style="{ fontSize: '16px', color: 'rgb(207, 19, 34)' }"/>
              <a-icon v-else type="arrow-up" class="arrow-up" :style="{ fontSize: '16px', color: '#52c41a' }"/>
            </div>
            <div class="indicator-statistic-item-rate">
              {{ statisticData[item.rate] }}%
            </div>
          </a-col>
          <!-- </div> -->
        </a-row>
      </div>
    </div>
    <!--  -->
  </div>
</template>

<script>
import { getSummary } from '@/api/operation'
import CountTo from 'vue-count-to'
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
  name: 'Statistic',
  components: {
    CountTo
  },
  created () {
    this.getSummary()
  },
  data () {
      return {
        statistic,
        statisticData: {}
      }
  },
  methods: {
    // 查询公众号统计数据概览
    async getSummary () {
     const { data, code } = await getSummary()
     if (code === 200) {
        this.statisticData = data
     }
    }
  }
}
</script>
<style scoped lang="less">
.Statistic {
    .indicator {
        width: 100%;
        // height: 164px;
        background-color: #fff;
        padding: 20px;
        &-label {
            font-size: 16px;
            font-weight: 900;
            color: #323233;
            span {
                display: inline-block;
                font-size: 14px;
                color: #969799;
                margin-left: 7px;
                font-weight: 400;
            }
        }
        .indicator-statistic {
            // display: flex;
            margin-left: 30px;
            margin-top: 10px;
            &-item {
                &-label {

                }
                &-count {
                    font-size: 28px;
                    color: #333;
                    display: flex;
                    justify-content: flex-start;
                    align-items: center;
                    .arrow-up {
                      margin-left: 5px;
                    }
                }
                &-rate {
                    color: #666;
                    font-size: 14px;
                }
            }
        }
    }
}
</style>
