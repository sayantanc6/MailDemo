package dummy.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@RestController
public class MailController {
	
	@Autowired
	JavaMailSenderImpl sender;
	
	@Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

	
	@GetMapping("/sendmimemessageMail")
	public String sendmimemessageMail() {
		sender.send(new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			     message.setFrom("sayantanc6@mail.com");
			     message.setTo("sayantanc6@gmail.com");
			     message.setSubject("my subject");
			     message.setText("my text", true);
			     message.setText("my text <img src='cid:myLogo'>", true);
			     message.addInline("myLogo", new ClassPathResource("static/logo-social-sq.png"));
			     message.addAttachment("books.csv", new ClassPathResource("static/books.csv"));
			}
		});
		return "success";
	}
	
	@GetMapping("/sendsimplemailmessage")
	public String sendsimplemailmessage() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("sayantanc6@gmail.com");
		message.setTo("sayantanc6@gmail.com");
		message.setSubject("This is a plain text email");
		message.setText("Hello guys! This is a plain text email.");
		sender.send(message);
		
		return "success";
	}
	
	
	@GetMapping("/sendhtmlmessage")
	public String sendhtmlmessage() throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setSubject("my subject"); 
		helper.setFrom("sayantanc6@gmail.com");
		helper.setTo("sayantanc6@gmail.com"); 
		boolean html = true;
		helper.setText("<b>Hey guys</b>,<br><i>Welcome to my new home</i>", html); 
		sender.send(message);
		 
		return "success";
	}
	
	
	@GetMapping("/templatesend")
	public void templatesend() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException, MessagingException {
		Map<String, Object> templateModel = new HashMap<String, Object>();
		templateModel.put("recipientName", "Sambeet");
		templateModel.put("text", "my sam text");
		templateModel.put("senderName", "Sayantan");
		Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("template-freemarker.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("sayantanc6@gmail.com");
        helper.setTo("sayantanc6@gmail.com");
        helper.setSubject("my subject");
        helper.setText(htmlBody, true);
        helper.addInline("logo-social-sq.png", new ClassPathResource("static/logo-social-sq.png"));
        sender.send(message);
	}
}