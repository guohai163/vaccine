<html>
<head>
    <title>疫苗计算器</title>
    <script src="/js/jquery-3.4.1.min.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"></meta>
    <meta name="apple-touch-fullscreen" content="YES"></meta>
    <meta name="apple-mobile-web-app-capable" content="yes"></meta>
</head>
<body>

<div style="float: left;">
    <div>出生日期：${brdate} ,建议横屏查看。<a href="https://github.com/guohai163/vaccine"  target="view_window">view source</a> </div>
    <div><#include "inc/vaccinetable.ftl"></div>

<div>
    <table>
        <tr>
            <td style="HEIGHT: 15px; WIDTH: 15px;background-color:#87CEFA"></td>
            <td style="WIDTH: 100px;">未到日期</td>
            <td style="HEIGHT: 15px; WIDTH: 15px;background-color:#FF4500"></td>
            <td style="WIDTH: 100px;">当前日期</td>
            <td style="HEIGHT: 15px; WIDTH: 15px;background-color:#00FF7F"></td>
            <td style="WIDTH: 100px;">已过日期</td>
        </tr>
    </table>


</div>


<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?5773c67c94e0364603537c15d9874e45";
  var s = document.getElementsByTagName("script")[0];
  s.parentNode.insertBefore(hm, s);
})();
</script>


</body>
</html>