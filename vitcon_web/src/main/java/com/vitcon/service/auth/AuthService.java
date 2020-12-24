package com.vitcon.service.auth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.service.account.AccountService;
import com.vitcon.service.user.UserVO;

@Service
public class AuthService {

	@Autowired
	private AccountService userService;

	/**
	 * 사용자의 사용기간이 유효한지 검사
	 * @param userVO
	 * @return 서비스 기한 유효 : true, 서비스 기한 종료 : false
	 */
	public boolean isServiceTime(UserVO userVO) {

		UserVO rvo = null;

		try {
			rvo = userService.getUser(userVO);

			if (rvo == null || rvo.getEnddate() == null)
				return false;

			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date enddate = dt.parse(rvo.getEnddate());
			Date currentTime = new Date();

			int compareTime = enddate.compareTo(currentTime);

			if (compareTime < 0)
				return false;

		} catch (Throwable e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean login(UserVO userVO, HttpSession session) {
		if (userVO == null)
			return false;
		UserVO rvo = null;

		try {
			rvo = userService.getUser(userVO);
		} catch (Throwable e) {
			//e.printStackTrace();
			return false;
		}

		if (rvo == null) {
			return false;
		}

		// 비밀번호 확인을 위해 user 정보를 가져온다.

		// 비밀번호 확인 (포털사이트(카카오, 네이버, 구글) 로그인은 해당하지 않음)
		if (rvo.getPortalid() == null) {
		    if (!rvo.getPasswd().equals(userVO.getPasswd())) {
		   	 // 비밀번호가 틀린 경우
		   	 return false;
		   }
		}

		// userVO.setGrade(10); //구매자
		//userVO.setGrade(20); // 중간관리자
		// userVO.setGrade(21); //사용자
		userVO.setGrade(rvo.getGrade());

		if (userVO.getUserid() == null)
			userVO.setUserid(rvo.getUserid());

		UserAuthentication authentication = null;

		if (userVO.getGrade() >= 10 && userVO.getGrade() < 20) {
			// 구매자
			authentication = new UserAuthentication(userVO.getUserid(),
					AuthorityUtils.createAuthorityList("ROLE_CUSTOMER"), true);
		} else if (userVO.getGrade() == 20) {
			// 중간관리자
			authentication = new UserAuthentication(userVO.getUserid(),
					AuthorityUtils.createAuthorityList("ROLE_USERADMIN"), true);
		} else if (userVO.getGrade() == 21) {
			// 사용자
			authentication = new UserAuthentication(userVO.getUserid(), AuthorityUtils.createAuthorityList("ROLE_USER"),
					true);
		}

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date startDate = null;
		Date endDate = null;

		try {
			String startdate = rvo.getStartdate();
			startDate = sdf.parse(startdate);

			String enddate = rvo.getEnddate();
			endDate = sdf.parse(enddate);
		} catch (ParseException e) {
			e.printStackTrace();
		}


		/* 세션 등록 */
		SecurityContextHolder.getContext().setAuthentication(authentication);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());
		session.setAttribute("grade", rvo.getGrade());
		session.setAttribute("startdate", startDate);
		session.setAttribute("enddate", endDate);
		session.setAttribute("userid", rvo.getUserid());
		return true;
	}

}
