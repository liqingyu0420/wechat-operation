<template>
  <div class="ImageText">
    <div
      class="ant-form"
      v-for="(item, index) in itemData.imagetextlist"
      :key="index"
      @click="currentEditIndex=index"
    >
      <div class="template-item">
        <div class="label">
          图片封面:
        </div>
        <show-img
          :imgUrl="item.picurl"
          @changeImg="changeImg"
        />
      </div>
      <div class="template-item">
        <div class="label">
          填写标题:
        </div>
        <div class="value">
          <div class="create-editor-box">
            <div-edit
              :ref="'edit_'+index"
              :data-index="index"
              class="medium-editor-form"
              @change="editChange"/>
          </div>
          <div class="fans-tag">
            点击插入  <a-tag color="pink" @click="innerNickName"> 粉丝标签 </a-tag>
          </div>
        </div>
      </div>
      <div class="template-item">
        <div class="label">
          添加描述:
        </div>
        <div class="value">
          <a-textarea
            v-model="item.description"
            placeholder="请填写图文描述"
            :auto-size="{ minRows: 5}"
          />
        </div>
      </div>
      <div class="template-item">
        <div class="label">
          点击跳转:
        </div>
        <div class="value">
          <a-input
            placeholder="请输入跳转链接，且必须以http://或https://开头"
            v-model="item.url"
          />
        </div>
      </div>
      <div class="template-item" v-if="!hideDelete">
        <div class="label">
          操作:
        </div>
        <div class="value">
          <a-tag class="cp" @click="itemData.imagetextlist.splice(index,1)">
            删除该条
          </a-tag>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import Upload from '@/components/upload'
import ImageView from '@/components/material/ImageView'
import ShowImg from '@/components/TemplateItem/ShowImg'
import DivEdit from '@/components/DivEdit'
import MsgType from '@/components/MsgType'
import { repalceTag } from '@/utils/global'
export default {
  name: 'ImageText',
  components: {
      Upload,
      ImageView,
      ShowImg,
      DivEdit,
      MsgType
  },
  props: {
      itemData: {
          type: Object,
          default: () => {
              return {
                  type: 1,
                  imagetextlist: [] // 图文内容列表
              }
          }
      },
      addStatus: {
        type: Boolean,
        default: false
      },
      hideDelete: {
        type: Boolean,
        default: false
      }
  },
  created () {
    this.mulItemData = this.itemData
  },
  mounted () {
    this.time = setTimeout(() => {
        if (this.itemData.hasOwnProperty('imagetextlist')) {
          if (!this.itemData.imagetextlist.length) { // 没有值
            this.itemData.imagetextlist = []
            this.$forceUpdate()
          } else { // 有值
            this.$nextTick(_ => {
              this.itemData.imagetextlist.forEach((res, index) => {
                if (res.title) {
                  /** 替换粉丝标签 */
                  const title = repalceTag.reResult(res.title)
                  this.handleEditInnerDom(title, index)
                }
              })
              this.$forceUpdate()
            })
          }
        }
    }, 1000)

    this.$once('hook:beforeDestory', () => {
      clearTimeout(this.time)
    })
  },
  watch: {
    addStatus: {
      handler () {
        if (this.itemData.imagetextlist.length < 8) {
           const aa = this.itemData.imagetextlist.concat([JSON.parse(JSON.stringify(this.editContainer))])
           this.$set(this.itemData, 'imagetextlist', aa)
        } else {
          this.$message.error('最多只能添加8条')
        }
           this.$forceUpdate()
      }
    }
  },
  data () {
      return {
        form: this.$form.createForm(this, { name: 'coordinated' }),
        currentEditIndex: 0, // 当前正在编辑的索引
        /** 编辑器 */
        editContainer: {
          picurl: '',
          title: '',
          media_id: '',
          description: '', // 图文描述
          url: ''
        },
        mulItemData: {}
      }
  },
  methods: {

     /** 点击插入粉丝昵称 */
    innerNickName () {
      this.handleEditInnerDom('<span contenteditable="false" class="user-nick-name"><span>粉丝昵称</span></span>', this.currentEditIndex)
    },
    /** 监听标题的值变化 */
    editChange (ev) {
      this.itemData.imagetextlist[this.currentEditIndex].title = ev
      console.log(ev, 'evevev')
      this.$forceUpdate()
    },
    // 选择图片
    changeImg (item) {
      this.itemData.imagetextlist[this.currentEditIndex].picurl = item.url
      this.itemData.imagetextlist[this.currentEditIndex].media_id = item.id
      this.$forceUpdate()
    },
     /**
     * 添加内容
     * edit inner
     * index 编辑器的索引位置
     * */
    handleEditInnerDom ($dom, index) {
      console.log(index, 'indexjindex')
      if ($dom) {
        this.$nextTick(() => {
          const editBox = this.$refs[`edit_${index}`][0]
          if (editBox) {
            editBox.innerDom($dom)
            this.$forceUpdate()
          }
        })
      }
    }
  }
}
</script>
<style scoped lang="less">
  @import '~./index.less';

.ImageText {

    .label {

    }
}

/deep/ .ant-form {
    border-bottom: 1px solid #d8d8d8;
    margin-bottom: 20px;
}

.add-image-text {
    width: 100px;
    margin: 0 auto;
    display: block;
}

.create-editor-box {
  overflow: hidden;
  outline: 0;
  border: 1px solid #d8d8d8;
  background: #fff;
  border-radius: 2px;
  cursor: unset;
}

</style>
