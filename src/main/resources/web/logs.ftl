<html>
<head>

    <#include "inc/head.ftl">
</head>
<body>

<div style="float: left;">
<table  border="1" cellspacing="0">
    <#list accesslogs as log>
        <tr>
        <td>${log.code}</td>
        <td>${log.openId}</td>
        <td>${log.userAgent}</td>
        <td><#if log.userIp?exists><a href="https://www.ipip.net/ip/${log.userIp}.html" target="view_window">${log.userIp}</a></#if></td>
        <td>${log.serviceCategory}</td>
        <td>${log.queryParam}</td>
        <td><#if log.accessFromid?exists>${log.accessFromid}</#if></td>
        <td>${log.queryResultNum}</td>
        <td>${log.accessDate?string("yyyy.MM.dd HH:mm:ss")}</td>
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