//app.js
App({

  globalData: {
    lastDate: null,
    // serverUrl: "https://vaccine.zhongdaiqi.com",
    serverUrl: "https://api.vaccine.pub",
    downloadUrl: "https://static.vaccine.pub",
    userCode: '',
    src: '',
    version: '0.8.5',
    shareTicket: undefined
  },
  onLaunch: function (options) {
    console.log("onLauch:"+options.shareTicket)

  },
  onShow: function(options){
    // this.globalData.shareTicket = options.shareTicket
    
  }
})