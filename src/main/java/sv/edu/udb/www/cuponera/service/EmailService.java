package sv.edu.udb.www.cuponera.service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/*
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
*/
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import sv.edu.udb.www.cuponera.utils.Mail;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender emailSender;
	
	public void SendSimpleMessage(String to, String subject, String body){
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			helper.setFrom("ezic2017@gmail.com");
			
			emailSender.send(mimeMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
