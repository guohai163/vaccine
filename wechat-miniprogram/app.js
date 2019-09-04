//app.js
App({
  onLaunch: function () {
    wx.login({
      success: res=>{
        console.log(res.code);
        wx.request({
          url: this.globalData.serverUrl+'/mini/oalogin?code='+res.code,
          success: data=>{
            console.log(data);
          }
        })
      }
    })
  },
  globalData: {
    lastDate: null,
    serverUrl: "https://vaccine.zhongdaiqi.com"
  }
})