// with polyfills
import 'core-js/stable'
import 'regenerator-runtime/runtime'

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/'
import i18n from './locales'
import { VueAxios } from './utils/request'
import ProLayout, { PageHeaderWrapper } from '@ant-design-vue/pro-layout'
import themePluginConfig from '../config/themePluginConfig'
import SlideVerify from 'vue-monoplasty-slide-verify'
// mock
// WARNING: `mockjs` NOT SUPPORT `IE` PLEASE DO NOT USE IN `production` ENV.
// import './mock'

import bootstrap from './core/bootstrap'
import './core/lazy_use'
// import './permission' // permission control
import './utils/filter' // global filter
import './global.less'
import ECharts from 'vue-echarts'

import 'echarts/lib/chart/line'
import 'echarts/lib/chart/map'
import 'echarts/lib/chart/pie'
import 'echarts/lib/component/title'
import 'echarts/lib/component/legend'
import 'echarts/lib/component/tooltip'
import 'echarts-gl'
import china from 'echarts/map/json/china.json'
import VueClipboard from 'vue-clipboard2'
Vue.use(VueClipboard)
Vue.component('vue-chart', ECharts)
ECharts.registerMap('china', china)
// mount axios to `Vue.$http` and `this.$http`
Vue.use(SlideVerify)
Vue.use(VueAxios)
Vue.component('pro-layout', ProLayout)
Vue.component('page-header-wrapper', PageHeaderWrapper)

window.umi_plugin_ant_themeVar = themePluginConfig.theme

new Vue({
  router,
  store,
  i18n,
  created: bootstrap,
  render: h => h(App)
}).$mount('#app')
