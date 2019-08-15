package com.woori.community.DAOImpl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.woori.community.DAO.BoardDAO;
import com.woori.community.DTO.AlertDTO;
import com.woori.community.DTO.CTmatchDTO;
import com.woori.community.DTO.CategoryDTO;
import com.woori.community.DTO.TopicDTO;
import com.woori.community.entity.Article;
import com.woori.community.entity.Customer;
import com.woori.community.entity.Reply;

@Repository
public class BoardDAOImpl implements BoardDAO {

	@Autowired
	private SqlSession sqlSession;
	private static final String nameSpace = "com.woori.community.BoardDAO";

	@Override
	public List<Article> loadArticles() throws Exception {

		List<Article> articleList = sqlSession.selectList(nameSpace + ".loadArticles");

		// 각 글들의 제목에 html 태그 방지
		for (int i = 0; i < articleList.size(); i++) {

			Article article = articleList.get(i);
			article.setTitle(article.getTitle().replace("<", "&lt;")); // html 태그 방지
			article.setTitle(article.getTitle().replace(">", "&gt;")); // html 태그 방지
		}

		return articleList;
	}

	@Override
	public List<Article> loadArticlesPaged(Map map) throws Exception {

		List<Article> articleList;

		if (map.get("topic").equals("myArticles"))
			articleList = sqlSession.selectList(nameSpace + ".loadMyArticles", map);
		else
			articleList = sqlSession.selectList(nameSpace + ".loadArticlesPaged", map);

		// 각 글들의 제목에 html 태그 방지
		for (int i = 0; i < articleList.size(); i++) {

			Article article = articleList.get(i);
			article.setTitle(article.getTitle().replace("<", "&lt;")); // html 태그 방지
			article.setTitle(article.getTitle().replace(">", "&gt;")); // html 태그 방지
		}

		return articleList;
	}

	/*
	 * @Override public Article loadOneArticle(int num) throws Exception { Article
	 * article = sqlSession.selectOne(nameSpace + ".loadOneArticle", num);
	 * article.setContent(article.getContent().replace("\r\n", "<br/>")); // 줄바꿈을
	 * 살려주기 위해. return article; }
	 */

	@Override
	public Article loadOneArticle(int num) throws Exception {

		Article article = sqlSession.selectOne(nameSpace + ".loadOneArticle", num);

		try {
			article.setTitle(article.getTitle().replace("<", "&lt;")); // html 태그 방지
			article.setTitle(article.getTitle().replace(">", "&gt;")); // html 태그 방지

			article.setContent(article.getContent().replace("<", "&lt;")); // html 태그 방지
			article.setContent(article.getContent().replace(">", "&gt;")); // html 태그 방지
			article.setContent(article.getContent().replace("\r\n", "<br/>")); // 줄바꿈을 살려주기 위해.

			return article;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public void saveArticle(Article article) throws Exception {

		// 최대 연속된 줄바꿈 갯수 제한 : 연속한 줄바꿈이 8개 이상 있을때 7개로 줄임.
		while (article.getContent().contains("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n")) {
			article.setContent(article.getContent().replace("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n", "\r\n\r\n\r\n\r\n\r\n\r\n\r\n"));
		}

		sqlSession.insert(nameSpace + ".saveArticle", article);
	}

	@Override
	public void deleteArticle(int articleNum) throws Exception {
		sqlSession.delete(nameSpace + ".deleteArticle", articleNum);

	}

	@Override
	public void editArticle(Article article) throws Exception {

		// 최대 연속된 줄바꿈 갯수 제한 : 연속한 줄바꿈이 8개 이상 있을때 7개로 줄임.
		while (article.getContent().contains("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n")) {
			article.setContent(article.getContent().replace("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n", "\r\n\r\n\r\n\r\n\r\n\r\n\r\n"));
		}

		sqlSession.update(nameSpace + ".editArticle", article);
	}

	@Override
	public int saveReply(Reply reply) throws Exception {

		// 최대 연속된 줄바꿈 갯수 제한 : 연속한 줄바꿈이 5개 이상 있을때 4개로 줄임.
		while (reply.getContent().contains("\r\n\r\n\r\n\r\n\r\n")) {

			reply.setContent(reply.getContent().replace("\r\n\r\n\r\n\r\n\r\n", "\r\n\r\n\r\n\r\n"));
		}

		sqlSession.insert(nameSpace + ".saveReply", reply);

		return sqlSession.selectOne(nameSpace + ".getRecentReplyNum");

	}

	@Override
	public List<Reply> loadReplies(Map<String, Object> map) throws Exception {

		List<Reply> replyList = sqlSession.selectList(nameSpace + ".loadReplies", map);
		for (int i = 0; i < replyList.size(); i++) {

			Reply reply = replyList.get(i);

			reply.setContent(reply.getContent().replace("<", "&lt;")); // html 태그 방지
			reply.setContent(reply.getContent().replace(">", "&gt;")); // html 태그 방지
			reply.setContent(reply.getContent().replace("\r\n", "<br/>")); // 줄바꿈을 살려주기 위해
		}

		return replyList;
	}

	@Override
	public int getTotalCount(String tableName) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getTotalCount", tableName);

	}

	@Override
	public int getTotalReplyCount(int num) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getTotalReplyCount", num);
	}

	@Override
	public void increaseViewCount(int num) throws Exception {
		sqlSession.update(nameSpace + ".increaseViewCount", num);

	}

	@Override
	public void increaseReplyCount(int articleNum) throws Exception {
		sqlSession.update(nameSpace + ".increaseReplyCount", articleNum);

	}

	@Override
	public void decreaseReplyCount(int articleNum) throws Exception {
		sqlSession.update(nameSpace + ".decreaseReplyCount", articleNum);

	}

	@Override
	public int getRecentArticleNum() throws Exception {
		return sqlSession.selectOne(nameSpace + ".getRecentArticleNum");
	}
	/*
	 * @Override public int getSearchedCount(Stack<String> searchTypes) throws
	 * Exception { return sqlSession.selectOne(nameSpace + ".getSearchedCount"); }
	 */

	@Override
	public int getSearchedCount(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getSearchedCount", map);
	}

	@Override
	public List<Article> loadSearchedArticles(Map<String, Object> map) throws Exception {
		return sqlSession.selectList(nameSpace + ".loadSearchedArticles", map);
	}

	@Override
	public void udtGrpNum() throws Exception {
		sqlSession.update(nameSpace + ".udtGrpNum");

	}

	@Override
	public Integer getGroupOrder(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getGroupOrder", map);
	}

	@Override
	public void adjustOrder(Map<String, Object> map) throws Exception {
		sqlSession.update(nameSpace + ".adjustOrder", map);
	}

	@Override
	public Integer getGroupCount(int groupNum) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getGroupCount", groupNum);
	}

	@Override
	public Integer getNextDepth(Reply reply) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getNextDepth", reply);
	}

	@Override
	public void hideReply(int num) throws Exception {
		sqlSession.update(nameSpace + ".hideReply", num);

	}

	@Override
	// 대댓글이 없는 경우 해당 댓글을 삭제하고 아래 댓글들의 groupOrder를 1씩 감소시키고
	// 부모 댓글이 hidden 상태일때 재귀적으로 부모에 대해 자기 자신 메소드를 호출함.
	public void deleteReply(Reply reply) throws Exception {

		sqlSession.delete(nameSpace + ".deleteReply", reply);

		// 삭제한 댓글 아래의 댓글들의 groupOrder 1씩 감소
		sqlSession.update(nameSpace + ".adjustAfterDelete", reply);

		// 부모 댓글이 hidden 상태일때 재귀적으로 부모에 대해 deleteReply 메소드를 호출하기 위한 과정.
		Reply parentReply = sqlSession.selectOne(nameSpace + ".getParentReply", reply);

		// 부모가 없을때 아무것도 안함.
		if (parentReply == null)
			;

		// 부모 댓글이 hidden 상태인지 테스트
		else if (parentReply.getIsDeleted() == 1) {

			// 부모의 직계자손 갯수 구하기 위해 바운더리 존재 여부부터 구함 : 부모 댓글과 depth가 같거나 작고 부모 댓글 아래에 있는 댓글이
			// 바운더리임.
			Reply boundaryReply = sqlSession.selectOne(nameSpace + ".getNextSameDepthReply", parentReply);

			// 직계자손 갯수 변수 선언
			int directChildCount;

			// 1. 바운더리가 없을때
			if (boundaryReply == null) {
				directChildCount = sqlSession.selectOne(nameSpace + ".getDirectChildCountNoBound", parentReply);

			}

			// 2. 바운더리가 있을때
			else {
				directChildCount = sqlSession.selectOne(nameSpace + ".getDirectChildCountWithBound", parentReply);

			}

			// 부모의 직계자손이 0개(지금 지우려는것)일때만 부모를 지울 수 있음.
			if (directChildCount == 0) {
				deleteReply(parentReply);
			}
		}
	}

	@Override
	public int getReplyPos(Reply reply) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getReplyPos", reply);
	}

	@Override
	public int getArticleOrder(int articleNum) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getArticleOrder", articleNum);
	}

	///////////////////////////////////////////////////////////////////////
	@Override
	public void getCategory(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		List<CategoryDTO> category = sqlSession.selectList(nameSpace + ".getCategory");
		session.setAttribute("categoryList", category);

	}

	@Override
	public void getCTmatch(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		List<CTmatchDTO> CTmatch = sqlSession.selectList(nameSpace + ".getCTmatch");
		session.setAttribute("CTmatchList", CTmatch);

	}

	@Override
	public void getTopic(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		List<TopicDTO> topic = sqlSession.selectList(nameSpace + ".getTopic");
		session.setAttribute("topicList", topic);

	}
	///////////////////////////////////////////////////////////////////////

	@Override
	public int getTotalArticleCount(HttpServletRequest request, String topic) throws Exception {
		
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		
		if (topic.equals("myArticles"))
			return sqlSession.selectOne(nameSpace + ".getMyArticleCount", customer.getId());
		else
			return sqlSession.selectOne(nameSpace + ".getTotalArticleCount", topic);
	}

	@Override
	public int getArticleOrderSearched(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getArticleOrderSearched", map);
	}

	@Override
	public int getTotalArticleCountSearched(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne(nameSpace + ".getTotalArticleCountSearched", map);
	}

	@Override
	public List<Article> loadArticlesPagedSearched(Map<String, Object> map2) throws Exception {
		List<Article> articleList = sqlSession.selectList(nameSpace + ".loadArticlesPagedSearched", map2);

		// 각 글들의 제목에 html 태그 방지
		for (int i = 0; i < articleList.size(); i++) {

			Article article = articleList.get(i);
			article.setTitle(article.getTitle().replace("<", "&lt;")); // html 태그 방지
			article.setTitle(article.getTitle().replace(">", "&gt;")); // html 태그 방지
		}

		return articleList;

	}

	@Override
	public Reply loadOneReply(int replyNum) {
		return sqlSession.selectOne(nameSpace + ".loadOneReply", replyNum);
	}

	@Override
	public void putAlert(AlertDTO alert) {

		String abbvContent = StringUtils.abbreviate(alert.getContent(), 28);
		alert.setContent(abbvContent);
		sqlSession.insert(nameSpace + ".putAlert", alert);

	}

	@Override
	public void increaseNewAlert(String customerId) {
		sqlSession.update(nameSpace + ".increaseNewAlert", customerId);

	}

	@Override
	public String loadOneReplyByGrp(Map<String, Object> map) {
		return sqlSession.selectOne(nameSpace + ".loadOneReplyByGrp", map);
	}

	@Override
	public void removeAlert(int replyNum) {
		sqlSession.delete(nameSpace + ".removeAlert", replyNum);

	}

	@Override
	public void decreaseNewAlert(String id) {
		sqlSession.update(nameSpace + ".decreaseNewAlert", id);

	}

	@Override
	public int getMyReplyCount(String id) {
		return sqlSession.selectOne(nameSpace + ".getMyReplyCount", id);
	}

	@Override
	public List<Reply> loadMyReplies(Map<String, Object> map) {
		return sqlSession.selectList(nameSpace + ".loadMyReplies", map);
	}
}
