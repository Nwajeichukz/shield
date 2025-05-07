package store.management.store_system.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import store.management.store_system.dto.AppResponse;
import store.management.store_system.dto.AuthenticationDto;
import store.management.store_system.dto.RegistrationDto;
import store.management.store_system.dto.UpdateProfile;
import store.management.store_system.entity.Role;
import store.management.store_system.entity.User;
import store.management.store_system.repository.RoleRepository;
import store.management.store_system.repository.UserRepository;
import store.management.store_system.service.JwtService;
import store.management.store_system.service.MyUserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final MyUserDetailsService myUserDetailsService;

    public AppResponse<String> createAccount(RegistrationDto registrationDto) {
        System.out.println(registrationDto.getDepartment() + "THIS SI THE DEPARTMENT");

        System.out.println(registrationDto.getEmail() + "this is the email");
        boolean check = userRepository.existsByEmail(registrationDto.getEmail());

        if(check) return new AppResponse<>(0, "user already exist");

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            return new AppResponse<>(-1, "Passwords do not match");
        }

        User user = new User();

        user.setEmail(registrationDto.getEmail());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        Role role = findRoleByDepartment(registrationDto.getDepartment());

        user.setRoles(role);

        User savedUser = userRepository.save(user);

        return new AppResponse<>(0, savedUser.getLastName() + " your account has been created");
    }


    public AppResponse<String> signIn(AuthenticationDto authenticationDto) {

        var user = myUserDetailsService.loadUserByUsername(authenticationDto.getEmail());

        if(!passwordEncoder.matches(authenticationDto.getPassword(), user.getPassword()))
            return new AppResponse<>(-1, "wrong email or password");

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtService.generateToken(user, roles);

        return new AppResponse<>(0, "successful signin", token);
    }

    private Role findRoleByDepartment(String department) {
        Optional<Role> role = roleRepository.findByName(department);

        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(department);

            return newRole;
        }

        return role.get();
    }


}
