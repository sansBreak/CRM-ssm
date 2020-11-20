<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

        $(function () {

            //页面加载完毕后触发第一个方法
            //默认展开列表第一页，每页两条信息
            pageList(1, 2)


            //为创建按钮绑定事件，执行打开 模态窗口操作
            $("#addBtn").click(function () {
                //加入时间控件  使用的是class选择器
                $(".time").datetimepicker({
                    minView: "month",
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });

                //创建市场活动的ajax请求
                $.ajax({
                    url: "workbench/activity/getUserList.do",
                    type: "get",
                    dataType: "json",
                    success: function (data) {

                        var html = "";

                        //遍历出来的每一个n，就是一个user对象
                        $.each(data, function (i, e) {
                            html += "<option value='" + e.id + "'> " + e.name + "</option>"
                        })

                        $("#create-owner").html(html);

                        //下拉框显示当前用户的名字作为默认值
                        var id = "${user.id}";
                        $("#create-owner").val(id);
                    }
                })

                //所有者下拉框处理完毕后，展现模态窗口
                /*
                操作模态窗口的方式：
                    需要操作的模态窗口的jQuery对象，调用modal方法，为该方法传递参数 show：打开模态窗口  hide：关闭模态窗口
                */
                $("#createActivityModal").modal("show");
            })

            //为保存按钮绑定事件，执行添加操作
            $("#saveBtn").click(function () {
                $.ajax ({
                    url: "workbench/activity/save.do",
                    data: {
                        "owner": $.trim($("#create-owner").val()),
                        "name": $.trim($("#create-name").val()),
                        "startDate": $.trim($("#create-startDate").val()),
                        "endDate": $.trim($("#create-endDate").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-description").val())
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        /*
                            data{"success": true/false}
                        */
                        if (data) {
                            /*   添加成功后，刷新市场活动信息列表（局部刷新）   */
                            //pageList(1,2)

                            /*
                             $("#activityPage").bs_pagination('getOption', 'currentPage'):
                                    表示操作后停留在当前页
                             $("#activityPage").bs_pagination('getOption', 'rowsPerPage')：
                                    表示操作后维持已经设置好的每页展现的记录数

                                 在添加操作中，我们希望添加完毕回到第一页看看我们新加的数据，所以第一个参数可以改为 1
                            */
                            pageList(1
                                ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


                            //关闭模态窗口前，先清空表单里的内容 注意：jQuery中提供的reset函数是无效的，要转为dom对象再使用
                            $("activityAddForm")[0].reset();

                            //关闭添加操作的模态窗口
                            $("#createActivityModal").modal("hide");

                        } else {
                            alert("添加市场活动失败！")
                        }
                    }
                })
            })

            //查询按钮绑定事件，触发pageList方法
            $("#searchBtn").click(function () {

                /*
                    点击查询按钮的时候，将查询框内的信息保存起来，保存到隐藏域中
                */
                $("#hidden-name").val($.trim($("#search-name").val()))
                $("#hidden-owner").val($.trim($("#search-owner").val()))
                $("#hidden-startDate").val($.trim($("#search-startDate").val()))
                $("#hidden-endDate").val($.trim($("#search-endDate").val()))

                pageList(1, 2)
            })


            //为全选的复选框绑定事件
            $("#qx").click(function () {
                //当qx被点击时，name为xz的input标签全部被选中
                $("input[name=xz]").prop("checked",this.checked);

            })

            /*
                以下这种方式是不行的，因为动态生成的元素，不能用普通的绑定事件的形式进行操作
            $("input[name=xz]").click(function () {
                alert("123")
            })
            */
            /**
             * 动态生成的元素，要以on方法的形式触发事件
             *      语法：
             *          $(需要绑定元素的有效外层元素).on(绑定事件的方式，需要绑定的元素的jQuery对象，回调函数)
             */

             $("#activityBody").on("click", $("input[name=xz]"), function () {

                 //若已经勾选的对象的数量与全部对象的数量相等，则全选框自动变为勾选；不相等则自动取消勾选
                 $("#qx").prop("checked", $("input[name=xz]").length == $("input[name=xz]:checked").length);

             })

            //为删除按钮绑定事件，执行市场活动删除操作
            $("#deleteBtn").click(function () {

                //找到复选框中所有被选中的jQuery对象,这里 $xz 是一个数组,里面存放的是被勾选后的记录
                var $xz = $("input[name=xz]:checked")

                if ($xz==0){
                    alert("请选择需要删除的记录！")
                }else {

                    if (confirm("确认是否要删除该项！")){

                        /*
                            发给后台的请求：
                                    url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx
                        */
                        //拼接参数
                        var param="";

                        //
                        //将$xz中的每一个dom对象遍历出来，取其value值，即这条记录的id（添加记录时，将记录的value设为了id相同）
                        for (var i=0;i < $xz.length; i++){
                            //   $xz[i]   取出第i个对象，这个对象是DOM对象，使用$( )将其转换为jQuery对象
                            param += "id=" +$($xz[i]).val();
                            if (i<$xz.length-1){
                                param += "&";
                            }
                        }

                        $.ajax({

                            url:"workbench/activity/delete.do",
                            data:param,
                            type:"post",
                            dataType:"json",
                            success:function (data) {
                                /*
                                    data{"success":true/false}
                                */

                                if (data){
                                    //删除成功后，刷新列表,维持在当前页，每页条数不变
                                    //pageList(1,2)
                                    pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                        ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


                                }else {
                                    alert("删除市场活动失败！")
                                }

                            }
                        })
                    }
                }


            })

            //点击修改按钮，打开修改市场活动的模态窗口
            $("#editBtn").click(function () {
                var $xz=$("input[name=xz]:checked");

                if ($xz.length==0){
                    alert("请选择需要修改的记录！")
                }else if ($xz.length > 1){
                    alert("只能同时修改一条记录，请重新选择！")
                }else {
                    var id=$xz.val();

                    $.ajax({
                        url:"workbench/activity/getUserListAndActivity.do",
                        //规定要发送到服务器的数据，可以是：string， 数组，多数是 json
                        data:{
                            "id":id
                        },
                        type:"post",
                        dataType:"json",
                        success:function (data) {
                            /*
                                data：
                                    用户列表uList
                                    一个活动市场a
                            */
                            //处理所有者下拉框
                            var html="<option></option>";

                            $.each(data.uList,function (index, element) {
                                html +="<option value='"+ element.id +"'>"+ element.name +"</option>";
                            })
                            $("#edit-owner").html(html)

                            //处理单条activity
                            $("#edit-id").val(data.a.id);
                            $("#edit-name").val(data.a.name);
                            $("#edit-owner").val(data.a.owner);
                            $("#edit-startDate").val(data.a.startDate);
                            $("#edit-endDate").val(data.a.endDate);
                            $("#edit-cost").val(data.a.cost);
                            $("#edit-description").val(data.a.description);

                        }

                    })
                    //模态窗口内的值都充填完毕后，打开模态窗口
                    $("#editActivityModal").modal("show");
                }
            })

            //点击修改市场活动的模态窗口中的 更新 按钮，更新数据
            $("#updateBtn").click(function () {
                $.ajax ({
                    url: "workbench/activity/update.do",
                    data: {
                        "id": $.trim($("#edit-id").val()),
                        "owner": $.trim($("#edit-owner").val()),
                        "name": $.trim($("#edit-name").val()),
                        "startDate": $.trim($("#edit-startDate").val()),
                        "endDate": $.trim($("#edit-endDate").val()),
                        "cost": $.trim($("#edit-cost").val()),
                        "description": $.trim($("#edit-description").val())
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        /*
                            data{"success": true/false}
                        */
                        if (data) {
                            //pageList(1,2)
                            //修改操作后，维持在当前页，当前每页的条数
                            pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


                            //关闭修改操作的模态窗口
                            $("#editActivityModal").modal("hide");

                        } else {
                            alert("添加市场活动失败！")
                        }
                    }
                })
            })


        });


        /**
         * 该方法发出ajax请求到后台，从后台取得最新的市场活动信息列表数据
         *                          通过响应回来的数据，局部刷新市场活动信息列表
         *
         *  需要调用pageList的情况：
         *      1）点击左侧“活动市场”超链接时
         *      2）添加、修改、删除后
         *      3）点击查询按钮时
         *      4）点击分页组件时
         *
         *
         * @param pageNo 页码
         * @param pageSize  每页所展现的记录数
         */
        function pageList(pageNo, pageSize) {

            //每次刷新前，将全选框改为未选中状态
            $("#qx").prop("checked", false);

            /*
                 触发pageList查询时，将保存在隐藏域中的数据取出
            */
            $("#search-name").val($.trim($("#hidden-name").val()))
            $("#search-owner").val($.trim($("#hidden-owner").val()))
            $("#search-startDate").val($.trim($("#hidden-startDate").val()))
            $("#search-endDate").val($.trim($("#hidden-endDate").val()))


            $.ajax({
                url: "workbench/activity/pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "name": $.trim($("#search-name").val()),
                    "owner": $.trim($("#search-owner").val()),
                    "startDate": $.trim($("#search-startDate").val()),
                    "endDate": $.trim($("#search-endDate").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {

                    /*
                        分页插件需要 查询结果的总数
                        {"total":100, "dataList":[{市场活动1}, {市场活动2}, {市场活动3}]}
                    */
                    //在这里，一个n就是一个市场活动对象
                    var html = "";
                    $.each(data.dataList, function (i, n) {
                            alert("id----->"+ n.id)
                        html += '<tr class="active">';
                        html += '<td><input type="checkbox" name="xz" value="' + n.id + '"/></td>';
                        html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">' + n.name + '</a>';
                        html += '</td>';
                        html += '<td>' + n.owner + '</td>';
                        html += '<td>' + n.startDate + '</td>';
                        html += '<td>' + n.endDate + '</td>';
                        html += '</tr>';
                    })
                    $("#activityBody").html(html);

                    //计算总页数      其中Math.ceil()是向上取整的函数
                    var totalPages = Math.ceil(data.total / pageSize);

                    //数据处理完毕后，结合分页插件，对前端展现分页信息
                    $("#activityPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,

                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    });

                }
            })
        }

    </script>
</head>
<body>

    <input type="hidden" id="hidden-name"/>
    <input type="hidden" id="hidden-owner"/>
    <input type="hidden" id="hidden-startDate"/>
    <input type="hidden" id="hidden-endDate"/>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="activityAddForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-owner">

                            </select>
                        </div>
                        <label for="create-name" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate" readonly>
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn" data-dismiss="modal">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <input type="hidden" id="edit-id">

                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">

                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startDate" >
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endDate" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="search-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="search-endDate">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="searchBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <%--
                    data-toggle="modal"：表示触发该按钮，将要打开一个模态窗口

                    data-target="#createActivityModal"：
                        表示要打开哪个模态窗口，通过id的形式找到该窗口



                --%>
                <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span>
                    创建
                </button>
                <button type="button" class="btn btn-default" id="editBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="qx"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityBody">
                <%--<tr class="active">
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a>
                    </td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a>
                    </td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>--%>
                </tbody>
            </table>
        </div>

        <%--分页--%>
        <div style="height: 50px; position: relative;top: 30px;">

            <div id="activityPage">

            </div>

        </div>


    </div>

</div>
</body>
</html>