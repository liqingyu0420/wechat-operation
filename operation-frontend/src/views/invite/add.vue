<template>
  <div class="InviteAdd">

    <a-form :form="form" :label-col="{ span: 6}" :wrapper-col="{ span: 12 }" @submit="handleSubmit">
      <a-form-item label="代理商归属地">
        <a-cascader

          :options="options"
          :fieldNames="fieldNames"
          @change="onChange"
          :placeholder="loading? '正在加载地区数据~' : '请选择地区'"
          changeOnSelect
          v-decorator="[
            'regionCode',
            {
              rules:[{ required: true, message: '请选择地区' }],
            }
          ]"
        >
        </a-cascader>
      </a-form-item>
      <a-form-item label="详细地址">
        <a-input v-model.trim="address" />
      </a-form-item>
      <a-form-item label="代理商姓名">
        <a-input
          v-decorator.trim="['name', { rules: [{ required: true, message: '请输入姓名' }] }]"
        />
      </a-form-item>
      <!-- <a-form-item label="照片">
        <a-upload
          :fileListProps="headImg"
          @changeImg="handleChangeCover"
          :count="1"
          v-decorator="['headImg', { rules: [{ required: true, message: '请上传照片' }] }, ]"
        />
      </a-form-item> -->
      <a-form-item label="性别">
        <a-radio-group
          v-decorator="['gender', { rules: [{ required: true, message: '请输入性别' }] }]"
        >
          <a-radio :value="1">
            男
          </a-radio>
          <a-radio :value="0">
            女
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="手机号">
        <a-input
          v-decorator="['phone', { rules: [{ required: true, message: '请输入密码' }] }]"
        />
      </a-form-item>
      <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
      </a-form-item>
    </a-form>

  </div>
</template>
<script>
import { Cascader } from 'ant-design-vue'
import { city } from './city'
import { getAreaListByLevel } from '@/api/area'
import { postWithPutInvite, getInviteDetail } from '@/api/agent.js'
console.log(city)
const options = Object.freeze(city)
export default {
  name: 'InviteAdd',
  components: {
    ACascader: Cascader
  },
  created () {
    // this.getAreaListByLevel()
  },
  data () {
    return {
      loading: false, // 地区code返回前提示，地区code请求时间不短
      address: '', // 详细地址绑定字段
      formLayout: 'horizontal',
      form: this.$form.createForm(this, { name: 'coordinated' }),
      headImg: [],
      options: options, // 获取地区
      fieldNames: { label: 'name', value: 'code', children: 'childList' }
    }
  },
  methods: {
    // 地区选择
    onChange (value) {
      console.log(value)
      this.getAreaListByLevel(value)
    },
    // 获取地区
    async getAreaListByLevel (value) {
      this.loading = true
      const data = await getAreaListByLevel({
        level: 3,
        isPage: 0
      })
      console.log(data)
      if (data.status === 200) {
        this.options = data.data
        this.loading = false
      }
    },

    handleSubmit () {
    //   e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values)
          const regionCode = values.regionCode.pop()
          console.log(regionCode, 'regionCode')
          const $par = Object.assign({}, { ...values }, { address: this.address }, { regionCode })
          console.log($par)
          this.postWithPutInvite($par)
        }
      })
    },
    // 新增或者编辑表单
    async postWithPutInvite (values) {
      console.log(values, 'valuesvaluesvalues')
      const $par = this.id ? Object.assign({}, values, { id: this.id }) : values

      const { data, status } = await postWithPutInvite($par)
      if (status === 200) {
        console.log(data)
        this.$message.success((this.id ? '修改' : '新增') + '成功')

        this.$emit('success')
      }
    },
    // 上传图片
    handleChangeCover (item) {
      const { data } = item[0].response
      console.log(data)
      this.headImg = [{ uid: '0', url: data }]
      this.form.setFieldsValue({ 'headImg': data })
    },
    // 查询邀请商详情
    async getInviteDetail (id) {
      this.id = id
      const { data, status } = await getInviteDetail(id)
      if (status === 200) {
        console.log(data)
        this.form.setFieldsValue({ ...data })
        this.disposeEditData(data)
      }
    },
    // 处理反填的数据
    disposeEditData (data) {
      const { address, region } = data
      const regionCode = []
      region.forEach(item => {
        regionCode.push(...Object.values({ code: item.code }))
      })

      console.log(regionCode)
      this.address = address
      this.form.setFieldsValue({ regionCode })
    }
  }
}
</script>
<style scoped lang="less">

</style>
