<template>
  <div class="Mass">
    <a-card>
      <div class="headers">
        <div class="headers-left">
          <range-picker @changeDate="changeDate"/>
          <!-- @change="handleChange" -->
          <query-type class="query-type" @queryType="queryType"/>
          <a-button icon="search" @click="searchCustomer" >搜索</a-button>
        </div>
        <div class="headers-right">
          <a-button type="primary" icon="plus" @click="$refs['account-list'].openModal()">新增群发消息</a-button>
        </div>
      </div>
      <a-table :columns="columns" :data-source="customerList" :loading="loading">
        <template slot="type" slot-scope="text, record">
          {{ record.type !== 1 ? '筛选' : '全部' }}
        </template>
        <template slot="msgType" slot-scope="text, record">
          {{ record.msgType | filtersMagType }}
        </template>
        <template slot="accountName" slot-scope="text, record">
          <img class="accountName-img" :src="record.headImage" alt="">
          <span class="accountName-nickName">{{ record.nickName }}</span>
        </template>
        <template slot="status" slot-scope="text, record">
          {{ record.status | filtersStatus }}
        </template>
        <template slot="action" slot-scope="text, record">
          <a @click="headerQueryDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a>删除</a>
        </template>
      </a-table>
    </a-card>
    <account-list ref="account-list" @select="jumpAddMass"/>
    <!--详情弹窗  -->
    <a-drawer width="400px" :visible="detailModel" @close="detailModel = false" title="预览模板" >
      <pre-view :list="curLookCodeItem"/>
    </a-drawer>
  </div>
</template>

<script>
import { getMass } from '@/api/information'
import { getMaterialDetail } from '@/api/material'
import RangePicker from '@/components/RangePicker'
import QueryType from '../components/QueryType'
import AccountList from '@/components/AccountList'
import PreView from '@/components/PreView'
const columns = [
    {
        title: '消息ID',
        dataIndex: 'id',
        key: 'id',
        width: 100,
        ellipsis: true
    },
    {
        title: '公众号',
        dataIndex: 'accountName',
        key: 'accountName',
        scopedSlots: { customRender: 'accountName' },
        width: 240,
        ellipsis: true
    },
    {
        title: '消息类型',
        dataIndex: 'msgType',
        key: 'msgType',
        scopedSlots: { customRender: 'msgType' }
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        key: 'createTime'
    },
    {
        title: '发送时间',
        dataIndex: 'sendTime',
        key: 'sendTime'
    },
    {
        title: '发送对象',
        dataIndex: 'type', // 0 全部 1 筛选
        key: 'type',
        scopedSlots: { customRender: 'type' }
    },
    {
        title: '发送人数',
        dataIndex: 'sendNum',
        key: 'sendNum'
    },
    {
        title: '发送状态',
        dataIndex: 'status', // 0 ''已发送 1 '未发生' 2 '发送中' 3 '发送终止' 4 发送失败
        key: 'status',
        scopedSlots: { customRender: 'status' }
    },
    {
        title: '操作',
        dataIndex: 'action',
        key: 'action',
        scopedSlots: { customRender: 'action' }
    }
]
export default {
  name: 'Mass',
  components: {
      RangePicker,
      QueryType,
      AccountList,
      PreView
  },
  filters: {
      filtersStatus (status) {
          let result
          switch (status) {
              case 0:
                  result = '已发送'
                  break
              case 1:
                  result = '未发送'
                  break
              case 2:
                  result = '发送中'
                  break
              case 3:
                  result = '发送终止'
                  break
              case 4:
                  result = '发送失败'
                  break
          }

          return result
      },
      filtersMagType (type) {
        switch (Number(type)) {
            case 1:
                return '图文'
            case 2:
                return '图片'
            case 3:
                return '文字'
            case 4:
                return '音频'
            case 5:
                return '视频'
            default:
            break
        }
        }

  },
  created () {
      this.getMass()
  },
  data () {
      return {
        loading: false,
        detailModel: false,
        curLookCodeItem: [],
        columns,
        form: {
            page: 1,
            startDate: '',
            endDate: '',
            status: ''
        },
        customerList: []
      }
  },
  methods: {
    async getMaterialDetail ($par, msgType) {
      const { data, code } = await getMaterialDetail($par)
      if (code === 200) {
        console.log(data)
        const imagetextlist = data.news_item.map(item => {
            item.url = item.thumb_url
            return item
        })
        this.curLookCodeItem = [{ imagetextlist, type: msgType }]

        // this.curLookCodeItem =
      }
    },
    //   查看详情
    headerQueryDetail (record) {
        console.log(record)
        this.detailModel = true
        const $par = {
            accountId: record.accountId,
            meidaId: JSON.parse(record.content)[0]['media_id']
        }
        this.getMaterialDetail($par, record.msgType)
    },
    //   跳转页码
    jumpAddMass (account) {
         console.log(account)
        this.$router.push({
            path: '/information/mass/add',
            query: {
                accountId: account[0].id
            }
        })
    },
    // 搜索
    searchCustomer () {
        this.getMass()
    },
    //  选择时间
    changeDate (date) {
        console.log(date)
        const [startDate, endDate] = date
        this.form.startDate = startDate
        this.form.endDate = endDate
    },
    //  查询状态
    queryType (status) {
        console.log(status)
        this.form.status = status
    },
    //   查询客服消息模板
    async getMass () {
      this.loading = true
      const { data, code } = await getMass(this.form)
      this.loading = false
      if (code === 200) {
          console.log(data)
          const { records } = data
          this.customerList = records
      }
    }
  }
}
</script>
<style scoped lang="less">
.Mass {
    .headers {
        margin-bottom: 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        &-left {
            display: flex;
            justify-content: center;
            align-items: center;
            .query-type {
                margin-left: 20px;
                margin-right: 20px;
            }
        }
    }
}
.accountName-img {
    width: 40px;
    height: 40px;
    object-fit: cover;
    margin-right: 5px;
}
</style>
