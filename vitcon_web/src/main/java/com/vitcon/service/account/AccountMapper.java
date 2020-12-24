package com.vitcon.service.account;

import java.util.HashMap;
import java.util.List;

import com.vitcon.service.user.UserDeleteVO;
import com.vitcon.service.user.UserVO;

public interface AccountMapper {
	
	/* 유저 정보 가져오는 기능 */
	public UserVO getUser(UserVO userVO) throws Throwable;
	
	/* 유저 정보 가져오는 기능 */
	public List<UserVO> getUserList(UserVO userVO) throws Throwable;
		
	/* 유저 추가 */
	public void insertUser(UserVO userVO) throws Throwable;
	
	/* 유저 정보 업데이트 */
	public void updateUser(UserVO userVO) throws Throwable;	

	/* 유저 삭제 */
	public void deleteUser(HashMap<String, Object> map) throws Throwable;
	
	/* 구매자 삭제에서의 중간 관리자, 사용자 삭제 */
	public void deleteMiddleUser(String userid) throws Throwable;

	/* 비밀번호 찾기*/
	public String findPasswd(UserVO userVO) throws Throwable;
	
	/* 계정관리 페이지 리스트 */
	public List<HashMap<String, Object>> getAccountList(UserVO userVO) throws Throwable;
	
	/* 회원탈퇴시 등록된 디바이스 체크 */
	public Integer deviceidCheck(String userid) throws Throwable;

	/* 회원탈퇴시 등록된 하위계정 체크 */
	public Integer subAccountCheck(String userid) throws Throwable;

	/* 회원탈퇴 유저 아이디 삭제 */
	public void useridDelete(String userid) throws Throwable;

	/* 회원탈퇴 유저앱 아이디 삭제 */
	public void userappDelete(String userid) throws Throwable;

	/* 회원탈퇴 디바이스그룹 삭제 */
	public void devicegroupDelete(String userid) throws Throwable;

	/* 회원탈퇴한 유저아이디 insert */
	public void insertUseridDelete(UserDeleteVO userDeleteVO) throws Throwable;

}
