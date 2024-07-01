package com.myweb.user.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessHandle.Info;

import com.myweb.user.model.UserDAO;
import com.myweb.user.model.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserServiceImpl implements UserService{
	
	//배고파요
	@Override
	public void join(HttpServletRequest request, HttpServletResponse response) throws ServletException , IOException{
		//값을 대신 받을 수 있음
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		
		//중복되는 회원이 있는지 확인
		//중복이 없는 경우 회원가입 처리
		UserDAO dao = UserDAO.getInstance();
		int cnt= dao.findUser(id);
		
		if(cnt == 1) { //아이디 중복
			
			request.setAttribute("msg", "이미 존재하는 회원입니다.");
			request.getRequestDispatcher("join.jsp").forward(request, response);

			} else { //중복 x - 회원가입
				
				dao.insertUser(id, pw, name, email, gender);
				response.sendRedirect("login.user");//MVC2 방식에서 리다이렉트는 다시 컨르롤러를 태울때 사용합니다.
			}

		
		
		
	}

	
	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		UserDAO dao = UserDAO.getInstance();
		UserDTO dto = dao.login(id, pw);
		
		if(dto==null) { //아이디 or 비밀번호가 틀렸음
			
			request.setAttribute("msg", "아이디 또는 비밀번호를 확인하세요");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			
		}else {//로그인 성공
			// 세션에 로그인 성공에 대한 내용을 저장
			HttpSession session=request.getSession(); //리퀘스트에서 현재 세션을 얻음
			session.setAttribute("user_id", dto.getId());
			session.setAttribute("user_name", dto.getName());
			
			response.sendRedirect("mypage.user"); //다시 컨트롤러를 태워 나간다.
			
			
		}
			
	}
	@Override
	public void getInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 아이디 기반으로 회원정보를 조회해서 데이터를 가지고 , 수정페이지로 이동
		//실습..
		/*
		 * 아이디는 세션이 있습니다
		 * 아이디 기반으로 회원정보를 조회하는 getInfo() DAO 에 생성합니다.
		 * 서비스에선 getInfo() 호출하고 , 조회한 데이터를 request에 저장합니다.
		 * forward를 이용해서 modify.jsp로 이동합니다.
		 * 회원정보를 input 태그에 미리 출력해주면 됩니다.
		 */
	
		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("user_id");
		
		//DAO객체 생성
		UserDAO dao = UserDAO.getInstance();
		UserDTO dto = dao.getInfo(user_id);
		
		
		//dto를 클라이언트로 가지고 감.
		if(dto!=null) {
			session.setAttribute("dto", dto); //이름 , 값
			request.getRequestDispatcher("modify.jsp").forward(request, response);
		
		
		
		}
		
		
		
	}

	
	@Override
	public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//클라이언트의 값을 받음
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String name = request.getParameter("name");
		String email=request.getParameter("email");
		String gender = request.getParameter("gender");
		
		//parameter를 dto에 저장
		UserDTO dto = new UserDTO(id, pw, name, email, gender, null);
		
		//DAO생성
		UserDAO dao = UserDAO.getInstance();
		//업데이트
		int result = dao.update(dto);
		
		if(result==1) { //update 성공
			//java에서 알림창을 화면에 보내는 방법
			//out객체 클라이언트로 출력
			//세션 네임을 수정
			HttpSession session =request.getSession();
			session.setAttribute("user_name",name);
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('회원정보가 정상 수정 되었습니다.');");
			out.println("location.href='mypage.user';");
			out.println("</script>");
			
			
			
		}else { //update 실패
			//유저페이지로 보냄
			response.sendRedirect("mypage.user"); //mvc2에서는 리다이렉트가 컨트롤러의 경로가 됩니다.
			
		}
		
		
	}
	
	
	@Override
	public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		HttpSession session=request.getSession(); // 세션
		String userId = (String) session.getAttribute("user_id"); //세션에 있는 아이디 받아오기
		String pw = request.getParameter("pw"); // 입력받은 패스워드
		
		
		//pw가 일치하는지 login 메서드로 확인
		UserDAO dao = UserDAO.getInstance();
		UserDTO dto = dao.login(userId, pw); //
		
		if(dto==null) { //pw가 틀린경우
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			request.setAttribute("msg","비밀번호가 일치하지 않습니다.");
			request.getRequestDispatcher("delete.jsp").forward(request, response);
			
			
//			out.println("<script>");
//			out.println("alert('비밀번호를 확인하세요.');");
//			out.println("location.href='mypage.user';");
//			out.println("</script>");
	}else{ //pw가 일치하는경우
		//delete 실행
		dao.delete(userId);
		session.invalidate();
		response.sendRedirect(request.getContextPath() + "/index.jsp");

	}
		/*
		 * 1.화면에서 넘어오는 pw값을 받으세요 (아이디는 세션에 있습니다 세션에서 받아오세요.)
		 * 2.회원탈퇴는 비밀번호가 일치하는지 확인하고 , 탈퇴를 진행합니다.
		 *  -> login 메서드는 id,pw를 받아서 비밀번호 일치하는지 확인할 수 있습니다.(재활용) 
		 * 3. login메서드가 DTO를 반환하면 DAO에 delete 메서드를 만들고 , 회원 삭제를 진행하면 됩니다.
		 * 4. 삭제 성공시에는 세션을 전부 삭제하고 , "홈화면"으로 리다이렉트, 
		 * login메서드가 NULL을 반환하면 (비밀번호가 틀린 경우)
		 *  delete.jsp에 비밀번호를 확인하세요 메시지를 남겨주면 됩니다.
		 */
		
	}
	
}

