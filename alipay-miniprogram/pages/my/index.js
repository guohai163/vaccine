let util = require('../../utils/utils.js')
const app = getApp();
Page({
  data: {
    isLogin: false,
    userAvatar: '',
    userNick: '',
    userHistoryList: null
  },
  bindHistoryData() {
    let historyCache = my.getStorageSync({
      key: 'myHistory'
    });
    if(null == historyCache.data) {
      my.request({
        url: app.globalData.serverUrl + '/mini/getmylist',
        method: 'GET',
        headers: {
          'login-code': app.globalData.userCode
        },
        success: (result) => {
 
          if(result.data.status == true) {
            this.setData({
              userHistoryList: result.data.data
            })
            my.setStorage({
              key: 'myHistory',
              data: result.data.data
            });
          }
        }
      })
    }
    else {
      // 使用本地缓存数据
      this.setData({
        userHistoryList: historyCache.data
      })
    }

  },
  onShow() {

    if(this.data.isLogin){
      this.bindHistoryData();
    }
    
  },
  onLoad() {
    console.info('onLoad')
    // 检查全局变量是否有用户信息
    if(app.globalData.isAllAuth) {
      this.setData({
        userAvatar: app.globalData.userAvatar,
        userNick: app.globalData.userNick,
        isLogin: true
      })
      this.bindHistoryData();
    }
    else {
      my.request({
        url: app.globalData.serverUrl + '/mini/getuserinfo',
        method: 'GET',
        headers:{
          'login-code': app.globalData.userCode
        },
        success: (result) => {

          if(result.data.status) {
            this.setData({
              isLogin: true,
              userAvatar: result.data.data.avatar,
              userNick: result.data.data.nickName
            })
            app.globalData.isAllAuth = true;
            app.globalData.userAvatar = result.data.data.avatar;
            app.globalData.userNick = result.data.data.nickName;
            this.bindHistoryData();
            util.storyUserInfo({'avatar': result.data.data.avatar, 'nick': result.data.data.nickName, 'code': app.globalData.userCode})
          }
        }
      });
    }

  },
  onGetAuthorize() {
    my.getOpenUserInfo({
      success: (res) => {
        let userData = JSON.parse(res.response).response;
        console.info(userData)
        if(null == userData.nickName) {
          // 用户没有昵称
          userData.nickName = '匿名用户'
        }
        if(null == userData.avatar) {
          userData.avatar = '/resources/images/personal.png'
        }
        // TODO: 请求服务器，记录用户明细信息
        this.setData({
          userAvatar: userData.avatar,
          userNick: userData.nickName,
          isLogin: true
        })
        app.globalData.isAllAuth = true;
        app.globalData.userAvatar = userData.avatar;
        app.globalData.userNick = userData.nickName;
        my.request({
          url: app.globalData.serverUrl + '/mini/postuserinfo',
          method: 'POST',
          headers:{
            'login-code': app.globalData.userCode
          },
          data: userData,
          success: (result) => {
            console.info('请求服务器提交用户信息成功')
          }
        })
        this.bindHistoryData();
        util.storyUserInfo({'avatar': userData.avatar, 'nick': userData.nickName,  'code': app.globalData.userCode})
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
  },
  more(event) {
    my.navigateTo({
      url: '/pages/vaccine/resultmore?issave=false&data=' + JSON.stringify(this.data.userHistoryList[event.currentTarget.id]).replace('&','%26')
    });
  }
})