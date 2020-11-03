import axios from 'axios'
import store from '@/store'
import storage from 'store'
import notification from 'ant-design-vue/es/notification'
import { VueAxios } from './axios'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { message } from 'ant-design-vue'
import { setItem, getItem } from '@/utils/storage'
import router from '@/router'

console.log(process.env.NODE_ENV)
const baseURL = 'https://open.luojigou.vip'
// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  // baseURL: 'https://api.zaojiao.net',
  baseURL: process.env.NODE_ENV === 'development' ? '/api' : baseURL,
  withCredentials: true,
  // baseURL: 'https://open.luojigou.vip',

  // timeout: 6000, // 请求超时时间
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 异常拦截处理器
const errorHandler = (error) => {
  console.log(error)
  if (error.response) {
    const data = error.response.data
    // 从 localstorage 获取 token
    const token = storage.get(ACCESS_TOKEN)
    if (error.response.status === 403) {
      notification.error({
        message: 'Forbidden',
        description: data.message
      })
    }
    if (error.response.status === 401 && !(data.result && data.result.isLogin)) {
      notification.error({
        message: 'Unauthorized',
        description: 'Authorization verification failed'
      })
      if (token) {
        store.dispatch('Logout').then(() => {
          setTimeout(() => {
            window.location.reload()
          }, 1500)
        })
      }
    }
  }
  return Promise.reject(error)
}

// request interceptor
request.interceptors.request.use(config => {
  // const token = storage.get(ACCESS_TOKEN)

  // 如果 token 存在
  // 让每个请求携带自定义 token 请根据实际情况自行修改
  // if (token) {
    // config.headers['Access-Token'] = token
  // }
  config.headers['token'] = getItem('token')
  config.headers['Access-Control-Allow-Origin'] = '*'

  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use((response) => {
  console.log(response)
  if (response.headers.hasOwnProperty('auth_token')) {
    setItem('token', response.headers.auth_token)
  }

  if (response.data.code === 500) {
    message.error(response.data.message)
  }
  if (response.data.status === 500) {
    message.error(response.data.msg)
  }

  if (response.data.code === 401) {
    message.error(response.data.message)
    router.push({ path: '/login' })
  }
  return response.data
}, errorHandler)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  }
}

export default request

export {
  installer as VueAxios,
  request as axios
}
