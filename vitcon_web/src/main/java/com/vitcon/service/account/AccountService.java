package com.vitcon.service.account;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vitcon.service.user.UserDeleteVO;
import com.vitcon.service.user.UserVO;

@Service
public class AccountService implements AccountMapper {

	@Autowired
	private SqlSession sqlSession;

	/*계정 정보 가져오는 기능*/
	@Override
	public UserVO getUser(UserVO userVO) throws Throwable {
		// TODO Auto-generated method stub
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
		
		return mapper.getUser(userVO);
	}
	
	/*계정 정보 가져오는 기능*/
	@Override
	public List<UserVO> getUserList(UserVO userVO) throws Throwable {
		// TODO Auto-generated method stub
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
		
		return mapper.getUserList(userVO);
	}

	/*아이디 추가*/
	@Override
	public void insertUser(UserVO userVO) throws Throwable {
		// TODO Auto-generated method stub
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
		mapper.insertUser(userVO);
	}

	/* 유저 정보 업데이트 */
	@Override
	public void updateUser(UserVO userVO) throws Throwable {
		// TODO Auto-generated method stub
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
		mapper.updateUser(userVO);
	}	
	
	/* 계정 삭제 */
	@Override
	public void deleteUser(HashMap<String, Object> map) throws Throwable {
		// TODO Auto-generated method stub
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
		mapper.deleteUser(map);
	}
	
	/* 중간 관리자 삭제 */
	@Override
	public void deleteMiddleUser(String userid) throws Throwable {
		// TODO Auto-generated method stub
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
		mapper.deleteMiddleUser(userid);
	}
	
	/*비밀번호 찾기*/
	@Override
	public String findPasswd(UserVO userVO) throws Throwable {
		// TODO Auto-generated method stub
		UserVO rvo = getUser(userVO);
		if (rvo == null)
			return null;
		
		return rvo.getPasswd();				
	}
	
	public UserVO getUserByID(String userid) throws Throwable {
		UserVO vo = new UserVO();
		vo.setUserid(userid);
		return getUser(vo);
	}

	@Override
	public List<HashMap<String, Object>> getAccountList(UserVO userVO) throws Throwable {
		// TODO Auto-generated method stub
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);		
		return mapper.getAccountList(userVO);
	}

	@Override
	public Integer deviceidCheck(String userid) throws Throwable {
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);	
		return mapper.deviceidCheck(userid);
	}

	@Override
	public Integer subAccountCheck(String userid) throws Throwable {
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);	
		return mapper.subAccountCheck(userid);
	}

	@Override
	public void useridDelete(String userid) throws Throwable {
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);	
		mapper.useridDelete(userid);
	}

	@Override
	public void userappDelete(String userid) throws Throwable {
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);	
		mapper.userappDelete(userid);
	}

	@Override
	public void devicegroupDelete(String userid) throws Throwable {
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);	
		mapper.devicegroupDelete(userid);
	}

	@Override
	public void insertUseridDelete(UserDeleteVO userDeleteVO) throws Throwable {
		AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);	
		mapper.insertUseridDelete(userDeleteVO);
	}
	
}
