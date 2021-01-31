<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>码云笔记</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<meta name="keywords"
	content="Clear login Form a Responsive Web Template, Bootstrap Web Templates, Flat Web Templates, Android Compatible Web Template, Smartphone Compatible Web Template, Free Webdesigns for Nokia, Samsung, LG, Sony Ericsson, Motorola Web Design">
<script>
	addEventListener("load", function() {
		setTimeout(hideURLbar, 0);
	}, false);

	function hideURLbar() {
		window.scrollTo(0, 1);
	}
</script>
<style type="text/css">
.code {
	font-family: Arial;
	font-style: italic;
	color: Red;
	border: 0;
	padding: 2px 3px;
	letter-spacing: 3px;
	font-weight: bolder;
}

.unchanged {
	border: 0;
	color: red;
	font-size: 18px;
}
</style>
<script src="/mymayunnote/statics/js/jquery-3.3.1.min.js"
	type=text/javascript></script>
<script src="/mymayunnote/statics/js/util.js" type=text/javascript></script>

<link href="/mymayunnote/statics/css/font-awesome.css" rel="stylesheet">
<link href="/mymayunnote/statics/css/style.css" rel='stylesheet'
	type='text/css' />

<script type="text/javascript" src="/mymayunnote/statics/js/gVerify.js"></script>
</head>
<body onload="createCode()">
	<h1>码云笔记</h1>
	<div class="w3ls-login box box--big">
		<!-- form starts here -->
		<form action="user" method="post" id="loginForm">
			<input type="hidden" name="actionName" value="login" />
			<div class="agile-field-txt">
				<label> <i class="fa fa-user" aria-hidden="true"></i>
					Username
				</label> <input id="userName" name="uname"
					value="${resultInfo.result.uname }"
					style="width: 341px; position: absolute; top: 262px; left: 570px"
					type="text" placeholder="Enter your name " />
			</div>
			<div class="agile-field-txt">
				<label> <i class="fa fa-envelope" aria-hidden="true"></i>
					password
				</label> <input id="userPwd" name="upwd" value="${resultInfo.result.upwd }"
					style="width: 341px; position: absolute; top: 353px; left: 570px"
					type="password" placeholder="Enter your password " />
			</div>
			<!--图片验证-->
			<div class="agile-field-txt">
				<label> <i class="fa fa-envelope" aria-hidden="true"></i>Verification
					Code
				</label> <input type="text" placeholder="Enter your verification code "
					style="width: 341px; position: absolute; top: 448px; left: 570px"
					id="code_input" value="" />
				<!--  <span id="v_container"
					style="width: 100px; height: 50px; position: absolute; top: 448px; right: 508px;" value=""></span> -->
				<input onclick="createCode()" readonly="readonly" id="checkCode"
					class="unchanged"
					style="width: 80px; height: 44px; font-size:20px; position:absolute; top: 448px; right: 520px;"
					value=""></input>
			</div>

			<div class="w3ls-bot">
				<div class="switch-agileits">
					<label class="switch"> <input type="checkbox" name="rem"
						type="checkbox" value="1"> <span class="slider round"></span>
						keep me signed in
					</label> <span id="msg"
						style="position: absolute; left: 246px; font-size: 19px; color: red;">${resultInfo.msg }</span>
				</div>
				<div class="form-end"
					style="position: absolute; top: 530px; left: 700px;">
					<input type="button" value="LOGIN" onclick="checkLogin()"
						style="width: 87px; height: 45px; position: absolute; top: 20px;">
				</div>
				<div class="clearfix"></div>
			</div>
		</form>
		<!-- <iframe id="iframe" name="iframe" style="display:none;"></iframe> -->
	</div>
	<!-- //form ends here -->
	<!-- script for show password -->
	<script>
		/*验证码*/
		/* 验证表单*/
		//var verifyCode = new GVerify("v_container");
		var code; //在全局 定义验证码   
		function createCode() {
			code = "";
			var codeLength = 4;//验证码的长度   
			var checkCode = document.getElementById("checkCode");
			var selectChar = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B',
					'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
					'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');//所有候选组成验证码的字符，当然也可以用中文的   

			for (var i = 0; i < codeLength; i++) {
			//数组下标
				var charIndex = Math.floor(Math.random() * 36);
				code += selectChar[charIndex];

			}
			if (checkCode) {
				checkCode.className = "code";
				checkCode.value = code;
			}

		}

		function checkLogin() {
			// 获取值
			var userName = $("#userName").val();
			var userPwd = $("#userPwd").val();
			var inputCode = document.getElementById("code_input").value;
			// 判断值

			if (isEmpty(userName)) {
				$("#msg").html("用户名称不能为空");
				return;
			}
			if (isEmpty(userPwd)) {
				$("#msg").html("用户密码不能为空");
				return;
			}
			if (inputCode.length <= 0) {
				$("#msg").html("验证码不能为空");
				return;
			} else if (inputCode != code) {
				$("#msg").html("验证码错误");
				createCode();//刷新验证码   
				return;
			}
			// 提交表单
			$("#loginForm").submit();
		}
	</script>
</body>

</html>
