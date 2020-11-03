<template>
  <div class="Upload">
    <a-upload
      name="file"
      :data="uploadData"
      :accept="type +'/*' "
      :action="uploadAdress"
      @change="handleChange"
      :headers="headers"
    >
      <a-button>
        <template v-if="loading"><a-icon type="loading" />{{ percent }}</template>
        <template v-else><a-icon type="upload" /> {{ title }}</template>
      </a-button>
    </a-upload>
  </div>
</template>

<script>
import { uploadAdress } from '@/utils/global'
export default {
  name: 'Upload',
  props: {
    type: {
      type: String,
      required: true
    },
    title: {
      type: String,
      default: '上传图片'
    }
  },
  watch: {
    type: {
      handler () {
        // this.uploadAdress = `${uploadAdress}?accountId=${this.$route.query.accountId}&type=${this.type}`
        this.uploadAdress = uploadAdress
        this.uploadData = {
            accountId: this.$route.query.accountId,
            type: this.type
        }
        this.headers = {
          token: window.localStorage.getItem('token')
        }
      },
      immediate: true
    }
  },
  data () {
      return {
          uploadAdress: '',
          uploadData: '',
          headers: '',
          loading: false, // 上传标识
          percent: 0
      }
  },
  methods: {

    handleChange (info) {
      console.log(info.event, 'fileListfileListfileList')

      this.loading = true
      if (info.event && info.event.percent === 100) {
          this.percent = info.event.percent.toFixed(2) + '%'
      }
      if (info.file && info.file.response && info.file.response.code) {
          const { response: { code, data } } = info.file

          if (code === 200 && data) {
            this.loading = false
            this.$message.success('上传成功')
            this.$emit('success')
          }
      }
    }
  }
}
</script>
<style scoped lang="less">
.Upload {
  margin-bottom: 10px;
}

/deep/ .ant-upload-list {
  display: none !important;
}
</style>
