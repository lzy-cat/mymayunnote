package com.mage.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.mage.po.Note;
import com.mage.po.User;
import com.mage.po.vo.NoteVo;
import com.mage.po.vo.ResultInfo;
import com.mage.service.NoteService;
import com.mage.util.Page;

/**
 * 主页
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private NoteService noteService = new NoteService();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取参数
		String actionName = request.getParameter("actionName");
		
		//给云记列表的分页导航栏的查询参数
		request.setAttribute("actionName", actionName);
		//判断用户行为
		if("searchTitle".equals(actionName)) {
			//顶部的搜索栏根据标题查询
			String title = request.getParameter("title");
			System.out.println("service中"+title);
			request.setAttribute("title",title);
			queryNote(request,response,title,null,null);
			return;
		}else if("searchDate".equals(actionName)) {
			//根据日期分页查询
			String date = request.getParameter("date");
			//前台传入一个date，后台需要用到，根据date查询
			request.setAttribute("date", date);
			queryNote(request,response,null,date,null);
			return;
		}else if("searchType".equals(actionName)) {
			//根据类型分页查询
			String typeId = request.getParameter("typeId");
			request.setAttribute("typeId", typeId);
			queryNote(request,response,null,null,typeId);
			return;
		}else {
			//分页查询云集列表
			queryNote(request,response,null,null,null);
		}
		
	}
	/**
	 * 	分页查询云集列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void queryNote(HttpServletRequest request, HttpServletResponse response,String title,String date,String typeId) throws ServletException, IOException {
		//获取pageSize，currentPage
		String  pageSize = request.getParameter("pageSize");
		String currentPage = request.getParameter("currentPage");
		User user = (User)request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		//点击左侧的一条记录，调用noteService中方法,实现不同条件分页查询
		ResultInfo<Page<Note>> resultInfo = noteService.queryNoteListByPage(userId,pageSize,currentPage,title,date,typeId);
		request.setAttribute("resultInfo", resultInfo);
		System.out.println("resultInfo"+JSON.toJSONString(resultInfo));
		//查询左侧云记日期（只是遍历出来）
		List<NoteVo> dateInfo = noteService.queryNoteByDate(userId);
		request.setAttribute("dateInfo", dateInfo);
		//查询左侧云记类别
		List<NoteVo> typeInfo = noteService.queryNoteByType(userId);
		request.getSession().setAttribute("typeInfo", typeInfo);
		//设置动态包含页面
		request.setAttribute("changePage", "note/list.jsp");
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
