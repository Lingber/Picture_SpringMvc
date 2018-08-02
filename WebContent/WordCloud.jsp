<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
  pageContext.setAttribute("APP_PATH",request.getContextPath());
 %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" href="./static/media/images/favicon.ico" type="image/ico" />
<title>WordCloud</title>
	<script type="text/javascript" src="${APP_PATH }/static/js/jquery-3.0.0.min.js"></script>
	<link href="${APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link href="${APP_PATH }/static/css/main.css" rel="stylesheet">
	<script type="text/javascript" src="${APP_PATH }/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
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
				<div id = "worldDiv" class="wrap"></div>
			<!-- 主体显示 -->
				<!-- WordCloud -->
<!-- <section id="home-slider">
    <div class="container">
        <div class="row">
            <div class="main-slider animate-in">
                <div class="slide-text">
                    <h1>GET WordCloud!</h1>
                    <p></p>
                    <a href="https://demo.pearsearch.org" class="btn btn-common">TRY NOW</a>
                    <a href="#" class="btn btn-common" data-toggle="modal" data-target="#myModal">select the content with WC</a>
                </div>
	               	<div class="slider-hill">
	               		<div id = "worldDiv" style="width:550px;height:450px;background-color: #ccc"></div>
	               	</div>
            </div>
        </div>
    </div>
</section> -->
			
			<style>
			.wrap{
			    background: #ccc;
			    width: 700px;
			    height: 400px;
			    position: fixed;
			    left: 50%;
			    top: 50%;
			    z-index: 11;
			    /*设定这个div的margin-top的负值为自身的高度的一半,margin-left的值也是自身的宽度的一半的负值.*/ 
			    /*宽为400,那么margin-top为-200px*/ 
			    /*高为200那么margin-left为-100px;*/ 
			    margin: -200px 0 0 -350px;
			}
		</style>
<!-- 	  <div class="slider-hill">
		<div id='worldDiv'></div>
	  </div> -->

    			</div>
			</div>
          </div>
        </div>
        <!-- /page content -->
  	<script type="text/javascript" src="./static/home/custom.min.js"></script>
	<script src="${APP_PATH }/static/echarts/echarts.js"></script>
	<script src="${APP_PATH }/static/echarts/echarts-wordcloud.min.js"></script>
	<script src="${APP_PATH }/static/echarts/worldData.js"></script>
</body>
</html>