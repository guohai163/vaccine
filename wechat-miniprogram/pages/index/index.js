//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    batchNo: ""
  },
  //事件处理函数
  search: function() {
    console.log(this.data.batchNo);
    wx.request({
      url: 'https://vaccine.zhongdaiqi.com/search/'+this.data.batchNo,
      success (res) {
        console.log(res.data);
        if (res.data.status) {
          console.log("查询正确");
          wx.navigateTo({
            url: '/pages/vaccine/result?data=' + JSON.stringify(res.data.data),
          })
        }
      }
    })
  },
  batchNoInput: function(e) {
    this.setData({
      batchNo:e.detail.value
    });
  },
  onLoad: function () {
    
  }
})
