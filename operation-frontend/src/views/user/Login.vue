<template>
  <div class="main">
    <a-form
      id="formLogin"
      class="user-layout-login"
      ref="formLogin"
      :form="form"
      @submit="handleSubmit"
    >
      <a-tabs
        :activeKey="customActiveKey"
        :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
        @change="handleTabClick"
      >
        <a-tab-pane key="tab1" tab="账号密码登录">
          <a-alert v-if="isLoginError" type="error" showIcon style="margin-bottom: 24px;" message="账户或密码错误（admin/ant.design )" />
          <a-form-item>
            <a-input
              size="large"
              type="text"
              placeholder="请输入账号"
              v-decorator="[
                'userCode',
                {rules: [{ required: true, message: '请检查账号输入' }], validateTrigger: 'change'}
              ]"
            >
              <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>

          <a-form-item>
            <a-input-password
              size="large"
              placeholder="请输入密码"
              v-decorator="[
                'password',
                {rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}
              ]"
            >
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input-password>

          </a-form-item>

        </a-tab-pane>
        <!-- <a-tab-pane key="tab2" tab="微信登录">

          <img class="qrcode-img" src="data:image/png;base64,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" alt="">

        </a-tab-pane> -->
      </a-tabs>

      <a-form-item>

        <a-form-item style="margin-top:24px">
          <a-button
            size="large"
            type="primary"
            htmlType="submit"
            class="login-button"

            :disabled="state.loginBtn"
          >确定</a-button>
        </a-form-item>
        <!--     :loading="state.loginBtn" -->
      </a-form-item></a-form>
    <s-dialog :visible="visible" @cancle="visible = false" title="滑动验证">
      <slide-verify
        :l="42"
        :r="10"
        :w="310"
        :h="155"
        @success="userLogin"
        slider-text="向右滑"
      ></slide-verify>
    </s-dialog>
  </div>
</template>

<script>
import md5 from 'md5'
// import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
import { mapActions } from 'vuex'
import SDialog from '@/components/Model'
import { timeFix } from '@/utils/util'
// import { getSmsCaptcha, get2step } from '@/api/login'
// import { getVerifyCode } from '@/api/user'
export default {
  name: 'Login',
  components: {
    // TwoStepCaptcha,
    SDialog
  },
  data () {
    return {
      customActiveKey: 'tab1',
      loginBtn: false,
      // login type: 0 email, 1 username, 2 telephone
      loginType: 0,
      isLoginError: false,
      requiredTwoStepCaptcha: false,
      stepCaptchaVisible: false,
      form: this.$form.createForm(this),
      state: {
        time: 60,
        loginBtn: false,
        // login type: 0 email, 1 username, 2 telephone
        loginType: 0,
        smsSendBtn: false
      },
      img: '',
      key: 1,
      visible: false
    }
  },
  created () {
    this.img = '/api/wxoperate/auth/captcha'
  },
  methods: {
    ...mapActions(['Login', 'Logout']),
    // handler
    // handleUsernameOrEmail (rule, value, callback) {
    //   const { state } = this
    //   const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
    //   if (regex.test(value)) {
    //     state.loginType = 0
    //   } else {
    //     state.loginType = 1
    //   }
    //   callback()
    // },
    //
    updateCaptchaCode () {
      const timeStamp = new Date().getTime()
      this.img = '/api/wxoperate/auth/captcha?timeStamp=' + timeStamp
    },
    handleTabClick (key) {
      this.customActiveKey = key
    },
    handleSubmit (e) {
      e.preventDefault()
        const {
        form: { validateFields },
        state,
        customActiveKey
      } = this

      const validateFieldsKey = customActiveKey === 'tab1' ? ['userCode', 'password'] : ['mobile', 'captcha']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if (!err) {
          this.userInfo = { ...values }
          this.visible = true
        } else {
          setTimeout(() => {
            state.loginBtn = false
          }, 600)
        }
      })
    },

    // 验证通过,用户开始登陆
    async userLogin () {
      const { userInfo, Login } = this
      const loginParams = { ...userInfo }
      loginParams.password = md5(loginParams.password)
      const data = await Login(loginParams)
      console.log(data)
      if (data.code === 200) {
        this.$store.dispatch('account/getAccount')
        this.loginSuccess()
      } else {
        this.visible = false
      }
    },

     loginSuccess () {
      this.$router.push('/operation')
      // 延迟 1 秒显示欢迎信息
      setTimeout(() => {
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`
        })
      }, 1000)
      this.isLoginError = false
    }
    // requestFailed (err) {
    //   this.isLoginError = true
    //   this.$notification['error']({
    //     message: '错误',
    //     description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
    //     duration: 4
    //   })
    // }
  }

}
</script>

<style lang="less" scoped>
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}

.verify-code {
  // width: 100px;
  width: 100%;

   height: 39px;
   display: block;

}

.qrcode-img {
  margin: 0 auto;
  display: block;
  width: 197px;
  height: 197px;
}
</style>
