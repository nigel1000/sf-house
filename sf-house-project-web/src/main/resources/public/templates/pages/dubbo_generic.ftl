<html>
<head>
    <title> Dubbo Generic 泛化调用</title>
</head>
<body>

<form id="dubbo" action="/dubbo/generic/gen" method="POST">
    <br>服务类信息:<br>
    类名：<input type="text" name="className" value="${(param.className)!'com.outer.goods.GoodsReadApiFacade'}" size="100"><br>
    方法名：<input type="text" name="methodName" value="${(param.methodName)!'queryGoodsDesc'}" size="100"><br>
    入参类型：<input type="text" name="methodParamType" value="${(param.methodParamType)!'java.util.List'}" size="100"><br>
    入参值：<input type="text" name="methodParamValue" value="${(param.methodParamValue)!'[[59149962]]'}" size="100"><br>
    <br>环境参数:<br>
    组名：<input type="text" name="group" value="${(param.group)!'user_test4jd'}" size="100"><br>
    版本：<input type="text" name="version" value="${(param.version)!'1.0'}" size="100"><br>
    注册中心地址：<input type="text" name="zkAddress" value="${(param.zkAddress)!'127.0.0.1'}" size="100"><br>
    注册中心端口：<input type="text" name="zkPort" value="${(param.zkPort)!'2181'}" size="100"><br>
    <br><input type="submit" value="山药当归枸杞 go!"><br>
</form>

<script type="text/javascript">

    function toggle(name) {
        var content = document.getElementById(name);
        var display = content.style.display;
        if (display == "none") {
            content.style.display = "block";
        } else {
            content.style.display = "none";
        }
    }

    function copy(name) {
//        var currentFocus = document.activeElement;
        var content = document.getElementById(name);
        content.focus();
        content.setSelectionRange(0, content.value.length);
        document.execCommand("copy", false, null);
//        currentFocus.focus();
    }

</script>

<#if result?exists>
    <br/>
    + 返回结果
    <a href="#" onclick="toggle('content')"> 展开</a>
    <a href="#" onclick="copy('copy')"> 复制</a>
    <textarea id="copy" style='opacity: 0;position: absolute;'>${result}</textarea>
    <br/>
    <div id="content" style="display: block;">
        <pre>${result}</pre>
    </div>
    <a href="#" onclick="toggle('content')"> 展开</a>
    <a href="#" onclick="copy('copy')"> 复制</a>
    <br/>
</#if>

</body>
</html>