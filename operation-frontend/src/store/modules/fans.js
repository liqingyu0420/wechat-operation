import { preFansNum } from '@/api/fans'
// removeItem
// import { setItem } from '@/utils/storage'

const account = {
    namespaced: true,
    state: {

    },

    mutations: {
        // SET_ACCOUNTLIST (state, data) {
        //     state.accountList = data
        //     setItem('accountList', state.accountList)
        // }
    },

    actions: {
      // 获取微信公众号列表
      preFansNum ({ commit }, params) {
          console.log('hello')
          return new Promise(async (resolve, reject) => {
            const { data, code } = await preFansNum(params)
            if (code === 200) {
                console.log(data)
            }
          })
      }
    }
}

export default account
