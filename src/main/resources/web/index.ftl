<html>
<head>
    <title>疫苗计算器</title>
    <link rel="stylesheet" href="/css/jquery-ui.css">
    <script src="/js/jquery-3.4.1.min.js"></script>
    <script src="/js/jquery-ui.min.js"></script>
<!--    <link rel="stylesheet" href="/css/style.css">-->
    <script>
      $(function() {
        $( "#datepicker" ).datepicker({dateFormat: "yy-mm-dd" } );
      });
     </script>
</head>
<body>

<div style="float: left;" id="level1">
    <p align="center">国家免疫规划疫苗儿童免疫程序表（2016年版）</p>
    <div><#include "inc/vaccinetable.ftl"></div>
    <button onclick="$('#level1').hide(); $('#level2').show();">下一步</button>

</div>

<div style="float: left; display: none;" id="level2">

    <p>下表二类疫苗，会按你的选择帮你生成疫苗时间列表（部分疫苗相冲突无法同时选择）：</p>
    <form action="/result">

        <table style=" BORDER-COLLAPSE: collapse" bordercolor="#000000" cellspacing="0" cellpadding="0" align="left" border="1">
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
                <td><input type="checkbox" name="level2vaccine" value="13" /></td>
                <td>13价肺炎</td>
                <td>。</td>
            </tr>
            </tbody>
        </table>

        新生儿出生日期：<input type="text" name="brdate" id="datepicker" /> <br/>
        <input type="submit" value="Submit" />
    </form>
</div>

</body>
</html>