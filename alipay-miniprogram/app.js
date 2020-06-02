let util = require('./utils/utils.js')
App({
  globalData: {
    lastDate: null,
    // serverUrl: "http://127.0.0.1:8000",
    // serverUrl: "http://vaccine.guohai.org",
    serverUrl: "https://api.vaccine.pub",
    downloadUrl: "https://static.vaccine.pub",
    userCode: '',
    userAvatar: '',
    userNick: '',
    isAllAuth: false,
    src: '',
    version: '0.9.5',
    shareTicket: undefined
  },
  onLaunch(options) {
    // 第一次打开
    let userInfo = util.getUserInfo()
    if(null != userInfo) {
      this.globalData.userCode = userInfo['code']
      this.globalData.userAvatar = userInfo['avatar']
      this.globalData.userNick = userInfo['nick']
      this.globalData.isAllAuth = true
    }

  },
  onShow(options) {
    // 从后台被 scheme 重新打开
    // options.query == {number:1}
  },
});
