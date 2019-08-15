package com.woori.community.service;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import com.woori.community.entity.Article;
import com.woori.community.entity.Customer;
import com.woori.community.entity.Reply;

public interface BoardService {
	
	
	
	public List<Article> loadArticles() throws Exception;

	public Article loadOneArticle(int num) throws Exception;

	public int saveArticle(Article article) throws Exception;

	public void deleteArticle(int articleNum) throws Exception;

	public void editArticle(Article article) throws Exception;

	// 댓글 저장시 해당 글의 댓글수 +1
	// 댓글 저장 후 방금 저장된 root 댓글의 groupNum을 num과 일치시켜줌
	public void saveReply(Reply reply) throws Exception; 
	
	// 대댓글 저장시 해당 글의 댓글수 +1
	public void saveReReply(Reply reply, int parentGrpOrd) throws Exception;

	public List<Reply> loadReplies(Map<String, Object> map) throws Exception;

	public void deleteReply(Reply reply) throws Exception;

	public List<Article> loadArticlesPaged(Map map) throws Exception;

	public int getTotalCount(String tableName) throws Exception;

	public int getTotalReplyCount(int num) throws Exception;

	public int getSearchedCount(Map<String, Object> map) throws Exception;

	public List<Article> loadSearchedArticles(Map<String, Object> map) throws Exception;

	public Integer getGroupOrder(Map<String, Object> map) throws Exception;

	public void adjustOrder(Map<String, Object> map) throws Exception;

	public Integer getGroupCount(int groupNum) throws Exception;

	public int getReplyPos(Reply reply) throws Exception;

	public int getArticleOrder(int articleNum) throws Exception;
	
	public void loadNav(HttpServletRequest request) throws Exception;

	public int getTotalArticleCount(HttpServletRequest request, String topic) throws Exception;

	public int getArticleOrderSearched(Map<String, Object> map) throws Exception;

	public int getTotalArticleCountSearched(Map<String, Object> map) throws Exception;

	public List<Article> loadArticlesPagedSearched(Map<String, Object> map2) throws Exception;

	public Reply loadOneReply(int replyNum);

	public void removeAlert(int replyNum);

	public void decreaseNewAlert(String id);

	public int getMyReplyCount(String id);

	public List<Reply> loadMyReplies(Map<String, Object> map);

	

	

	

}
