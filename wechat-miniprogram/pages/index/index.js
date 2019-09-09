//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    batchNo: '',
    lastDate: '',
    src: ''
  },
  easterEgg: function() {
    wx.navigateTo({
      url: '/pages/index/easter-egg',
    })
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
  formSubmit: function(e) {
    console.log(e.detail.formId);
    if (this.data.batchNo.length < 4) {
      wx.showToast({
        title: '请确认所输入批次号！',
        icon: 'none',
        duration: 2000
      })
    } else {

      wx.navigateTo({
        url: '/pages/vaccine/result?query=' + this.data.batchNo + '&formId=' + e.detail.formId
      });
    }
  },
  batchNoInput: function(e) {
    this.setData({
      batchNo: e.detail.value.replace(/\s+/g, '').replace(/%/g,'')
    });
  },
  onLoad: function (options) {
    console.log(options.src);
    if (typeof (options.src) !="undefined") {
      app.globalData.src = options.src;
    }
    if(app.globalData.userCode == ''){
      console.log("全局变量中未取到数据");
      wx.getStorage({
        key: 'userCode',
        success: function (res) {
          console.log("从持久存储内加载成功"+res.data)
          app.globalData.userCode = res.data;
        },
        fail: function (res) {
          wx.login({
            success: res => {
              app.globalData.userCode = res.code;
              wx.request({
                url: app.globalData.serverUrl + '/mini/oalogin?code=' + res.code + '&src=' + app.globalData.src,
                success: data => {
                  //持久化存储，这里我们把code当为临时用户编号，openid只存储在服务器里
                  wx.setStorage({
                    key: "userCode",
                    data: app.globalData.userCode

                  })
                }
              })
            }
          })
        }
      })
    }
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
