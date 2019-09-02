//app.js
App({
  onLaunch: function () {
    //配置初始值
    wx.request({
      url: 'https://vaccine.zhongdaiqi.com/getlast',
      success: res=>{
        console.log(res.data.data);
        // if(res.data.status){
        this.globalData.lastDate = res.data.data;
        // }else{
        //   this.globalData.lastDate='2019-09-01';
        // }
      }
    })
  },
  globalData: {
    lastDate: null
  }
})