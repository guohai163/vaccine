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
      wx.request({
        url: 'https://vaccine.zhongdaiqi.com/search/'+this.data.batchNo,
        success (res) {
          console.log(res.data);
          if (res.data.status) {
            console.log(JSON.stringify(res.data.data));
            wx.navigateTo({
              url: '/pages/vaccine/result?data=' + JSON.stringify(res.data.data),
            })
          }
        }
      });
    }
  },
  batchNoInput: function(e) {
    this.setData({
      batchNo: e.detail.value.replace(/\s+/g, '')
    });
  },
  onLoad: function () {
    wx.request({
      url: 'https://vaccine.zhongdaiqi.com/getlast',
      success: res => {
        console.log(res.data.data);
        
        this.setData({
          lastDate: res.data.data
        });
        app.globalData.lastDate = res.data.data;
      }
    })
  }
})
