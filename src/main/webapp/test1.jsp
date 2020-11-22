<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%--本jsp保存一些经常重复使用的代码--%>

    <%--base标签--%>



    <base href="<%=basePath%>">

    <%--  ajax  --%>
    <script type="text/javascript">

        $.ajax({

            url:"",
            //规定要发送到服务器的数据，可以是：string， 数组，多数是 json
            data:{

            },
            type:"get/post",
            dataType:"json",
            success:function () {

            }
        })

        //加入时间控件  使用的是class选择器
        $(".time").datetimepicker({
            minView: "month",
            language: 'zh-CN',
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayBtn: true,
            pickerPosition: "bottom-left"
        });


    </script>




</body>
</html>
