let util = require('../../utils/utils.js')
const app = getApp();
Page({
  data: {
    vaccineData:null,
    downProgress:0,
    downDisabled:false,
    loginCode: '',
    issave: true
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
    
    let vaccine_result = decodeURIComponent(options.data);
    console.info(options)
    this.setData({
      vaccineData: JSON.parse(vaccine_result.replace('%26','&')),
      issave: options.issave
    });
  },
  onReady() {
    console.info(this.data.issave)
    if(this.data.issave == 'true') {
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
      // 取出存储中内容并增加新的查询结果进入
      let historyCache = my.getStorageSync({
        key: 'myHistory'
      });
      console.info(historyCache)
      if(historyCache.data != null){
        this.data.vaccineData['squeryDate'] = util.formatTime(new Date())
        historyCache.data.splice(0, 0, this.data.vaccineData)
        console.info(historyCache)
        my.setStorage({
          key: 'myHistory',
          data: historyCache.data
        });
      }

    }
    
  }
})