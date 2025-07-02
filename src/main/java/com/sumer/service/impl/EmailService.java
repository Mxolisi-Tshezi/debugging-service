package com.sumer.service.impl;

import com.sumer.dto.RiskDtos;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendQuoteEmail(RiskDtos quote) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo("davymbaimbai@gmail.com"); // Replace with actual recipient
            helper.setSubject("Your Insurance Quote");
            helper.setText(buildEmailBody(quote), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildEmailBody(RiskDtos quote) {
        return "<h1>Your Insurance Quote</h1>" +
                "<p><strong>Address:</strong> " + quote.getAddressLine1() + "</p>" +
                "<p><strong>Sum Insured:</strong> $" + quote.getSumInsured() + "</p>";
    }
}
