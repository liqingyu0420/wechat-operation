<template>
  <div>
    <div class="create-box">
      <div class="create-box-title">填写标题：</div>
      <div class="create-box-content">
        <div class="create-editor-box">
          <div-edit ref="edit" class="medium-editor-form" @change="editChange"></div-edit>
        </div>
        <div class="create-add-tag">
          <span>点击插入</span>
          <a-tag v-if="isShow" color="pink" class="cp" @click="innerTag('粉丝昵称')">粉丝昵称</a-tag>
          <a-popover
            v-model="isPop"
            trigger="click"
          >
            <template slot="content">
              <emoji @clickEmoji="innerEmoji"></emoji>
            </template>
            <a-tag color="pink" class="cp">表情</a-tag>
          </a-popover>
          <a-popover
            title="新建互动链"
            width="400"
            trigger="click"
            v-model="visibleLinkPop"
          >
            <div class="like-cont" slot="content">
              <div class="like-box">
                <div class="like-title">链接文字：</div>
                <div class="input-box">
                  <a-input v-model="linkBox.title" size="small" placeholder="互动连显示的文字"></a-input>
                </div>
              </div>
              <div class="like-box">
                <div class="like-title">点击发送：</div>
                <div class="input-box">
                  <a-input v-model="linkBox.link" size="small" placeholder="粉丝点击互动链后，自动向公众号发送的消息"></a-input>
                </div>
              </div>
              <p class="like-desc">推荐与微信官方后台的【关键词回复】结合使用</p>
              <div style="text-align: right">
                <a-button size="small" @click="handleButton(false)">取消</a-button>
                <a-button class="confirm-btn" size="small" type="primary" @click="handleButton">确定</a-button>
              </div>
            </div>
            <a-tag v-if="isShow" class="user-nick-name cp" color="pink">互动链</a-tag>
          </a-popover>
        </div>

        <div class="form-text-desc" ref="hello">
          如何设置链接：输入文案后， 鼠标选中想要插入链接的关键词，点击出现的“设置链接”，即可填入链接。文案不能包含" >"字符 ，关键词必须是连续文字（不能空格、换行）。 多个链接之间需空格。
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import DivEdit from '@/components/DivEdit'
  import { repalceTag } from '@/utils/global'
  export default {
    name: 'TextView',
    components: {
      DivEdit,
      emoji: () => import('@/components/emoji')
    },
    props: {
      // 数据内容
      itemData: {
        type: Object,
        default: () => {
          return {
            type: 3,
            content: ''
          }
        }
      },
      // 是否显示粉丝昵称和互动连
      isShow: {
        type: Boolean,
        default: true
      }
    },
    data () {
      return {
        isPop: false,
        linkBox: {
          title: '',
          link: ''
        },
        value: [],
        visibleLinkPop: false // 链接的弹窗
      }
    },
    mounted () {
      console.log(this.$refs['hello'])
      /** 替换粉丝标签 */
      if (this.itemData.content) {
        this.itemData.content = repalceTag.reResult(this.itemData.content)
      }
      this.handleEditInnerDom(this.itemData.content)
    },
    methods: {
      /** 富文本添加标签 */
      innerTag (text) {
        this.handleEditInnerDom(`<span contenteditable="false" class="user-nick-name"><span>${text}</span></span>`)
      },
      /** 插入表情 */
      innerEmoji (emoji) {
        this.handleEditInnerDom(`${emoji}`)
        this.isPop = false
      },
      /**
       * 添加内容
       * edit inner
       * */
      handleEditInnerDom ($dom) {
          const editBox = this.$refs.edit
        if ($dom) {
          this.$nextTick(() => {
            setTimeout(() => {
              if (editBox) {
                console.log(editBox, 'hello')
                editBox.innerDom($dom)
              }
            }, 0)
          })
        } else { // 解决focus失焦问题
          this.$nextTick(() => {
           editBox.innerDom($dom)
          })
        }
      },
      /** 监听标题的值变化 */
      editChange (ev) {
        console.log(ev, 'evevevee')
        this.itemData.content = ev
        this.$emit('changeText', ev)
        this.$forceUpdate()
      },
      /** 点击取消或者确定 */
      handleButton (is = true) {
        if (!is) { // 取消
          this.clearLinkBox()
        } else {
          let errorTxt = ''
          if (!this.linkBox.link.trim()) {
            errorTxt = '请输入链接'
          }
          if (!this.linkBox.title) {
            errorTxt = '请输入标题'
          }
          if (errorTxt) {
            this.$message.error(errorTxt)
            return
          }
          this.handleEditInnerDom(`<a href="${this.linkBox.link.trim()}">${this.linkBox.title.trim()}</a>`)
          this.clearLinkBox()
        }
      },
      /** 清空 */
      clearLinkBox () {
        this.linkBox = {
          link: '',
          title: ''
        }
        this.visibleLinkPop = false
      }
    }
  }
</script>

<style lang="less" scoped>
  // @import "../create-box";

.create-box {
  display: flex;
  margin-bottom: 15px;

  .create-box-title {
    width: 120px;
    text-align: right;
  }

  .create-box-content {
    flex: 1;
    padding-left: 10px;
  }

  .upload-wrap {
    position: relative;
    width: 96px;
    height: 96px;
    border: 1px dashed #d8d8d8;
    background-color: #fff;
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;

    i.el-icon-plus {
      font-size: 32px;
      color: #686868;
    }

    span.choose-image-again {
      position: absolute;
      bottom: 0;
      width: 100%;
      height: 30px;
      line-height: 30px;
      text-align: center;
      font-size: 12px;
      background-color: rgba(0, 0, 0, 0.63);
      color: #fff;
      cursor: pointer;
    }
  }

  .upload-audio-video {
    width: 100%;
    height: 110px;
    cursor: pointer;

    i.el-icon-plus {
      font-size: 16px;
      color: #222;
      margin-left: 10px;
    }
  }

  .medium-editor-form {
    line-height: initial;
    a{
      color: #0a76a4;
    }
  }

  .create-editor-box {
    overflow: hidden;
    outline: 0;
    border: 1px solid #d8d8d8;
    background: #fff;
    border-radius: 2px;
    cursor: unset;
  }

}

  .create-editor-box, .medium-editor-form {
    min-height: 120px !important;
    height: auto;
  }

  .form-text-desc {
    position: relative;
    background: #fff2db;
    border: 1px solid #ffdda6;
    border-radius: 2px;
    padding: 7px 27px 6px 10px;
    font-size: 12px;
    color: #1a1a1a;
    margin-top: 12px;
    line-height: initial;
  }

  .like-cont {
    margin-top: 40px;
    margin-bottom: 20px;
    font-size: 12px;
    padding: 0 20px;
  }

  .like-desc {
    margin-top: 40px;
    margin-bottom: 10px;
    color: #999999;
  }

  .like-box {
    display: flex;
    align-items: center;
    margin-bottom: 10px;

    .input-box {
      flex: 1;
    }
  }
/deep/ .slick-dots {

  li {
    background-color:#606266;
    color: #606266;
  }

  /deep/ .slick-active {
    color: #000;
    button {
      background-color: #000 !important;
    }
  }
}

/deep/ .slick-slider  {
  height: 230px;
}

.confirm-btn {
  margin-left: 10px;
}
</style>
