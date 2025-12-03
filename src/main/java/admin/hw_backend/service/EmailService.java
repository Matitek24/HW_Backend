package admin.hw_backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Async
    public void sendProjectConfirmation(String recipientEmail, String name, UUID projectId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("Twoja wizualizacja HEADWEAR - Projekt #" + projectId.toString().substring(0, 8));

            String magicLink = frontendUrl + "/projekt/" + projectId;

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin: 0; padding: 0; background-color: #f3f4f6; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f3f4f6; padding: 40px 20px;  margin-top:40px; margin-bottom:40px;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                                    <tr>
                                        <td style="padding: 40px 30px;">
                                            <h1 style="margin: 0 0 20px 0; color: #1f2937; font-size: 24px; font-weight: 600;">
                                                Witaj %s!
                                            </h1>
                                            <p style="margin: 0 0 15px 0; color: #4b5563; font-size: 16px; line-height: 1.6;">
                                                Dziękujemy za stworzenie projektu w konfiguratorze HEADWEAR.
                                            </p>
                                            <p style="margin: 0 0 30px 0; color: #4b5563; font-size: 16px; line-height: 1.6;">
                                                Twoja konfiguracja została zapisana. Możesz do niej wrócić w każdej chwili:
                                            </p>
                                            <table width="100%%" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td align="center" style="padding: 0 0 30px 0;">
                                                        <a href="%s" style="display: inline-block; background-color: #1f2937; color: #ffffff; padding: 14px 32px; text-decoration: none; border-radius: 6px; font-weight: 600; font-size: 16px;">
                                                            Otwórz Projekt
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                            <div style="background-color: #f9fafb; padding: 15px; border-radius: 6px; border-left: 3px solid #1f2937;">
                                                <p style="margin: 0; color: #6b7280; font-size: 13px;">
                                                    <strong>Link do projektu:</strong><br>
                                                    <a href="%s" style="color: #1f2937; word-break: break-all;">%s</a>
                                                </p>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 20px 30px; background-color: #f9fafb; border-top: 1px solid #e5e7eb; border-radius: 0 0 8px 8px;">
                                            <p style="margin: 0; color: #9ca3af; font-size: 12px; text-align: center;">
                                                Wiadomość wygenerowana automatycznie przez system HEADWEAR
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(name, magicLink, magicLink, magicLink);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Wysłano email z projektem do: {}", recipientEmail);

        } catch (MessagingException e) {
            log.error("Błąd wysyłania maila do: " + recipientEmail, e);
        }
    }

    @Async
    public void sendPasswordResetLink(String recipientEmail, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("Reset hasła - HEADWEAR Configurator");

            String resetLink = frontendUrl + "/reset-password?token=" + token;

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin: 0; padding: 0; background-color: #f3f4f6; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f3f4f6; padding: 40px 20px; margin-top:40px; margin-bottom:40px;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                                    <tr>
                                        <td style="padding: 40px 30px;">
                                            <div style="text-align: center; margin-bottom: 30px;">
                                                <span style="font-size: 48px;"></span>
                                            </div>
                                            <h1 style="margin: 0 0 20px 0; color: #1f2937; font-size: 24px; font-weight: 600; text-align: center;">
                                                Zresetuj swoje hasło
                                            </h1>
                                            <p style="margin: 0 0 15px 0; color: #4b5563; font-size: 16px; line-height: 1.6;">
                                                Otrzymaliśmy prośbę o zmianę hasła do Twojego konta administratora.
                                            </p>
                                            <p style="margin: 0 0 30px 0; color: #4b5563; font-size: 16px; line-height: 1.6;">
                                                Kliknij przycisk poniżej, aby ustawić nowe hasło:
                                            </p>
                                            <table width="100%%" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td align="center" style="padding: 0 0 30px 0;">
                                                        <a href="%s" style="display: inline-block; background-color: #dc2626; color: #ffffff; padding: 14px 32px; text-decoration: none; border-radius: 6px; font-weight: 600; font-size: 16px;">
                                                            Zresetuj Hasło
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                            <div style="background-color: #fef2f2; padding: 15px; border-radius: 6px; border-left: 3px solid #dc2626; margin-bottom: 20px;">
                                                <p style="margin: 0; color: #991b1b; font-size: 13px;">
                                                    <strong>Ważne:</strong> Ten link wygaśnie za 15 minut
                                                </p>
                                            </div>
                                            <div style="background-color: #f9fafb; padding: 15px; border-radius: 6px;">
                                                <p style="margin: 0 0 8px 0; color: #6b7280; font-size: 13px;">
                                                    <strong>Lub skopiuj link:</strong>
                                                </p>
                                                <p style="margin: 0; font-size: 12px; word-break: break-all;">
                                                    <a href="%s" style="color: #dc2626;">%s</a>
                                                </p>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 20px 30px; background-color: #f9fafb; border-top: 1px solid #e5e7eb; border-radius: 0 0 8px 8px;">
                                            <p style="margin: 0; color: #9ca3af; font-size: 12px; text-align: center;">
                                                Jeśli to nie Ty prosiłeś o zmianę hasła, zignoruj tę wiadomość
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(resetLink, resetLink, resetLink);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Wysłano link resetujący hasło do: {}", recipientEmail);

        } catch (MessagingException e) {
            log.error("Błąd wysyłania maila resetującego do: " + recipientEmail, e);
        }
    }
}