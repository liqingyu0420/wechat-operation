import { getAccount } from '@/api/publicAccount'
// removeItem
import { setItem, getItem } from '@/utils/storage'

const account = {
    namespaced: true,
    state: {
        accountList: getItem('accountList') || 'hellol'
    },

    mutations: {
        SET_ACCOUNTLIST (state, data) {
            state.accountList = data
            setItem('accountList', state.accountList)
        }
    },

    actions: {
      // 获取微信公众号列表
      getAccount ({ commit }) {
          console.log('hello')
          return new Promise(async (resolve, reject) => {
            const { data, code } = await getAccount()
            if (code === 200) {
                commit('SET_ACCOUNTLIST', data)
            }
          })
      },
      //  获取指定的公众号
      getAssignAccount ({ state }, accountId) {
          console.log(state)
        return new Promise(async (resolve, reject) => {
            const account = JSON.parse(state.accountList).filter(account => {
                return Number(account.id) === Number(accountId)
            })
            console.log(account)
            resolve(account)
        })
      }
    }
}

export default account
