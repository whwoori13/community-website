package com.woori.community.DAOImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.woori.community.DAO.HomeDAO;
import com.woori.community.DTO.AlertDTO;
import com.woori.community.DTO.RankDTO;
import com.woori.community.entity.Customer;

@Repository
public class HomeDAOImpl implements HomeDAO {

	@Autowired
	private SqlSession sqlSession;
	private static final String nameSpace = "com.woori.community.HomeDAO";

	@Override
	public void insertCustomer(Customer customer) throws Exception {
		sqlSession.insert(nameSpace + ".insertCustomer", customer);

	}

	@Override
	public Customer checkIdPw(Customer customer) {
		return sqlSession.selectOne(nameSpace + ".checkIdPw", customer);
	}

	@Override
	public List<AlertDTO> loadAlert(String id) {
		return sqlSession.selectList(nameSpace + ".loadAlert", id);
	}

	@Override
	public int loadNewAlertCnt(String id) {
		return sqlSession.selectOne(nameSpace + ".loadNewAlertCnt", id);
	}

	@Override
	public void flushAlert(String customerId) {
		sqlSession.delete(nameSpace + ".flushAlert", customerId);
		sqlSession.update(nameSpace + ".setNewAlertZero", customerId);

	}

	@Override
	public List<String> getFavorite(String id) {
		return sqlSession.selectList(nameSpace + ".getFavorite", id);
	}

	@Override
	public void addFav(String id, String topic) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("topic", topic);
		sqlSession.insert(nameSpace + ".addFav", map);

	}

	@Override
	public void deFav(String id, String topic) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("topic", topic);
		sqlSession.insert(nameSpace + ".deFav", map);
	}

	@Override
	public List<RankDTO> loadRating() {
		return sqlSession.selectList(nameSpace + ".loadRating");

	}

	@Override
	public int checkIdDup(String inputId) {
		return sqlSession.selectOne(nameSpace + ".checkIdDup", inputId);
	}

	@Override
	public int checkcName(String input) {
		return sqlSession.selectOne(nameSpace + ".checkcName", input);
	}

	@Override
	public int checkcCode(String input) {
		return sqlSession.selectOne(nameSpace + ".checkcCode", input);
	}

	@Override
	public int checktName(String input) {
		return sqlSession.selectOne(nameSpace + ".checktName", input);
	}

	@Override
	public int checktCode(String input) {
		return sqlSession.selectOne(nameSpace + ".checktCode", input);
	}

	@Override
	public void deleteCustomer(Customer customer) {
		sqlSession.delete(nameSpace + ".deleteCustomer", customer);
		
	}

}
