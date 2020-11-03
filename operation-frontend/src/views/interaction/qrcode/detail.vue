<template>
  <div class="AccountDetail">
    <div class="info-box">
      <div class="info-code">
        <div class="code-image">
          <div>
            <img class="code-image-url" :src="WXBaseUrl + curLookCodeItem.ticket" alt="">
            <p class="title">{{ curLookCodeItem.name }}</p>
            <p class="code-type">{{ curLookCodeItem.type === 1 ? '永久二维码':`该二维码将于
                  ${curLookCodeItem.expireTime} 过期` }}</p>
            <a id="downloadLink" class="down-code" @click="downloadCode">下载渠道二维码</a>
          </div>
        </div>
        <div class="code-bet">
          <div>
            <span class="bet-title">美化二维码：<a-tooltip
              class="item"
              effect="dark"
              content="可粘贴此链接到二维码美化工具，生成您喜欢的二维码样式"
              placement="top">
              <i class="icon-question el-icon-question"></i>
            </a-tooltip></span>
            <a-input-search class="get-code" v-model="beautyCode" disabled placeholder="美化二维码">
              <a-button
                slot="enterButton"
                type="primary"
                v-clipboard:copy="beautyCode"
                v-clipboard:success="onCopy">复制
              </a-button>
            </a-input-search >
          </div>
        </div>
      </div>
      <div class="info-view">
        <p class="view-desc" v-if="Number(curLookCodeItem.qr_type) === 1">不启用扫码推送消息</p>
        <pre-view :list="replaceContent"></pre-view>
      </div>
    </div>
  </div>
</template>

<script>
/*eslint-disable*/
import PreView from '@/components/PreView'
import QRCode from 'qrcode'
import { WXBaseUrl } from '@/utils/global.js'
export default {
  name: 'AccountDetail',
  components: {
      PreView
  },
  props: {
      curLookCodeItem: {
          type: Object,
          required: true
      }
  },
  computed: {
    replaceContent () {
      console.log(this.curLookCodeItem, 'curLookCodeItemcurLookCodeItem');
      return JSON.parse(this.curLookCodeItem.content) 
     }
  },
  data () {
      return {
          WXBaseUrl,
          beautyCode: 'https://cli.im/'
      }
  },
  methods: {
    onCopy () {
    
        this.$message.info('复制成功')
    },
    downloadCode () {
       QRCode.toDataURL(this.WXBaseUrl + this.curLookCodeItem.ticket, { margin: 1 }, (err, url) => {
           console.log(url);
            const downloadLink = document.getElementById('downloadLink')
            downloadLink.setAttribute('href', url)
            downloadLink.setAttribute('download', this.curLookCodeItem.nickName || new Date().getTime() + '.png')
            downloadLink.click()
      })
    },
  }
}
</script>
<style scoped lang="less">
.info-box {
  display: flex;

  .info-code {
    position: relative;
    width: 450px;
    height: 569px;
    border: 1px solid rgb(216, 216, 216);
    margin-top: 42px;
    .code-image {
      display: flex;
      height: 300px;
      justify-content: center;
      align-items: center;
      text-align: center;

      .code-image-url {
        width: 150px;
        padding: 10px;
        border: 1px solid #e9e9e9;
      }

      p.title {
        font-size: 16px;
        margin-top: 16px;
        color: #1a1a1a;
      }

      p.code-type {
        margin-top: 12px;
        font-size: 14px;
        color: #ff6600;
        margin-bottom: 16px;
      }
    }

    .bet-title {
      font-size: 14px;
      margin-bottom: 10px;
      display: block;
    }

    .code-tags {
      padding: 15px;
      height: 240px;
      border-top: 1px solid rgb(216, 216, 216);
      border-bottom: 1px solid rgb(216, 216, 216);
      overflow: hidden;
      overflow-y: auto;

      .code-tags-item {
        margin-right: 5px;
        margin-bottom: 5px;
      }
    }

    .code-bet {
      height: 640-300-240px;
      display: flex;
      padding: 0 15px;
      align-items: center;
      border-top: 1px solid#d8d8d8;
      > div {
        width: 100%;
      }
    }
  }

  .down-code {
    display: inline-block;
    color: #fff;
    width: 100%;
    padding: 5px;
    background: #459ae9;
    cursor: pointer;
  }

  .info-view {
    position: relative;
  }

  .create-view {
    padding-top: 42px;
  }

  .view-desc {
    position: absolute;
    color: #f60;
    top: 15px;
    left: 15px;
  }
}
</style>
