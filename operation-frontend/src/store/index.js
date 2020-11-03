import Vue from 'vue'
import Vuex from 'vuex'

import app from './modules/app'
import user from './modules/user'
import account from './modules/account'
import information from './modules/information'
import tag from './modules/tag'
import fans from './modules/fans'
// default router permission control
import permission from './modules/permission'

// dynamic router permission control (Experimental)
// import permission from './modules/async-router'
import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    user,
    permission,
    account,
    information,
    tag,
    fans
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  getters
})
