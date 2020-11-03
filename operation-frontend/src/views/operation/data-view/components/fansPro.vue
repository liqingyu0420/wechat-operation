<template>
  <div class="FansPro">
    <a-radio-group :default-value="1" button-style="solid" v-model="currentRadio" @change="getFansPro">
      <a-radio-button :value="1">
        新增粉丝
      </a-radio-button>
      <a-radio-button :value="2">
        全部粉丝
      </a-radio-button>
    </a-radio-group>
    <div class="box-search">
      <range-picker v-if="currentRadio === 1" :defaultValue="currentTime" @changeDate="changeDate"/>
    </div>
    <div class="box-desc" v-if="currentRadio === 1">
      这段时间里，新增粉丝 <span>{{ fansProData.mulPeople && fansProData.mulPeople.xz_fs_num || '--' }}</span> 人，
      男粉丝 <span>{{ fansProData.mulPeople && fansProData.mulPeople.man_fs_num || '--' }}</span> 人，占比 <span>{{ fansProData.mulPeople &&fansProData.mulPeople.man_fs_rate || '--' }}</span>
      , 女粉丝 <span>{{ fansProData.mulPeople &&fansProData.mulPeople.woman_fs_num || '--' }}</span> 人，占比 <span>{{ fansProData.mulPeople &&fansProData.mulPeople.woman_fs_rate || '--' }}</span> , 未知粉丝数量 <span>{{ fansProData.mulPeople && fansProData.mulPeople.wz_fs_num || '--' }}</span>
      人，占比 <span>{{ fansProData.mulPeople &&fansProData.mulPeople.wz_fs_rate || '--' }}</span>
    </div>
    <div class="data-view-box">
      <div class="data-view-title">粉丝数据来源</div>
      <div class="data-view-content">
        <vue-chart style="width:100%;height:100%;" :autoresize="true" :options="fansProOptions"></vue-chart>
      </div>
      <div class="data-view-title table-list-data">详细数据</div>
      <div class="data-table">
        <a-table :columns="columns" :data-source="fansProData.tableData" />
      </div>
    </div>
    <div class="data-view-box">
      <div class="data-view-title">新增粉丝地域统计</div>
      <div class="data-view-two">
        <div class="data-view-left">
          <vue-chart style="width:100%;height:100%;" :autoresize="true" :options="fansProDataCity"></vue-chart>
        </div>
        <div class="data-view-right">
          <div class="data-right-item" v-for="(item,index) in cityList" :key="index">
            <span>{{ item.name }}</span>
            <span>{{ item.value }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MixinOperation from '@/mixin'
import { getFansPro, getsubscribeScene } from '@/api/operation'
import echartsOptions from './echartsOptions'
const { fansPro, city } = Object.freeze(echartsOptions)
const columns = [
  {
    title: '渠道',
    dataIndex: 'channel',
    key: 'channel'
  },
  {
    title: '关注量',
    dataIndex: 'attention',
    key: 'attention'
  },
  {
    title: '关注量占比',
    dataIndex: 'attentionRate',
    key: 'attentionRate'
  }
]

const scribeScene = Object.freeze([{
		id: 1,
		value: 'ADD_SCENE_SEARCH',
		label: '公众号搜索'
	},
   {
		id: 2,
		value: 'ADD_SCENE_ACCOUNT_MIGRATION',
		label: '公众号迁移'
	}, {
		id: 3,
		value: 'ADD_SCENE_PROFILE_CARD',
		label: '名片分享'
	}, {
		id: 4,
		value: 'ADD_SCENE_QR_CODE',
		label: '扫描二维码'
	}, {
		id: 5,
		value: 'ADD_SCENE_PROFILE_LINK',
		label: '图文页内名称点击'
	}, {
		id: 6,
		value: 'ADD_SCENE_PROFILE_ITEM',
		label: '图文页右上角菜单'
	}, {
		id: 7,
		value: 'ADD_SCENE_PAID',
		label: '支付后关注'
	}, {
		id: 8,
		value: 'ADD_SCENE_WECHAT_ADVERTISEMENT',
		label: '微信广告'
	}, {
		id: 9,
		value: 'ADD_SCENE_OTHERS',
		label: '其他'
	}
])
export default {
  name: 'FansPro',
  mixins: [MixinOperation.Operation],
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  async created () {
    // await this.getsubscribeScene()
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
      currentRadio: 1,
      fansProData: {}, // 粉丝属性数据
      scribeScene, // 公众号来源类型
      fansProOptions: fansPro,
      fansProDataCity: city,
      cityList: []
    }
  },
  methods: {
    // 时间切换
    changeDate (dates) {
      this.currentTime = dates
      this.getFansPro()
    },
    // 选择模式
    handleChooseSearch (type) {
      console.log(type)
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
        this.getFansPro()
    },
    // 获取粉丝属性
    async getFansPro (e) {
      console.log(e, 'valuevalue')

      const { currentTime: [ startDate, endDate ], accountId } = this
      this.currentRadio = e && e.target.value || 1
      // 1 新增粉丝 2 默认粉丝
     const { data, code } = await getFansPro(accountId, this.currentRadio === 1 ? { startDate, endDate } : {})
     console.log(data, code, 'data, codedata, code')
     if (code === 200) {
       console.log(!!data)
       this.fansProData = Object.keys(data).length ? data : { province: [], sex: [], subscribeScene: [] }

        console.log(this.fansProData, 'this.fansProData ')
        const { province } = this.fansProData
        // 统计人数
        this.mulPeople(this.fansProData)
        // 表格人数
        this.mulDetail(this.fansProData)
        // 填充饼图
        this.fansProOptions.series[0].data = this.fansProData.tableData.map(item => {
          return {
            name: item.channel,
            value: item.attention,
            rateValue: item.attentionRate
          }
        })

        // 填充地图
        province.forEach(item => {
          item.value = item['number']
          item.name = item['key']
        })
        console.log(province, 'province')
        this.fansProDataCity.series[0].data = province
        this.cityList = province
     }
    },
    // 计算男/女/未知性别 关注、人数or占比
    mulPeople (data) {
      console.log(data, 'datadata')
      const val = Object.create(null)
      console.log(data.sex, 'data.sexdata.sex')
      const totalNumber = data.sex.map(item => item.number).reduce((pre, next) => pre + next)
       val['xz_fs_num'] = totalNumber
       data.sex.forEach(item => {
         switch (item.key) {
           case '0':
            val['woman_fs_num'] = item.number
            val['woman_fs_rate'] = item.number === 0 ? item.number : (item.number / totalNumber).toFixed(2)
            break
           case '1':
            val['man_fs_num'] = item.number
            val['man_fs_rate'] = item.number === 0 ? item.number : (item.number / totalNumber).toFixed(2)
            break
           case '2':
            val['wz_fs_num'] = item.number
            val['wz_fs_rate'] = item.number === 0 ? item.number : (item.number / totalNumber).toFixed(2)
            break
         }
       })

       this.fansProData.mulPeople = val
    },
    // 计算详细数据
    mulDetail (data) {
      const result = Object.create(null)
        this.scribeScene.forEach(item => {
          result[item.value] = item.label
      })
      const totalNumber = data?.subscribeScene.map(item => item.number).reduce((pre, next) => pre + next)
      this.fansProData.tableData = data.subscribeScene.map(item => {
        return {
          'channel': result[item.key],
          'attention': item.number,
          'attentionRate': item.number === 0 ? item.number : 100 * (item.number / totalNumber).toFixed(2) + '%'
        }
      })
    },
    // 查询粉丝关注公众号来源类型
    async getsubscribeScene () {
     const { data, code } = await getsubscribeScene()
     if (code === 200) {
       this.scribeScene = data
     }
    }
 }
}
</script>
<style scoped lang="less">
@import './config-style';
.FansPro {
  border: 1px solid #d9d9d9;

}
</style>
