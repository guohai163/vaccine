const app = getApp();
Page({
  data: {
    vaccineData:null,
    showLoading:true,
    showError: true,
    resultCount: 0
  },
  onLoad(options) {
    console.info(options)
    console.info(app.globalData.userCode)
    my.request({
      url: app.globalData.serverUrl + '/mini/search/' + options.query + '?formId=' + options.formId,
      method: 'GET',
      headers:{
        'login-code': app.globalData.userCode
      },
      success: (result) => {
        console.info(result)
        console.info(result.status)
        console.info(result.data.status)
        console.info(result.data.data.length)
        if(result.status == 200 && result.data.status == true && result.data.data.length>0){
          this.setData({
            vaccineData: result.data.data,
            showLoading: false
          })
        }
        else {
          this.setData({
            showLoading: false,
            showError: false
          })
        }
      }
    });
  },
  more(event) {
    my.navigateTo({
      url: '/pages/vaccine/resultmore?data=' + JSON.stringify(this.data.vaccineData[event.currentTarget.id]).replace('&','%26')
    });
  }
});