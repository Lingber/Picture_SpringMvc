<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <!-- 获取当前项目路径，以/开始，不以/结尾 -->
<%
  pageContext.setAttribute("APP_PATH",request.getContextPath());
 %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片信息管理</title>
	<script type="text/javascript" src="./static/js/jquery-3.0.0.min.js"></script>
	<link href="./static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="./static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
	<link href="${APP_PATH }/static/css/main.css" rel="stylesheet">
	 <!-- Font Awesome -->
    <!-- 侧面导航栏的图标、按键 -->
    <!-- <link href="./static/home/font-awesome.min.css" rel="stylesheet"> -->
        <link href="./static/home/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- Custom Theme Style -->
    <!-- 颜色之类的在里面 -->
        <link href="./static/home/custom.min.css" rel="stylesheet">
</head>
<body class="nav-md">
<header id="header">
    <div class="container body">
      <div class="main_container">
        <div class="col-md-3 left_col">
          <div class="left_col scroll-view">
            <div class="navbar nav_title" style="border: 0;">
              <a href="index.html" class="site_title"><i class="fa fa-paw"></i> <span>拍照识图应用!</span></a>
            </div>

            <div class="clearfix"></div>

            <!-- 顶头头像图标menu profile quick info -->
            <div class="profile clearfix">
              <div class="profile_pic">
              <!--   <img src="./static/media/images/img.jpg" alt="..." class="img-circle profile_img"> -->
              </div>
              <div class="profile_info">
                <span>欢迎您，管理员</span>
              </div>
            </div>
            <!-- /顶头头像图标menu profile quick info -->

            <br />

            <!-- 侧面导航sidebar menu -->
            <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
              <div class="menu_section">
                <h3>General</h3>
                <ul class="nav side-menu">
                  <li><a><i class="fa fa-home"></i> 用户管理 <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                   	  <li><a href="Home.jsp">用户管理</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-edit"></i> 图片管理  <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="picture_manage.jsp">上传图片管理主页</a></li>
                      <li><a href="picture_manage_with_userImage.jsp">头像管理主页</a></li>
                    </ul>
                  </li>
                   <li><a><i class="fa fa-edit"></i> 评论管理  <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="comment_manage.jsp">评论管理主页</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-table"></i> 私信管理 <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="picture_manage_with_chat.jsp">私信管理主页</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-bar-chart-o"></i> 数据统计 <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="WordCloud.jsp">用户最常识别云图</a></li>
                      <li><a href="distribution.jsp">用户分布统计</a></li>
                      <li><a href="userOnline.jsp">在线用户实时统计</a></li>
                       <li><a href="userCount.jsp">用户地图</a></li>
                    </ul>
                  </li>
                </ul>
              </div>
            </div>
            <!-- /侧面导航sidebar menu -->

            <!-- /menu footer buttons -->
            <div class="sidebar-footer hidden-small">
              <a data-toggle="tooltip" data-placement="top" title="Settings">
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="FullScreen">
                <span class="glyphicon glyphicon-fullscreen" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="Lock">
                <span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="Logout" href="login.html">
                <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
              </a>
            </div>
            <!-- /menu footer buttons -->
          </div>
        </div>

        <!-- top navigation -->
        <div class="top_nav">
          <div class="nav_menu">
            <nav>
              <div class="nav toggle">
                <a id="menu_toggle"><i class="fa fa-bars"></i></a>
              </div>

              <ul class="nav navbar-nav navbar-right">
                <li class="">
                  <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <img src="./static/media/images/admin.jpg" alt="">管理员账户
                    <span class=" fa fa-angle-down"></span>
                  </a>
                  <ul class="dropdown-menu dropdown-usermenu pull-right">
                    <li><a href="javascript:;"> Profile</a></li>
                    <li>
                      <a href="javascript:;">
                        <span class="badge bg-red pull-right">50%</span>
                        <span>Settings</span>
                      </a>
                    </li>
                    <li><a href="javascript:;">Help</a></li>
                    <li><a href="login.html"><i class="fa fa-sign-out pull-right"></i> Log Out</a></li>
                  </ul>
                </li>
              </ul>
            </nav>
          </div>
        </div>
        <!-- /top navigation -->


        <!-- page content -->
        <div class="right_col" role="main">
          <!-- top tiles -->
          <div class="row tile_count">
			<div class="container-fluid">
				<div class="row-fluid">

	<%--搭建显示页面，--%>
	<div class="container">
		<%--标题--%>
		<div class="row">
			<div class="col-md-12">
				<h1>图片信息管理</h1>
			</div>
		</div>
		<%--按钮--%>
		<div class="row">
		
			<div class="col-md-4">
	            <button class="btn btn-primary" id="emp_add_modal_btn">新增</button>
	            <button class="btn btn-danger" id="emp_delete_all_btn">删除</button>
	        </div>
		
			<div class="col-md-4 col-md-offset-8">
				<!-- <button class="btn btn-primary" id="emp_add_modal_btn">查看生成词云</button> -->
				   <div class="row">
					    <div class="input-group">
					      <input type="text" name="content" class="form-control" placeholder="请输入图片内容...">
					      <span class="input-group-btn">
					        <button class="btn btn-default" type="button" onclick="get_job_by_key()">Go!</button>
					      </span>
					    </div><!-- /input-group -->
					</div><!-- /.row -->
			</div>
		</div>
		<%--表格--%>
		<div class="row">
			<div class="col-md-12">
				<table class="table table-hover" id="rough_table">
					<thead>
						<tr>
							<th><input type="checkbox" id="check_all"></th>
							<th>用户ID</th>
							<th>图片内容</th>
							<th>上传时间</th>
							<th>图片位置</th>
							<th>查看图片</th>
							<th></th>
						</tr>
				  	<colgroup>
				  		<col width='5%'></col> <!--	checkbox -->
					    <col width='25%'></col> <!--用户昵称 -->
					    <col width='15%'></col><!-- 图片内容 -->
					    <col width='15%'></col><!-- 上传时间 -->
					    <col width='20%'></col><!-- 图片位置 -->
					    <col width='5%'></col><!-- 查看图片 -->
					    <col width='5%'></col><!-- 修改 -->
					    <col width='5%'></col><!-- 删除 -->
				   </colgroup>
					<thead>
					<tbody>

					</tbody>
				</table>
			</div>
		</div>
		<%--分页信息--%>
		<div class="row">
			<%--分页文字信息--%>
			<div class="col-md-4" id="page_info_area"></div>
			<%--分页条--%>
			<div class="col-md-8" id="page_nav_area"></div>
		</div>
	</div>
				</div>
			</div>
          </div>
        </div>
        <!-- /page content -->
	<!-- 图片添加的模态框 -->
	<div class="modal fade" id="showPictureModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                <h4 class="modal-title" id="myModalLabel">图片展示</h4>
	            </div>
	            <div class="modal-body">

				<img id="picture_url" alt="图片" src="">


	            </div>

	        </div>
	    </div>
	</div>


	<%--员工修改模态框--%>
	<div class="modal fade" id="empUpdataModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                <h4 class="modal-title">修改图片</h4>
	            </div>
	            <div class="modal-body">
	                <form class="form-horizontal">
	                
		                <div class="form-group">
	                        <label for="userID_update_input" class="col-sm-2 control-label">用户ID</label>
	                        <div class="col-sm-10">
	                            <input type="text" name="userId_update" class="form-control" id="userID_update_input" placeholder="用户昵称">
	                            <span class="help-block"></span>
	                        </div>
	                    </div>	
	
	                    <div class="form-group">
	                        <label for="password_updata_input" class="col-sm-2 control-label">内容</label>
	                        <div class="col-sm-10">
	                            <input type="text" name="content_updata" class="form-control" id="password_updata_input" placeholder="password">
	                            <span class="help-block"></span>
	                        </div>
	                    </div>

	                </form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	                <button type="button" class="btn btn-primary" id="emp_updata_btn">更新</button>
	            </div>
	        </div>
	    </div>
	</div>
	
	<!-- 图片添加的模态框 -->
	<div class="modal fade" id="empAndModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                <h4 class="modal-title" id="myModalLabel">图片添加</h4>
	            </div>
	            <div class="modal-body">
 	                <form class="form-horizontal" action="upload/server_upload_picture_to_oss" method="post"  enctype="multipart/form-data"> 
				<!-- <form class="form-horizontal" action="upload/client_upload_userImage_to_oss" method="post"  enctype="multipart/form-data"> -->
						<div class="form-group">
	                        <label for="userName_add_input" class="col-sm-2 control-label">选择图片</label>
	                        <div class="col-sm-10">
	                            <input type="file" name="file">
	                            <span class="help-block"></span>
	                        </div>
	                    </div>
					
	                    <div class="form-group">
	                        <label for="userName_add_input" class="col-sm-2 control-label">用户ID</label>
	                        <div class="col-sm-10">
	                            <input type="text" name="userId" class="form-control" id="userName_add_input" placeholder="UserName">
	                            <span class="help-block"></span>
	                        </div>
	                    </div>
	                 <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		                <button type="submit" class="btn btn-primary" id="emp_save_btn">提交</button>
		            </div>
	                    
	                </form>
	            </div>

	        </div>
	    </div>
	</div>
	
	
	<!-- js脚本开始 -->
	<script type="text/javascript">
	 console.log("${APP_PATH}");
	var totalRecord, currentPage;
	//页面加载完成后，直接去发送一个Ajax请求，要到分页数据
	$(function() {
		to_page(1)
	});
	function to_page(pn) {
		var picture_Class="01";
		$.ajax({
			url : "/Picture_SpringMvc/picture/getall",
			data : "pn=" + pn+"&picture_Class="+picture_Class,
			type : "GET",
			success : function(result) {
				console.log(result);
				//1.解析并显示员工数据
				build_rough_table(result);
				//2.解析并显示分页信息
				build_page_info(result);
				//3.解析显示分页条数据
				build_page_nav(result);
			}
		});
	}
	function build_rough_table(result) {
		$("#rough_table tbody").empty();/* 清空之前表内所有数据 */
		var rough = result.extend.pageInfo.list;
		$
				.each(
						rough,
						function(index, item) {
							var checkboxTd = $("<td><input type ='checkbox' class='check_item'/></td>");
							//alert(item.userName);
							var userIdTD = $("<td></td>").append(item.userID);
							var locationTD = $("<td></td>").append(item.location);
							//将content分解开始
							var pic_content=item.content.content_Class;
/* 							var arr_value = Array.from(item.content.picContent);
							for(var i=0;i<1;i++){
								pic_content=pic_content+"、"+arr_value[0];//数组的索引是从0开始的
							} */
							//将content分解完成
							var contentTD = $("<td></td>").append(pic_content);
							var shootingTimeTD = $("<td></td>").append(item.shootingTime);
							/* 数据来源在数据详情显示 */
							var editBtn = $("<button></button>")
									.addClass("btn btn-success btn-primary edit_btn")
									.append($("<span></span>")
									.addClass("glyphicon glyphicon-pencil"))
									.append("修改");
							//为编辑按钮添加自定义属性，表示当前id
							editBtn.attr("edit-id", item.picture_Name);
							var delBtn = $("<button></button>")
									.addClass("btn btn-danger btn-primary delete_btn")
									.append($("<span></span>")
									.addClass("glyphicon glyphicon-trash"))
									.append("删除");
							//为删除按钮添加自定义属性，表示当前id
							delBtn.attr("del-id", item.picture_Name);
							
							var urlBtn = $("<button></button>")
									.addClass("btn btn-default url_btn")
									.append("查看图片");
							//为URL按钮添加自定义属性，表示当前id
							/* urlBtn.attr("url-id", item.url); */
							urlBtn.attr("url-id", item.picture_Name);
							
							var commBtn = $("<button></button>")
									.addClass("btn btn-default comment_btn")
									.append("查看评论");
							//为片评论查询按钮添加自定义属性，表示当前id
							commBtn.attr("comm-id", item.picture_Name);
							
							var ebtnTd = $("<td></td>").append(editBtn);
							var dbtnTd = $("<td></td>").append(delBtn);
							var ubtnTd = $("<td></td>").append(urlBtn);
							var cbtnTd = $("<td></td>").append(commBtn);
							$("<tr></tr>").append(checkboxTd).append(
									userIdTD).append(contentTD).append(shootingTimeTD).append(locationTD)
									.append(ubtnTd).append(ebtnTd).append(dbtnTd).appendTo(
									"#rough_table tbody");
						});
	}
	//解析显示分页信息
	function build_page_info(result) {
		$("#page_info_area").empty();
		$("#page_info_area").append(
				"当前" + result.extend.pageInfo.pageNum + "页,总"
						+ result.extend.pageInfo.pages + "页,总共"
						+ result.extend.pageInfo.total + "条记录");
		totalRecord = result.extend.pageInfo.total;
		currentPage = result.extend.pageInfo.pageNum;
	}
	
	
	//解析显示分页条
	function build_page_nav(result) {
		$("#page_nav_area").empty();
		var ul = $("<ul></ul>").addClass("pagination");
		var firstPageLi = $("<li></li>").append(
				$("<a></a>").append("首页").attr("href", "#"));
		var prePageLi = $("<li></li>").append(
				$("<a></a>").append("&laquo;"));
		if (result.extend.pageInfo.hasPreviousPage == false) {
			firstPageLi.addClass("disabled");
			prePageLi.addClass("disabled")
		} else {
			firstPageLi.click(function() {
				to_page(1);
			});
			prePageLi.click(function() {
				to_page(result.extend.pageInfo.pageNum - 1);
			});
		}
		var nextPageLi = $("<li></li>").append(
				$("<a></a>").append("&raquo;"));
		var lastPageLi = $("<li></li>").append(
				$("<a></a>").append("末页").attr("href", "#"));
		if (result.extend.pageInfo.hasNextPage == false) {
			nextPageLi.addClass("disabled");
			lastPageLi.addClass("disabled");
		} else {
			nextPageLi.click(function() {
				to_page(result.extend.pageInfo.pageNum + 1);
			});
			lastPageLi.click(function() {
				to_page(result.extend.pageInfo.pages);
			});
		}
		//添加首页和前一页
		ul.append(firstPageLi).append(prePageLi);
		//添加1、2、3遍历ul中添加页码
		$.each(result.extend.pageInfo.navigatepageNums, function(index,
				item) {
			var numLi = $("<li></li>").append($("<a></a>").append(item));
			if (result.extend.pageInfo.pageNum == item) {
				numLi.addClass("active")
			}
			numLi.click(function() {
				to_page(item);
			});
			ul.append(numLi);
		});
		//添加末页和下一页
		ul.append(nextPageLi).append(lastPageLi);
		var navEl = $("<nav></nav>").append(ul);
		$("#page_nav_area").append(navEl);
		//navEl.appendTo("#page_nav_area");
	}
	
	function get_job_by_key() {
		var jobname = document.getElementsByName("content")[0].value;
		$.ajax({
			url : "/Picture_SpringMvc/picture/get_by_key_with_content",
			data : "content=" + jobname,
			type : "GET",
			success : function(result) {
				console.log(result);
				//1.解析并显示员工数据
				build_rough_table(result);
				//2.解析并显示分页信息
				build_page_info(result);
				//3.解析显示分页条数据
				build_page_nav(result);
			}
		});
	}
	
	
	/* 添加用户 */
	
    //弹出模态框
    $("#emp_add_modal_btn").click(function () {
        $("#empAndModal").modal({
            backdrop:"static"
        });
    });

    //保存事件
    $("#emp_save_btn").click(function () {
    	var code11 ="11";
    	var code12 ="12";
        //发送Ajax请求
        $.ajax({
            url:"/Picture_SpringMvc/upload/server_upload_picture_to_oss",  
            /* url:"/Picture_SpringMvc/upload/client_upload_userImage_to_oss", */
            type:"POST",
            data: $("#empAndModal form").serialize(),
            success:function (result) {
                if(result.msg_code == code11){
                	alert("保存成功");
                    //保存成功，关闭模态框，显示刚才保存的数据
                    $("#empAndModal").modal("hide");
                    to_page(1);
                }else {
                	alert("保存!");
                }
            }
        });
    });
	
    
    /* 修改用户信息 */
    
    $(document).on("click", ".edit_btn", function () {

        getEmp($(this).attr("edit-id"));
        //吧员工的id传递给更新按钮
        $("#emp_updata_btn").attr("edit-id",$(this).attr("edit-id"));
        //弹出模态框
        $("#empUpdataModal").modal({
            backdrop:"static"
        });
    });
    function getEmp(id) {
        $.ajax({
            url:"/Picture_SpringMvc/picture/client_get_by_key",
            type:"GET",
            data: "picture_name="+id,
            success:function (result) {
                console.log(result);
                var empData = result.result[0];
                $("#userID_update_input").val(empData.userID);
                $("#password_updata_input").val(empData.content);
            }
        });
    }
    
    
    //点击更新 更新员工
    $("#emp_updata_btn").click(function () {
    	var picture_name=$(this).attr("edit-id");
    	var code18 ="18";
        //发送Ajax请求
        $.ajax({
            url:"/Picture_SpringMvc/picture/server_updata",
            type:"GET",
            /* data:$("#empUpdataModal form").serialize()+userId, */
            data:$("#empUpdataModal form").serialize()+"&picture_name="+picture_name,
            success:function (result) {
                if(result.msg_code == code18){
                	alert("图片修改成功！")
                    //员工保存成功，关闭模态框，显示刚才保存的数据
                    $("#empUpdataModal").modal("hide");
                    to_page(1);
                }else {
                	 alert("修改错误!");
                }
            }
        });
    });
    
    
    /* 显示图片 */
    
    $(document).on("click", ".url_btn", function () {

    	 geturl($(this).attr("url-id")); 
        //弹出模态框
        $("#showPictureModal").modal({
            backdrop:"static"
        });
    });
    function geturl(id) {
                $.ajax({
                    url:"/Picture_SpringMvc/picture/client_get_by_key",
                    type:"GET",
                    data:"picture_name="+id,
                    success:function (result) {
                    	var url = result.result[0];
                    	$("#picture_url").attr("src", url.url);
                    }
                });
    }
    
    /* 删除用户 */
        //删除员工
    $(document).on("click", ".delete_btn", function () {
        //1.弹出确认对话框
        //得到姓名
        var userName = $(this).attr("del-id");
        //得到ID
        var picture_name = $(this).attr("del-id");
        //确认发送请求
        if(confirm("确认删除【" + userName + "】吗？")){
            $.ajax({
                url:"/Picture_SpringMvc/picture/del_by_key",
                type:"GET",
                data:"picture_name="+picture_name,
                success:function (result) {
                	alert("删除成功")
                    to_page(1);
                }
            });
        }
    });
    //全选、全不选
    $("#check_all").click(function () {
        //这些dom原生的属性用prop()获取或者修改
        //自定义的属性用attr()获取
        //若用attr()获取原生，但属性为标注出，得到的是undefined
        //$(this).prop("checked");
        $(".check_item").prop("checked", $(this).prop("checked"));
    });
    //check_item全选后 check_all选择
    $(document).on("click", ".check_item", function () {
        //判断当前选中的元素是否为5个
        var flag = $(".check_item:checked").length ==  $(".check_item").length;
            $("#check_all").prop("checked", flag);
    });
    //批量删除事件
    $("#emp_delete_all_btn").click(function () {
        //$(".check_item")
        var userNames = "";
        var del_list = "";
        $.each($(".check_item:checked"), function () {
        	userNames += $(this).attr("del-id") + ",";
            //组装id字符串
            del_list += $(this).attr("del-id"); + "-";
        });
        //去除多余的逗号
        userNames = userNames.substring(0, userNames.length - 1);
        del_list = del_list.substring(0, del_list.length - 1);
        alert(del_list);
        if(confirm("确认删除【" + userNames + "】吗？")){
            //alert(empNames);
            $.ajax({
                url:"/Picture_SpringMvc/picture/del_by_keys",
                type:"GET",
                data:"picture_names="+del_list,
                success:function (result) {
                	alert("删除成功！");
                    to_page(1);
                }
            });
        }
    });
    
    
	</script>
<script type="text/javascript" src="./static/home/custom.min.js"></script>
</body>
</html>