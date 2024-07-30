package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;


// View : 사용자가 보게될 시각적인 요소(화면) 출력 및 입력
public class MemberMenu {
	// Scanner 객체 생성
	private Scanner sc = new Scanner(System.in);
	// MemberController 객체 생성
	private MemberController mc = new MemberController();

	/**
	 * 사용자가 보게 될 첫 화면 : 메인메뉴(화면)
	 */
	public void mainMenu() {
		
		while(true) {
			System.out.println("======= 회원 관리 프로그램 =======");
			System.out.println("1. 회원 추가"); 		// C (Create)
			System.out.println("2. 전체 회원 조회");	// R (Read)
			System.out.println("3. 회원 아이디로 검색");// R (Read)
			System.out.println("4. 회원 정보 수정");	// U (Update)
			System.out.println("5. 회원 탈퇴");		// D (Delete)
			System.out.println("9. 프로그램 종료");
		
			System.out.print(">> 메뉴 번호: ");
			int menu = sc.nextInt();
			sc.nextLine();
			
			switch(menu) {
			case 1:
				addMenu();
				break;
			case 2:
				mc.selectList();
				break;
			case 3:
				searchID();
				break;
			case 4:
				break;
			case 5:
				withdrawal();
				break;
			case 9:
				System.out.println("프로그램 종료합니다...");
				return;
			}
		}
		
			
	}
	
	/**
	 * 회원 추가를 위한 메뉴(화면) : 회원정보를 입력받아서 추가 요청
	 */
	public void addMenu() {
		System.out.println("아이디: ");
		String userId = sc.nextLine();
		
		System.out.println("비밀번호: ");
		String userPw = sc.nextLine();
		
		System.out.println("이름: ");
		String name = sc.nextLine();
		
		System.out.println("성별 (M/F): ");
		char gender = sc.nextLine().toUpperCase().charAt(0);
		
		// 회원 추가 요청 --> Controller에게 요청
		mc.insertMember(userId, userPw, name, gender);
	}

	/**
	 * 요청 처리 후 성공했을 경우 사용자에게 표시할 화면
	 * @param message 성공 메세지
	 */
	public void displaySuccess(String message) {
		System.out.println("서비스 요청 성공: " + message);
	}
	
	/**
	 * 요청 처리 후 실패했을 경우 사용자에게 표시할 화면
	 * @param message 실패 메세지
	 */
	public void displayFailed(String message) {
		System.out.println("서비스 요청 실패: " + message);
	}
	
	/**
	 * 조회 결과가 없을 때사용자에게 표시할 화면
	 * @param message 조회 결과에 대한 메세지
	 */
	public void displayNoData(String message) {
		System.out.println(message);
	}
	
	
	/**
	 * 조회 결과가 여러 행일 때 사용자에게 표시할 화면
	 * @param list 조회된 회원 정보가 담겨있는 리스트
	 */
	public void displayMemberList(ArrayList<Member> list) {
		System.out.println("--- 조회 결과 ---");
		for(Member m : list) {
			System.out.println(m);
		}
		
//		for(int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}
	}
	
	public void searchID() {
		System.out.println("검색할 아이디를 입력하세요: ");
		String id = sc.nextLine();
		mc.searchID(id);
	}
	
	
	/**
	 * 조회 결과 Member 객체를 화면에 표시
	 * @param m 
	 * 
	 */
	public void displayMember(Member m) {
		System.out.println("--- 조회 결과 ---");
		System.out.println(m);
	}
	
	public void withdrawal() {
		System.out.println("삭제할 회원 아이디를 입력하세요");
		System.out.print("아이디: ");
		String id = sc.nextLine();
		
		if(mc.withdrawal(id) > 0) {
			System.out.println("회원 탈퇴되었습니다.");
		}  else {
			System.out.println("회원정보를 찾을 수 없습니다.");
		}
	};
}

