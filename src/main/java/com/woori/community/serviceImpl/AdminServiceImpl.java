package com.woori.community.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woori.community.DAO.AdminDAO;
import com.woori.community.DTO.CTmatchDTO;
import com.woori.community.DTO.CategoryDTO;
import com.woori.community.DTO.TopicDTO;
import com.woori.community.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminDAO dao;

	@Override
	public int insertCategory(CategoryDTO category) {
		if (dao.getCategoryCount() > 6)
			// too many -> fail code 0
			return 0;
		else {
			// success code 1
			dao.insertCategory(category);
			return 1;
		}
	}

	@Override
	public int insertTopic(TopicDTO topic) {

		if (dao.getTopicCount() > 49)
			// too many -> fail code 0
			return 0;
		else {
			// success code 1
			dao.insertTopic(topic);
			return 1;
		}

	}

	@Override
	public void deleteCategory(CategoryDTO category) {
		dao.deleteCategory(category);

	}

	@Override
	public void deleteTopic(TopicDTO topic) {
		dao.deleteTopic(topic);

	}

	@Override
	public void match(CTmatchDTO match) {
		dao.match(match);

	}

	@Override
	public void unatch(CTmatchDTO unmatch) {
		dao.unmatch(unmatch);

	}

}
