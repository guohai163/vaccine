<html>
<head>
    <title>疫苗计算器</title>
</head>

${brdate}


<table style="BORDER-COLLAPSE: collapse" bordercolor="#000000" cellspacing="0" cellpadding="0" align="left" border="1">
    <tbody>
    <tr>
        <td style="HEIGHT: 28px; WIDTH: 114px">
            <p align="center">名称</p>
        </td>
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
            <td style="HEIGHT: 15px; WIDTH: 114px">
                <p align="left">${value.des}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 49px">
                <p align="center">${value.keyName}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 45px">
                <p align="center">${value.accinationMonthAge[0]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 28px">
                <p align="center">${value.accinationMonthAge[1]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 28px">
                <p align="center">${value.accinationMonthAge[2]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 30px">
                <p align="center">${value.accinationMonthAge[3]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 41px">
                <p align="center">${value.accinationMonthAge[4]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 41px">
                <p align="center">${value.accinationMonthAge[5]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 43px">
                <p align="center">${value.accinationMonthAge[6]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 52px">
                <p align="center">${value.accinationMonthAge[7]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 45px">
                <p align="center">${value.accinationMonthAge[8]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 45px">
                <p align="center">${value.accinationMonthAge[9]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 46px">
                <p align="center">${value.accinationMonthAge[10]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 47px">
                <p align="center">${value.accinationMonthAge[11]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 47px">
                <p align="center">${value.accinationMonthAge[12]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 47px">
                <p align="center">${value.accinationMonthAge[13]}</p>
            </td>
            <td style="HEIGHT: 15px; WIDTH: 43px">
                <p align="center">${value.accinationMonthAge[14]}</p>
            </td>
        </tr>
    </#if>
    </#list>
    </tbody>
</table>




</html>