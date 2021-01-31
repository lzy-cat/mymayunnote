package com.mage.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.mage.dao.UserDao;
import com.mage.po.User;
import com.mage.po.vo.ResultInfo;
import com.mage.util.MD5Util;
import com.mage.util.StringUtil;

public class UserService {
	private UserDao userDao = new UserDao();
	/**
	 * 	用户登录
	 * @param uname
	 * @param upwd
	 * @return
	 */
	public ResultInfo<User> login(String uname, String upwd) {
		ResultInfo<User> resultInfo = new ResultInfo<User>();
		//非空判断
		if(StringUtil.isEmpty(uname)||StringUtil.isEmpty(upwd)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("用户名或密码不能为空");
			return resultInfo;
		}
		//回显对象
		User user = new User();
		user.setUname(uname);
		user.setUpwd(upwd);
		User u = userDao.queryUserByUname(uname);
		//判断用户是否为空
		if(u==null) {
			resultInfo.setCode(0);
			resultInfo.setMsg("用户不存在");
			//设置回显
			resultInfo.setResult(user);
			return resultInfo;
		}
		//判断密码是否一致
		String pwd = MD5Util.encode(upwd);
		if(!pwd.equals(u.getUpwd())) {
			resultInfo.setCode(0);
			resultInfo.setMsg("用户密码不正确！");
			resultInfo.setResult(user);
			return resultInfo;
		}
		resultInfo.setCode(1);
		resultInfo.setMsg("登录成功！");
		resultInfo.setResult(u);
		return resultInfo;
	}
	/**
	 * 	昵称唯一性
	 * @param nick
	 * @param userId
	 * @return
	 */
	public ResultInfo<User> chickNick(String nick, Integer userId) {
		ResultInfo<User> resultInfo = new ResultInfo<>();
		//非空判断
		if(StringUtil.isEmpty(nick)) {
			resultInfo.setMsg("昵称不能为空");
			resultInfo.setCode(0);
			return resultInfo;
		}
		Integer count = userDao.chickNick(nick,userId);
		//count>0,不可用
		if(count>0) {
			resultInfo.setMsg("昵称已存在");
			resultInfo.setCode(0);
			return  resultInfo;
		}else {
			resultInfo.setMsg("昵称可用");
			resultInfo.setCode(1);
			
		}
		return  resultInfo;
	}
	/**
	 * 	个人中心的修改
	 * @param request
	 * @return
	 */
	public ResultInfo<User> update(HttpServletRequest request) {
		ResultInfo<User> resultInfo= new ResultInfo<User>();
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		String nick = request.getParameter("nick");
		String mood = request.getParameter("mood");
		//非空判断
		if(StringUtil.isEmpty(nick)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("昵称不能为空");
			return resultInfo;
		}
		//默认头像
		String head = user.getHead();
		//文件上传
		try {
			//通过getPart（name)方法，得到part对象 name：表单中file文件域的name属性值
			Part part = request.getPart("img");
			//得到文件存放位置D:\\eclipse-workspace\\mymayunnote\\WebContent\\WEB-INF\\upload"
			String path = request.getServletContext().getRealPath("/WEB-INF/upload/");
			//得到上传的文件名
			String fileName = part.getSubmittedFileName();
			//如果文件名不为空，表示上传了文件，上传到指定路径如果文件名为空，则设置默认的值为session域对象中的user的head属性的值
			if(!StringUtil.isEmpty(fileName)) {
				part.write(path+fileName);
				head = fileName;
			}
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
		//调用dao层，修改信息
		int row = userDao.update(nick,head,mood,userId);
		if(row>0) {
			resultInfo.setCode(1);
			resultInfo.setMsg("修改成功");
			//更新session中的user对象
			user.setNick(nick);
			user.setMood(mood);
			user.setHead(head);
			request.getSession().setAttribute("user",user);
		}else {
			resultInfo.setCode(0);
			resultInfo.setMsg("修改失败");
		}
		return resultInfo;
	}

}
