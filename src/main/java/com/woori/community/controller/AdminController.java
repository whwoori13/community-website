package com.woori.community.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.woori.community.DTO.CTmatchDTO;
import com.woori.community.DTO.CategoryDTO;
import com.woori.community.DTO.TopicDTO;
import com.woori.community.entity.Customer;
import com.woori.community.service.AdminService;
import com.woori.community.service.BoardService;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	private AdminService service;

	// nav 메뉴를 가져오기 위한 dao가 boardService에 있어서.
	@Autowired
	private BoardService boardService;

	@GetMapping("/adminPage")
	public String adminPage(HttpServletRequest request, Model model) throws Exception {

		boardService.loadNav(request);

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		if (customer == null)
			return "redirect:/";

		if (customer.getId().equals("admin")) {

			return "admin-page";
		} else
			return "auth-failed";

	}

	@PostMapping("/insertCategory")
	public String insertCategory(@RequestParam("code") String code, @RequestParam("name") String name, HttpServletRequest request, Model model) {

		Customer customer = (Customer) request.getSession().getAttribute("customer");
		String referer = request.getHeader("Referer");
		model.addAttribute("referer", referer);

		try {

			if (!(customer.getId().equals("admin"))) {
				return "auth-failed";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "auth-failed";                                                 
		}

		CategoryDTO category = new CategoryDTO(code, name);

		int success = service.insertCategory(category);

		if (success == 1)
			return "redirect:/admin/adminPage";
		else
			return "too-many";

	}

	@PostMapping("/insertTopic")
	public String insertTopic(@RequestParam("code") String code, @RequestParam("name") String name, HttpServletRequest request, Model model) {

		Customer customer = (Customer) request.getSession().getAttribute("customer");
		String referer = request.getHeader("Referer");
		model.addAttribute("referer", referer);

		try {

			if (!(customer.getId().equals("admin"))) {
				return "auth-failed";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "auth-failed";
		}

		TopicDTO topic = new TopicDTO(code, name);

		int success = service.insertTopic(topic);

		if (success == 1)
			return "redirect:/admin/adminPage";
		else
			return "too-many";

	}

	@GetMapping("/deleteCategory")
	public String deleteCategory(@RequestParam("code") String code, HttpServletRequest request) {

		// 시연용 항목 삭제 방지 처리
		if (code.equals("game"))
			return "protected-for-display";
		if (code.equals("sport"))
			return "protected-for-display";

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		try {

			if (!(customer.getId().equals("admin"))) {
				return "auth-failed";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "auth-failed";
		}

		CategoryDTO category = new CategoryDTO(code, null);
		service.deleteCategory(category);

		return "redirect:/admin/adminPage";

	}

	@GetMapping("/deleteTopic")
	public String deleteTopic(@RequestParam("code") String code, HttpServletRequest request) {

		// 시연용 항목 삭제 방지 처리
		if (code.equals("basketball"))
			return "protected-for-display";
		if (code.equals("leagueoflegends"))
			return "protected-for-display";
		if (code.equals("swimming"))
			return "protected-for-display";
		if (code.equals("starcraft"))
			return "protected-for-display";

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		try {

			if (!(customer.getId().equals("admin"))) {
				return "auth-failed";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "auth-failed";
		}

		TopicDTO topic = new TopicDTO(code, null);
		service.deleteTopic(topic);

		return "redirect:/admin/adminPage";

	}

	@PostMapping("/match")
	public String match(@RequestParam("category") String category, @RequestParam("topic") String topic, HttpServletRequest request) {

		// 시연용 항목 삭제 방지 처리

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		try {

			if (!(customer.getId().equals("admin"))) {
				return "auth-failed";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "auth-failed";
		}

		CTmatchDTO match = new CTmatchDTO(category, topic);
		service.match(match);

		return "redirect:/admin/adminPage";

	}

	@PostMapping("/unmatch")
	public String unmatch(@RequestParam("match") String match, HttpServletRequest request) {

		
		String[] parts = match.split("-");
		String category = parts[0];
		String topic = parts[1];

		// 시연용 삭제 방지 처리
		if (topic.equals("basketball"))
			return "protected-for-display";
		if (topic.equals("leagueoflegends"))
			return "protected-for-display";
		if (topic.equals("swimming"))
			return "protected-for-display";
		if (topic.equals("starcraft"))
			return "protected-for-display";

		Customer customer = (Customer) request.getSession().getAttribute("customer");

		try {

			if (!(customer.getId().equals("admin"))) {
				return "auth-failed";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "auth-failed";
		}

		CTmatchDTO unmatch = new CTmatchDTO(category, topic);
		service.unatch(unmatch);

		return "redirect:/admin/adminPage";

	}

}
