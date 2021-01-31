package com.mage.service;

import java.util.ArrayList;
import java.util.List;

import com.mage.dao.NoteDao;
import com.mage.po.Note;
import com.mage.po.NoteType;
import com.mage.po.vo.NoteVo;
import com.mage.po.vo.ResultInfo;
import com.mage.util.Page;
import com.mage.util.StringUtil;

public class NoteService {

	private NoteDao noteDao = new NoteDao();
	
	/**
	 * 	实现不同条件分页查询
	 * @param userId
	 * @param pageSize
	 * @param currentPage
	 * @param title
	 * @param date
	 * @param typeId
	 * @return
	 */
	public ResultInfo<Page<Note>> queryNoteListByPage(Integer userId, String pageSizeStr, String currentPageStr, String title,
			String date, String typeId) {
		ResultInfo<Page<Note>> resultInfo = new ResultInfo<Page<Note>>();
		//如果前台没传pageSize、currentPage 则设置默认值,如果传了，则赋值
		Integer pageSize = 2;
		Integer currentPage = 1;
		if(!StringUtil.isEmpty(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		if(!StringUtil.isEmpty(currentPageStr)) {
			currentPage = Integer.parseInt(currentPageStr);
		}
		//通过userId查询云记总数
		Integer totalCount = noteDao.queryNoteCount(userId,title,date,typeId);
		if(totalCount==0) {
			resultInfo.setCode(0);
			resultInfo.setMsg("没有云集记录");
			return resultInfo;
		}
		//通过不同的条件查询指定数量的云记记录信息
		
		Integer index = (currentPage-1)*pageSize;
		List<Note> noteList = noteDao.queryNoteByPage(index,pageSize,userId,title,date,typeId);
		//调用分页工具中的方法
		Page<Note> page = new Page<>(totalCount,pageSize,currentPage);
		page.setDataList(noteList);
		resultInfo.setResult(page);
		resultInfo.setCode(1);
		return resultInfo;
	}
	/**
	 * 	查询左侧云记日期
	 * @param userId
	 * @return
	 */
	public List<NoteVo> queryNoteByDate(Integer userId) {
		List<NoteVo> list = noteDao.queryNoteByDate(userId);
		return list;
	}
	/**
	 * 查询左侧云记类别
	 * 
	 * @param userId
	 * @return
	 */
	public List<NoteVo> queryNoteByType(Integer userId) {
		List<NoteVo> typeList = NoteDao.queryNoteByType(userId);
		return typeList;
	}
	
	/**
	 * 添加或修改云记
	 * @param typeId
	 * @param title
	 * @param content
	 * @param noteId
	 * @return
	 */
	
	public ResultInfo<Note> addOrUpdate(String typeId, String title, String content, String noteId) {
		ResultInfo<Note> resultInfo = new ResultInfo<>();
		//非空判断
		if(StringUtil.isEmpty(typeId)||StringUtil.isEmpty(title)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("保存失败！");
			return resultInfo;
		}
		//调用dao层
		int row  = noteDao.addOrUpdate(typeId,title,content,noteId);
		if(row>0) {
			resultInfo.setCode(1);
			resultInfo.setMsg("保存成功！");
			return resultInfo;
		}else {
			resultInfo.setCode(0);
			resultInfo.setMsg("保存失败！");
			Note note = new Note();
			note.setContent(content);
			note.setTitle(title);
			note.setTypeId(Integer.parseInt(typeId));
			resultInfo.setResult(note);
		}
		return resultInfo;
	}
	/**
	 * 	点击云记记录，查看详细信息
	 * @param noteId
	 * @return
	 */
	public Note queryNoteDetail(String noteId) {
		Note note = new Note();
		//非空判断
		if(StringUtil.isEmpty(noteId)) {
			return null;
		}
		note = noteDao.queryNoteDetail(noteId);
		return note;
	}
	/**
	 * 	删除云记详细信息
	 * @param noteId
	 * @return
	 */
	public Integer deleteNote(String noteId) {
		// 1、判断参数
		if(StringUtil.isEmpty(noteId)){
			return null;
		}
		// 2、调用Dao层，删除记录，返回受影响的行数
		int row = noteDao.deleteNote(noteId);
		// 3、判断受影响的行数
		if(row>0){
			return 1;
		}
		// 4、返回code
		return 0;
	}
	/**
	 *  获取当前登录用户的所有云记类型
	 * @param userId
	 * @return
	 */
	public List<NoteType> findTypeList(Integer userId) {
		List<NoteType> list = noteDao.findTypeList(userId);
		
		return list;
	}
}
