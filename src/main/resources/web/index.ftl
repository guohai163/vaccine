<html>
<head>
    <title>疫苗计算器</title>
    <link rel="stylesheet" href="/css/jquery-ui.css"></link>
    <script src="/js/jquery-3.4.1.min.js"></script>
    <script src="/js/jquery-ui.min.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes"></meta>
    <meta name="apple-touch-fullscreen" content="YES"></meta>
    <meta name="apple-mobile-web-app-capable" content="yes"></meta>
    <!--    <link rel="stylesheet" href="/css/style.css">-->
    <script>
      $(function() {
        $( "#datepicker" ).datepicker({dateFormat: "yy-mm-dd" } );
      });

      function checkBrDate(){
       if($("#datepicker").val()=="") {
         alert("出生日期为必填！");
        return false;
       }
      }
     </script>
</head>
<body>

<div style="float: left;" id="level1">
    <p align="center">国家免疫规划疫苗儿童免疫程序表（2016年版）</p>
    <div><#include "inc/vaccinetable.ftl"></div>

    <button style="margin: 20px 200px;" onclick="$('#level1').hide(); $('#level2').show();">下一步</button>

</div>

<div style="float: left; display: none;" id="level2">

    <p>下表二类疫苗，会按你的选择帮你生成疫苗时间列表（部分疫苗相冲突无法同时选择）：</p>
    <form action="/result">
<div>
        <table style=" BORDER-COLLAPSE: collapse" cellspacing="0" cellpadding="0" align="left" border="1">
            <tbody>
            <tr>
                <td style="HEIGHT: 14px; WIDTH: 163px" >
                    <p align="center">是否使用</p>
                </td>
                <td style="HEIGHT: 14px; WIDTH: 163px" >
                    <p align="center">疫苗名称</p>
                </td>
                <td style="HEIGHT: 14px; WIDTH: 628px" >
                    <p align="center">疫苗说明</p>
                </td>
            </tr>
            <tr>
                <td><input type="checkbox" name="level2vaccine" value="DTaP-IPV/Hib" /></td>
                <td>五联</td>
                <td>吸附无细胞百白破灭活脊髓灰质炎和b型流感嗜血杆菌（结合）联合疫苗,可以同时联合5种疫苗，能从之前的12针缩减到4针。</td>
            </tr>
            <tr>
                <td><input type="checkbox" name="level2vaccine" value="PCV13" /></td>
                <td>13价肺炎</td>
                <td>13价肺炎球菌结合疫苗</td>
            </tr>
            </tbody>
        </table>
</div>
        <div>
        新生儿出生日期：<input type="text" name="brdate" id="datepicker" />
        </div>
        <div>
        <input style="margin:20px 150px;" type="submit" value="提交" onclick="return checkBrDate();" />
        </div>
    </form>
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