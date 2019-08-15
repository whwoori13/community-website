package logic;

import java.util.HashMap;
import java.util.Map;

public class Paging {

	/*
	 * ����¡ �޼ҵ� : �ش� �Խ����� �� �� ��, �������� �� ��, ���� ������ ��ȣ�� �޾Ƽ� �������ٿ� ��Ÿ�� ���� ��
	 * ��������ȣ(startPage), ������ �� ������ ��ȣ(lastPage), ������ ������ ��ȣ(endPage), db���� �ش� ��������
	 * �ҷ����� ���� offset���� ������.
	 * 
	 * @ < > ��ư ���� ȭ��ǥ ��ư ���� ���� ���� ������ = ���� ������ +- 10 if (���� ������ - 10 < 1) : < ���� ����
	 * ���� ������ = 1 if (���� ������ + 10 > ���� ������) : > ���� ���� ���� ������ = ���� ������
	 * 
	 * @ ���� ������ ���� ����, �� ������ ��� ���� ������ : ���� ������ - 4 �� ���� 1���� ���� ��� ���� �������� 1, �� ��������
	 * 10 �� ������ : ���� ������ + 5 �� ���� ���� ���������� Ŭ ��� �� �������� ���� ������, ���� �������� ���� ������ - 9
	 * 
	 * �� ������ �� = ceiling(�ѱۼ� / �������� �ۼ�)
	 * 
	 */

	public static Map paging(int currentPage, int totalCount, int countPerPage) {

		int endPage = (int) java.lang.Math.ceil((double) totalCount / (double) countPerPage);
		if (endPage == 0)
			endPage = 1; // totalCount�� 0�� ��� endPage�� 0�� �ǹǷ� 1�� �����ش�.

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

		// �Ʒ� �� ���� ���ǻ� ����¡�� �ʿ��� ������ ��� ����ϱ� ���� �޾ƿ� �Ķ���͸� �״�� �ʿ� �־���.
		map.put("limit", countPerPage);
		map.put("currentPage", currentPage);

		return map;

	}
}
