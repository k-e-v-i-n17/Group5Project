<!DOCTYPE html>
<html>
<head><title>Group5</title>
    <link rel="stylesheet" href="initialStyle.css">
    <link rel="icon" type="image/x-icon" href="https://avatars.githubusercontent.com/u/32927890?v=4">
</head>
<body>
<ul class="headerBar">
    <li class="li">Welcome to the Group 5</li>
    <form action="/home" method="POST">
        <input class="li button" type="submit" value="Home" name="add">
    </form>
    <form action="/rawdata" method="POST">
        <input class="li active button" type="submit" value="Raw Data" name="add">
    </form>
    <form action="/button3" method="POST">
        <input class="li button" type="submit" value="Button 3" name="add">
    </form>
</ul>
<div class="main">
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
</div>
<div class="footer"><p>Ruairí Donaghey</p></div>
</body>
</html>
