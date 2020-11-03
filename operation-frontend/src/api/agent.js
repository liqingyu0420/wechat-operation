import { axios } from '@/utils/request'

// 临时登录
export function temporary (data) {
  return axios({
    url: '/adminTeach/agent/login',
    method: 'POST',
    data
  })
}

// 代理商列表
export function getAgentList (params) {
  return axios({
    url: 'manage/agentUsers',
    method: 'GET',
    params
  })
}

// 查询代理区域
export function getAgentAreaById (id) {
  return axios({
    url: `manage/agentUser/${id}/regions`,
    method: 'GET'
  })
}

// 删除代理区域
export function delAgentAreaById (id) {
  return axios({
    url: `manage/agentUser/region/${id}`,
    method: 'DELETE'
  })
}

// 开启关闭代理区域直播
export function changeLiveStatus (params) {
  return axios({
    url: `manage/agentUser/region/live`,
    method: 'PUT',
    data: params.code,
    params: {
      state: params.state
    }
  })
}

// 添加代理区域
export function saveOrUpdateAgentArea (data) {
  return axios({
    url: `manage/agentUser/region`,
    method: 'POST',
    data
  })
}

// 代理的学校
export function getAgentSchoolById (id, params) {
  return axios({
    url: `manage/agentUser/${id}/schools`,
    method: 'GET',
    params
  })
}

// 获取代理商列表
export function getPartnerList (params) {
  return axios({
    url: '/manage/officialPartner/getAll',
    method: 'GET',
    params
  })
}

// 添加/修改代理商
export function savaOrUptPartner (data) {
  return axios({
    url: 'manage/officialPartner',
    method: data.id ? 'PUT' : 'POST',
    data
  })
}

export function handlePartnerDetail (id) {
  return axios({
    url: `manage/officialPartner/${id}`,
    method: 'GET'
  })
}

// 删除合作商
export function delPartnerById (id) {
  return axios({
    url: `manage/officialPartner/${id}`,
    method: 'DELETE'
  })
}

const baseURL = 'https://api.zaojiao.net'

// 查询邀请商
export function getInvite (params) {
  return axios({
    url: `/adminTeach/agent`,
    method: 'GET',
    baseURL,
    params
  })
}
// 查询邀请商详情
export function getInviteDetail (id) {
  return axios({
    url: `/adminTeach/agent/${id}`,
    method: 'GET',
    baseURL,
    id
  })
}
// 增加/修改 邀请商
export function postWithPutInvite (data) {
  return axios({
    url: `/adminTeach/agent`,
    method: data.id ? 'PUT' : 'POST',
    baseURL,
    data
  })
}

// 删除邀请商
export function deleteInvite (id) {
  return axios({
    url: `/adminTeach/agent/${id}`,
    method: 'DELETE',
    baseURL
  })
}
// 查询入园邀请码
export function getInviteCode (params) {
  return axios({
    url: `/adminTeach/agent/invite/code`,
    method: 'GET',
    baseURL,
    responseType: 'arraybuffer',
    params
  })
}
