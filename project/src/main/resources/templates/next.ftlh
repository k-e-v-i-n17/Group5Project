<!DOCTYPE html>
<html>
<head>
    <title>Group5</title>
    <link rel="stylesheet" href="initialStyle.css">
</head>
<body>
<p>index</p>
<ul><p>Data:</p>
    <#if data??>
    <#list data as point>
    <li>
        <p>${point}</p>
    </li>
    <#else>
    <p>You don't have any data.</p>
</#list>
</#if>
</ul>
        </body>
        </html>
