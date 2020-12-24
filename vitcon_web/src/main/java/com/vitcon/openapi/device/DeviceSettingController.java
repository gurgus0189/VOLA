
package com.vitcon.openapi.device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitcon.core.auth.UserAuthentication;
import com.vitcon.openapi.code.StatusCode;
import com.vitcon.openapi.response.ResponseObject;
import com.vitcon.service.device.DeviceChannelRangeVO;
import com.vitcon.service.device.DeviceService;
import com.vitcon.service.device.DeviceVO;



@RestController
@RequestMapping("/openapi/device/setting")
public class DeviceSettingController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceSettingController.class);
	
	@Autowired 
	private DeviceService device;
    
    /**
     * 디바이스 설정값 반환
     * 디바이스 설정의 기본 설정값(센서명(devicename), 센서설명(devicedesc), 범위설정값을 JSON 으로 반환한다. 
     * 
     * @param deviceid 센서설정값을 가져올 device 의 아이디 
     * @return ResponseObject 형태의 JSON 문자열
     */
    @RequestMapping("get")
	public ResponseObject get(String deviceid) {
    	ResponseObject ret = new ResponseObject();
                
    	HashMap<String, Object> data = new HashMap<>();
        
        // tune
        // deviceid 의 소유자가 현재 세션의 사용자인지 여부를 체크하는 로직이 필요하다.
    	try {
    		List<HashMap<String, Object>> list = device.getDeviceSetList(deviceid);
    		HashMap<String, Object> map = null;
    		
    		if (list.size() > 0) {
    			map = list.get(0);
    			//공통인 부분만 추림
    			data.put("devicedesc", map.get("devicedesc"));
    			data.put("deviceinfo", map.get("deviceinfo"));
    			data.put("deviceid", map.get("deviceid"));
    			data.put("devicename", map.get("devicename"));
    			data.put("devicetypecd", map.get("devicetypecd"));
    			data.put("groupid", map.get("groupid"));
    			
    			//2018-12-03 kwt 추가 푸쉬 및 저장주기 관련 
    			data.put("saveinterval", map.get("saveinterval"));
    			data.put("pushenable", map.get("pushenable"));
    			data.put("pushinterval", map.get("pushinterval"));
    			data.put("pushrepeat", map.get("pushrepeat"));
    		
    			data.put("pushsysenable" , map.get("pushsysenable"));
    		}
    		
    		for (HashMap<String, Object> v : list) {
    			v.remove("devicedesc");
    			v.remove("deviceinfo");
    			v.remove("deviceid");
    			v.remove("devicename");
    			v.remove("devicetypecd");
    			v.remove("groupid");

    			v.remove("saveinterval");
    			v.remove("pushenable");
    			v.remove("pushinterval");
    			v.remove("pushrepeat");
    		
    			v.remove("pushsysenable");
    		}
    		data.put("channelrange", list);
    		
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}
    	
    	ret.setReturnCode(StatusCode.OK);    	
        ret.setData(data);
        
    	return ret;
	}    
    
    /**
     * 디바이스 일괄변경
     * 
     * 설정값을 변경할 디바이스아이디를 인자로 받아, 해당 디바이스의 설정값을 변경한다.
     * 
     * 1) 인자 deviceids 는 변경할 디바이스의 아이디로서 , 를 구분자로 여러개의 디바이스를 변경할 수 있다. 
     *  
     * 2) 인자 channelrangedata 는 디바이스의 채널별 범위값이다.
     * channelrangedata 는 JSON 형식의 문자열로 이루어져 있어야만 한다.
     * channelrangedata 형식은 아래와 같다.
     * 
     * [
     *   {
     *     "channelid": 1,
     *     "channelrangedata": "{\"min\":50,\"max\":80}"
     *   }, {
     *   	"channelid": 2,
     *   	"channelrangedata": "{\"min\":-30,\"max\":150}"
     *   }, {
     *   	"channelid": 3,
     *   	"channelrangedata": "{\"min\":-20,\"max\":90}"
     *   }
     * ]
     * 
     * @param deviceids 디바이스아이디(, 구분자로 이루어진 디바이스의 아이디들) 
     * @param channelrange JSON 형태의 문자열
     * @return
     */
 
   
    
    /**
     * 디바이스 일괄변경
     * 
     * 설정값을 변경할 디바이스아이디를 인자로 받아, 해당 디바이스의 설정값을 변경한다.
     * 
     * 1) 인자 deviceids 는 변경할 디바이스의 아이디로서 , 를 구분자로 여러개의 디바이스를 변경할 수 있다. 
     *  
     * 2) 인자 channelrangedata 는 디바이스의 채널별 범위값이다.
     * channelrangedata 는 JSON 형식의 문자열로 이루어져 있어야만 한다.
     * channelrangedata 형식은 아래와 같다.
     * 
     * [
     *   {
     *     "channelid": 1,
     *     "channelrangedata": "{\"min\":50,\"max\":80}"
     *   }, {
     *   	"channelid": 2,
     *   	"channelrangedata": "{\"min\":-30,\"max\":150}"
     *   }, {
     *   	"channelid": 3,
     *   	"channelrangedata": "{\"min\":-20,\"max\":90}"
     *   }
     * ] 
     * @param deviceids 디바이스아이디(, 구분자로 이루어진 디바이스의 아이디들) 
     * @param channelrange JSON 형태의 문자열
     * @return
     */
    @RequestMapping("update")
   	public ResponseObject update(String deviceids, String channelrange,String pushEnable,String pushInterval, String pushRepeat, 
   			String saveInterval, String pushSysEnable) {
    	
    	
       	ResponseObject ret = new ResponseObject();
        
       	if (deviceids == null || channelrange == null) {
       		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
       		return ret;
       	}
       	// 디바이스들의 채널정보 업데이트
       	String[] devicearr = deviceids.split(",");
       	ArrayList<DeviceVO> devicevoList = new ArrayList<>();
       	for (String deviceid : devicearr) {
       		//Integer devicetypecd = null;
       		DeviceVO vo = new DeviceVO();
       		vo.setDeviceid(deviceid);
       		
       		vo.setSaveinterval(Integer.parseInt(saveInterval));
       		vo.setPushenable(pushEnable);
       		vo.setPushsysenable(pushSysEnable);
       		vo.setPushinterval(Integer.parseInt(pushInterval));
       		vo.setPushrepeat(Integer.parseInt(pushRepeat));
       		
       		List<DeviceVO> devicetypecd;
			try {
				//devicetypecd = select devicetypecd from device where deviceid = #deviceid
				//devicetypecd = device.selectDevice(vo);
				// 수정 제대로 돌아가는지 체크
				//devicetypecd =  device.selectDevice(vo); // 디바이스 타입이 제대로 들어왔는지확인
				devicetypecd =  device.selectDeviceList(vo); // 디바이스 타입이 제대로 들어왔는지확인
				vo.setDevicetypecd(devicetypecd.get(0).getDevicetypecd()); // 전주임님은 deviceids와 channelrange 만 넘김으로 
				                                                    // update 함수는 devicechannelrange 테이블을 
																	// delete후  insert함으로 back단에서 스스로 디바이스타입을 셀렉트하여 가져와야한다.  
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}
       		devicevoList.add(vo);    	
       	}
       	if (devicevoList.size() == 0) {
       		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
       		return ret;
       	}
       	
       	ObjectMapper mapper = new ObjectMapper();
       	DeviceChannelRangeVO[] deviceChannelRangeVOList = null;
       	try {
       		deviceChannelRangeVOList = mapper.readValue(channelrange, DeviceChannelRangeVO[].class);
   		} catch (IOException e2) {
   			// TODO Auto-generated catch block
   			e2.printStackTrace();
   			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
   			return ret;
   		}
       	boolean b = false;
       	try {
   			b = device.updateChannelRange(devicevoList, deviceChannelRangeVOList);
   			/*device.updateChannelRange(devicevoList, deviceChannelRangeVOList);*/
   		} catch (Throwable e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
   			b = false;			
   		}
       	if (!b) {    		
       		ret.setReturnCode(StatusCode.ERROR_SERVICE);
       		return ret;
       	}
       	
       	//2018.12.05 김원태 추가 전체 업데이트 시에도 푸쉬 부분 , device 저장기간 업데이트 가능하게
       	for (DeviceVO vo : devicevoList) {
			if (vo.getDeviceid() == null || vo.getDevicetypecd() == null) {
				continue;
			}

			vo.setGroupid(null); //그룹아이디는 영향 안주기 위해 null로 넣어둔다, 여기서는 
			vo.setDevicetypecd(null);
			
	    	try {
				device.updateDevice(vo);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ret.setReturnCode(StatusCode.ERROR_SERVICE);
				return ret;
			}
		}
       	
       	ret.setReturnCode(StatusCode.OK);
       	
       	return ret;
    }
    

    @RequestMapping("set")
	public ResponseObject set(String deviceid, String devicename, 
			@RequestParam(value = "groupid", required = false) Integer groupid,
			String devicedesc,  String channelrange , Integer devicetypecd,String pushEnable,String pushInterval, String pushRepeat, String saveInterval) {
    	
    	
    	ResponseObject ret = new ResponseObject();
    	    	
    	ObjectMapper mapper = new ObjectMapper();
    	DeviceChannelRangeVO[] deviceChannelRangeVOList = null;
    	
    	try {
    		deviceChannelRangeVOList = mapper.readValue(channelrange, DeviceChannelRangeVO[].class);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_PARAMETER);
			return ret;
		}
    	    	    	
    	DeviceVO devicevo = new DeviceVO();
    	devicevo.setDeviceid(deviceid);
    	devicevo.setDevicetypecd(devicetypecd);

    	devicevo.setSaveinterval(Integer.parseInt(saveInterval));
    	devicevo.setPushenable(pushEnable);
    	devicevo.setPushinterval(Integer.parseInt(pushInterval));
    	devicevo.setPushrepeat(Integer.parseInt(pushRepeat));
    	
    	boolean b = false;
    	
    	try {
			b = device.updateChannelRange(devicevo, deviceChannelRangeVOList);
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			b = false;			
		}
    	
    	if (!b) {    		
    		ret.setReturnCode(StatusCode.ERROR_SERVICE);
    		return ret;
    	}
    	
    	DeviceVO vo = new DeviceVO();
    	
    	vo.setDeviceid(deviceid);  
    	
    	vo.setDevicedesc(devicedesc);    	
    	vo.setDevicename(devicename);
    	
    	vo.setSaveinterval(Integer.parseInt(saveInterval));
    	vo.setPushenable(pushEnable);
    	vo.setPushinterval(Integer.parseInt(pushInterval));
    	vo.setPushrepeat(Integer.parseInt(pushRepeat));
    	
    	
    	// groupid 의 경우 null 이면 변경 안함
    	if (groupid != null)    	
    		vo.setGroupid(groupid);    		
    	 
    	try {
			device.updateDevice(vo);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}	
    	
    	ret.setReturnCode(StatusCode.OK);
    	return ret;
	}
    
    @RequestMapping("sysPushSet")
	public ResponseObject sysPushSet(String deviceid, @RequestParam(value = "groupid", required = false) Integer groupid, 
			String pushSysEnable) {
    	
    	ResponseObject ret = new ResponseObject();
    	
    	if(deviceid == null || groupid == null || pushSysEnable == null) {
    		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
       		return ret;
    	}
    	
    	DeviceVO vo = new DeviceVO();
    	
    	vo.setDeviceid(deviceid);  
    	vo.setPushsysenable(pushSysEnable);
    	
    	// groupid 의 경우 null 이면 변경 안함
    	if (groupid != null)    	
    		vo.setGroupid(groupid);    		
    	 
    	try {
			device.updateSysPush(vo);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			return ret;
		}	
    	
    	ret.setReturnCode(StatusCode.OK);
    	return ret;
	}
    
    @RequestMapping("getSysPush")
	public ResponseObject getSystemPush(String deviceid) {
    	ResponseObject ret = new ResponseObject();
                
    	HashMap<String, Object> data = null;
    	
    	if(deviceid == null) {
    		ret.setReturnCode(StatusCode.ERROR_PARAMETER);
       		return ret;
    	}
    	
        // tune
        // deviceid 의 소유자가 현재 세션의 사용자인지 여부를 체크하는 로직이 필요하다.
    	try {
    		
    		data = device.getSystemPush(deviceid);
    		
		} catch (Throwable e) {
			ret.setReturnCode(StatusCode.ERROR_SERVICE);
			e.printStackTrace();
			return ret;
		}
    	
    	ret.setReturnCode(StatusCode.OK);    	
        ret.setData(data);
        
    	return ret;
	}    
    
    
    
    
}