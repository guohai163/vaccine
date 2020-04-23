// pages/vaccine/resultmore.js
//获取应用实例
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    vaccineData:null,
    downProgress:0,
    downDisabled:false
  },
  copy: function(event) {
    wx.setClipboardData({
      data: event.target.dataset.url,
      success(){
        wx.showToast({
          title: '原始地址复制成功',
          icon: 'success',
          duration: 2000
        })
      }
    })
  },
  downInstruction: function(event) {
    this.setData({
      downDisabled: true
    })
    let that = this;
    console.log(event.target.dataset.url)
    const downloadTask = wx.downloadFile({
      // 示例 url，并非真实存在
      url: event.target.dataset.url,
      showMenu: true,
      success: function (res) {
        that.setData({
          downDisabled: false
        })
        if (res.statusCode === 200) {
          const filePath = res.tempFilePath
          wx.openDocument({
            filePath: filePath,
            success: function (res) {
              console.log('打开文档成功')
            }
          })
        }
      }
    })
    downloadTask.onProgressUpdate((res) => {
      console.log('下载进度', res.progress)
      console.log('已经下载的数据长度', res.totalBytesWritten)
      console.log('预期需要下载的数据总长度', res.totalBytesExpectedToWrite)
      this.setData({
        downProgress: res.progress
      })
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let vaccine_result = decodeURIComponent(options.data);
    console.log(vaccine_result);
    this.setData({
      vaccineData: JSON.parse(vaccine_result.replace('%26','&'))
    });
    
    let instructionUrl = ''
    switch(this.data.vaccineData.productName){
      case '九价人乳头瘤病毒疫苗（酿酒酵母）':
        instructionUrl = app.globalData.downloadUrl + '/ab04d0128a09d128d5f6345569297d61.pdf'
        break;
      default:
        instructionUrl = ''
        break;
    }

    this.setData({
      instructionUrl: instructionUrl
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