package admin.hw_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class SecureTestController {

    @GetMapping("/secure-data")
    public String getSecureData() {
        return "Dane z chronionego API, jeśli zobaczysz to, uwierzytelnienie działa!";
    }
}