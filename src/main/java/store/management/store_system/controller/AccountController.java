package store.management.store_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import store.management.store_system.dto.AppResponse;
import store.management.store_system.dto.UpdateProfile;
import store.management.store_system.service.account.UserAccountService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final UserAccountService userAccountService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all-staff")
    public ResponseEntity<AppResponse<Map<String, Object>>> getAllStaff(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ){
       return ResponseEntity.ok(userAccountService.getAllStaff(PageRequest.of(page, size)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AppResponse<String>> updateProfile(@PathVariable long id, @RequestBody UpdateProfile updateProfile){
        return ResponseEntity.ok(userAccountService.updateProfile(updateProfile, id));
    }

    
}