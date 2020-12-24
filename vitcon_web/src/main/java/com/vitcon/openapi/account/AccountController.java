package com.vitcon.openapi.account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.core.password.Password;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.deviceuser.DeviceUserController;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.account.AccountService;
import com.vitcon.service.device.DeviceGroupVO;
import com.vitcon.service.device.DeviceService;
import com.vitcon.service.mail.EmailVO;
import com.vitcon.service.mail.MailService;
import com.vitcon.service.user.UserDeleteVO;
import com.vitcon.service.user.UserVO;

@RestController
@RequestMapping("/openapi/account")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountService accountService;

	@Autowired
	private MailService mailService;

	@Autowired
	private DeviceUserController deviceController;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private Environment env;

	@Autowired
	PlatformTransactionManager transactionManager;
	
	/**
	 * 아이디 찾기
	 * @param userVO
	 * @param username
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/getuserid")
	public ResponseObject getUserID(@ModelAttribute UserVO userVO) {
		ResponseObject ret = new ResponseObject();

		if (userVO.getUsername() == null || userVO.getMobile() == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		UserVO vo = null;

		try {
			vo = accountService.getUser(userVO);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);
		ret.setData(vo);

		return ret;
	}

	/**
	 * 사용자 정보 가져오기
	 * @return
	 */
	@RequestMapping("/getuser")
	public ResponseObject getUser(HttpSession session) {

		ResponseObject ret = new ResponseObject();
		UserVO vo = null;

		String userid = (String) session.getAttribute("userid");

		try {
			vo = accountService.getUserByID(userid);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);
		ret.setData(vo);

		return ret;
	}

	/**
	 * 아디디, 비밀번호로 정보 조회하기
	 * @return
	 */
	@RequestMapping("/getuserinfo")
	public ResponseObject getUserInfo(@ModelAttribute UserVO userVO) {

		ResponseObject ret = new ResponseObject();
		UserVO vo = null;

    //비밀번호 암호화
    String passwd = Password.password(userVO.getPasswd());
    userVO.setPasswd(passwd);

		try {
			vo = accountService.getUserByID(userVO.getUserid());
			if (userVO.getPasswd().equals(vo.getPasswd())) {
				ret.setData("1"); // 계정 정보 일치
			} else {
				ret.setData("0"); // 계정 정보 불일치
			}
			ret.setReturnCode(StatusCode.OK);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		return ret;
	}

	/**
	 * 사용자 정보 가져오기
	 * @return
	 */
	@RequestMapping("/getaccountlist")
	public ResponseObject getUserList(@ModelAttribute UserVO userVO, HttpSession session) {

		ResponseObject ret = new ResponseObject();
		List<HashMap<String, Object>> voList = null;

		try {
			String puserid = (String) session.getAttribute("userid");
			userVO.setPuserid(puserid);
			voList = accountService.getAccountList(userVO);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);
		ret.setData(voList);

		return ret;
	}

	/**
	 * 사용자 정보 업데이트
	 * @param userVO
	 * @return
	 */
	@RequestMapping("/updateuser")
	public ResponseObject updateUser(@ModelAttribute UserVO userVO) {
		ResponseObject ret =  new ResponseObject();

		if (userVO == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}


    //비밀번호 암호화
		if (userVO.getPasswd() != null && !"".equals(userVO.getPasswd())) {
      String passwd = Password.password(userVO.getPasswd());
      userVO.setPasswd(passwd);
		}


		UserVO resultVo = new UserVO();
		resultVo.setMobile(userVO.getMobile());
		try {
				// 휴대폰 번호가 없는 UPDATE 기능도 존재하므로 이렇게 해주어야한다.
				// 휴대폰 번호가 있는 경우에만 아래 내용을 실행한다.
				if (userVO.getMobile() != null && userVO.getMobile().trim() != "") {
					resultVo = accountService.getUser(resultVo);
					if (resultVo !=  null) {
						// 휴대폰 번호가 중복되는 경우에는 중복 에러코드를 리턴한다.
						ret.setReturnCode(StatusCode.WARNING_EXISTS);
						return ret;
					}
				}
				accountService.updateUser(userVO);
			} catch (Throwable e) {
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				e.printStackTrace();
				return ret;
			}

		ret.setReturnCode(StatusCode.OK);

		return ret;
	}

	/**
	 * 구매자 추가
	 * @param userVO
	 * @return
	 */
	@RequestMapping(value = "/insertuser")
	public ResponseObject insertUser(@ModelAttribute UserVO userVO) {
		ResponseObject ret = new ResponseObject();

		if (userVO == null || userVO.getUserid() == null || userVO.getUsername() == null ||
			userVO.getPasswd() == null || userVO.getMobile() == null || userVO.getLocalecd() == null) {

			ret.setReturnCode(StatusCode.ERROR_PARAMETER);

			return ret;
		}

		// 중복된 이메일 체크
		UserVO ressultVo = new UserVO();
		ressultVo.setMobile(userVO.getMobile());

		try {
			UserVO resultVo = accountService.getUser(ressultVo);

			if (resultVo !=  null) {
				ret.setReturnCode(StatusCode.WARNING_EXISTS);
				return ret;
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
		}

		// 중복된 아이디 체크
		ressultVo = new UserVO();
		ressultVo.setUserid(userVO.getUserid());

		try {
			UserVO resultVo = accountService.getUser(ressultVo);

			if (resultVo !=  null) {
				ret.setReturnCode(StatusCode.WARNING_EXISTS);
				return ret;
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
		}

		String startdate;
		String enddate;

		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		Date dt = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.MONTH, 1); // 1달 후로 서비스 이용기간 설정

		startdate = dtf.format(dt);
		enddate = dtf.format(cal.getTime());

		userVO.setStartdate(startdate);
		userVO.setEnddate(enddate);

    //비밀번호 암호화
    String passwd = Password.password(userVO.getPasswd());
    userVO.setPasswd(passwd);

		try {
			userVO.setGrade(10);
			accountService.insertUser(userVO);
			DeviceGroupVO groupvo = new DeviceGroupVO();
			groupvo.setDefaultyn("Y");
			groupvo.setUserid(userVO.getUserid());
			groupvo.setGroupname("default");
			deviceService.insertDeviceGroup(groupvo);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);

		return ret;
	}

	@RequestMapping(value = "/insertsubuser")
	public ResponseObject insertHanlderUser(@ModelAttribute UserVO userVO, HttpSession session) {
		ResponseObject ret = new ResponseObject();

		if (userVO == null || userVO.getUserid() == null || userVO.getUsername() == null) {

			ret.setReturnCode(StatusCode.ERROR_PARAMETER);

			return ret;
		}

		if (userVO.getLocalecd() == null || userVO.getLocalecd().equals("")) {
			// localecd 가 넘어오지 않았을때 세팅
			Locale locale = LocaleContextHolder.getLocale();
			userVO.setLocalecd(locale.getLanguage());
		}

		// 중복된 이메일 체크
		UserVO ressultVo = new UserVO();
		ressultVo.setMobile(userVO.getMobile());

    //비밀번호 암호화
    if (userVO.getPasswd() != null && !"".equals(userVO.getPasswd())) {
      String passwd = Password.password(userVO.getPasswd());
      userVO.setPasswd(passwd);
    }

		try {
			UserVO resultVo = accountService.getUser(ressultVo);

			if (resultVo !=  null) {
				ret.setReturnCode(StatusCode.WARNING_EXISTS);
				return ret;
			}

			String puserid = (String) session.getAttribute("userid");
			userVO.setPuserid(puserid);
			accountService.insertUser(userVO);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);

		return ret;
	}


	/* naver, kakao, google 등 외부 계정 중복 체크 */
	@RequestMapping("/getportalid")
	public ResponseObject getPortalid(@ModelAttribute UserVO userVO) {

		ResponseObject ret = new ResponseObject();

		if (userVO.getPortalid() == null || userVO.getRegistrationtype() == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		UserVO rvo = null;

		try {
			rvo = accountService.getUser(userVO);
			ret.setData(rvo);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		if (rvo == null) {
			// 파라메터로 넘어온 userid 로 검색했을 때 record 를 못 찾음(즉 사용자 id 존재하지 않음)
			ret.setReturnCode(StatusCode.OK);
			return ret;
		}

		// 파라메터로 넘어온 userid 로 검색했을 때 record 를 찾음
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}

	/* 이메일 중복 체크 */
	@RequestMapping("/getemail")
	public ResponseObject getemail(@ModelAttribute UserVO userVO) {

		ResponseObject ret = new ResponseObject();

		if (userVO.getUserid() == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		UserVO rvo = null;

		try {
			rvo = accountService.getUser(userVO);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);

		if (rvo == null) {
			// 파라메터로 넘어온 userid 로 검색했을 때 record 를 못 찾음(즉 사용자 id 존재하지 않음)
			ret.setData("0");
			return ret;
		}

		// 파라메터로 넘어온 userid 로 검색했을 때 record 를 찾음
		ret.setData("1");
		return ret;
	}

	/* 구매자 삭제 ==> 테스트 해봐야함*/
	@RequestMapping("/deletebuyer")
	public ResponseObject deleteBuyer(@RequestParam String userid) {
		ResponseObject ret = new ResponseObject();

		if (userid == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		deviceController.removeDevice(userid);

		//중간 관리자 삭제
		String puserid = userid;

		try {
			accountService.deleteMiddleUser(puserid);
		} catch (Throwable e1) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e1.printStackTrace();
			return ret;
		}

		//구매자 삭제
		String[] useridList = userid.split(",");
		HashMap<String, Object> map = new HashMap<>();

		List<String> list = new ArrayList<>();

		for (String uid : useridList) {
			list.add(uid);
		}

		map.put("userid", list);

		try {
			accountService.deleteUser(map);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}

	/* 중간 관리자 삭제 */
	@RequestMapping("/deletemiddlemanager")
	public ResponseObject deleteMiddleManager(@ModelAttribute UserVO userVO) {
		ResponseObject ret = new ResponseObject();

		if (userVO == null || userVO.getUserid() == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		deviceController.deleteUserDevice(userVO);

		String[] useridList = userVO.getUserid().split(",");
		HashMap<String, Object> map = new HashMap<>();

		List<String> list = new ArrayList<>();
		for(String uid : useridList) {
			list.add(uid);
		}
		map.put("userid", list);

		try {
			deviceService.deleteUserApp(map);
			accountService.deleteUser(map);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			e.printStackTrace();

			return ret;
		}

		ret.setReturnCode(StatusCode.OK);

		return ret;
	}

	/* 사용자 아이디 삭제 */
	@RequestMapping("/deleteuserid")
	public ResponseObject deleteUser(@RequestParam String userid) {
		ResponseObject ret = new ResponseObject();

		// 파라미터 체크
		if (userid == null) {
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		String[] useridList = userid.split(",");
		HashMap<String, Object> map  = new HashMap<>();

		List<String> list = new ArrayList<>();

		for (String uid : useridList) {
			list.add(uid);
		}

		map.put("userid", list);
		try {
			deviceService.deleteUserApp(map);
			accountService.deleteUser(map);
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();

			return ret;
		}

		ret.setReturnCode(StatusCode.OK);

		return ret;
	}

	/* 비밀번호 찾기 기능 */
	@RequestMapping("/getpasswd")
	public ResponseObject getPasswd(@ModelAttribute UserVO userVO) {
		ResponseObject ret = new ResponseObject();
		//tune
		//원래 admin0@gmail.com이라는 가상의 userid을 쓰지만 이메일 테스트를 하기 위해서 실제 이메일을 입력하였음.
		//추후에 userid 값은 세션 값으로 대체한다.
		//String userid = "blancmain@gmail.com";
		//String userid = "net1506@naver.com";

		try {
			userVO = accountService.getUserByID(userVO.getUserid());
			if (userVO == null) {
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}

		//tune
		// 관리자 이메일로 대체한다.
		String from = env.getProperty("spring.mail.username");
    String subject = "";
    String text = "";
    String html = "";

    //TODO 추후 리팩토링 필요함
    String passwd = mailService.makeRandomString();
    userVO.setPasswd(Password.password(passwd));
    try {
      accountService.updateUser(userVO);
    } catch (Throwable e) {
      ret.setReturnCode(StatusCode.ERROR_SERVICE);
      return ret;
    }

    Locale locale = LocaleContextHolder.getLocale();

    if (locale.getLanguage() == null) {
      //korean default
      subject = "비밀번호 찾기 결과";
    }

    switch (locale.getLanguage()) {
      case "ko":
        subject = "비밀번호 찾기 결과";
        text = "고객님의 비밀번호가 변경되었습니다.\n"
            + "비밀번호 : " + passwd;
        html = "<p>고객님의 비밀번호가 변경되었습니다.<br></p>"
            + "<p>비밀번호 : " + passwd + "</p>";
        break;
      case "en":
        subject = "Password Find Results"; //en
        text = "Your password has changed.\n"
            + "password : " + passwd;
        html = "<p>Your password has changed.<br></p>"
            + "<p>password : " + passwd + "</p>";
        break;
      case "ja":
        subject = "パスワードを忘れた結果"; //ja
        text = "お客様のパスワードが変更されました。\n"
            + "パスワード：" + passwd;
        html = "<p>お客様のパスワードが変更されました。<br></p>"
            + "<p>パスワード：" + passwd + "</p>";
        break;
    }

    EmailVO mailVO = new EmailVO();
    mailVO.setTo(userVO.getUserid());
    mailVO.setFrom(from);
		mailVO.setSubject(subject);
		mailVO.setText(text);
		mailVO.setHtml(html);

		boolean r;
		try {
			r = mailService.sendEmail(mailVO);
		} catch (Exception e) {
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}

		if (!r) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}

		ret.setReturnCode(StatusCode.OK);

		return ret;
	}


	@RequestMapping("/changelanguage")
	public ResponseObject changeLanguage(String localecd) {
		UserAuthentication user = null;
	    String userid = null;
	    ResponseObject ret = new ResponseObject();

	    if (localecd == null) {
	    	ret.setReturnCode(StatusCode.ERROR_PARAMETER);
	    	return ret;
	    }

	    try {
	       user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
	       userid = user.getName(); // 로그인한 사용자의 id
	    } catch (Exception e) {
	    }

	    if (userid == null) {
	       // 유저아이디 존재하지 않음.
	       ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
	       return ret;
	    }

	    UserVO uservo = new UserVO();
	    uservo.setUserid(userid);
	    uservo.setLocalecd(localecd);

	    try {
	    	accountService.updateUser(uservo);
	    	ret.setReturnCode(StatusCode.OK);
	    } catch (Throwable e) {
	    	ret.setReturnCode(StatusCode.ERROR_SERVICE);
		    return ret;
	    }

	    return ret;
	}
	
	// 아이디에 등록된 디바이스  체크
	@RequestMapping("/deviceidCheck")
	public ResponseObject deviceidCheck(String userid) throws Throwable {
		ResponseObject ret = new ResponseObject();
		
		if(userid == "" || userid == null || userid == "undefined") {
			ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
		    return ret;
		}
		
		try {
			ret.setData(accountService.deviceidCheck(userid));
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
	
	// 아이디에 등록된 하위 계정 체크
	@RequestMapping("/subAccountCheck")
	public ResponseObject subAccountCheck(String userid) throws Throwable {
		ResponseObject ret = new ResponseObject();
		
		if(userid == "" || userid == null || userid == "undefined") {
			ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
		    return ret;
		}
		
		try {
			ret.setData(accountService.subAccountCheck(userid));
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		ret.setReturnCode(StatusCode.OK);
		return ret;
	}
	
	/* 회원탈퇴 유저 삭제 */
	@RequestMapping("/useridDelete")
	@Transactional
	public ResponseObject useridDelete(String userid) throws Throwable {
		
		ResponseObject ret = new ResponseObject();
		TransactionStatus status=this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			
			if(userid == "" || userid == null || userid == "undefined") {
				ret.setReturnCode(StatusCode.ERROR_UNAUTHORIZED);
			    return ret;
			}
			
			accountService.devicegroupDelete(userid);
			accountService.userappDelete(userid);
			accountService.useridDelete(userid);
			
			/*int number = 100;
			int result = 0;
			
			for(int i=0; i<10; i++){
				result = number / (int)(Math.random() *10);
				System.out.println(result);
			}*/
			
			UserDeleteVO userDeleteVO = new UserDeleteVO();
			
			java.util.Date dt = new java.util.Date();
			
			userDeleteVO.setUserid(userid);
			userDeleteVO.setRegdate(dt);

			accountService.insertUseridDelete(userDeleteVO);
			
			this.transactionManager.commit(status);
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.transactionManager.rollback(status);
            throw e;
			
		}
		ret.setReturnCode(StatusCode.OK);
		return ret;
		
	}
		
		
	
}
