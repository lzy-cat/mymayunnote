package com.mage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mage.po.NoteType;
import com.mage.util.DBUtil;
import com.mage.util.StringUtil;

public class TypeDao {

	/**
	 * 	查询类别
	 * @param userId
	 * @return
	 */
	public List<NoteType> queryNoteTypeByUserId(Integer userId) {
		List<NoteType> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			//获取连接
			conn = DBUtil.getConnection();
			//编写sql
			String sql = "select * from tb_note_type where userId = ?";
			//预编译
			sta = conn.prepareStatement(sql);
			sta.setInt(1, userId);
			res = sta.executeQuery();
			while(res.next()) {
				NoteType noteType = new NoteType();
				noteType.setTypeId(res.getInt("typeId"));
				noteType.setTypeName(res.getString("typeName"));
				noteType.setUserId(userId);
				list.add(noteType);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(res, sta, conn);
		}
		return list;
	}
	/**
	 * 	验证类别名唯一性
	 * @param typeId
	 * @param userId
	 * @param typeName
	 * @return
	 */
	public NoteType checkType(String typeId, Integer userId, String typeName) {
		NoteType noteType = new NoteType();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			//获取连接
			conn = DBUtil.getConnection();
			//编写sql
			String sql = "select * from tb_note_type where typeName=? and userId=?";
			if(!(StringUtil.isEmpty(typeId))){
				sql += " and typeId != ?";
			}
			sta = conn.prepareStatement(sql);
			sta.setString(1, typeName);
			sta.setInt(2, userId);
			if(!(StringUtil.isEmpty(typeId))){
				sta.setInt(3, Integer.parseInt(typeId));
			}
			res = sta.executeQuery();
			while(res.next()) {
				noteType.setTypeId(res.getInt("typeId"));
				noteType.setTypeName(res.getString("typeName"));
				noteType.setUserId(res.getInt("userId"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(res, sta, conn);
		}
		return noteType;
	}
	
}
