const app = getApp();
Page({
  data: {
    isLogin: app.globalData.isAllAuth,
    userAvatar: app.globalData.userAvatar,
    userNick: app.globalData.userNick
  },
  onLoad() {
    my.request({
      url: app.globalData.serverUrl + '/mini/getuserinfo',
      method: 'GET',
      headers:{
        'login-code': app.globalData.userCode
      },
      success: (result) => {
        console.info(result.data)
        if(result.data.status) {
          this.setData({
            isLogin: true,
            userAvatar: result.data.data.avatar,
            userNick: result.data.data.nickName
          })
          // TODO:写globalData
          app.globalData.isAllAuth = true;
          app.globalData.userAvatar = result.data.data.avatar;
          app.globalData.userNick = result.data.data.nickName
        }
      }
    });
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
        my.request({
          url: app.globalData.serverUrl + '/mini/postuserinfo',
          method: 'POST',
          headers:{
            'login-code': app.globalData.userCode
          },
          data: userData,
          success: (result) => {
            console.info(result)
          }
        })
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