const getters = {
  isMobile: state => state.app.isMobile,
  lang: state => state.app.lang,
  theme: state => state.app.theme,
  color: state => state.app.color,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  nickname: state => state.user.name,
  welcome: state => state.user.welcome,
  roles: state => state.user.roles,
  userInfo: state => state.user.userInfo,
  addRouters: state => state.permission.addRouters,
  multiTab: state => state.app.multiTab,
  account: state => state.account.accountList,
  tagList: state => state.tag.tagList,
  copyInformation: state => state.information.copyInformation
}

export default getters
