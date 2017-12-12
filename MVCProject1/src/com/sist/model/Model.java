package com.sist.model;

import javax.servlet.http.HttpServletRequest;

public interface Model {
	//1.8이상 : default메소드, static메소드 => 구현된 메소드를 만들 수 있다.
	//default : 필요한 부분에만 재정의(1.8d이상부터)
	/*
	 * 상속
	 *   class / interface
	 *   	extends
	 *   class ====> class
	 *   		extends
	 *   interface ====> interface
	 *   			implements
	 *   interface ====> class
	 *   			(X)불가능  ==> interface는 상속 불가능
	 *   class ====> interface
	 */
	public String execute(HttpServletRequest req) throws Exception;
}
