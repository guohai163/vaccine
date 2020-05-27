const app = getApp();
Page({
  data: {
    vaccineData:null,
    downProgress:0,
    downDisabled:false,
    loginCode: ''
  },
  copy: function(event) {
    console.info(event.target.dataset.url)
    my.setClipboard({
      text: event.target.dataset.url,
      success(){
        my.showToast({
          type: 'success',
          content: '原始地址复制成功',
          duration: 2000
        })
      },
      fail(){
        console.info('调用失败')
      }
    })
  },
  onLoad(options) {
    console.info(options)
    let vaccine_result = decodeURIComponent(options.data);
    this.setData({
      vaccineData: JSON.parse(vaccine_result.replace('%26','&'))
    });
  },
  onReady() {
    console.info(app.globalData.userCode)
    console.info(this.data.vaccineData.code)
    my.request({
      url: app.globalData.serverUrl + '/mini/postvcode',
      method: 'POST',
      data: JSON.parse('{"vaccineCode": '+this.data.vaccineData.code+'}'),
      headers:{
        'login-code': app.globalData.userCode
      },
      success: (result) => {
        console.info(result)
      }
    })
  }
})