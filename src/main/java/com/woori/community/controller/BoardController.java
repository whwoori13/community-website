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
	 * 텍스트 작성시 줄바꿈, 태그 프로세싱
	 * 
	 * 1. dao에서 저장할때 지나치게 연속한 \n\r 트리밍
	 * 
	 * 2. dao에서 불러올때 꺽쇄 escape 먼저 하고 \n\r을 <br/>로 치환.
	 * 
	 * 3. 컨트롤러에서 글 수정시 글 수정 화면으로 글 오브젝트를 넘겨주기 전에 <br/>을 \n\r로 치환후 escape를 원상복귀.
	 * 
	 */

	@Autowired
	private BoardService service;

	// list 화면에서 페이지당 글 수, list(), searchList()에서 쓰임.
	int articleCountPerPage = 25;

	// 글 읽기 화면 내 댓글 영역 페이지당 댓글 수, readArticle(), saveReReply()에서 쓰임.
	int replyCountPerPage = 20;

	// 대댓글 최대 깊이 제한, saveRereply()에서 쓰임.
	// jsp에 대댓글 indent 제한이
	// margin-left: ${(1 + reply.depth * 3 <= 49)?(1 + reply.depth * 3) : 49}%;
	// 와 같이 되어있으므로 maxDepth는 16 이하의 값으로 설정.
	// 깊이가 maxDepth인 댓글a에 대댓글을 달면 a의 부모에게 대댓글을 단것과 같은 결과가 나옴.
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
		model.addAttribute("currentTopic", topic); // 뷰에서 현재 토픽을 유지하기 위함.
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null && topic.equals("myArticles"))
			return "invalid-page";

		boolean isTopicValid = false;

		if (topic.equals("myArticles")) {
			isTopicValid = true;
			model.addAttribute("topicName", "myArticles");
		} else {
			// 리스트를 불러오기 전에 topic 파라미터가 db에 있는 값인지 검사
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

			model.addAttribute("keyword", null); // list-articles.jsp에서 검색한 상태가 아님을 판별하기 위함.

			// 내가 쓴 글 댓글 부분////////////////////////////////////////////////////
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
			// 내가 쓴 글 댓글 부분/////////////////////////////////////////////////////

			return "list-articles";
		} else
			return "invalid-page";
	}

	// /board/searchList?searchType=title&keyword=테스트
	// @RP searchTye : title, content, title_content, customer_Id
	@GetMapping("/searchList")
	public String saerchList(Model model, HttpServletRequest request, @RequestParam("topic") String topic,
			@RequestParam("searchType") String searchType, @RequestParam("keyword") String keyword,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage) throws Exception {
		service.loadNav(request);
		model.addAttribute("currentTopic", topic); // 뷰에서 현재 토픽을 유지하기 위함.

		// 리스트를 불러오기 전에 topic 파라미터가 db에 있는 값인지 검사
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

			// 페이징 처리를 위해 검색 조건에 맞는 게시글들의 총 수를 먼저 구해온다.
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
			HttpServletRequest request) throws Exception { // currentPage는

		service.loadNav(request);
		model.addAttribute("currentTopic", topic); // 현재 토픽을 알기 위함
		model.addAttribute("yPos", yPos); // 이전 스크롤 위치 유지

		boolean isTopicValid = false;

		// 글을 불러오기 전에 topic 파라미터가 db에 있는 값인지 검사
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

			// ------------ 아래 : 댓글 페이징 구현 ------------//
			int totalReplyCount = service.getTotalReplyCount(articleNum);

			// replyCountPerPage 는 페이지당 댓글 수를 나타내는 전역변수.
			int countPerPage = replyCountPerPage;

			// CurrentPage가 null일경우 마지막 페이지를 계산하여 default value로 가짐.
			int defaultCurr = (int) java.lang.Math.ceil((double) totalReplyCount / (double) countPerPage);
			if (defaultCurr == 0)
				defaultCurr = 1;

			if (totalReplyCount != 0) { // 댓글이 0개일때는 댓글박스, 페이지바를 표시하지 않음.

				// CurrentPage가 null일경우 위에서 계산한 마지막 페이지를 default value로 가짐.
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
			// ------------ 위 : 댓글 페이징 구현 ------------//

			Article article = service.loadOneArticle(articleNum);
			if (article == null)
				return "deleted-article";

			model.addAttribute("article", article);

			// 새로 달릴 댓글의 dummy 오브젝트를 model에 등록.
			model.addAttribute("newReply", new Reply());

			// ------------ 아래 : 글 읽기 화면 내부 미니 리스트 구현 ------------//

			int articleOrder;
			int totalArticleCount;

			// 검색한 상태가 아닐때
			if (keyword == null) {
				articleOrder = service.getArticleOrder(articleNum);
				totalArticleCount = service.getTotalArticleCount(request, topic);
			}
			// 검색한 상태일때
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
			// 미니 리스트에 보여줄 게시글 수
			int limit = 10;

			if (articleOrder + 5 > totalArticleCount && !(offset < 0))
				offset = totalArticleCount - 10;
			if (offset < 0 && !(articleOrder + 5 > totalArticleCount))
				offset = 0;
			if (offset < 0 && articleOrder + 5 > totalArticleCount)
				offset = 0;

			// offset, limit을 전달하기 위한 Map.
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
			// ------------ 위 : 글 읽기 화면 내부 미니 리스트 구현 ------------//

			return "read-article";

		}

		else
			return "invalid-page";

	}

	// 글쓰기 버튼 눌렀을때 작성 form으로 인도하는 메소드
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

		// 읽기 좋게 변환된 글을 다시 작성할때 시점에서의 형태로 되돌림.
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

	@GetMapping("/saveReply") // root 댓글 저장
	public String saveReply(@RequestParam("yPos") int yPos, @RequestParam("topic") String topic, @ModelAttribute("newReply") Reply reply,
			RedirectAttributes ra, HttpServletRequest request, Model model) throws Exception {

		// 없는 게시글일때의 처리
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

	@GetMapping("/saveReReply") // 대댓글 저장
	public String saveReReply(@RequestParam("yPos") int yPos, @RequestParam("topic") String topic, @RequestParam("currentPage") int currentPage,
			@RequestParam("articleNum") int articleNum, @RequestParam("groupNum") int groupNum, @RequestParam("depth") int depth,
			@RequestParam("content") String content, @RequestParam("parentGrpOrd") int parentGrpOrd, RedirectAttributes ra,
			HttpServletRequest request, Model model) throws Exception {

		// 없는 게시글일때의 처리
		model.addAttribute("currentTopic", topic);
		Article article = sqlSession.selectOne(nameSpace + ".loadOneArticle", articleNum);
		if (article == null)
			return "deleted-article";

		// 대댓글 최대 깊이 제한, 전역변수.
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

			// 새로운 대댓글이 들어갈 자리의 groupOrder를 구한다.
			Map<String, Object> map = new HashMap<String, Object>(); // Data Transfer Map
			map.put("depth", depth);
			map.put("parentGrpOrd", parentGrpOrd);
			map.put("groupNum", groupNum);
			Integer groupOrder = service.getGroupOrder(map); // 새로운 대댓글이 들어갈 자리

			// 순서 계산을 위한 아래 바운더리가 없는 경우 -> 해당 그룹의 총 갯수 + 1이 이 댓글의 groupOrder가 된다.
			if (groupOrder == null) {
				groupOrder = service.getGroupCount(groupNum) + 1;
			}

			reply.setGroupOrder(groupOrder);

			// 새로운 대댓글이 들어갈 자리를 비워주기 위해 위에서 구한 groupOrder부터 1씩 더해서 뒤로 1칸씩 밀어줌.
			Map<String, Object> map2 = new HashMap<String, Object>(); // Data Transfer Map
			map2.put("groupOrder", groupOrder);
			map2.put("groupNum", groupNum);
			service.adjustOrder(map2);

			service.saveReReply(reply, parentGrpOrd);

			// 해당 게시글에서 해당 댓글의 순서를 구함.
			int replyPos = service.getReplyPos(reply);

			// replyCountPerPage 는 페이지당 댓글 수를 나타내는 전역변수.
			int countPerPage = replyCountPerPage;

			// 위의 두 값을 이용해 새로운 현재 댓글 페이지를 계산
			int newCurrentPage = (int) java.lang.Math.ceil((double) replyPos / (double) countPerPage);

			// 페이지 변동이 있을시 read-article 뷰에게 그것을 알려주는 -1 플래그 값 할당.
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

		// 삭제 확인창 엔터 연타시 트랜잭션이 끝나기 전에 리퀘스트 여러개가 전송되는것에 대한 처리
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
			service.deleteReply(reply); // 서비스에서 대댓글 여부에 따라 나눠서 처리.

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

		// 이미 삭제된 댓글일때
		if (reply == null || reply.getIsDeleted() == 1) {
			service.removeAlert(replyNum);
			service.decreaseNewAlert(customer.getId());
			return "deleted-reply";
		}

		Article article = service.loadOneArticle(reply.getArticleNum());

		int replyPos = service.getReplyPos(reply);

		// 글읽기 화면 내에서 댓글이 위치한 페이지
		int currentPage = (int) java.lang.Math.ceil((double) replyPos / (double) replyCountPerPage);

		ra.addAttribute("articleNum", reply.getArticleNum());
		ra.addAttribute("currentPage", currentPage);
		ra.addAttribute("topic", article.getTopic());

		// 확인한 댓글은 alert 테이블에서 삭제.
		if (fromMyRep == 0) {
			service.removeAlert(replyNum);
			service.decreaseNewAlert(customer.getId());
		}

		return "redirect:/board/readArticle" + "#root_" + replyNum;

	}

}
