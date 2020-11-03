<template>
  <div class="AudioView template-item">
    <div class="label">
      <!-- 语音: -->
    </div>
    <div class="value qrcode-right cp" @click="audioWithVideoStatus = true">
      <template v-if="audioInfo.id">
        <a-icon type="check-circle"/>已选择音频[<a href="javascript:;">修改</a> ]
      </template>
      <template v-else>
        <a-icon type="plus"/>选择音频消息
      </template>
    </div>
    <a-modal width="800px" v-model="audioWithVideoStatus" @ok="handleOk">
      <audio-video ref="audio-video" @onChange="onChange" :isAudio="isAudio"/>
    </a-modal>
  </div>
</template>

<script>
import AudioVideo from '@/components/material/AudioWithVideo'
export default {
  name: 'AudioView',
  components: {
    AudioVideo
  },
  props: {
    audioUrl: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  watch: {
    audioUrl: {
      handler () {
        this.audioInfo = this.audioUrl
      },
      deep: true,
      immediate: true
    }
  },
  data () {
      return {
        isAudio: true,
        audioWithVideoStatus: false,
        audioInfo: {}
      }
  },
  methods: {
    handleOk () {
      this.$refs['audio-video'].reactiveParent()
    },
    onChange (item) {
       console.log('hbhdkashdiagdi')
      console.log(item, 'itemitem')
      this.audioInfo = item
      this.$emit('changeAudit', this.audioInfo)
      this.audioWithVideoStatus = false
    }
  }
}
</script>
<style scoped lang="less">
@import '~./index.less';
.AudioView {
    .qrcode-right {
        position: relative;
        border: 1px dashed #d8d8d8;
        background-color: #fff;
        height: 150px;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-left: 10px;
    }
}

.modal {
    width: 800px;
}
</style>
