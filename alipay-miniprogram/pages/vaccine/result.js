const app = getApp();
Page({
  data: {
    vaccineData:null,
    showLoading:true,
    showError: true,
    resultCount: 0
  },
  onLoad(options) {
    console.info(app.globalData.serverUrl + '/mini/search/' + options.query.replace('/','') + '?formId=' + options.formId)
    my.request({
      url: app.globalData.serverUrl + '/mini/search/' + options.query.replace('/','') + '?formId=' + options.formId,
      method: 'GET',
      headers:{
        'login-code': app.globalData.userCode
      },
      success: (result) => {
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
      url: '/pages/vaccine/resultmore?issave=true&data=' + JSON.stringify(this.data.vaccineData[event.currentTarget.id]).replace('&','%26')
    });
  }
});