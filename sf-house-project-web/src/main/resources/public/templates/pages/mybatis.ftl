<html>
<head>
    <title> Mybatis 自动生成代码</title>
    <!--SyntaxHighlighter高亮代码使用-->
    <script type="text/javascript" src="../../syntaxhighlighter_3.0.83/scripts/shCore.js"></script>
    <script type="text/javascript" src="../../syntaxhighlighter_3.0.83/scripts/shBrushJava.js"></script>
    <script type="text/javascript" src="../../syntaxhighlighter_3.0.83/scripts/shBrushXml.js"></script>
    <link type="text/css" rel="stylesheet" href="../../syntaxhighlighter_3.0.83/styles/shCoreDefault.css">
    <script type="text/javascript">
        SyntaxHighlighter.config.bloggerMode = true;
        /*博客模式。如果在博客网上使用该插件，因为通常博主习惯用
        替换所有的新行(’\n’)，这会造成SyntaxHighlighter 插件无法拆开每一行。开启此选项内部会将
        替换为新行’\n’*/
        SyntaxHighlighter.all();
    </script>
</head>
<body>

<form id="mybatis" action="/mybatis/gen" method="POST">
    <br>数据库信息:<br>
    数据库名：<input type="text" name="db_schema" value="${(param.db_schema)!'test_base_mapper'}" size="100"><br>
    数据库链接：<input type="text" name="db_url"
                 value="${(param.db_url)!'jdbc:mysql://127.0.0.1:3306/test_base_mapper?characterEncoding=UTF-8&useSSL=false'}"
                 size="100"><br>
    用户名：<input type="text" name="db_user" value="${(param.db_user)!'root'}" size="100"><br>
    密码：<input type="text" name="db_pwd" value="${(param.db_pwd)!'#/d5)anzaVlN'}" size="100"><br>
    表名：<input type="text" name="table_names" value="${(param.table_names)!'test'}" size="100"><br>
    <br>domain:<br>
    <input type="radio" name="gen_domain" value="true" checked>生成
    <input type="radio" name="gen_domain" value="false">不生成
    <br>
    包路径：<input type="text" name="domain_package" value="${(param.domain_package)!'sf.house'}"
               size="100"><br>
    <br>dto:<br>
    <input type="radio" name="gen_dto" value="true">生成
    <input type="radio" name="gen_dto" value="false" checked>不生成
    <br>
    包路径：<input type="text" name="dto_package" value="${(param.dto_package)!'sf.house'}" size="100"><br>
    <br>mapper:<br>
    <input type="radio" name="gen_mapper" value="true" checked>生成
    <input type="radio" name="gen_mapper" value="false">不生成
    <br>
    包路径：<input type="text" name="mapper_dao_package" value="${(param.mapper_dao_package)!'sf.house'}"
               size="100"><br>
    sqlId：<input type="text" name="mapper_sql_ids"
                 value="${(param.mapper_sql_ids)!'create,creates,list,paging,count,update,load,loads,delete,deletes'}"
                 size="100"><br>
    <br>作者:<br>
    <input type="text" name="author" value="${(param.author)!'hznijianfeng'}" size="10"><br>
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


<#if fileVos?exists>
    <#list fileVos as fileVo>
    <br/>
    + ${fileVo.fileName}
    <a href="#" onclick="toggle('content${fileVo.fileName}')"> 展开</a>
    <a href="#" onclick="copy('copy${fileVo.fileName}')"> 复制</a>
    <textarea id="copy${fileVo.fileName}" style='opacity: 0;position: absolute;'>${fileVo.fileContent}</textarea>
    <br/>
    <div id="content${fileVo.fileName}" style="display: none;">
        <#if fileVo.fileName?index_of('.xml')!=-1>
            <pre class="brush: xml;">${fileVo.fileContent}</pre>
        </#if>
        <#if fileVo.fileName?index_of('.java')!=-1>
            <pre class="brush: java;">${fileVo.fileContent}</pre>
        </#if>
    </div>
    + ${fileVo.fileName}
    <a href="#" onclick="toggle('content${fileVo.fileName}')"> 收起</a>
    <a href="#" onclick="copy('copy${fileVo.fileName}')"> 复制</a>
    <br/>
    </#list>
</#if>

</body>
</html>