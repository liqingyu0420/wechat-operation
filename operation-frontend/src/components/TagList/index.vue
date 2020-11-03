<template>
  <div class="TagList">
    <a-modal width="600px" v-model="visable" title="为选中得粉丝打上标签" >
      <a-checkbox-group @change="onChange">
        <a-row class="tag-list-row">
          <a-col :span="8" v-for="(tag, index) in tagList" :key="index">
            <a-checkbox :value="tag.wxId">
              {{ tag.name }}
            </a-checkbox>
          </a-col>
        </a-row>
      </a-checkbox-group>
      <template slot="footer">
        <div class="custom-footer">

          <div class="custom-footer-left">
            <a-button icon="plus" @click="isNewAddTag = true" v-if="!isNewAddTag">新建标签</a-button>
            <template v-else>
              <a-input :maxLength="6" v-model="name" placeholder="不得超过六个字符">
                <template slot="addonAfter">
                  <span class="cp" @click="editAddTag">  <a-icon v-if="loading" spin type="loading" />   <span v-else>确定</span></span>
                </template>
              </a-input>
            </template>

          </div>
          <div class="custom-footer-right">
            <a-button key="back" @click="visable = false">
              取消
            </a-button>
            <a-button key="submit" type="primary" :loading="loading" @click="handleOk">
              确定
            </a-button>
          </div>
        </div>

      </template>
    </a-modal>
  </div>
</template>

<script>
import { editAddTag } from '@/api/fans'
export default {
  name: 'TagList',
  data () {
      return {
          visable: false,
          accountId: '',
          name: '',
          loading: false,
          isNewAddTag: false,
          tagList: [],
          checkboxList: []
      }
  },
  methods: {
    onChange (checkboxList) {
        console.log(checkboxList)
        this.checkboxList = checkboxList
    },
    handleOk () {
        const $par = {
            checkboxList: this.checkboxList,
            cb: () => {
                this.visable = false
            }
        }
        this.$emit('selectTag', $par)
        // this.loading = true
    },
    openModal (accountId) {
        this.accountId = accountId
        this.visable = true
        this.getTagList()
    },
    // 新增标签
    async editAddTag () {
        console.log('hello')
      const $par = {
        accountId: this.accountId,
        name: this.name
      }
      console.log($par)
      this.loading = true
      const { data, code } = await editAddTag($par)
      this.loading = false
      if (code === 200) {
        console.log(data)
        this.getTagList()
        this.$message.success('新增成功')
      }
        this.isNewAddTag = false
        this.name = ''
    },

    // 查询标签列表
    async getTagList () {
     this.tagList = await this.$store.dispatch('tag/getTagList', { accountId: this.accountId })
    }
  }
}
</script>
<style scoped lang="less">
.ant-checkbox-group{
    width: 552px;
}

.custom-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
/deep/ .ant-input-group-wrapper{
    width: 250px;
}
</style>
