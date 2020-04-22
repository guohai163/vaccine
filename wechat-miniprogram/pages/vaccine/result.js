// pages/vaccine/result.js
//获取应用实例
const app = getApp()


Page({

  /**
   * 页面的初始数据
   */
  data: {
    vaccineData:null,
    showLoading:true,
    showError: true,
    resultCount: 0
  },

  more:function(event) {
    // console.log(JSON.stringify(this.data.vaccineData[event.currentTarget.id]).replace('&','%26'));
    wx.navigateTo({
      url: '/pages/vaccine/resultmore?data=' + JSON.stringify(this.data.vaccineData[event.currentTarget.id]).replace('&','%26'),
    })


  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    wx.request({
      url: app.globalData.serverUrl +'/mini/search/' + options.query+'?formId='+options.formId,
      header: {
        'login-code': app.globalData.userCode
      },
      success: res=>{
        if(res.data.status==false || res.data.data.length<1){
          this.setData({
            showLoading: false,
            showError: false
          })
        }
        this.setData({
          vaccineData: res.data.data,
          showLoading: false,
          resultCount: res.data.data.length
        });
      }
    })

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})