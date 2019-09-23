<html>
<head>

    <#include "inc/head.ftl">
</head>
<body>

<div style="float: left;">
<table  border="1" cellspacing="0">
    <#list userlist as user>
        <tr>
        <td>${user.openId}</td>
        <td><#if user.src?exists>${user.src}</#if></td>
        <td>${user.createTime?string("yyyy.MM.dd HH:mm:ss")}</td>
        </tr>
    </#list>
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