<template>
  <div class="Customer">
    <a-card>
      <div class="headers">
        <div class="headers-left">
          <range-picker @changeDate="changeDate"/>
          <query-type class="query-type" @queryType="queryType"/>
          <a-button icon="search" @click="getCustomer()">搜索</a-button>
        </div>
        <div class="headers-right">
          <a-button type="primary" icon="plus" @click="$refs['account-list'].openModal()">新增客服消息</a-button>
        </div>
      </div>
      <a-table :columns="columns" :data-source="customerList" :loading="loading">
        <template slot="type" slot-scope="text, record">
          {{ record.type === 1 ? '筛选' : '全部' }}
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
          <a @click="copyCustomer(record)">复制</a>
        </template>
      </a-table>
    </a-card>
    <account-list ref="account-list" @select="jumpAddCustomer"/>
    <!--详情弹窗  -->
    <a-drawer width="400px" :visible="detailModel" @close="detailModel = false" title="预览模板" >
      <pre-view :list="curLookCodeItem"/>
    </a-drawer>
  </div>
</template>

<script>

import { getCustomer } from '@/api/information'
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
        title: '消息名称',
        dataIndex: 'label',
        key: 'label',
        width: 120,
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
        title: '创建时间',
        dataIndex: 'createTime',
        key: 'createTime',
        width: 170,
        ellipsis: true
    },
    {
        title: '发送时间',
        dataIndex: 'sendTime',
        key: 'sendTime',
        width: 170,
        ellipsis: true
    },
    {
        title: '发送对象',
        dataIndex: 'type', // 0 全部 1 筛选
        key: 'type',
        scopedSlots: { customRender: 'type' }
    },
    {
        title: '预估发送人数',
        dataIndex: 'preSuccessNum',
        key: 'preSuccessNum'
    },
    {
        title: '实际发送人数',
        dataIndex: 'successNum',
        key: 'successNum'
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
  name: 'Customer',
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
      }
  },
  created () {
      this.getCustomer()
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
        type: 0,
        customerList: []
      }
  },
  methods: {

    //   查看详情
    headerQueryDetail (record) {
        this.detailModel = true
        this.curLookCodeItem = JSON.parse(record.content)
    },
    // 复制客服消息
    copyCustomer (records) {
        this.$store.commit('information/SET_ACCOUNTLIST', records)
        this.$refs['account-list'].openModal()
        this.type = 1
    },
    // 跳转到新增客服消息
    jumpAddCustomer (account) {
        console.log(account)
            this.$router.push({
                path: '/information/customer/add',
                query: {
                    accountId: account[0].id,
                    type: this.type
                }
            })
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
    async getCustomer () {
      this.loading = true
      const { data, code } = await getCustomer(this.form)
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
.Customer {
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
.accountName-nickName {

}
</style>
