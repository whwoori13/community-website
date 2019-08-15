package com.woori.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.woori.community.DTO.CategoryDTO;
import com.woori.community.DTO.TopicDTO;
import com.woori.community.entity.Article;
import com.woori.community.entity.Customer;
import com.woori.community.entity.Reply;
import com.woori.community.service.BoardService;

import logic.Paging;

@Controller
@RequestMapping("board")
public class BoardController {

	/*
	 * �ؽ�Ʈ �ۼ��� �ٹٲ�, �±� ���μ���
	 * 
	 * 1. dao���� �����Ҷ� ����ġ�� ������ \n\r Ʈ����
	 * 
	 * 2. dao���� �ҷ��ö� ���� escape ���� �ϰ� \n\r�� <br/>�� ġȯ.
	 * 
	 * 3. ��Ʈ�ѷ����� �� ������ �� ���� ȭ������ �� ������Ʈ�� �Ѱ��ֱ� ���� <br/>�� \n\r�� ġȯ�� escape�� ���󺹱�.
	 * 
	 */

	@Autowired
	private BoardService service;

	// list ȭ�鿡�� �������� �� ��, list(), searchList()���� ����.
	int articleCountPerPage = 25;

	// �� �б� ȭ�� �� ��� ���� �������� ��� ��, readArticle(), saveReReply()���� ����.
	int replyCountPerPage = 20;

	// ���� �ִ� ���� ����, saveRereply()���� ����.
	// jsp�� ���� indent ������
	// margin-left: ${(1 + reply.depth * 3 <= 49)?(1 + reply.depth * 3) : 49}%;
	// �� ���� �Ǿ������Ƿ� maxDepth�� 16 ������ ������ ����.
	// ���̰� maxDepth�� ���a�� ������ �޸� a�� �θ𿡰� ������ �ܰͰ� ���� ����� ����.
	int maxDepth = 16;

	// for testing ==========================================================
	@Autowired
	private SqlSession sqlSession;
	private static final String nameSpace = "com.woori.community.BoardDAO";

	@GetMapping("/test3")
	public String test1() throws Exception {

		List<CategoryDTO> category = sqlSession.selectList(nameSpace + ".getCategory");

		return "/";
	}
	// for testing ==========================================================

	@GetMapping("/list")
	public String list(Model model, HttpServletRequest request, @RequestParam("topic") String topic,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(value = "currentPage2", required = false, defaultValue = "1") Integer currentPage2) throws Exception {
		service.loadNav(request);
		model.addAttribute("currentTopic", topic); // �信�� ���� ������ �����ϱ� ����.
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null && topic.equals("myArticles"))
			return "invalid-page";

		boolean isTopicValid = false;

		if (topic.equals("myArticles")) {
			isTopicValid = true;
			model.addAttribute("topicName", "myArticles");
		} else {
			// ����Ʈ�� �ҷ����� ���� topic �Ķ���Ͱ� db�� �ִ� ������ �˻�
			List<TopicDTO> topicList = (List<TopicDTO>) request.getSession().getAttribute("topicList");
			if (topicList != null) {

				for (int i = 0; i < topicList.size(); i++) {
					if (topicList.get(i).getCode().equals(topic)) {
						isTopicValid = true;
						model.addAttribute("topicName", topicList.get(i).getName());
					}
				}
			}

		}

		if (isTopicValid) {

			// int totalCount = service.getTotalCount("article");
			int totalCount = service.getTotalArticleCount(request, topic);

			//////////////////////////////////////////////////////////////////////////
			Map<String, Object> map = Paging.paging(currentPage, totalCount, articleCountPerPage);
			map.put("topic", topic);
			if (customer != null)
				map.put("myId", customer.getId());
			Integer startPage = (Integer) map.get("startPage");
			Integer lastPage = (Integer) map.get("lastPage");
			Integer newCurrentPage = (Integer) map.get("currentPage");
			Integer endPage = (Integer) map.get("endPage");
			model.addAttribute("startPage", startPage);
			model.addAttribute("lastPage", lastPage);
			model.addAttribute("currentPage", newCurrentPage);
			model.addAttribute("endPage", endPage);

			List<Article> articleList = service.loadArticlesPaged(map);
			model.addAttribute("articleList", articleList);
			//////////////////////////////////////////////////////////////////////////

			model.addAttribute("keyword", null); // list-articles.jsp���� �˻��� ���°� �ƴ��� �Ǻ��ϱ� ����.

			// ���� �� �� ��� �κ�////////////////////////////////////////////////////
			if (topic.equals("myArticles")) {

				int totalCount2 = service.getMyReplyCount(customer.getId());

				Map<String, Object> map2 = Paging.paging(currentPage2, totalCount2, articleCountPerPage);
				map2.put("myId", customer.getId());
				Integer startPage2 = (Integer) map2.get("startPage");
				Integer lastPage2 = (Integer) map2.get("lastPage");
				Integer newCurrentPage2 = (Integer) map2.get("currentPage");
				Integer endPage2 = (Integer) map2.get("endPage");
				model.addAttribute("startPage2", startPage2);
				model.addAttribute("lastPage2", lastPage2);
				model.addAttribute("currentPage2", newCurrentPage2);
				model.addAttribute("endPage2", endPage2);

				List<Reply> replyListUnProcessed = service.loadMyReplies(map2);
				List<Reply> replyList = new ArrayList<Reply>();
				for (Reply element : replyListUnProcessed) {
					element.setContent(StringUtils.abbreviate(element.getContent(), 45));
					replyList.add(element);
				}
				model.addAttribute("replyList", replyList);
				//////////////////////////////////////////////////////////////////////////

			}
			// ���� �� �� ��� �κ�/////////////////////////////////////////////////////

			return "list-articles";
		} else
			return "invalid-page";
	}

	// /board/searchList?searchType=title&keyword=�׽�Ʈ
	// @RP searchTye : title, content, title_content, customer_Id
	@GetMapping("/searchList")
	public String saerchList(Model model, HttpServletRequest request, @RequestParam("topic") String topic,
			@RequestParam("searchType") String searchType, @RequestParam("keyword") String keyword,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage) throws Exception {
		service.loadNav(request);
		model.addAttribute("currentTopic", topic); // �信�� ���� ������ �����ϱ� ����.

		// ����Ʈ�� �ҷ����� ���� topic �Ķ���Ͱ� db�� �ִ� ������ �˻�
		boolean isTopicValid = false;

		List<TopicDTO> topicList = (List<TopicDTO>) request.getSession().getAttribute("topicList");

		if (topicList != null) {

			for (int i = 0; i < topicList.size(); i++) {
				if (topicList.get(i).getCode().equals(topic)) {
					isTopicValid = true;
					model.addAttribute("topicName", topicList.get(i).getName());
				}
			}
		}

		if (isTopicValid) {

			request.setAttribute("currentTopic", topic);
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("topic", topic);
			map1.put("keyword", keyword);
			map1.put("searchType", searchType);
			map1.put("topic", topic);

			// ����¡ ó���� ���� �˻� ���ǿ� �´� �Խñ۵��� �� ���� ���� ���ؿ´�.
			int searchedCount = service.getSearchedCount(map1);

			//////////////////////////////////////////////////////////////////////////
			Map<String, Object> map = Paging.paging(currentPage, searchedCount, articleCountPerPage);
			Integer startPage = (Integer) map.get("startPage");
			Integer lastPage = (Integer) map.get("lastPage");
			Integer newCurrentPage = (Integer) map.get("currentPage");
			Integer endPage = (Integer) map.get("endPage");
			model.addAttribute("startPage", startPage);
			model.addAttribute("lastPage", lastPage);
			model.addAttribute("currentPage", newCurrentPage);
			model.addAttribute("endPage", endPage);

			model.addAttribute("searchType", searchType);
			model.addAttribute("keyword", keyword);

			Integer offset = (Integer) map.get("offset");
			Integer limit = (Integer) map.get("limit");
			map1.put("offset", offset);
			map1.put("limit", limit);

			List<Article> articleList = service.loadSearchedArticles(map1);

			model.addAttribute("articleList", articleList);
			//////////////////////////////////////////////////////////////////////////

			return "list-articles";

		}

		else
			return "invalid-page";

	}

	@GetMapping("/readArticle")
	public String readArticle(@RequestParam(value = "yPos", required = false) String yPos,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "searchType", required = false) String searchType, Model model, @RequestParam("articleNum") int articleNum,
			@RequestParam("topic") String topic, @RequestParam(value = "currentPage", required = false) Integer currentPage,
			HttpServletRequest request) throws Exception { // currentPage��

		service.loadNav(request);
		model.addAttribute("currentTopic", topic); // ���� ������ �˱� ����
		model.addAttribute("yPos", yPos); // ���� ��ũ�� ��ġ ����

		boolean isTopicValid = false;

		// ���� �ҷ����� ���� topic �Ķ���Ͱ� db�� �ִ� ������ �˻�
		List<TopicDTO> topicList = (List<TopicDTO>) request.getSession().getAttribute("topicList");
		if (topicList != null) {
			for (int i = 0; i < topicList.size(); i++) {
				if (topicList.get(i).getCode().equals(topic)) {
					isTopicValid = true;
					model.addAttribute("topicName", topicList.get(i).getName());
				}
			}
		}

		if (isTopicValid) {

			// ------------ �Ʒ� : ��� ����¡ ���� ------------//
			int totalReplyCount = service.getTotalReplyCount(articleNum);

			// replyCountPerPage �� �������� ��� ���� ��Ÿ���� ��������.
			int countPerPage = replyCountPerPage;

			// CurrentPage�� null�ϰ�� ������ �������� ����Ͽ� default value�� ����.
			int defaultCurr = (int) java.lang.Math.ceil((double) totalReplyCount / (double) countPerPage);
			if (defaultCurr == 0)
				defaultCurr = 1;

			if (totalReplyCount != 0) { // ����� 0���϶��� ��۹ڽ�, �������ٸ� ǥ������ ����.

				// CurrentPage�� null�ϰ�� ������ ����� ������ �������� default value�� ����.
				if (currentPage == null)
					currentPage = defaultCurr;

				Map<String, Object> map = Paging.paging(currentPage, totalReplyCount, countPerPage);
				map.put("articleNum", articleNum);
				List<Reply> replyList = service.loadReplies(map);

				Integer startPage = (Integer) map.get("startPage");
				Integer lastPage = (Integer) map.get("lastPage");
				Integer newCurrentPage = (Integer) map.get("currentPage");
				Integer endPage = (Integer) map.get("endPage");
				model.addAttribute("startPage", startPage);
				model.addAttribute("lastPage", lastPage);
				model.addAttribute("currentPage", newCurrentPage);
				model.addAttribute("endPage", endPage);
				model.addAttribute("replyList", replyList);

			}
			// ------------ �� : ��� ����¡ ���� ------------//

			Article article = service.loadOneArticle(articleNum);
			if (article == null)
				return "deleted-article";

			model.addAttribute("article", article);

			// ���� �޸� ����� dummy ������Ʈ�� model�� ���.
			model.addAttribute("newReply", new Reply());

			// ------------ �Ʒ� : �� �б� ȭ�� ���� �̴� ����Ʈ ���� ------------//

			int articleOrder;
			int totalArticleCount;

			// �˻��� ���°� �ƴҶ�
			if (keyword == null) {
				articleOrder = service.getArticleOrder(articleNum);
				totalArticleCount = service.getTotalArticleCount(request, topic);
			}
			// �˻��� �����϶�
			else {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("articleNum", articleNum);
				map.put("topic", topic);
				map.put("keyword", keyword);
				map.put("searchType", searchType);
				articleOrder = service.getArticleOrderSearched(map);
				totalArticleCount = service.getTotalArticleCountSearched(map);

				model.addAttribute("searchType", searchType);
				model.addAttribute("keyword", keyword);

			}

			int offset = articleOrder - 5;
			// �̴� ����Ʈ�� ������ �Խñ� ��
			int limit = 10;

			if (articleOrder + 5 > totalArticleCount && !(offset < 0))
				offset = totalArticleCount - 10;
			if (offset < 0 && !(articleOrder + 5 > totalArticleCount))
				offset = 0;
			if (offset < 0 && articleOrder + 5 > totalArticleCount)
				offset = 0;

			// offset, limit�� �����ϱ� ���� Map.
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("offset", offset);
			map2.put("limit", limit);
			map2.put("topic", topic);
			map2.put("searchType", searchType);
			map2.put("keyword", keyword);

			List<Article> list;
			if (keyword == null)
				list = service.loadArticlesPaged(map2);
			else
				list = service.loadArticlesPagedSearched(map2);

			model.addAttribute("articleList", list);
			// ------------ �� : �� �б� ȭ�� ���� �̴� ����Ʈ ���� ------------//

			return "read-article";

		}

		else
			return "invalid-page";

	}

	// �۾��� ��ư �������� �ۼ� form���� �ε��ϴ� �޼ҵ�
	@GetMapping("/writeArticle")
	public String writeArticle(HttpServletRequest request, Model model, @RequestParam("topic") String topic) throws Exception {
		service.loadNav(request);
		Article article = new Article();
		article.setTopic(topic);
		model.addAttribute("article", article);
		model.addAttribute("currentTopic", topic);
		if (request.getSession().getAttribute("customer") == null)
			return "redirect:/loginForm";
		else
			return "write-article";
	}

	@PostMapping("/saveArticle")
	public String saveArticle(@ModelAttribute("article") Article article, RedirectAttributes ra, @RequestParam("topic2") String topic)
			throws Exception {

		int articleNum = service.saveArticle(article);

		ra.addAttribute("articleNum", articleNum);
		ra.addAttribute("topic", topic);

		return "redirect:/board/readArticle";
	}

	@GetMapping("/deleteArticle")
	public String deleteArticle(@RequestParam("topic") String topic, @RequestParam("articleNum") int articleNum,
			@RequestParam("customerId") String customerId, HttpServletRequest request, RedirectAttributes ra) throws Exception {

		ra.addAttribute("topic", topic);

		Article article = service.loadOneArticle(articleNum);
		if (!(article.getCustomerId().equals(customerId)))
			return "delete-fail";

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		if (customer == null)
			return "delete-fail";

		else if (customerId.equals(customer.getId())) {

			service.deleteArticle(articleNum);

			return "redirect:/board/list";
		} else
			return "delete-fail";

	}

	@GetMapping("/editArticleForm")
	public String editArticleForm(Model model, @RequestParam("topic") String topic, @RequestParam("articleNum") int articleNum,
			HttpServletRequest request) throws Exception {
		service.loadNav(request);
		model.addAttribute("currentTopic", topic);
		Article article = service.loadOneArticle(articleNum);

		// �б� ���� ��ȯ�� ���� �ٽ� �ۼ��Ҷ� ���������� ���·� �ǵ���.
		article.setTitle(article.getTitle().replace("&lt;", "<"));
		article.setTitle(article.getTitle().replace("&gt;", ">"));

		article.setContent(article.getContent().replace("<br/>", "\r\n"));
		article.setContent(article.getContent().replace("&lt;", "<"));
		article.setContent(article.getContent().replace("&gt;", ">"));

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		if (customer == null)
			return "delete-fail";

		else if (article.getCustomerId().equals(customer.getId())) {

			model.addAttribute("article", article);
			return "edit-article";

		} else
			return "delete-fail";

	}

	@PostMapping("/editArticle")
	public String editArticle(@RequestParam("topic") String topic, @ModelAttribute("article") Article article, HttpServletRequest request,
			RedirectAttributes ra) throws Exception {

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		if (customer == null)
			return "delete-fail";

		else if (article.getCustomerId().equals(customer.getId())) {

			service.editArticle(article);
			ra.addAttribute("articleNum", article.getNum());
			ra.addAttribute("topic", topic);

			return "redirect:/board/readArticle";
		} else
			return "delete-fail";
	}

	@GetMapping("/saveReply") // root ��� ����
	public String saveReply(@RequestParam("yPos") int yPos, @RequestParam("topic") String topic, @ModelAttribute("newReply") Reply reply,
			RedirectAttributes ra, HttpServletRequest request, Model model) throws Exception {

		// ���� �Խñ��϶��� ó��
		model.addAttribute("currentTopic", topic);
		Article article = sqlSession.selectOne(nameSpace + ".loadOneArticle", reply.getArticleNum());
		if (article == null)
			return "deleted-article";

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		ra.addAttribute("articleNum", reply.getArticleNum());
		ra.addAttribute("topic", topic);
		ra.addAttribute("yPos", yPos);

		if (customer == null)
			return "redirect:/loginForm";
		else {
			service.saveReply(reply);
			return "redirect:/board/readArticle";
		}
	}

	@GetMapping("/saveReReply") // ���� ����
	public String saveReReply(@RequestParam("yPos") int yPos, @RequestParam("topic") String topic, @RequestParam("currentPage") int currentPage,
			@RequestParam("articleNum") int articleNum, @RequestParam("groupNum") int groupNum, @RequestParam("depth") int depth,
			@RequestParam("content") String content, @RequestParam("parentGrpOrd") int parentGrpOrd, RedirectAttributes ra,
			HttpServletRequest request, Model model) throws Exception {

		// ���� �Խñ��϶��� ó��
		model.addAttribute("currentTopic", topic);
		Article article = sqlSession.selectOne(nameSpace + ".loadOneArticle", articleNum);
		if (article == null)
			return "deleted-article";

		// ���� �ִ� ���� ����, ��������.
		if (depth > maxDepth)
			depth = maxDepth;

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		if (customer == null)
			return "redirect:/loginForm";

		else {

			Reply reply = new Reply();
			reply.setCustomerId(customer.getId());
			reply.setArticleNum(articleNum);

			reply.setGroupNum(groupNum);
			reply.setDepth(depth);
			reply.setContent(content);

			// ���ο� ������ �� �ڸ��� groupOrder�� ���Ѵ�.
			Map<String, Object> map = new HashMap<String, Object>(); // Data Transfer Map
			map.put("depth", depth);
			map.put("parentGrpOrd", parentGrpOrd);
			map.put("groupNum", groupNum);
			Integer groupOrder = service.getGroupOrder(map); // ���ο� ������ �� �ڸ�

			// ���� ����� ���� �Ʒ� �ٿ������ ���� ��� -> �ش� �׷��� �� ���� + 1�� �� ����� groupOrder�� �ȴ�.
			if (groupOrder == null) {
				groupOrder = service.getGroupCount(groupNum) + 1;
			}

			reply.setGroupOrder(groupOrder);

			// ���ο� ������ �� �ڸ��� ����ֱ� ���� ������ ���� groupOrder���� 1�� ���ؼ� �ڷ� 1ĭ�� �о���.
			Map<String, Object> map2 = new HashMap<String, Object>(); // Data Transfer Map
			map2.put("groupOrder", groupOrder);
			map2.put("groupNum", groupNum);
			service.adjustOrder(map2);

			service.saveReReply(reply, parentGrpOrd);

			// �ش� �Խñۿ��� �ش� ����� ������ ����.
			int replyPos = service.getReplyPos(reply);

			// replyCountPerPage �� �������� ��� ���� ��Ÿ���� ��������.
			int countPerPage = replyCountPerPage;

			// ���� �� ���� �̿��� ���ο� ���� ��� �������� ���
			int newCurrentPage = (int) java.lang.Math.ceil((double) replyPos / (double) countPerPage);

			// ������ ������ ������ read-article �信�� �װ��� �˷��ִ� -1 �÷��� �� �Ҵ�.
			if (currentPage != newCurrentPage)
				yPos = -1;

			ra.addAttribute("articleNum", articleNum);
			ra.addAttribute("currentPage", newCurrentPage);
			ra.addAttribute("topic", topic);
			ra.addAttribute("yPos", yPos);

			return "redirect:/board/readArticle";
		}
	}

	@GetMapping("/deleteReply")
	public String deleteReply(@RequestParam("yPos") int yPos, @RequestParam("topic") String topic, @RequestParam("articleNum") int articleNum,
			@RequestParam("currentPage") int currentPage, @RequestParam("replyNum") int replyNum, @RequestParam("customerId") String customerId,
			@RequestParam("depth") int depth, @RequestParam("groupOrder") int groupOrder, @RequestParam("groupNum") int groupNum,
			RedirectAttributes ra, HttpServletRequest request, Model model) throws Exception {

		// ���� Ȯ��â ���� ��Ÿ�� Ʈ������� ������ ���� ������Ʈ �������� ���۵Ǵ°Ϳ� ���� ó��
		model.addAttribute("referer", request.getHeader("Referer"));
		try {
			if (request.getSession().getAttribute("deletingReply").equals(replyNum))
				return "deleted-reply";
			else
				request.getSession().setAttribute("deletingReply", replyNum);
		} catch (NullPointerException e) {

			request.getSession().setAttribute("deletingReply", replyNum);
		}
		if (service.loadOneReply(replyNum) == null)
			return "deleted-reply";

		ra.addAttribute("articleNum", articleNum);
		ra.addAttribute("currentPage", currentPage);
		ra.addAttribute("topic", topic);
		ra.addAttribute("yPos", yPos);
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		Reply reply = new Reply();
		reply.setArticleNum(articleNum);
		reply.setNum(replyNum);
		reply.setDepth(depth);
		reply.setGroupOrder(groupOrder);
		reply.setGroupNum(groupNum);

		Reply reply2 = service.loadOneReply(replyNum);
		
		if (customer == null)
			return "delete-fail";

		else if (!(customer.getId().equals(reply2.getCustomerId())))
			return "delete-fail";

		else
			service.deleteReply(reply); // ���񽺿��� ���� ���ο� ���� ������ ó��.

		return "redirect:/board/readArticle";
	}

	@GetMapping("/locate")
	@Transactional
	public String locate(@RequestParam("replyNum") int replyNum,
			@RequestParam(value = "fromMyRep", required = false, defaultValue = "0") int fromMyRep, RedirectAttributes ra, HttpServletRequest request)
			throws Exception {

		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null)
			return "auth-failed";

		request.getSession().setAttribute("referer", request.getHeader("Referer"));

		Reply reply = service.loadOneReply(replyNum);

		// �̹� ������ ����϶�
		if (reply == null || reply.getIsDeleted() == 1) {
			service.removeAlert(replyNum);
			service.decreaseNewAlert(customer.getId());
			return "deleted-reply";
		}

		Article article = service.loadOneArticle(reply.getArticleNum());

		int replyPos = service.getReplyPos(reply);

		// ���б� ȭ�� ������ ����� ��ġ�� ������
		int currentPage = (int) java.lang.Math.ceil((double) replyPos / (double) replyCountPerPage);

		ra.addAttribute("articleNum", reply.getArticleNum());
		ra.addAttribute("currentPage", currentPage);
		ra.addAttribute("topic", article.getTopic());

		// Ȯ���� ����� alert ���̺��� ����.
		if (fromMyRep == 0) {
			service.removeAlert(replyNum);
			service.decreaseNewAlert(customer.getId());
		}

		return "redirect:/board/readArticle" + "#root_" + replyNum;

	}

}
