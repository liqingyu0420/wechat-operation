<template>
  <a-carousel height="300px" trigger="click" :autoplay="false">
    <div>
      <span class="emoji" @click="handleClickEmoji('😄')">😄</span><span class="emoji" @click="handleClickEmoji('😊')">😊</span><span class="emoji" @click="handleClickEmoji('😃')">😃</span>
      <span class="emoji" @click="handleClickEmoji('😉')">😉</span><span class="emoji" @click="handleClickEmoji('😍')">😍</span><span class="emoji" @click="handleClickEmoji('😘')">😘</span>
      <span class="emoji" @click="handleClickEmoji('😚')">😚</span><span class="emoji" @click="handleClickEmoji('😳')">😳</span><span class="emoji" @click="handleClickEmoji('😁')">😁</span>
      <span class="emoji" @click="handleClickEmoji('😌')">😌</span><span class="emoji" @click="handleClickEmoji('😜')">😜</span><span class="emoji" @click="handleClickEmoji('😝')">😝</span>
      <span class="emoji" @click="handleClickEmoji('😒')">😒</span><span class="emoji" @click="handleClickEmoji('😏')">😏</span><span class="emoji" @click="handleClickEmoji('😓')">😓</span>
      <span class="emoji" @click="handleClickEmoji('😔')">😔</span><span class="emoji" @click="handleClickEmoji('😞')">😞</span><span class="emoji" @click="handleClickEmoji('😖')">😖</span>
      <span class="emoji" @click="handleClickEmoji('😥')">😥</span><span class="emoji" @click="handleClickEmoji('😰')">😰</span><span class="emoji" @click="handleClickEmoji('😨')">😨</span>
      <span class="emoji" @click="handleClickEmoji('😣')">😣</span><span class="emoji" @click="handleClickEmoji('😢')">😢</span><span class="emoji" @click="handleClickEmoji('😭')">😭</span>
      <span class="emoji" @click="handleClickEmoji('😂')">😂</span><span class="emoji" @click="handleClickEmoji('😲')">😲</span><span class="emoji" @click="handleClickEmoji('😱')">😱</span>
      <span class="emoji" @click="handleClickEmoji('😠')">😠</span><span class="emoji" @click="handleClickEmoji('😡')">😡</span><span class="emoji" @click="handleClickEmoji('😪')">😪</span>
      <span class="emoji" @click="handleClickEmoji('😷')">😷</span><span class="emoji" @click="handleClickEmoji('👿')">👿</span><span class="emoji" @click="handleClickEmoji('👽')">👽</span>
      <span class="emoji" @click="handleClickEmoji('❤')">❤</span><span class="emoji" @click="handleClickEmoji('💔')">💔</span><span class="emoji" @click="handleClickEmoji('💘')">💘</span>
      <span class="emoji" @click="handleClickEmoji('✨')">✨</span><span class="emoji" @click="handleClickEmoji('🌟')">🌟</span><span class="emoji" @click="handleClickEmoji('❕')">❕</span>
      <span class="emoji" @click="handleClickEmoji('❔')">❔</span><span class="emoji" @click="handleClickEmoji('💤')">💤</span><span class="emoji" @click="handleClickEmoji('💦')">💦</span>
      <span class="emoji" @click="handleClickEmoji('🎵')">🎵</span><span class="emoji" @click="handleClickEmoji('🔥')">🔥</span><span class="emoji" @click="handleClickEmoji('💩')">💩</span>
      <span class="emoji" @click="handleClickEmoji('👍')">👍</span><span class="emoji" @click="handleClickEmoji('👎')">👎</span><span class="emoji" @click="handleClickEmoji('👊')">👊</span>
      <span class="emoji" @click="handleClickEmoji('✌')">✌</span><span class="emoji" @click="handleClickEmoji('👆')">👆</span><span class="emoji" @click="handleClickEmoji('👇')">👇</span>
      <span class="emoji" @click="handleClickEmoji('👉')">👉</span><span class="emoji" @click="handleClickEmoji('👈')">👈</span><span class="emoji" @click="handleClickEmoji('☝')">☝</span>
    </div>
    <div>
      <span class="emoji" @click="handleClickEmoji('💪')">💪</span><span class="emoji" @click="handleClickEmoji('💏')">💏</span><span class="emoji" @click="handleClickEmoji('💑')">💑</span>
      <span class="emoji" @click="handleClickEmoji('👦')">👦</span><span class="emoji" @click="handleClickEmoji('👧')">👧</span><span class="emoji" @click="handleClickEmoji('👩')">👩</span>
      <span class="emoji" @click="handleClickEmoji('👨')">👨</span><span class="emoji" @click="handleClickEmoji('👼')">👼</span><span class="emoji" @click="handleClickEmoji('💀')">💀</span>
      <span class="emoji" @click="handleClickEmoji('👄')">👄</span><span class="emoji" @click="handleClickEmoji('☀')">☀</span><span class="emoji" @click="handleClickEmoji('☔')">☔</span>
      <span class="emoji" @click="handleClickEmoji('☁')">☁</span><span class="emoji" @click="handleClickEmoji('⛄')">⛄</span><span class="emoji" @click="handleClickEmoji('🌙')">🌙</span>
      <span class="emoji" @click="handleClickEmoji('⚡')">⚡</span><span class="emoji" @click="handleClickEmoji('🌊')">🌊</span><span class="emoji" @click="handleClickEmoji('🐱')">🐱</span>
      <span class="emoji" @click="handleClickEmoji('🐶')">🐶</span><span class="emoji" @click="handleClickEmoji('🐭')">🐭</span><span class="emoji" @click="handleClickEmoji('🐹')">🐹</span>
      <span class="emoji" @click="handleClickEmoji('🐰')">🐰</span><span class="emoji" @click="handleClickEmoji('🐺')">🐺</span><span class="emoji" @click="handleClickEmoji('🐸')">🐸</span>
      <span class="emoji" @click="handleClickEmoji('🐯')">🐯</span><span class="emoji" @click="handleClickEmoji('🐨')">🐨</span><span class="emoji" @click="handleClickEmoji('🐻')">🐻</span>
      <span class="emoji" @click="handleClickEmoji('🐷')">🐷</span><span class="emoji" @click="handleClickEmoji('🐮')">🐮</span><span class="emoji" @click="handleClickEmoji('🐗')">🐗</span>
      <span class="emoji" @click="handleClickEmoji('🐵')">🐵</span><span class="emoji" @click="handleClickEmoji('🐴')">🐴</span><span class="emoji" @click="handleClickEmoji('🐍')">🐍</span>
      <span class="emoji" @click="handleClickEmoji('🐦')">🐦</span><span class="emoji" @click="handleClickEmoji('🐔')">🐔</span><span class="emoji" @click="handleClickEmoji('🐧')">🐧</span>
      <span class="emoji" @click="handleClickEmoji('🐛')">🐛</span><span class="emoji" @click="handleClickEmoji('🐙')">🐙</span><span class="emoji" @click="handleClickEmoji('🐠')">🐠</span>
      <span class="emoji" @click="handleClickEmoji('🐳')">🐳</span><span class="emoji" @click="handleClickEmoji('🐬')">🐬</span><span class="emoji" @click="handleClickEmoji('🌹')">🌹</span>
      <span class="emoji" @click="handleClickEmoji('🌺')">🌺</span><span class="emoji" @click="handleClickEmoji('🌴')">🌴</span><span class="emoji" @click="handleClickEmoji('🌵')">🌵</span>
      <span class="emoji" @click="handleClickEmoji('💝')">💝</span><span class="emoji" @click="handleClickEmoji('🎃')">🎃</span><span class="emoji" @click="handleClickEmoji('👻')">👻</span>
      <span class="emoji" @click="handleClickEmoji('🎅')">🎅</span><span class="emoji" @click="handleClickEmoji('🎄')">🎄</span><span class="emoji" @click="handleClickEmoji('🎁')">🎁</span>
      <span class="emoji" @click="handleClickEmoji('🔔')">🔔</span><span class="emoji" @click="handleClickEmoji('🎉')">🎉</span><span class="emoji" @click="handleClickEmoji('🎈')">🎈</span>
    </div>
    <div>
      <span class="emoji" @click="handleClickEmoji('💿')">💿</span><span class="emoji" @click="handleClickEmoji('📷')">📷</span><span class="emoji" @click="handleClickEmoji('🎥')">🎥</span>
      <span class="emoji" @click="handleClickEmoji('💻')">💻</span><span class="emoji" @click="handleClickEmoji('📺')">📺</span><span class="emoji" @click="handleClickEmoji('☎')">☎</span>
      <span class="emoji" @click="handleClickEmoji('🔓')">🔓</span><span class="emoji" @click="handleClickEmoji('🔒')">🔒</span><span class="emoji" @click="handleClickEmoji('🔑')">🔑</span>
      <span class="emoji" @click="handleClickEmoji('🔨')">🔨</span><span class="emoji" @click="handleClickEmoji('💡')">💡</span><span class="emoji" @click="handleClickEmoji('📫')">📫</span>
      <span class="emoji" @click="handleClickEmoji('🛀')">🛀</span><span class="emoji" @click="handleClickEmoji('💰')">💰</span><span class="emoji" @click="handleClickEmoji('💣')">💣</span>
      <span class="emoji" @click="handleClickEmoji('🔫')">🔫</span><span class="emoji" @click="handleClickEmoji('💊')">💊</span><span class="emoji" @click="handleClickEmoji('🏈')">🏈</span>
      <span class="emoji" @click="handleClickEmoji('🏀')">🏀</span><span class="emoji" @click="handleClickEmoji('⚽')">⚽</span><span class="emoji" @click="handleClickEmoji('⚾')">⚾</span>
      <span class="emoji" @click="handleClickEmoji('⛳')">⛳</span><span class="emoji" @click="handleClickEmoji('🏆')">🏆</span><span class="emoji" @click="handleClickEmoji('👾')">👾</span>
      <span class="emoji" @click="handleClickEmoji('🎤')">🎤</span><span class="emoji" @click="handleClickEmoji('🎸')">🎸</span><span class="emoji" @click="handleClickEmoji('👙')">👙</span>
      <span class="emoji" @click="handleClickEmoji('👑')">👑</span><span class="emoji" @click="handleClickEmoji('🌂')">🌂</span><span class="emoji" @click="handleClickEmoji('👜')">👜</span>
      <span class="emoji" @click="handleClickEmoji('💄')">💄</span><span class="emoji" @click="handleClickEmoji('💍')">💍</span><span class="emoji" @click="handleClickEmoji('💎')">💎</span>
      <span class="emoji" @click="handleClickEmoji('☕')">☕</span><span class="emoji" @click="handleClickEmoji('🍺')">🍺</span><span class="emoji" @click="handleClickEmoji('🍻')">🍻</span>
      <span class="emoji" @click="handleClickEmoji('🍸')">🍸</span><span class="emoji" @click="handleClickEmoji('🍔')">🍔</span><span class="emoji" @click="handleClickEmoji('🍟')">🍟</span>
      <span class="emoji" @click="handleClickEmoji('🍝')">🍝</span><span class="emoji" @click="handleClickEmoji('🍣')">🍣</span><span class="emoji" @click="handleClickEmoji('🍜')">🍜</span>
      <span class="emoji" @click="handleClickEmoji('🍳')">🍳</span><span class="emoji" @click="handleClickEmoji('🍦')">🍦</span><span class="emoji" @click="handleClickEmoji('🎂')">🎂</span>
      <span class="emoji" @click="handleClickEmoji('🍎')">🍎</span><span class="emoji" @click="handleClickEmoji('✈')">✈</span><span class="emoji" @click="handleClickEmoji('🚀')">🚀</span>
      <span class="emoji" @click="handleClickEmoji('🚲')">🚲</span><span class="emoji" @click="handleClickEmoji('🚄')">🚄</span><span class="emoji" @click="handleClickEmoji('⚠')">⚠</span>
      <span class="emoji" @click="handleClickEmoji('🏁')">🏁</span><span class="emoji" @click="handleClickEmoji('🚹')">🚹</span><span class="emoji" @click="handleClickEmoji('🚺')">🚺</span>
    </div>
    <div>
      <span class="emoji" @click="handleClickEmoji('⭕')">⭕</span><span class="emoji" @click="handleClickEmoji('❌')">❌</span><span class="emoji" @click="handleClickEmoji('©')">©</span>
      <span class="emoji" @click="handleClickEmoji('®')">®</span><span class="emoji" @click="handleClickEmoji('™')">™</span>
    </div>
  </a-carousel>

</template>

<script>

import { carousel } from 'ant-design-vue'
  export default {
    name: 'Emoji',
    components: {
        ACarousel: carousel
    },
    methods: {
      /** 点击 */
      handleClickEmoji (emoji) {
        this.$emit('clickEmoji', emoji)
      }
    }
  }
</script>

<style lang="less" scoped>

  .emoji {
    color: #000;
    float: left;
    width: 41px;
    height: 41px;
    padding: 8px;
    cursor: pointer;
    border-radius: 3px;
    font-size: 20px;
    text-align: center;

    &:hover {
      background: rgba(99, 99, 99, 0.1);
    }
  }

.ant-carousel {
  width: 500px !important;
  height: 230px;
  .slick-slider {
      width: 300px;
  }
}

</style>
