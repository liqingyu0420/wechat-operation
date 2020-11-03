import { axios } from '@/utils/request'
const baseURL = 'http://admin.zaojiao.net'

// 获取地区
// export function getAreaListByLevel (params) {
//   return axios({
//     url: '/adminTeach/region',
//     method: 'GET',
//     params
//   })
// }

// 获取地区
// export function getAreaListByCode (params) {
//   return axios({
//     url: 'manage/region',
//     method: 'GET',
//     params,
//     baseURL
//   })
// }

export function getAreaListByLevel (params) {
  return axios({
    url: 'manage/region/level',
    method: 'GET',
    params,
    baseURL
  })
}

// export function getRegionCodeListByCode (code) {
//   return axios({
//     url: `manage/region/${code}`,
//     method: 'GET',
//     baseURL
//   })
// }
