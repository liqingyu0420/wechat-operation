// import storage from 'store'
// import { login, getInfo, logout } from '@/api/loginbase'
// import { ACCESS_TOKEN } from '@/store/mutation-types'
// import { welcome } from '@/utils/util'
import { Login } from '@/api/user'
import { getItem, removeItem, setItem } from '@/utils/storage'

const user = {
  state: {
    token: getItem('token') || '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: {},
    userInfo: getItem('userInfo') || ''
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    },

    SET_USERINFO: (state, info) => {
      state.userInfo = { ...info }
      setItem('userInfo', state.userInfo)
    }
  },

  actions: {

    Login ({ commit }, userInfo) {
      return new Promise(async (resolve, reject) => {
        console.log(userInfo)
        const $par = Object.assign({}, userInfo, { nickName: 1 })
        const data = await Login($par)
        console.log(data)
        try {
          commit('SET_USERINFO', data.data)
          resolve(data)
        } catch (e) {
          reject(e)
        }
      })
    },

    // Login ({ commit }, userInfo) {
    //   return new Promise((resolve, reject) => {
    //     login(userInfo).then(response => {
    //       const result = response.result
    //       storage.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
    //       commit('SET_TOKEN', result.token)
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    // GetInfo ({ commit }) {
    //   return new Promise((resolve, reject) => {
    //     getInfo().then(response => {
    //       const result = response.result

    //       if (result.role && result.role.permissions.length > 0) {
    //         const role = result.role
    //         role.permissions = result.role.permissions
    //         role.permissions.map(per => {
    //           if (per.actionEntitySet != null && per.actionEntitySet.length > 0) {
    //             const action = per.actionEntitySet.map(action => { return action.action })
    //             per.actionList = action
    //           }
    //         })
    //         role.permissionList = role.permissions.map(permission => { return permission.permissionId })
    //         commit('SET_ROLES', result.role)
    //         commit('SET_INFO', result)
    //       } else {
    //         reject(new Error('getInfo: roles must be a non-null array !'))
    //       }

    //       commit('SET_NAME', { name: result.name, welcome: welcome() })
    //       commit('SET_AVATAR', result.avatar)

    //       resolve(response)
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        // commit('SET_TOKEN', '')
        // commit('SET_ROLES', [])
        removeItem('token')
        removeItem('userInfo')
        resolve({ res: '2' })
        // logout(state.token).then(() => {
        //   resolve()
        // }).catch(() => {
        //   resolve()
        // }).finally(() => {
        //   commit('SET_TOKEN', '')
        //   commit('SET_ROLES', [])

        // })
      })
    }

  }
}

export default user
