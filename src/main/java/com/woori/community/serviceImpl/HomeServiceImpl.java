package com.woori.community.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woori.community.DAO.HomeDAO;
import com.woori.community.DTO.AlertDTO;
import com.woori.community.DTO.RankDTO;
import com.woori.community.entity.Customer;
import com.woori.community.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	private HomeDAO dao;

	@Override
	public void insertCustomer(Customer customer) throws Exception {
		dao.insertCustomer(customer);

	}

	@Override
	public Customer login(Customer customer) {

		return dao.checkIdPw(customer);
	}

	@Override
	public List<AlertDTO> loadAlert(String id) {
		return dao.loadAlert(id);

	}

	@Override
	public int loadNewAlertCnt(String id) {
		return dao.loadNewAlertCnt(id);
	}

	@Override
	public void flushAlert(String customerId) {
		dao.flushAlert(customerId);

	}

	@Override
	public List<String> getFavorite(String id) {
		return dao.getFavorite(id);
	}

	@Override
	public void addFav(String id, String topic) {
		dao.addFav(id, topic);

	}

	@Override
	public void deFav(String id, String topic) {
		dao.deFav(id, topic);

	}

	@Override
	public List<RankDTO> loadRating() {

		return dao.loadRating();

	}

	@Override
	public int checkIdDup(String inputId) {
		return dao.checkIdDup(inputId);
	}

	@Override
	public int chechcName(String input) {
		return dao.checkcName(input);
	}

	@Override
	public int chechcCode(String input) {
		return dao.checkcCode(input);
	}

	@Override
	public int chechtName(String input) {
		return dao.checktName(input);
	}

	@Override
	public int chechtCode(String input) {
		return dao.checktCode(input);
	}

	@Override
	public void deleteCustomer(Customer customer) {
		dao.deleteCustomer(customer);
	}

}
