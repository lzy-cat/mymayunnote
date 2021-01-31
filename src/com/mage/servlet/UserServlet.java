package com.mage.servlet;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.mage.po.User;
import com.mage.po.vo.ResultInfo;
import com.mage.service.UserService;
import com.mage.util.JsonUtil;
import com.mage.util.StringUtil;

/**
 * 	用户模块
 * 		1.用户登录
 * 		2. 用户退出
 * 		3. 进入个人中心
 * 		4. 加载用户头像
 * 		5. 个人中心的昵称唯一性验证
 * 		6. 修改用户信息 上传文件
 * 
 */
@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService(); 
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//接收用户参数
		String actionName = request.getParameter("actionName");
		System.out.println(actionName);
		//判断用户行为
		if("login".equals(actionName)) {
			userLogin(request,response);
			return;
		}else if("userCenter".equals(actionName)) {
			//个人中心
			userCenter(request,response);
		}else if("checkNick".equals(actionName)){
			//昵称的唯一性
			checkNick(request,response);
			return;
		}else if("userHead".equals(actionName)){
			//加载头像
			userHead(request,response);
			return;
		} else if("updateInfo".equals(actionName)){
			//修改个人中心信息
			update(request,response);
			return;
		}else if("logout".equals(actionName)){
			logout(request,response);
			return;
		}else {
			// 跳转到登录页面
			response.sendRedirect("login.jsp");
		}
	}
	/**
	 *  退出
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		Cookie cookie = new Cookie("user",null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		response.sendRedirect("login.jsp");
	}
	/**
	 * 	修改个人中心信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//调用Service中的修改方法，参数为request
		ResultInfo<User> resultInfo = userService.update(request);
		request.setAttribute("resultInfo", resultInfo);
		request.getRequestDispatcher("user?actionName=userCenter").forward(request, response);
		
		
	}
	/**
	 * 	加载头像
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String imageName = request.getParameter("imageName");
		String filePath = request.getServletContext().getRealPath("/WEB-INF/upload/" + imageName);
		//获取file对象
		File file = new File(filePath);
		//判断file
		if(file.exists()&&file.isFile()) {
			//判断图片类型MIME
			String pic = imageName.substring(imageName.indexOf(".")+1);
			if("png".equalsIgnoreCase(pic)) {
				response.setContentType("image/png");
			}else if("gif".equalsIgnoreCase(pic)) {
				response.setContentType("image/gif");
			}else if("jpg".equalsIgnoreCase(pic) || "jpeg".equalsIgnoreCase(pic)) {
				response.setContentType("image/jpeg");
			}
			//拷贝文件
			FileUtils.copyFile(file, response.getOutputStream());
		}
		
		
	}
	/**
	 * 	昵称的唯一性
	 * @param request
	 * @param response
	 */
	private void checkNick(HttpServletRequest request, HttpServletResponse response) {
		String nick = request.getParameter("nickName");
		User user = (User)request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		ResultInfo<User> resultInfo = userService.chickNick(nick,userId);
		//将resultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
		JsonUtil.toJson(resultInfo, response);
	}
	/**
	 * 	个人中心
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("topActive", 4);
		request.setAttribute("changePage", "note/info.jsp");
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	/**
	 * 	用户登录
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void userLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 获取参数
		String uname = request.getParameter("uname");
		String upwd = request.getParameter("upwd");
		ResultInfo<User> resultInfo = userService.login(uname,upwd);
		if(resultInfo.getCode()==1) {
			request.getSession().setAttribute("user", resultInfo.getResult());
			String rem = request.getParameter("rem");
			if("1".equals(rem)) {
				Cookie cookie = new Cookie("user",uname+"-"+upwd);
				cookie.setMaxAge(7*24*60*60);
				response.addCookie(cookie);
			}
			response.sendRedirect("index");
		}else {
			
			// 失败	将resultInfo对象存到request作用域中
			request.setAttribute("resultInfo", resultInfo);
			// 请求转发跳转到登录界面
			request.getRequestDispatcher("login.jsp").forward(request, response);
			
		}
	}

}
