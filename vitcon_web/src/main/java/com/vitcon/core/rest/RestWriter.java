package com.vitcon.core.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitcon.openapi.response.ResponseObject;

@Component
public class RestWriter {

  public final void writeCode(HttpServletRequest request, HttpServletResponse response,
      String errorcode) throws IOException {
    ResponseObject obj = new ResponseObject();
    obj.setReturnCode(errorcode);

    writeObject(request, response, obj);
  }

  public final void writeObject(HttpServletRequest request, HttpServletResponse response,
      ResponseObject obj) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    response.setContentType("application/json;charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache");

    // 리턴 코드를 JSON Object 로 변환
    String ret = mapper.writeValueAsString(obj);
    response.getWriter().write(ret);
  }
  
}