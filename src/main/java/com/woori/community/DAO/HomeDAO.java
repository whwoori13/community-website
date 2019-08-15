package com.woori.community.DAO;

import java.util.List;

import com.woori.community.DTO.AlertDTO;
import com.woori.community.DTO.RankDTO;
import com.woori.community.entity.Customer;

public interface HomeDAO {
	
	public void insertCustomer(Customer customer) throws Exception;

	public Customer checkIdPw(Customer customer);

	public List<AlertDTO> loadAlert(String id);

	public int loadNewAlertCnt(String id);

	public void flushAlert(String customerId);

	public List<String> getFavorite(String id);

	public void addFav(String id, String topic);

	public void deFav(String id, String topic);

	public List<RankDTO> loadRating();

	public int checkIdDup(String inputId);

	public int checkcName(String input);

	public int checkcCode(String input);

	public int checktName(String input);

	public int checktCode(String input);

	public void deleteCustomer(Customer customer);

	

	
	
}
