package com.woori.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.woori.community.DTO.AlertDTO;
import com.woori.community.DTO.RankDTO;
import com.woori.community.entity.Article;
import com.woori.community.entity.Customer;
import com.woori.community.entity.Reply;
import com.woori.community.service.BoardService;
import com.woori.community.service.HomeService;

@Controller
@RequestMapping
public class HomeController {

	@Autowired
	private HomeService homeService;

	// nav 메뉴를 가져오기 위한 dao가 boardService에 있어서.
	@Autowired
	private BoardService boardService;

	@GetMapping("/intro")
	public String home() throws Exception {
		return "intro";
	}

	@GetMapping("/")
	public String home(HttpServletRequest request, Model model) throws Exception {
		// topicList, categoryList, ctmatchList를 불러와서 세션에 등록.
		boardService.loadNav(request);

		List<RankDTO> topTopicList = homeService.loadRating();
		int maxCount;
		if (topTopicList.size() < 10)
			maxCount = topTopicList.size();
		else
			maxCount = 10;
		List<RankDTO> top10topicList = new ArrayList<RankDTO>(topTopicList.subList(0, maxCount));
		List<Article> homeArticleList = new ArrayList<Article>();

		for (RankDTO element : top10topicList) {

			Map<String, Object> dtm = new HashMap<String, Object>();
			dtm.put("topic", element.getTopic());
			dtm.put("limit", 10);
			dtm.put("offset", 0);
			List<Article> tempArticleList = boardService.loadArticlesPaged(dtm);

			for (Article element2 : tempArticleList) {
				element2.setTitle(StringUtils.abbreviate(element2.getTitle(), 30));
				homeArticleList.add(element2);
			}
		}

		// 홈에 보여질 게시판들의 topic이 들어있는 리스트
		model.addAttribute("topTopicList", top10topicList);
		// 홈에 보여질 모든 게시글들이 들어있는 리스트
		model.addAttribute("homeArticleList", homeArticleList);

		return "home";
	}

	@GetMapping("/joinForm")
	public String joinForm(Model model) throws Exception {

		model.addAttribute("customer", new Customer());
		return "join-form";
	}

	@GetMapping("/insertCustomer")
	public String insertCustomer(@ModelAttribute("customer") Customer customer, HttpServletRequest request) throws Exception {
		homeService.insertCustomer(customer);
		customer = homeService.login(customer);

		List<String> favorite = homeService.getFavorite(customer.getId());
		request.getSession().setAttribute("favList", favorite);
		request.getSession().setAttribute("customer", customer);

		return "redirect:/";

	}

	@GetMapping("/loginForm")
	public String login(Model model, HttpServletRequest request) throws Exception {

		model.addAttribute("customer", new Customer());

		// 로그인 성공시 로긴폼 이전 페이지로 redirect 하기위함.
		model.addAttribute("referer", request.getHeader("Referer"));

		return "login-form";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("customer") Customer customer, @RequestParam("referer") String referer, HttpServletRequest request)
			throws Exception {

		customer = homeService.login(customer);
		if (customer == null)
			return "login-fail";

		List<String> favorite = homeService.getFavorite(customer.getId());
		request.getSession().setAttribute("favList", favorite);

		// 인증 성공시 세션에 유저 오브젝트 등록
		request.getSession().setAttribute("customer", customer);

		if (referer == null)
			// referer가 없을시 홈으로 이동.
			return "redirect:/";

		else
			return "redirect:" + referer + "#";

	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.setAttribute("customer", null);
		session.setAttribute("deletingReply", null);
		session.setAttribute("currentTopic", null);

		String referer = request.getHeader("Referer");
		if (referer.contains("adminPage"))
			referer = "/";

		if (referer.contains("&yPos="))
			referer = referer.split("&yPos=")[0];

		return "redirect:" + referer + "#";
	}

	@GetMapping("/getAlert")
	public @ResponseBody List<AlertDTO> getAlert(HttpServletRequest request) {

		// 로그인 인증.
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null)
			return null;

		// 최근 20개의 알림 불러옴.
		List<AlertDTO> alertList = homeService.loadAlert(customer.getId());

		return alertList;
	}

	@GetMapping("/getAlertCnt")
	public @ResponseBody List<Customer> getAlertCnt(HttpServletRequest request) {

		// 로그인 인증.
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null)
			return null;

		Integer newAlertCnt = homeService.loadNewAlertCnt(customer.getId());
		customer.setNewAlert(newAlertCnt);

		List<Customer> list = new ArrayList<Customer>();
		list.add(customer);

		return list;

	}

	@GetMapping("/flushAlert")
	// 리퀘스트 맵핑 메소드의 리턴 타입이 void 일때 아래와 같이 해줌.
	@ResponseStatus(value = HttpStatus.OK)
	public void flushAlert(HttpServletRequest request) {

		// 로그인 인증.
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null)
			return;

		customer.setNewAlert(0);
		homeService.flushAlert(customer.getId());

	}

	@GetMapping("/addFav")
	// 리퀘스트 맵핑 메소드의 리턴 타입이 void 일때 아래와 같이 해줌.
	@ResponseStatus(value = HttpStatus.OK)
	public void addFav(@RequestParam("topic") String topic, HttpServletRequest request) {

		// 로그인 인증.
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null)
			return;

		homeService.addFav(customer.getId(), topic);
		List<String> favList = homeService.getFavorite(customer.getId());
		request.getSession().setAttribute("favList", favList);

	}

	@GetMapping("/deFav")
	// 리퀘스트 맵핑 메소드의 리턴 타입이 void 일때 아래와 같이 해줌.
	@ResponseStatus(value = HttpStatus.OK)
	public void deFav(@RequestParam("topic") String topic, HttpServletRequest request) {

		// 로그인 인증.
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null)
			return;

		homeService.deFav(customer.getId(), topic);
		List<String> favList = homeService.getFavorite(customer.getId());
		request.getSession().setAttribute("favList", favList);

	}

	@GetMapping("/checkIdDup")
	public @ResponseBody List<Reply> checkIdDup(@RequestParam("inputId") String inputId) {

		int dup = homeService.checkIdDup(inputId);
		if (dup == 1)
			dup = 0;
		else
			dup = 1;

		Reply reply = new Reply();
		reply.setNum(dup);

		List<Reply> list = new ArrayList<Reply>();
		list.add(reply);

		return list;
	}

	@GetMapping("/APValid")
	public @ResponseBody List<Reply> APValid(@RequestParam("input") String input, @RequestParam("type") String type) {

		int dup;

		if (type.equals("checkcName"))
			dup = homeService.chechcName(input);
		else if (type.equals("checkcCode"))
			dup = homeService.chechcCode(input);
		else if (type.equals("checktName"))
			dup = homeService.chechtName(input);
		else
			dup = homeService.chechtCode(input);

		if (dup == 1)
			dup = 0;
		else
			dup = 1;

		Reply reply = new Reply();
		reply.setNum(dup);

		List<Reply> list = new ArrayList<Reply>();
		list.add(reply);

		return list;
	}

	@GetMapping("/myInfo")
	public String myInfo(HttpServletRequest request) {
		
		if (request.getSession().getAttribute("customer") == null) return "invalid-page";
		
		return "myInfo";

	}
                    
	@PostMapping("/delCust")
	public String delCust(HttpServletRequest request) {

		Customer customer = (Customer) request.getSession().getAttribute("customer");
		
		if (customer.getId().equals("admin")) return "protected-admin";
		if (customer.getId().equals("test")) return "protected-admin";
		
		homeService.deleteCustomer(customer);
		request.getSession().setAttribute("customer", null);
		return "deleted-succ";
	}
}
