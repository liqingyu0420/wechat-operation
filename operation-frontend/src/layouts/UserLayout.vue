<template>
  <div id="userLayout" :class="['user-layout-wrapper', isMobile && 'mobile']">
    <div class="container">
      <div class="top">
        <div class="header">
          <a @click="jumpGithub">
            <img src="https://img-blog.csdnimg.cn/20201102164123661.jpg" class="logo" alt="logo">
            <span class="title">serven-idiot</span>
            <!--<span class="title">中德智慧-运营</span> -->
          </a>
        </div>
        <div class="desc">
          <!-- serven-idiot 是朝阳区最具影响力的 Web 设计规范 -->
          微信公众号深度运营平台
        </div>
      </div>

      <router-view />

      <div class="footer">
        <div class="links">
          <a href="_self">运营</a>
          <a href="_self">管理</a>
          <a href="_self">帮助</a>
        </div>
        <div class="copyright">
          Copyright &copy; 2020 - serven idiot
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { deviceMixin } from '@/store/device-mixin'

export default {
  name: 'UserLayout',
  mixins: [deviceMixin],
  mounted () {
    document.body.classList.add('userLayout')
  },
  beforeDestroy () {
    document.body.classList.remove('userLayout')
  },
  methods: {
    jumpGithub () {
      window.open('https://github.com/7-idiot/weChat-Operation')
    }
  }
}
</script>

<style lang="less" scoped>
#userLayout.user-layout-wrapper {
    height: 100%;

    &.mobile {
      .container {
        .main {
          max-width: 368px;
          width: 98%;
        }
      }
    }

    .container {
      width: 100%;
      min-height: 100%;
      background: #f0f2f5 url(~@/assets/background.svg) no-repeat 50%;
      background-size: 100%;
      padding: 110px 0 144px;
      position: relative;

      a {
        text-decoration: none;
      }

      .top {
        text-align: center;

        .header {
          height: 44px;
          line-height: 44px;

          .badge {
            position: absolute;
            display: inline-block;
            line-height: 1;
            vertical-align: middle;
            margin-left: -12px;
            margin-top: -10px;
            opacity: 0.8;
          }

          .logo {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 50%;
            vertical-align: top;
            margin-right: 16px;
            border-style: none;
          }

          .title {
            font-size: 33px;
            color: rgba(0, 0, 0, .85);
            font-style: normal;
            font-variant-ligatures: normal;
            font-variant-caps: normal;
            font-variant-numeric: normal;
            font-variant-east-asian: normal;
            font-weight: normal;
            font-stretch: normal;

            line-height: normal;
            font-family: Italiana, sans-serif;
            font-weight: 600;
            position: relative;
            top: 2px;
          }
        }
        .desc {
          font-size: 14px;
          color: rgba(0, 0, 0, 0.45);
          margin-top: 12px;
          margin-bottom: 40px;
        }
      }

      .main {
        min-width: 260px;
        width: 368px;
        margin: 0 auto;
      }

      .footer {
        position: absolute;
        width: 100%;
        bottom: 0;
        padding: 0 16px;
        margin: 48px 0 24px;
        text-align: center;

        .links {
          margin-bottom: 8px;
          font-size: 14px;
          a {
            color: rgba(0, 0, 0, 0.45);
            transition: all 0.3s;
            &:not(:last-child) {
              margin-right: 40px;
            }
          }
        }
        .copyright {
          color: rgba(0, 0, 0, 0.45);
          font-size: 14px;
        }
      }
    }
  }
</style>
