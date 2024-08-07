package com.myweb.board.service;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface BoardService  {
	
	//등록기능
	void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException ;
	//목록조회
	void getList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException ;
	//글 상세내용
	void getContent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException ;
	//글 수정화면
	void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException ;
	// 글 수정
	void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException ;
	// 글 삭제
	void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException ;
}
