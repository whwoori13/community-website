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
		// 읽을때마다 조회수 +1
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
	public void saveReply(Reply reply) throws Exception { // depth = 0인 root 댓글 저장
		dao.increaseReplyCount(reply.getArticleNum()); // 해당 글의 댓글수 +1

		// root 댓글이므로 비어있는 칼럼을 다음과 같이 채워줌.
		reply.setGroupOrder(1);
		reply.setDepth(0);
		reply.setGroupNum(reply.getArticleNum());

		int replyNum = dao.saveReply(reply);
		dao.udtGrpNum(); // 방금 저장된 root 댓글의 groupNum을 num과 일치시켜준다.

		// 알림 처리
		Article article = dao.loadOneArticle(reply.getArticleNum());
		String customerId = article.getCustomerId();
		AlertDTO alert = new AlertDTO(replyNum, customerId, reply.getContent(), reply.getCustomerId());
		dao.putAlert(alert);
		dao.increaseNewAlert(customerId);
		
	}

	@Override
	@Transactional
	public void saveReReply(Reply reply, int parentGrpOrd) throws Exception {
		dao.increaseReplyCount(reply.getArticleNum()); // 해당 글의 댓글수 +1
		
		int replyNum = dao.saveReply(reply);

		// 알림 처리
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

		// 해당 글의 댓글수 -1
		dao.decreaseReplyCount(reply.getArticleNum());

		// 지우려는 대상 댓글에 달린 대댓글이 있는지 여부를 판단하기 위해 아래 댓글의 depth를 구함.
		Integer nextDepth = dao.getNextDepth(reply);

		if (nextDepth == null)
			nextDepth = reply.getDepth(); // null시에 else로 보내기 위함.

		// 바로 다음 댓글의 깊이가 현재 깊이 + 1과 같다면 대댓글이 있는것.
		if (nextDepth == reply.getDepth() + 1) {
			// 대댓글이 있으면 가리기만 함.
			dao.hideReply(reply.getNum());
		} else {
			// 대댓글이 없다면 삭제.
			// dao에서 삭제된 댓글 아래 댓글들 orderNum 값 고쳐주고 부모 댓글이 hidden 상태일 경우 재귀적으로 삭제 해준다.
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
	// db로부터 nav 바의 메뉴 항목들을 불러오는 메소드, 매번 불러오면 리소스 낭비이므로
	// nav 바를 사용하는 요청일때 sesison에 등록된 메뉴 정보 List가 null일때 or 홈으로 갈때만 새로 받아와서 session에
	// 등록.
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
