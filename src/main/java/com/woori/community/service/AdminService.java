package com.woori.community.service;

import com.woori.community.DTO.CTmatchDTO;
import com.woori.community.DTO.CategoryDTO;
import com.woori.community.DTO.TopicDTO;

public interface AdminService {

	public int insertCategory(CategoryDTO category);

	public int insertTopic(TopicDTO topic);

	public void deleteCategory(CategoryDTO category);
	
	public void deleteTopic(TopicDTO topic);

	public void match(CTmatchDTO match);

	public void unatch(CTmatchDTO unmatch);
}
