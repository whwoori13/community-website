package com.woori.community.DAOImpl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.woori.community.DAO.AdminDAO;
import com.woori.community.DTO.CTmatchDTO;
import com.woori.community.DTO.CategoryDTO;
import com.woori.community.DTO.TopicDTO;

@Repository
public class AdminDAOImpl implements AdminDAO {

	@Autowired
	private SqlSession sqlSession;
	private static final String nameSpace = "com.woori.community.AdminDAO";

	@Override
	public void insertCategory(CategoryDTO category) {

		sqlSession.insert(nameSpace + ".insertCategory", category);

	}

	@Override
	public void insertTopic(TopicDTO topic) {
		sqlSession.insert(nameSpace + ".insertTopic", topic);

	}

	@Override
	public void deleteCategory(CategoryDTO category) {
		sqlSession.delete(nameSpace + ".deleteCategory", category);

	}

	@Override
	public void deleteTopic(TopicDTO topic) {
		sqlSession.delete(nameSpace + ".deleteTopic", topic);

	}

	@Override
	public void match(CTmatchDTO match) {
		
		System.out.println(match.getCategory() + " " + match.getTopic());
		sqlSession.insert(nameSpace + ".match", match);
		
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		System.out.println(match.getCategory() + " " + match.getTopic());
		
	}

	@Override
	public void unmatch(CTmatchDTO unmatch) {
		sqlSession.delete(nameSpace + ".unmatch", unmatch);
		
	}

	@Override
	public int getCategoryCount() {
		return sqlSession.selectOne(nameSpace + ".getCategoryCount");
		
	}

	@Override
	public int getTopicCount() {
		return sqlSession.selectOne(nameSpace + ".getTopicCount");
	}

}
