package admin.hw_backend.controller;

import admin.hw_backend.entity.Role; // Zaimportuj swoją encję Role
import admin.hw_backend.repository.RoleRepository; // Zaimportuj swoje repozytorium
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicTestController {

    private final RoleRepository roleRepository;

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getPublicRolesList() {

        List<Role> roles = roleRepository.findAll();

        List<String> roleNames = roles.stream()
                .map(Role::getNazwaRoli)
                .collect(Collectors.toList());


        return ResponseEntity.ok(roleNames);
    }
}