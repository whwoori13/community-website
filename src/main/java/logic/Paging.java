package logic;

import java.util.HashMap;
import java.util.Map;

public class Paging {

	/*
	 * 페이징 메소드 : 해당 게시판의 총 글 수, 페이지당 글 수, 현재 페이지 번호를 받아서 페이지바에 나타날 왼쪽 끝
	 * 페이지번호(startPage), 오른쪽 끝 페이지 번호(lastPage), 마지막 페이지 번호(endPage), db에서 해당 페이지를
	 * 불러오기 위한 offset값을 리턴함.
	 * 
	 * @ < > 버튼 동작 화살표 버튼 누른 후의 현재 페이지 = 현재 페이지 +- 10 if (현재 페이지 - 10 < 1) : < 누른 후의
	 * 현재 페이지 = 1 if (현재 페이지 + 10 > 엔딩 페이지) : > 누른 후의 현재 페이지 = 엔딩 페이지
	 * 
	 * @ 현재 페이지 기준 시작, 끝 페이지 계산 시작 페이지 : 현재 페이지 - 4 그 값이 1보다 작을 경우 시작 페이지는 1, 끝 페이지는
	 * 10 끝 페이지 : 현재 페이지 + 5 그 값이 엔딩 페이지보다 클 경우 끝 페이지는 엔딩 페이지, 시작 페이지는 엔딩 페이지 - 9
	 * 
	 * 총 페이지 수 = ceiling(총글수 / 페이지당 글수)
	 * 
	 */

	public static Map paging(int currentPage, int totalCount, int countPerPage) {

		int endPage = (int) java.lang.Math.ceil((double) totalCount / (double) countPerPage);
		if (endPage == 0)
			endPage = 1; // totalCount가 0인 경우 endPage가 0이 되므로 1로 고쳐준다.

		int startPage;
		int lastPage;
		int offset;

		if (currentPage < 1)
			currentPage = 1;
		if (currentPage > endPage)
			currentPage = endPage;

		startPage = currentPage - 4;
		lastPage = currentPage + 5;
		offset = (currentPage - 1) * countPerPage;

		if (endPage < 10) {
			startPage = 1;
			lastPage = endPage;
		} else {

			if (startPage < 1 && !(lastPage > endPage)) {
				startPage = 1;
				lastPage = 10;
			}
			if (lastPage > endPage && !(startPage < 1)) {
				lastPage = endPage;
				startPage = endPage - 9;
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startPage", startPage);
		map.put("lastPage", lastPage);
		map.put("endPage", endPage);
		map.put("offset", offset);

		// 아래 두 값은 편의상 페이징에 필요한 값들을 묶어서 사용하기 위해 받아온 파라미터를 그대로 맵에 넣어줌.
		map.put("limit", countPerPage);
		map.put("currentPage", currentPage);

		return map;

	}
}
