package com.mage.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.Detail;

import com.mage.dao.TypeDao;
import com.mage.po.Note;
import com.mage.po.NoteType;
import com.mage.po.User;
import com.mage.po.vo.ResultInfo;
import com.mage.service.NoteService;
import com.mage.service.TypeService;
import com.mage.util.StringUtil;

/**
 * Servlet implementation class NoteServlet
 */
@WebServlet("/note")
public class NoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private NoteService noteService = new NoteService();
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String actionName = request.getParameter("actionName");
		
		if("view".equals(actionName)) {
			//点击发布云记后的方法
			notePublish(request,response);
			return;
		}else if("addOrUpdate".equals(actionName)) {
			//添加或修改云记
			addOrUpdate(request,response);
			return;
		}else if("detail".equals(actionName)) {
			//点击云记，查看详细信息
			detailNote(request,response);
			return;
		}else if("delete".equals(actionName)) {
			deleteNoteDetail(request,response);
			return;
		}
	}
	/**
	 * 	删除云记详细信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void deleteNoteDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 1、接收参数
		String noteId = request.getParameter("noteId");
		// 2、调用Service层，返回code
		Integer code = noteService.deleteNote(noteId);
		// 3、响应结果给回调函数
		response.getWriter().write(code+"");
		response.getWriter().close();
	}

	/**
	 * 	点击云记记录，查看详细信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void detailNote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//接收参数
		String noteId = request.getParameter("noteId");
		//调用service方法获取Note对象
		Note note = noteService.queryNoteDetail(noteId);
		// 将note存到域对象
		request.setAttribute("note", note);
		//跳转到detail页面
		request.setAttribute("changePage", "note/detail.jsp");
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
		
	}
	/**
	 * 	添加或修改云记
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 获取参数
		String typeId = request.getParameter("typeId");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		//修改云记需要noteId
		String noteId = request.getParameter("noteId");
		//调用dao层方法
		ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId,title,content,noteId);
		//对dao层传过来的值进行判断
		if(resultInfo.getCode()==1) {
			//重定向跳转到index
			response.sendRedirect("index");
		}else {
			//将失败信息存到域对象中
			request.setAttribute("resultInfo", resultInfo);
			String url = "note?actionName=view";
			if(!StringUtil.isEmpty(noteId)) {
				url+="&typeId="+noteId;
			}
			// 请求转发跳转到note?actionName=view
			request.getRequestDispatcher(url).forward(request, response);
		}
		
	}

	/**
	 * 	点击发布云记后的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void notePublish(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 如果是修改云记，则需要通过noteId查询云记对象
		// 1、接收参数（云记ID）
		String noteId = request.getParameter("noteId");
		// 2、调用Service层，查询云记对象
		Note note = noteService.queryNoteDetail(noteId);
		// 3、将云记对象存到request作用域中
		request.setAttribute("noteInfo", note);
		User user = (User)request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		//获取当前登录用户的所有云记类型(调用typeService中的方法
		//List<NoteType> noteList =noteService.findTypeList(userId); 
		List<NoteType> noteList = new TypeService().queryNoteTypeByUserId(userId);
		request.setAttribute("noteList", noteList);
		//设置动态页面值
		request.setAttribute("changePage", "note/view.jsp");
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}

}
