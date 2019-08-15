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

		// �� �۵��� ���� html �±� ����
		for (int i = 0; i < articleList.size(); i++) {

			Article article = articleList.get(i);
			article.setTitle(article.getTitle().replace("<", "&lt;")); // html �±� ����
			article.setTitle(article.getTitle().replace(">", "&gt;")); // html �±� ����
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

		// �� �۵��� ���� html �±� ����
		for (int i = 0; i < articleList.size(); i++) {

			Article article = articleList.get(i);
			article.setTitle(article.getTitle().replace("<", "&lt;")); // html �±� ����
			article.setTitle(article.getTitle().replace(">", "&gt;")); // html �±� ����
		}

		return articleList;
	}

	/*
	 * @Override public Article loadOneArticle(int num) throws Exception { Article
	 * article = sqlSession.selectOne(nameSpace + ".loadOneArticle", num);
	 * article.setContent(article.getContent().replace("\r\n", "<br/>")); // �ٹٲ���
	 * ����ֱ� ����. return article; }
	 */

	@Override
	public Article loadOneArticle(int num) throws Exception {

		Article article = sqlSession.selectOne(nameSpace + ".loadOneArticle", num);

		try {
			article.setTitle(article.getTitle().replace("<", "&lt;")); // html �±� ����
			article.setTitle(article.getTitle().replace(">", "&gt;")); // html �±� ����

			article.setContent(article.getContent().replace("<", "&lt;")); // html �±� ����
			article.setContent(article.getContent().replace(">", "&gt;")); // html �±� ����
			article.setContent(article.getContent().replace("\r\n", "<br/>")); // �ٹٲ��� ����ֱ� ����.

			return article;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public void saveArticle(Article article) throws Exception {

		// �ִ� ���ӵ� �ٹٲ� ���� ���� : ������ �ٹٲ��� 8�� �̻� ������ 7���� ����.
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

		// �ִ� ���ӵ� �ٹٲ� ���� ���� : ������ �ٹٲ��� 8�� �̻� ������ 7���� ����.
		while (article.getContent().contains("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n")) {
			article.setContent(article.getContent().replace("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n", "\r\n\r\n\r\n\r\n\r\n\r\n\r\n"));
		}

		sqlSession.update(nameSpace + ".editArticle", article);
	}

	@Override
	public int saveReply(Reply reply) throws Exception {

		// �ִ� ���ӵ� �ٹٲ� ���� ���� : ������ �ٹٲ��� 5�� �̻� ������ 4���� ����.
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

			reply.setContent(reply.getContent().replace("<", "&lt;")); // html �±� ����
			reply.setContent(reply.getContent().replace(">", "&gt;")); // html �±� ����
			reply.setContent(reply.getContent().replace("\r\n", "<br/>")); // �ٹٲ��� ����ֱ� ����
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
	// ������ ���� ��� �ش� ����� �����ϰ� �Ʒ� ��۵��� groupOrder�� 1�� ���ҽ�Ű��
	// �θ� ����� hidden �����϶� ��������� �θ� ���� �ڱ� �ڽ� �޼ҵ带 ȣ����.
	public void deleteReply(Reply reply) throws Exception {

		sqlSession.delete(nameSpace + ".deleteReply", reply);

		// ������ ��� �Ʒ��� ��۵��� groupOrder 1�� ����
		sqlSession.update(nameSpace + ".adjustAfterDelete", reply);

		// �θ� ����� hidden �����϶� ��������� �θ� ���� deleteReply �޼ҵ带 ȣ���ϱ� ���� ����.
		Reply parentReply = sqlSession.selectOne(nameSpace + ".getParentReply", reply);

		// �θ� ������ �ƹ��͵� ����.
		if (parentReply == null)
			;

		// �θ� ����� hidden �������� �׽�Ʈ
		else if (parentReply.getIsDeleted() == 1) {

			// �θ��� �����ڼ� ���� ���ϱ� ���� �ٿ���� ���� ���κ��� ���� : �θ� ��۰� depth�� ���ų� �۰� �θ� ��� �Ʒ��� �ִ� �����
			// �ٿ������.
			Reply boundaryReply = sqlSession.selectOne(nameSpace + ".getNextSameDepthReply", parentReply);

			// �����ڼ� ���� ���� ����
			int directChildCount;

			// 1. �ٿ������ ������
			if (boundaryReply == null) {
				directChildCount = sqlSession.selectOne(nameSpace + ".getDirectChildCountNoBound", parentReply);

			}

			// 2. �ٿ������ ������
			else {
				directChildCount = sqlSession.selectOne(nameSpace + ".getDirectChildCountWithBound", parentReply);

			}

			// �θ��� �����ڼ��� 0��(���� ������°�)�϶��� �θ� ���� �� ����.
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

		// �� �۵��� ���� html �±� ����
		for (int i = 0; i < articleList.size(); i++) {

			Article article = articleList.get(i);
			article.setTitle(article.getTitle().replace("<", "&lt;")); // html �±� ����
			article.setTitle(article.getTitle().replace(">", "&gt;")); // html �±� ����
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
