package sit.int221.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sit.int221.entities.Announcement;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final String BASE_URL = "https://intproj22.sit.kmutt.ac.th/kw2";

    private final JavaMailSender javaMailSender;

    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text) {
        try {
            MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage(), true, "UTF-8");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text, true);
            javaMailSender.send(message.getMimeMessage());
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public void sendSubscriberEmail(String email, Announcement announcement) {
        String text = getAnnouncementTemplate(email, announcement.getAnnouncementDescription(), announcement.getId(), announcement.getCategory().getId());
        sendEmail(email, announcement.getAnnouncementTitle(), text);
    }

    public String getAnnouncementTemplate(String email, String announcementDescription, Integer announcementId, Integer categoryId) {
        return announcementDescription + "<br><br>" +
                "<a href=\"" + BASE_URL + "/announcement/" + announcementId + "\">Announcement Link</a>" +
                "<br>" +
                "<a href=\"" + BASE_URL + "/subscription/unsubscribe?email=" + email + "&categoryId=" + categoryId + "\">Unsubscribe Link</a>";
    }

    public String getOtpTemplate(String otp) {
        return "<div style=\"background-color: #f2f2f2; padding: 50px;\">\n" +
                "    <div style=\"background-color: white; padding: 50px; border-radius: 10px;\">\n" +
                "        <h1 style=\"text-align: center; color: #ff6f00;\">OTP Verification</h1>\n" +
                "        <p style=\"text-align: center; color: #ff6f00;\">Your OTP is <b>" + otp + "</b></p>\n" +
                "        <p style=\"text-align: center; color: #ff6f00;\">Please enter this OTP to verify your email.</p>\n" +
                "        <p style=\"text-align: center; color: #ff6f00;\">This OTP will expire in 5 minutes.</p>\n" +
                "        <p style=\"text-align: center; color: #ff6f00;\">Thank you for using our service.</p>\n" +
                "    </div>\n" +
                "</div>";
    }

    public String getOtpResponseTemplate(String categoryName) {
        return "<div style=\"background-color: #f2f2f2; padding: 50px;\">\n" +
                "    <div style=\"background-color: white; padding: 50px; border-radius: 10px;\">\n" +
                "        <h1 style=\"text-align: center; color: #ff6f00;\">You have successfully subscribed to category <b>" + categoryName + "</b></h1>\n" +
                "        <p style=\"text-align: center; color: #ff6f00;\">Thank you for using our service.</p>\n" +
                "    </div>\n" +
                "</div>";
    }
}
