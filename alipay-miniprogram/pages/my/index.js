const app = getApp();
Page({
  data: {
    isLogin: app.globalData.isAllAuth,
    userAvatar: '',
    userNick: ''
  },
  onLoad() {
    console.info(app.globalData.isAllAuth)
  },
  onGetAuthorize() {
    my.getOpenUserInfo({
      success: (res) => {
        let userData = JSON.parse(res.response).response;
        console.info(userData)
        console.info(userData)
        // TODO: 请求服务器，记录用户明细信息
        this.setData({
          userAvatar: userData.avatar,
          userNick: userData.nickName,
          isLogin: true
        })
        app.globalData.isAllAuth = true;
      },
      fail: (res) => {
        console.info(res)
      }
    });
  },
  onAuthError(e) {
    console.info(e)
  },
  onShareAppMessage() {
    // 返回自定义分享信息
    return {
      title: '疫苗批号查询',
      desc: '通过疫苗批号查询疫苗批签情况',
      path: 'pages/index/index',
    };
  }
})