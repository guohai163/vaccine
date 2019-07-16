<html>
<head>
    <title>疫苗计算器</title>
</head>

${brdate}

<#list mapVaccine as key, value>
    <#if value.state>
        ${value.keyName}
    </#if>


</#list>
</html>