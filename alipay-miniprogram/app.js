App({
  globalData: {
    lastDate: null,
    // serverUrl: "http://localhost:8000",
    serverUrl: "https://api.vaccine.pub",
    downloadUrl: "https://static.vaccine.pub",
    userCode: '',
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
