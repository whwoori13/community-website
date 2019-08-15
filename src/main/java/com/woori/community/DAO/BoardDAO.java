package com.woori.community.DAO;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import com.woori.community.DTO.AlertDTO;
import com.woori.community.DTO.CategoryDTO;
import com.woori.community.entity.Article;
import com.woori.community.entity.Reply;

public interface BoardDAO {

	public List<Article> loadArticles() throws Exception;

	public Article loadOneArticle(int num) throws Exception;

	public void saveArticle(Article article) throws Exception;

	public void deleteArticle(int articleNum) throws Exception;

	public void editArticle(Article article) throws Exception;

	public int saveReply(Reply reply) throws Exception;

	public List<Reply> loadReplies(Map<String, Object> map) throws Exception;

	public void deleteReply(Reply reply) throws Exception;

	public List<Article> loadArticlesPaged(Map map) throws Exception;

	public int getTotalCount(String tableName) throws Exception;

	public void increaseViewCount(int num) throws Exception;

	public void increaseReplyCount(int articleNum) throws Exception;

	public void decreaseReplyCount(int articleNum) throws Exception;

	public int getTotalReplyCount(int num) throws Exception;

	public int getRecentArticleNum() throws Exception;

	public int getSearchedCount(Map<String, Object> map) throws Exception;

	public List<Article> loadSearchedArticles(Map<String, Object> map) throws Exception;

	public void udtGrpNum() throws Exception;

	public Integer getGroupOrder(Map<String, Object> map) throws Exception;

	public void adjustOrder(Map<String, Object> map) throws Exception;

	public Integer getGroupCount(int groupNum) throws Exception;

	

	public Integer getNextDepth(Reply reply) throws Exception;

	public void hideReply(int num) throws Exception;

	public int getReplyPos(Reply reply) throws Exception;

	public int getArticleOrder(int articleNum) throws Exception;

	public void getCategory(HttpServletRequest request) throws Exception;

	public void getCTmatch(HttpServletRequest request) throws Exception;

	public void getTopic(HttpServletRequest request) throws Exception;

	public int getTotalArticleCount(HttpServletRequest request, String topic) throws Exception;

	public int getArticleOrderSearched(Map<String, Object> map) throws Exception;

	public int getTotalArticleCountSearched(Map<String, Object> map) throws Exception;

	public List<Article> loadArticlesPagedSearched(Map<String, Object> map2) throws Exception;

	public Reply loadOneReply(int replyNum);

	public void putAlert(AlertDTO alert);

	public void increaseNewAlert(String customerId);

	public String loadOneReplyByGrp(Map<String, Object> map);

	public void removeAlert(int replyNum);

	public void decreaseNewAlert(String id);

	public int getMyReplyCount(String id);

	public List<Reply> loadMyReplies(Map<String, Object> map);

	
}
