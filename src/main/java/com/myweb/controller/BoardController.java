package com.myweb.controller;

import java.io.IOException;

import com.myweb.board.service.BoardService;
import com.myweb.board.service.BoardServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("*.board") //board로 끝나는 모든 요청은 서블릿으로 연결.
public class BoardController extends HttpServlet{
	
	public BoardController() {
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doAction(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		doAction(req,resp);
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//요청 나눔
		//3. 요청을 분기
				request.setCharacterEncoding("utf-8");
				String uri = request.getRequestURI(); // ip port번호 제외된 주소
				String path = request.getContextPath(); //프로젝트 식별 이름
				String command = uri.substring(path.length());
				
				System.out.println(command);
				BoardService service;
				
				if(command.equals("/board/list.board")) {//목록화면
					//mvc2의 기본 이동은 forward이다
					//서비스 영역을 거쳐서 목록 data를 가지고 감
					service = new BoardServiceImpl();
					service.getList(request, response);
		
					
				}else if(command.equals("/board/write.board")) { //글 작성 화면
					request.getRequestDispatcher("board_write.jsp").forward(request, response);
				
				}else if(command.equals("/board/registForm.board")) {//글 등록
					//서비스...
					service = new BoardServiceImpl();
					service.regist(request,response);
				}
				
				
	}

}
