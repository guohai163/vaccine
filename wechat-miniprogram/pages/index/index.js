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
  login: function() {
    wx.login({
      success: res => {
        app.globalData.userCode = res.code;
        wx.request({
          url: app.globalData.serverUrl + '/mini/oalogin?code=' + res.code + '&src=' + app.globalData.src,
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
    console.log(options.src);
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
          console.log('从持久化存储中获得登录值失败，准备调用登录方法');
          that.login();
        }
      })
    }

    wx.request({
      url: app.globalData.serverUrl+'/mini/getlast',
      success: res => {
        
        this.setData({
          lastDate: res.data.data
        });
        app.globalData.lastDate = res.data.data;
      }
    })
  }
})
