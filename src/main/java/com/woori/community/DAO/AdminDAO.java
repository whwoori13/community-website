package com.woori.community.DAO;

import com.woori.community.DTO.CTmatchDTO;
import com.woori.community.DTO.CategoryDTO;
import com.woori.community.DTO.TopicDTO;

public interface AdminDAO {

	public void insertCategory(CategoryDTO category);

	public void insertTopic(TopicDTO topic);

	public void deleteCategory(CategoryDTO category);

	public void deleteTopic(TopicDTO topic);

	public void match(CTmatchDTO match);

	public void unmatch(CTmatchDTO unmatch);

	public int getCategoryCount();
	
	public int getTopicCount();
	
}
