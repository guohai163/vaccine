

<table style="BORDER-COLLAPSE: collapse" bordercolor="#000000" cellspacing="0" cellpadding="0" align="left" border="1">
    <tbody>
    <tr>
<!--        <td style="HEIGHT: 28px; WIDTH: 114px">-->
<!--            <p align="center">名称</p>-->
<!--        </td>-->
        <td style="HEIGHT: 28px; WIDTH: 49px">
            <p align="center">缩写</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 45px">
            <p align="center">出生时</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 28px">
            <p align="center">1月</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 28px">
            <p align="center">2月</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 30px">
            <p align="center">3月</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 41px">
            <p align="center">4月</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 41px">
            <p align="center">5月</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 43px">
            <p align="center">6月</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 52px">
            <p align="center">8月</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 45px">
            <p align="center">9月</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 45px">
            <p align="center">18月</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 46px">
            <p align="center">2岁</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 47px">
            <p align="center">3岁</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 47px">
            <p align="center">4岁</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 47px">
            <p align="center">5岁</p>
        </td>
        <td style="HEIGHT: 28px; WIDTH: 43px">
            <p align="center">6岁</p>
        </td>
    </tr>
    <#list mapVaccine as key, value>
    <#if value.state>
    <tr>
<!--        <td style="HEIGHT: 15px; WIDTH: 114px">-->
<!--            <p align="left">${value.des}</p>-->
<!--        </td>-->
        <td  style="HEIGHT: 15px; WIDTH: 49px">
            <p align="center">${value.keyName}</p>
        </td>
        <td class="monthage0" style="HEIGHT: 15px; WIDTH: 45px">
            <p align="center">${value.accinationMonthAge[0]}</p>
        </td>
        <td class="monthage1" style="HEIGHT: 15px; WIDTH: 28px">
            <p align="center">${value.accinationMonthAge[1]}</p>
        </td>
        <td class="monthage2" style="HEIGHT: 15px; WIDTH: 28px">
            <p align="center">${value.accinationMonthAge[2]}</p>
        </td>
        <td class="monthage3" style="HEIGHT: 15px; WIDTH: 30px">
            <p align="center">${value.accinationMonthAge[3]}</p>
        </td>
        <td class="monthage4" style="HEIGHT: 15px; WIDTH: 41px">
            <p align="center">${value.accinationMonthAge[4]}</p>
        </td>
        <td class="monthage5" style="HEIGHT: 15px; WIDTH: 41px">
            <p align="center">${value.accinationMonthAge[5]}</p>
        </td>
        <td class="monthage6" style="HEIGHT: 15px; WIDTH: 43px">
            <p align="center">${value.accinationMonthAge[6]}</p>
        </td>
        <td class="monthage8" style="HEIGHT: 15px; WIDTH: 52px">
            <p align="center">${value.accinationMonthAge[7]}</p>
        </td>
        <td class="monthage9" style="HEIGHT: 15px; WIDTH: 45px">
            <p align="center">${value.accinationMonthAge[8]}</p>
        </td>
        <td class="monthage18" style="HEIGHT: 15px; WIDTH: 45px">
            <p align="center">${value.accinationMonthAge[9]}</p>
        </td>
        <td class="monthage24" style="HEIGHT: 15px; WIDTH: 46px">
            <p align="center">${value.accinationMonthAge[10]}</p>
        </td>
        <td class="monthage36" style="HEIGHT: 15px; WIDTH: 47px">
            <p align="center">${value.accinationMonthAge[11]}</p>
        </td>
        <td class="monthage48" style="HEIGHT: 15px; WIDTH: 47px">
            <p align="center">${value.accinationMonthAge[12]}</p>
        </td>
        <td class="monthage60" style="HEIGHT: 15px; WIDTH: 47px">
            <p align="center">${value.accinationMonthAge[13]}</p>
        </td>
        <td class="monthage72" style="HEIGHT: 15px; WIDTH: 43px">
            <p align="center">${value.accinationMonthAge[14]}</p>
        </td>
    </tr>
    </#if>
</#list>
</tbody>
</table>
<#if brdate?exists>
<script>

Date.prototype.addDays = function(days) {
    this.setDate(this.getDate() + parseInt(days));
    return this;
};

    function checkDateToColor(styleClass,date1,date2,month, scope) {

        if(date2<date1.addDays(month*30)) {
            styleClass.css('backgroundColor', '#87CEFA');
        } else if(date2<=date1.addDays(month*30+scope)) {
            styleClass.css('backgroundColor', '#FF4500');
        }else {
            styleClass.css('backgroundColor', '#00FF7F');
        }

    }



    var nowTime = new Date();
    var brTime = new Date("${brdate}".replace("-", "/"));

    checkDateToColor($('.monthage0'),brTime, nowTime, 0, 1);
    checkDateToColor($('.monthage1'),brTime, nowTime, 1, 30);
    checkDateToColor($('.monthage2'),brTime, nowTime, 2, 30);
    checkDateToColor($('.monthage3'),brTime, nowTime, 3, 30);
    checkDateToColor($('.monthage4'),brTime, nowTime, 4, 30);
    checkDateToColor($('.monthage5'),brTime, nowTime, 5, 30);
    checkDateToColor($('.monthage6'),brTime, nowTime, 6, 30);
    checkDateToColor($('.monthage8'),brTime, nowTime, 8, 30);
    checkDateToColor($('.monthage9'),brTime, nowTime, 9, 30);
    checkDateToColor($('.monthage18'),brTime, nowTime, 18, 30);
    checkDateToColor($('.monthage24'),brTime, nowTime, 24, 365);
    checkDateToColor($('.monthage36'),brTime, nowTime, 36, 365);
    checkDateToColor($('.monthage48'),brTime, nowTime, 48, 365);
    checkDateToColor($('.monthage60'),brTime, nowTime, 60, 365);
    checkDateToColor($('.monthage72'),brTime, nowTime, 72, 365);

</script>
</#if>