package com.vitcon.service.mail;

import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	@Autowired
  private JavaMailSender sender;	
	
	public boolean sendEmail(EmailVO vo) throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        if (vo == null || vo.getText() == null ||
        		vo.getFrom() == null || vo.getTo() == null) {
        	return false;
        }
                
        helper.setTo(vo.getTo());
        helper.setFrom(vo.getFrom());
        helper.setSubject(vo.getSubject());
        
        if (vo.getHtml() != null) {
        	//helper.setText(vo.getText(), vo.getHtml());
        	helper.setText(vo.getText());        	
        } else {
        	helper.setText(vo.getText());
        }

        sender.send(message);
        
        return true;
	}

  public String makeRandomString() {
    Random rnd = new Random();

    StringBuffer buf = new StringBuffer();

    for (int i = 0; i < 10; i++) {
      // rnd.nextBoolean() 는 랜덤으로 true, false 를 리턴. true일 시 랜덤 한 소문자를, false 일 시 랜덤 한
      // 숫자를 StringBuffer 에 append 한다.
      if (rnd.nextBoolean()) {
        buf.append((char) ((int) (rnd.nextInt(26)) + 97));
      } else {
        buf.append((rnd.nextInt(10)));
      }
    }
    
    return buf.toString();
  }
}
