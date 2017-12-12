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

import java.util.*; //Map (��û => Ŭ����(��) ��Ī)

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
	
	//config�� ���ؼ� xml�� ��ϵ� ���� �о��
	//init�޼ҵ�� �� �ѹ��� ȣ��
	public void init(ServletConfig config) throws ServletException {
		try {
			for(int i=0; i<strCls.length; i++) {
				Class clsName = Class.forName(strCls[i]);
				Object obj = clsName.newInstance(); //��ü ����
				clsMap.put(strCmd[i], obj);
			}
			//�Ʒ�ó�� �ص������� Ŭ���� �������� �ڵ� �����
//			clsMap.put("list", new MovieList());
//			clsMap.put("detail", new MovieDetail());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. list.do, detail.do (�� ����� �� ��ȣ)
			// 2. movie.do?cmd=list
			String cmd = request.getRequestURI(); // URI : ����ڰ� �ּ��Է¶��� ��û�� ����(��Ʈ��ȣ ����)
			//http://localhost:8080/MVCProject1/list.do
			// => URI : /MVCProject1/list.do
			//			============
			//			ContextPath
			
			//����ڰ� ��û�� ���� ������
			cmd = cmd.substring(request.getContextPath().length()+1, cmd.lastIndexOf("."));
			
			//��û ó�� => �� Ŭ����(Ŭ����, �޼ҵ�)
			Model model = (Model)clsMap.get(cmd);
			//model => ���� �Ŀ� ��� ���� request�� ��ƴ޶�
			//Call By Reference => �ּҸ� �Ѱ��ְ� �ּҿ� ���� ä���
			String jsp = model.execute(request);
			
			//jsp�� request, session�� ����
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
