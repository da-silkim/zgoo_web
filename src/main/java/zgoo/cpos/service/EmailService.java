package zgoo.cpos.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String USERNAME;

    @Async
    public void sendMailMessage(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(USERNAME);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        // javaMailSender.send(simpleMailMessage);

        try {
            MimeMessage message = createMessage(to, subject, text);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("메일 전송 실패: {}", e.getMessage(), e);
        }
    }

    public MimeMessage createMessage(String to, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.addRecipients(Message.RecipientType.TO, to);
        mimeMessage.setSubject(String.format("[(주)동아일렉콤] %s 이용약관 개정 안내", subject));
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String date = today.format(formatter);
    
        String text = "";
        text += "<div style=\"margin: 0 auto; font-family: Verdana, Geneva, Tahoma, sans-serif; font-size: 16px;\">";
        text += String.format("<p>안녕하세요. (주)동아일렉콤입니다.<br>%s 이용약관이 아래와 같이 개정됨을 알려드리오니 이용에 참고하여 주시기 바랍니다.</p><br><hr><br>", subject);
        text += String.format("<p><b>1. 개정약관</b><br><span style=\"padding-left: 10px;\">%s</span></p><br>", subject);
        text += "<p><b>2. 시행일자</b><br><span style=\"padding-left: 10px;\">2025년 6월 1일</span></p><br>";
        text += String.format("<p><b>3. 대상고객</b><br><span style=\"padding-left: 10px;\">%s 동의한 고객</span></p><br>", subject);
        text += "<p><b>4. 기존 고객에 대한 적용 여부</b><br><span style=\"padding-left: 10px;\">적용</span></p><br>";
        text += "<p><b>5. 약관변경 주요내용</b>";
        text += "<div>";
        text += "<table style=\"width: 800px; border-collapse: collapse; border: 1px solid #3d3b40;\">";
        text += "<thead>";
        text += "<tr style=\"background-color: #EAEAEA;\">";
        text += "<th style=\"border: 1px solid #3d3b40;\">약관명</th>";
        text += "<th style=\"border: 1px solid #3d3b40;\">주요 개정 내용</th></tr>";
        text += "</thead>";
        text += "<tbody>";
        text += "<tr><td style=\"border: 1px solid #3d3b40;\">";
        text += subject;
        text += "</td>";
        text += "<td style=\"border: 1px solid #3d3b40;\">";
        text += content;
        text += "</td></tr>";
        text += "</tbody>";
        text += "</table></div></p><br><hr>";
        text += "<p style=\"font-size: 14px;\">※ 약관 변경에 동의하지 않는 경우에는 시행일 전까지 계약을 해지할 수 있으며, 계약해지의 의사표시를 하지 아니한 경우에는 약관의 변경 내용에 동의한 것으로 봅니다.<br>";
        text += String.format("※ 본 메일은 %s 기준 이메일 정보가 등록된 고객님께 발송됩니다.</p><hr>", date);
        text += "</div>";
        mimeMessage.setText(text, "utf-8", "html");
        mimeMessage.setFrom(USERNAME);
        return mimeMessage;
    }
}
