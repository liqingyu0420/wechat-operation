// import { getAccount } from '@/api/publicAccount'
// removeItem
import { setItem, getItem } from '@/utils/storage'

const information = {
    namespaced: true,
    state: {
        copyInformation: getItem('copyInformation') || {}
    },

    mutations: {
        SET_ACCOUNTLIST (state, data) {
            console.log('hello')
            state.copyInformation = data
            setItem('copyInformation', state.copyInformation)
        }
    },

    actions: {

    }
}

export default information
