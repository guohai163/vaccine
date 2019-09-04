//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    batchNo: '',
    lastDate: ''
  },
  //事件处理函数
  search: function() {
    console.log(this.data.batchNo);
    if (this.data.batchNo.length<4){
      wx.showToast({
        title: '请确认所输入批次号！',
        icon: 'none',
        duration: 2000
      })
    }else{

      wx.navigateTo({
        url: '/pages/vaccine/result?query='+this.data.batchNo
      });
    }
  },
  batchNoInput: function(e) {
    this.setData({
      batchNo: e.detail.value.replace(/\s+/g, '').replace(/%/g,'')
    });
  },
  onLoad: function () {
    wx.request({
      url: app.globalData.serverUrl+'/getlast',
      success: res => {
        
        this.setData({
          lastDate: res.data.data
        });
        app.globalData.lastDate = res.data.data;
      }
    })
  }
})
