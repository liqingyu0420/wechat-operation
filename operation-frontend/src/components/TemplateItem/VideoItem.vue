<template>
  <div class="VideoView template-item">
    <div class="label">
      <!-- 视频: -->
    </div>
    <div class="value qrcode-right cp" @click="audioWithVideoStatus = true">
      <template v-if="videoInfo.id">
        <a-icon type="check-circle"/>已选择视频[<a href="javascript:;">修改</a> ]
      </template>
      <template v-else>
        <a-icon type="plus"/>选择视频消息
      </template>
    </div>
    <a-modal width="800px" v-model="audioWithVideoStatus" @ok="handleOk">
      <audio-video ref="audio-video" @onChange="onChange"/>
    </a-modal>
  </div>
</template>

<script>
import AudioVideo from '@/components/material/AudioWithVideo'
export default {
  name: 'VideoView',
  components: {
    AudioVideo
  },
  props: {
    videoUrl: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  watch: {
    videoUrl: {
      handler () {
        this.videoInfo = this.videoUrl
      },
      deep: true,
      immediate: true
    }
  },
  data () {
      return {
        isAudio: true,
        audioWithVideoStatus: false,
        videoInfo: {}
      }
  },
   methods: {
    handleOk () {
      this.$refs['audio-video'].reactiveParent()
    },
    onChange (item) {
      this.videoInfo = item
      this.$emit('changeVideo', this.videoInfo)
      this.audioWithVideoStatus = false
    }
  }
}
</script>
<style scoped lang="less">
@import '~./index.less';
.VideoView {
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
