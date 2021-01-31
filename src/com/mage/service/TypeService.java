package com.mage.service;

import java.util.List;

import com.mage.dao.TypeDao;
import com.mage.po.NoteType;
import com.mage.po.vo.ResultInfo;
import com.mage.util.StringUtil;

public class TypeService {

	private TypeDao typeDao = new TypeDao();

	/**
	 * 	查询类别
	 * @param userId
	 * @return
	 */
	public List<NoteType> queryNoteTypeByUserId(Integer userId) {
		List<NoteType> NoteTypeList = typeDao.queryNoteTypeByUserId(userId);
		return NoteTypeList;
	}
	/**
	 * 	验证类别名唯一性
	 * @param typeName
	 * @param typeId
	 * @param userId
	 */
	public int checkType(String typeName, String typeId, Integer userId) {
		//非空判断
		if(StringUtil.isEmpty(typeName)) {
			return 0;
		}
		//调用方法
		NoteType noteType = typeDao.checkType(typeId,userId,typeName);
		if(noteType!=null) {
			return 0;
		}
		return 1;
	}
	


	
	
}
