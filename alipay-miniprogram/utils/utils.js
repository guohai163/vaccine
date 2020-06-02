const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}
// 存储用户数据，过期时间一个月
const storyUserInfo = data => {
  let userInfo = {}
  userInfo['avatar'] = data['avatar']
  userInfo['nick'] = data['nick']
  userInfo['code'] = data['code']
  userInfo['expire'] = new Date().setMonth(new Date().getMonth() + 1)
  console.info(userInfo)
  my.setStorage({
    key: 'userInfo',
    data: userInfo
  });
}
//取用户数据，并检查是否过期
const getUserInfo = () => {
  let userInfo = my.getStorageSync({
    key: 'userInfo'
  });
  if(userInfo.data == null) {
    console.info('缓存中没有用户数据')
    return null
  }
  if(new Date() > userInfo.data['expire']) {
    console.info('缓存中用户数据已过期')
    my.removeStorage({
      key: 'userInfo'
    });
    return null
  }
  if(null == userInfo.data['nick']) {
    console.info('缓存数据异常')
    my.removeStorage({
      key: 'userInfo'
    });
    return null
  }
  return userInfo.data
}
module.exports = {
  formatTime: formatTime,
  storyUserInfo: storyUserInfo,
  getUserInfo: getUserInfo
}
