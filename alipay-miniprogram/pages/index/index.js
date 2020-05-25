const app = getApp();
Page({
  data: {
    lastDate: ''
  },
  onLoad(query) {
    // 页面加载
    console.info(`Page onLoad with query: ${JSON.stringify(query)}`);
    // 请求服务器最新版本日期
    my.request({
      url: app.globalData.serverUrl + '/mini/getlast',
      method: 'GET',
      success: (result) => {
        this.setData({
          lastDate: result.data.data
        })
      }
    });
    // 获取用户信息
    my.getAuthCode({
      scopes: 'auth_base',
      success: (res) => {
        console.info(res.authCode)
        my.request({
          url: app.globalData.serverUrl + '/mini/ali_login?code=' + res.authCode,
          method: 'GET',
          success: (res) => {
            console.info(result)
            app.globalData.userCode = res.authCode
          },
          fail: function(res) {
            console.info(res);
          }
        })
      }
    });
  },
  formSubmit(e) {
    // 表单提交，准备跳转页面
    console.info(my.canIUse ('form.report-submit'))
    console.info(e.detail.formId)
    console.info(e.detail.value.batchno.length)
    if (e.detail.value.batchno.length < 4) {
      my.showToast({
        type: 'fail',
        content: '请确认所输入批次号！',
        duration: 2000
      });
    }
    else {
      my.navigateTo({
        url: '/pages/vaccine/result?query=' + e.detail.value.batchno + '&formId=' + e.detail.formId
      });
    }
    
  },
  onReady() {
    // 页面加载完成
  },
  onShow() {
    // 页面显示
  },
  onHide() {
    // 页面隐藏
  },
  onUnload() {
    // 页面被关闭
  },
  onTitleClick() {
    // 标题被点击
  },
  onReachBottom() {
    // 页面被拉到底部
  },
  onShareAppMessage() {
    // 返回自定义分享信息
    return {
      title: '疫苗批号验证',
      desc: '通过疫苗批号查询疫苗批签情况',
      path: 'pages/index/index',
    };
  },
});
