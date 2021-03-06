//index.js
//获取应用实例
const app = getApp()
let clickCount = 0

Page({
  data: {
    batchNo: '',
    lastDate: '',
    src: ''
  },
  easterEgg: function() {
    clickCount ++;
    if(clickCount>3){
      wx.navigateTo({
        url: '/pages/index/easter-egg',
      })
    }
  },
  //事件处理函数
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
  checkLoginCode: function(loginCode) {
    let that = this;
    wx.request({
      url: app.globalData.serverUrl + '/mini/checkcode',
      header: {
        'login-code': loginCode
      },
      success(res) {
        console.log(res.data);
        if(res.data.status){
          console.log('值合法，赋值给全局变量');
          app.globalData.userCode = loginCode;
        }
        else{
          console.log('登录值非法，重新登录');
          that.login();
        }
      },
      fail (res){
        console.log('登录值非法，重新登录');
        that.login();
      }
    })
  },
  login: function(shareTicket) {
    wx.login({
      success: res => {
        app.globalData.userCode = res.code;
        wx.request({
          url: app.globalData.serverUrl + '/mini/oalogin?code=' + res.code + '&src=' + app.globalData.src + shareTicket,
          success: data => {
            console.log(data.data);
            //持久化存储，这里我们把code当为临时用户编号，openid只存储在服务器里
            wx.setStorage({
              key: "userCode",
              data: app.globalData.userCode

            })

          }
        })
      }
    })
  },
  onLoad: function (options) {
    console.log("来源：" + options.src);
    if (typeof (options.src) !="undefined") {
      app.globalData.src = options.src;
    }
    
    if(app.globalData.userCode == ''){
      console.log("全局变量中未取到数据");
      let that = this;
      wx.getStorage({
        key: 'userCode',
        success: function (res) {
          console.log("从持久存储内加载成功"+res.data)
          that.checkLoginCode(res.data);
        },
        fail(){
          console.log('从持久化存储中获得登录值失败,可能是首次登录，准备调用登录方法');
          // TODO:首次使用如果有分享来源进行记录
          if (typeof (app.globalData.shareTicket) !="undefined") {
            wx.getShareInfo({
              shareTicket: app.globalData.shareTicket,
              success: res => {
                if (res.errMsg == 'getShareInfo:ok') {
                  let shareTicket = '&share_data='+ encodeURIComponent(res.encryptedData) + '&iv=' + encodeURIComponent(res.iv)
                  console.log(shareTicket)
                  that.login(shareTicket);
                }
              }
            })
          }
          else {
            that.login();
          }
        }
      })
    }
    // 打开分享回调时票据功能
    wx.showShareMenu({
      withShareTicket: true
    })

    wx.request({
       url: app.globalData.serverUrl+'/mini/getlast',
       success: res => {      
         this.setData({
           lastDate: res.data.data
         });
         app.globalData.lastDate = res.data.data;
       }
    })
    
  },
    /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    return {
      title: '帮您验证批号',
      path: '/pages/index/index?src=share',
      imageUrl: '/resources/images/400x340.jpg'
    }
  }
})
