/*eslint-disable */
import { getTagList } from '@/api/fans'
// removeItem
import { setItem, getItem } from '@/utils/storage'

const tag = {
    namespaced: true,
    state: {
        tagList: getItem('tagList') || []
    },
    mutations: {
        SET_TAGLIST (state, data) {
            state.tagList = data
            setItem('tagList', state.tagList)
        }
    },
    actions: {
      // 获取标签列表
      getTagList ({ commit }, params) {
          console.log('hello')
          return new Promise(async (resolve, reject) => {
            const { data, code } = await getTagList(params)
            if (code === 200) {
                // commit('SET_TAGLIST', data)
                const $data = data.map(item => {
                    item.name = eval("'" + item.name + "'")
                    return item
                })
                commit('SET_TAGLIST', $data)
                resolve($data)
            }
          })
      }
    }
}

export default tag
