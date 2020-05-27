App({
  globalData: {
    lastDate: null,
    // serverUrl: "http://127.0.0.1:8000",
    // serverUrl: "http://vaccine.guohai.org",
    serverUrl: "https://api.vaccine.pub",
    downloadUrl: "https://static.vaccine.pub",
    userCode: '',
    isAllAuth: false,
    src: '',
    version: '0.8.5',
    shareTicket: undefined
  },
  onLaunch(options) {
    // 第一次打开
    // options.query == {number:1}
    console.info('App onLaunch');
  },
  onShow(options) {
    // 从后台被 scheme 重新打开
    // options.query == {number:1}
  },
});
