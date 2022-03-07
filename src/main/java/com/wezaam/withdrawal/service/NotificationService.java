package com.wezaam.withdrawal.service;


import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.wezaam.withdrawal.domain.entities.Employee;
import com.wezaam.withdrawal.domain.entities.WithdrawStatusEnum;
import com.wezaam.withdrawal.exception.NotificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class NotificationService {

    private final static String emailSubject = "the status of the withdrawal request";
    private final static String HTML_BODY = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "    <title> </title>\n" +
            "</head>\n" +
            "<body style=\"background: whitesmoke; padding: 30px; height: 100%\">\n" +
            "<h5 style=\"font-size: 18px; margin-bottom: 6px\">Hello,</h5>\n" +
            "<p style=\"font-size: 16px; font-weight: 500\"> the status of the withdrawal request </p>\n" +
            "<p> #STATUS# </p>\n" +
            "</body>\n" +
            "</html>";

    @Value("${notification.aws.email}")
    private String emailFrom;

    @Autowired
    private AmazonSimpleEmailService amazonSimpleEmailService;

    public void emailSender(Employee employee, WithdrawStatusEnum status) {
        final String body = HTML_BODY.replace("#STATUS#", status.name());
        try {
            log.info("sending notification.....");
            SendEmailRequest sendEmailRequest = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(employee.getEmail()))
                    .withMessage(new Message()
                            .withBody(new Body().withHtml(
                                    new Content().withCharset("UTF-8").withData(body)))
                            .withSubject(new Content().withCharset("UTF-8").withData(emailSubject)))
                    .withSource(emailFrom);
            amazonSimpleEmailService.sendEmail(sendEmailRequest);
            log.info("sent notification to {} ", employee.getEmail());
        } catch (Exception e) {
            log.info("error sending notification....");
            throw new NotificationException("Notification has been failed");
        }
    }

}
