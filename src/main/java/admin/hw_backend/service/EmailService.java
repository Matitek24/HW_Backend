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

    @Async // To sprawia, że metoda leci w tle
    public void sendProjectConfirmation(String recipientEmail, String name, UUID projectId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("Twoja wizualizacja HEADWEAR - Projekt #" + projectId.toString().substring(0, 8));

            // Generujemy Magiczny Link
            String magicLink = frontendUrl + "/projekt/" + projectId;

            // Treść HTML
            String htmlContent = """
                <html>
                <body>
                    <h2>Witaj %s!</h2>
                    <p>Dziękujemy za stworzenie projektu w konfiguratorze HEADWEAR.</p>
                    <p>Twoja konfiguracja została zapisana. Możesz do niej wrócić w każdej chwili, klikając w poniższy link:</p>
                    <br>
                    <a href="%s" style="background-color: #1f2937; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                        Otwórz Projekt
                    </a>
                    <br><br>
                    <p>Lub skopiuj ten adres: <br> %s</p>
                    <hr>
                    <p style="font-size: 12px; color: gray;">Wiadomość wygenerowana automatycznie.</p>
                </body>
                </html>
                """.formatted(name, magicLink, magicLink);

            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
            log.info("Wysłano email z projektem do: {}", recipientEmail);

        } catch (MessagingException e) {
            log.error("Błąd wysyłania maila do: " + recipientEmail, e);
        }
    }
}