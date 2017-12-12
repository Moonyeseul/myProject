package com.sist.model;

import javax.servlet.http.HttpServletRequest;

public interface Model {
	//1.8�̻� : default�޼ҵ�, static�޼ҵ� => ������ �޼ҵ带 ���� �� �ִ�.
	//default : �ʿ��� �κп��� ������(1.8d�̻����)
	/*
	 * ���
	 *   class / interface
	 *   	extends
	 *   class ====> class
	 *   		extends
	 *   interface ====> interface
	 *   			implements
	 *   interface ====> class
	 *   			(X)�Ұ���  ==> interface�� ��� �Ұ���
	 *   class ====> interface
	 */
	public String execute(HttpServletRequest req) throws Exception;
}
