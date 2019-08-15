package com.woori.community.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woori.community.DAO.BoardDAO;
import com.woori.community.DTO.AlertDTO;
import com.woori.community.entity.Article;
import com.woori.community.entity.Reply;
import com.woori.community.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDAO dao;

	@Override
	public List<Article> loadArticles() throws Exception {
		return dao.loadArticles();
	}

	@Override
	public Article loadOneArticle(int num) throws Exception {
		// ���������� ��ȸ�� +1
		dao.increaseViewCount(num);
		return dao.loadOneArticle(num);
	}

	@Override
	public int saveArticle(Article article) throws Exception {
		dao.saveArticle(article);
		return dao.getRecentArticleNum();

	}

	@Override
	public void deleteArticle(int articleNum) throws Exception {
		dao.deleteArticle(articleNum);

	}

	@Override
	public void editArticle(Article article) throws Exception {
		dao.editArticle(article);

	}

	@Override
	@Transactional
	public void saveReply(Reply reply) throws Exception { // depth = 0�� root ��� ����
		dao.increaseReplyCount(reply.getArticleNum()); // �ش� ���� ��ۼ� +1

		// root ����̹Ƿ� ����ִ� Į���� ������ ���� ä����.
		reply.setGroupOrder(1);
		reply.setDepth(0);
		reply.setGroupNum(reply.getArticleNum());

		int replyNum = dao.saveReply(reply);
		dao.udtGrpNum(); // ��� ����� root ����� groupNum�� num�� ��ġ�����ش�.

		// �˸� ó��
		Article article = dao.loadOneArticle(reply.getArticleNum());
		String customerId = article.getCustomerId();
		AlertDTO alert = new AlertDTO(replyNum, customerId, reply.getContent(), reply.getCustomerId());
		dao.putAlert(alert);
		dao.increaseNewAlert(customerId);
		
	}

	@Override
	@Transactional
	public void saveReReply(Reply reply, int parentGrpOrd) throws Exception {
		dao.increaseReplyCount(reply.getArticleNum()); // �ش� ���� ��ۼ� +1
		
		int replyNum = dao.saveReply(reply);

		// �˸� ó��
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupOrder", parentGrpOrd);
		map.put("groupNum", reply.getGroupNum());
		String parentId = dao.loadOneReplyByGrp(map);
		
		AlertDTO alert = new AlertDTO(replyNum, parentId, reply.getContent(), reply.getCustomerId());
		dao.putAlert(alert);
		dao.increaseNewAlert(parentId);
		
	}

	@Override
	public List<Reply> loadReplies(Map<String, Object> map) throws Exception {
		return dao.loadReplies(map);
	}

	@Override
	@Transactional
	public void deleteReply(Reply reply) throws Exception {

		// �ش� ���� ��ۼ� -1
		dao.decreaseReplyCount(reply.getArticleNum());

		// ������� ��� ��ۿ� �޸� ������ �ִ��� ���θ� �Ǵ��ϱ� ���� �Ʒ� ����� depth�� ����.
		Integer nextDepth = dao.getNextDepth(reply);

		if (nextDepth == null)
			nextDepth = reply.getDepth(); // null�ÿ� else�� ������ ����.

		// �ٷ� ���� ����� ���̰� ���� ���� + 1�� ���ٸ� ������ �ִ°�.
		if (nextDepth == reply.getDepth() + 1) {
			// ������ ������ �����⸸ ��.
			dao.hideReply(reply.getNum());
		} else {
			// ������ ���ٸ� ����.
			// dao���� ������ ��� �Ʒ� ��۵� orderNum �� �����ְ� �θ� ����� hidden ������ ��� ��������� ���� ���ش�.
			dao.deleteReply(reply);
		}
	}

	@Override
	public List<Article> loadArticlesPaged(Map map) throws Exception {
		return dao.loadArticlesPaged(map);
	}

	@Override
	public int getTotalCount(String tableName) throws Exception {
		return dao.getTotalCount(tableName);
	}

	@Override
	public int getTotalReplyCount(int num) throws Exception {
		return dao.getTotalReplyCount(num);
	}

	@Override
	public int getSearchedCount(Map<String, Object> map) throws Exception {
		return dao.getSearchedCount(map);
	}

	@Override
	public List<Article> loadSearchedArticles(Map<String, Object> map) throws Exception {
		return dao.loadSearchedArticles(map);
	}

	@Override
	public Integer getGroupOrder(Map<String, Object> map) throws Exception {
		return dao.getGroupOrder(map);
	}

	@Override
	public void adjustOrder(Map<String, Object> map) throws Exception {
		dao.adjustOrder(map);

	}

	@Override
	public Integer getGroupCount(int groupNum) throws Exception {
		return dao.getGroupCount(groupNum);
	}

	@Override
	public int getReplyPos(Reply reply) throws Exception {
		return dao.getReplyPos(reply);
	}

	@Override
	public int getArticleOrder(int articleNum) throws Exception {
		return dao.getArticleOrder(articleNum);
	}

	@Override
	// db�κ��� nav ���� �޴� �׸���� �ҷ����� �޼ҵ�, �Ź� �ҷ����� ���ҽ� �����̹Ƿ�
	// nav �ٸ� ����ϴ� ��û�϶� sesison�� ��ϵ� �޴� ���� List�� null�϶� or Ȩ���� ������ ���� �޾ƿͼ� session��
	// ���.
	public void loadNav(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();

		// if (session.getAttribute("categoryList") == null) {}
		dao.getCategory(request);
		dao.getCTmatch(request);
		dao.getTopic(request);

	}

	@Override
	public int getTotalArticleCount(HttpServletRequest request, String topic) throws Exception {
		return dao.getTotalArticleCount(request, topic);
	}

	@Override
	public int getArticleOrderSearched(Map<String, Object> map) throws Exception {
		return dao.getArticleOrderSearched(map);
	}

	@Override
	public int getTotalArticleCountSearched(Map<String, Object> map) throws Exception {
		return dao.getTotalArticleCountSearched(map);
	}

	@Override
	public List<Article> loadArticlesPagedSearched(Map<String, Object> map2) throws Exception {
		return dao.loadArticlesPagedSearched(map2);
	}

	@Override
	public Reply loadOneReply(int replyNum) {
		return dao.loadOneReply(replyNum);
	}

	@Override
	public void removeAlert(int replyNum) {
		dao.removeAlert(replyNum);
		
	}

	@Override
	public void decreaseNewAlert(String id) {
		dao.decreaseNewAlert(id);
		
	}

	@Override
	public int getMyReplyCount(String id) {
		return dao.getMyReplyCount(id);
	}

	@Override
	public List<Reply> loadMyReplies(Map<String, Object> map) {
		return dao.loadMyReplies(map);
	}

}
