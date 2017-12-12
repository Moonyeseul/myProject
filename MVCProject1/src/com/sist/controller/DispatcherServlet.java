package com.sist.controller;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.model.Model;
import com.sist.model.MovieDetail;
import com.sist.model.MovieList;

import java.util.*; //Map (요청 => 클래스(모델) 매칭)

public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String[] strCls = {
		"com.sist.model.MovieList",
		"com.sist.model.MovieDetail"
	};
	private String[] strCmd = {
		"list", "detail"	
	};
	
	private Map clsMap = new HashMap();
	
	//config를 통해서 xml에 등록된 정보 읽어옴
	//init메소드는 딱 한번만 호출
	public void init(ServletConfig config) throws ServletException {
		try {
			for(int i=0; i<strCls.length; i++) {
				Class clsName = Class.forName(strCls[i]);
				Object obj = clsName.newInstance(); //객체 생성
				clsMap.put(strCmd[i], obj);
			}
			//아래처럼 해도되지만 클래스 많아지면 코드 길어짐
//			clsMap.put("list", new MovieList());
//			clsMap.put("detail", new MovieDetail());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. list.do, detail.do (이 방법을 더 선호)
			// 2. movie.do?cmd=list
			String cmd = request.getRequestURI(); // URI : 사용자가 주소입력란에 요청한 파일(포트번호 다음)
			//http://localhost:8080/MVCProject1/list.do
			// => URI : /MVCProject1/list.do
			//			============
			//			ContextPath
			
			//사용자가 요청한 내용 가져옴
			cmd = cmd.substring(request.getContextPath().length()+1, cmd.lastIndexOf("."));
			
			//요청 처리 => 모델 클래스(클래스, 메소드)
			Model model = (Model)clsMap.get(cmd);
			//model => 실행 후에 결과 값을 request에 담아달라
			//Call By Reference => 주소를 넘겨주고 주소에 값을 채운다
			String jsp = model.execute(request);
			
			//jsp에 request, session값 전송
			RequestDispatcher rd = request.getRequestDispatcher(jsp);
			rd.forward(request, response);
			
			/*
			 * 	service(request,response) {
			 * 		_jspService(request);
			 * 	}
			 */
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
