package com.abarrotes.pos.service;

import com.abarrotes.pos.exception.ApiException;
import com.abarrotes.pos.model.dto.LoginRequest;
import com.abarrotes.pos.model.dto.LoginResponse;
import com.abarrotes.pos.model.entity.Usuario;
import com.abarrotes.pos.model.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    public AuthService(UsuarioRepository usuarioRepository) { this.usuarioRepository = usuarioRepository; }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(request.username())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado", "No existe usuario activo: " + request.username()));
        if (!usuario.password.equals(request.password())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta", "Las credenciales no son válidas");
        }
        return new LoginResponse(usuario.idUsuario, usuario.nombre, usuario.rol.nombre, usuario.username);
    }
}
