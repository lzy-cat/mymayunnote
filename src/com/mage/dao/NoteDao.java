package com.mage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mage.po.Note;
import com.mage.po.NoteType;
import com.mage.po.vo.NoteVo;
import com.mage.util.DBUtil;
import com.mage.util.StringUtil;

public class NoteDao {

	/**
	 * 	查询云记总数
	 * @param userId
	 * @param title
	 * @param date
	 * @param typeId
	 * @return
	 */
	public Integer queryNoteCount(Integer userId, String title, String date, String typeId) {
		Integer count = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT count(*) from tb_note  nt JOIN tb_note_type ty on nt.typeId=ty.typeId where ty.userId=?";
			if(!StringUtil.isEmpty(title)) {
				sql += " and title like ?";
			}
			if(StringUtil.isNotEmpty(date)) {
				sql += " and date_format(pubtime,'%Y年%m月') = ? ";
			}
			if(StringUtil.isNotEmpty(typeId)) {
				sql += " and ty.typeId = ?";
			}
			//预编译
			sta = conn.prepareStatement(sql);
			//设置参数
			sta.setInt(1, userId);
			if(StringUtil.isNotEmpty(title)){
				sta.setString(2, "%"+title+"%");
			}
			if(StringUtil.isNotEmpty(date)){
				sta.setString(2, date);
			}
			if(StringUtil.isNotEmpty(typeId)){
				sta.setInt(2, Integer.parseInt(typeId));
			}
			res = sta.executeQuery();
			while(res.next()) {
				count = res.getInt(1);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(res, sta, conn);
		}
		return count;
	}
	/**
	 * 	根据不同条件分页查询云记,返回一个集合
	 * @param index
	 * @param pageSize
	 * @param userId
	 * @param title
	 * @param date
	 * @param typeId
	 * @return
	 */
	public List<Note> queryNoteByPage(Integer index, Integer pageSize, Integer userId, String title, String date,
			String typeId) {
		List<Note> noteList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT * from tb_note  nt JOIN tb_note_type ty on nt.typeId=ty.typeId where ty.userId=?";
			if(!StringUtil.isEmpty(title)) {
				sql += " and title like ?";
			}
			if(StringUtil.isNotEmpty(date)) {
				sql += " and date_format(pubtime,'%Y年%m月') = ?";
			}
			if(StringUtil.isNotEmpty(typeId)) {
				sql += " and ty.typeId = ?";
			}
			sql += " order by nt.pubtime desc limit ?,?";
			//预编译
			sta = conn.prepareStatement(sql);
			sta.setInt(1, userId);
			if(StringUtil.isNotEmpty(title)){
				sta.setString(2, "%"+title+"%");
				sta.setInt(3, index);
				sta.setInt(4, pageSize);
			} else if(StringUtil.isNotEmpty(date)){
				sta.setString(2, date);
				sta.setInt(3, index);
				sta.setInt(4, pageSize);
			} else if(StringUtil.isNotEmpty(typeId)){
				sta.setInt(2, Integer.parseInt(typeId));
				sta.setInt(3, index);
				sta.setInt(4, pageSize);
			} else {
				sta.setInt(2, index);
				sta.setInt(3, pageSize);
			}
			res = sta.executeQuery();
			while(res.next()) {
				Note note = new Note();
				note.setNoteId(res.getInt("noteId"));
				note.setContent(res.getString("content"));
				note.setPubTime(res.getTimestamp("pubTime"));
				note.setTitle(res.getString("title"));
				note.setTypeId(res.getInt("typeId"));
				noteList.add(note);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(res, sta, conn);
		}
		return noteList;
	}
	/**
	 * 	云记日期分组
	 * @param userId
	 * @return
	 */
	public List<NoteVo> queryNoteByDate(Integer userId) {
		List<NoteVo> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			String sql = "SELECT DATE_FORMAT(pubtime,'%Y年%m月') as noteGroupName,count(noteId) as noteCount "
					+ "from tb_note nt left join tb_note_type ty on nt.typeId = ty.typeId where userId=? "
					+ "group by noteGroupName order by noteGroupName desc";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, userId);
			// 执行查询
			res = sta.executeQuery();
			// 分析结果集
			while(res.next()){
				NoteVo noteVo = new NoteVo();
				noteVo.setNoteGroupName(res.getString("noteGroupName"));
				noteVo.setNoteCount(res.getInt("noteCount"));
				list.add(noteVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, sta, conn);
		}
		
		return list;
	}
	
	/**
	 * 	左侧云记类别分组
	 * @param userId
	 * @return
	 */
	public static List<NoteVo> queryNoteByType(Integer userId) {
		List<NoteVo> typeList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "select ty.typeId,ty.typeName as noteGroupName ,count(noteId) as noteCount from tb_note_type ty left join tb_note nt on ty.typeId=nt.typeId where ty.userId=? group by ty.typeId order by ty.typeId desc";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, userId);
			// 执行查询
			res = sta.executeQuery();
			// 分析结果集
			while(res.next()){
				NoteVo noteVo = new NoteVo();
				noteVo.setNoteGroupName(res.getString("noteGroupName"));
				noteVo.setNoteCount(res.getInt("noteCount"));
				noteVo.setTypeId(res.getInt("typeId"));
				typeList.add(noteVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, sta, conn);
		}
		return typeList;
	}
	/**
	 * 	添加或修改云记
	 * @param typeId
	 * @param title
	 * @param content
	 * @param noteId
	 * @return
	 */
	public int addOrUpdate(String typeId, String title, String content, String noteId) {
		int row = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "";
			if(StringUtil.isEmpty(noteId)){ // 添加操作
				sql = "insert into tb_note (title,content,typeId,pubTime) values (?,?,?,now())";
			} else {
				sql = "update tb_note set title = ?,content = ?,typeId = ?,pubTime = now() where noteId = ?";
			}
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setString(1, title);
			sta.setString(2, content);
			sta.setInt(3, Integer.parseInt(typeId));
			if(StringUtil.isNotEmpty(noteId)){
				sta.setInt(4, Integer.parseInt(noteId));
			}
			// 执行更新
			row = sta.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, sta, conn);
		}
		return row;
	}
	
	/**
	 * 	点击云记记录，查看详细信息
	 * @param noteId
	 * @return
	 */
	public Note queryNoteDetail(String noteId) {
		Note note = new Note();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res =null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select title, content, pubTime, noteId, n.typeId, typeName from tb_note n left join tb_note_type t on n.typeId = t.typeId where n.noteId = ?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, Integer.parseInt(noteId));
			// 执行查询
			res = sta.executeQuery();
			// 分析结果集
			while(res.next()){
				note.setContent(res.getString("content"));
				note.setNoteId(res.getInt("noteId"));
				note.setPubTime(res.getTimestamp("pubTime"));
				note.setTitle(res.getString("title"));
				note.setTypeId(res.getInt("typeId"));
				note.setTypeName(res.getString("typeName"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(res, sta, conn);
		}
			
		return note;
	}
	/**
	 * 		删除云记详细信息
	 * @param noteId
	 * @return
	 */
	public int deleteNote(String noteId) {
		int row = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "delete from tb_note where noteId = ?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, Integer.parseInt(noteId));
			// 执行更新
			row = sta.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, sta, conn);
		}
		return row;
	}
	/**
	 * 	查询云记
	 * @param userId
	 * @return
	 */
	public List<NoteType> findTypeList(Integer userId) {
		List<NoteType> list = new ArrayList();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
		// 获取数据库连接
		conn = DBUtil.getConnection();
		// 编写sql
		String sql = "select * from tb_note_type where userId= ?";
		// 预编译
		sta = conn.prepareStatement(sql);
		// 设置参数
		sta.setInt(1,userId);
		// 执行更新
		res = sta.executeQuery();
		while(res.next()) {
			NoteType noteType = new NoteType();
			noteType.setTypeId(res.getInt("typeId"));
			noteType.setTypeName(res.getString("typeName"));
			noteType.setUserId(userId);
			list.add(noteType);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		DBUtil.close(null, sta, conn);
	}
	return list;
	}
}
