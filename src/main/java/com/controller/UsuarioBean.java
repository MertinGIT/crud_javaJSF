package com.controller;

import com.dao.UsuarioDao;
import com.model.Persona;
import com.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

@ManagedBean(name = "usuarioBean")
@SessionScoped
public class UsuarioBean {
    public String getEmail() {
        return email;
    }

    @Inject
    private UsuarioDao usuarioDao;

    public void setEmail(String email) {
        this.email = email;
    }

    private Usuario usuario = new Usuario();
    private String mensaje, password, email, confirmPassword;
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    // Registro de usuario
    public String registrar() {
        if (!usuario.getPassword().equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Las contraseñas no coinciden", "Las contraseñas no coinciden."));
            return null;
        }

        try {
            // Validar si el usuario o email ya existen
            if (usuarioDao.buscarPorUsername(usuario.getUsername()) != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "El nombre de usuario ya está en uso", "El nombre de usuario ya está en uso."));
                return null;
            }

            if (usuarioDao.buscarPorEmail(usuario.getEmail()) != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "El email ya está registrado", "El email ya está registrado."));
                return null;
            }

            // Encriptar la contraseña antes de guardarla
            String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
            usuario.setPassword(hashedPassword);

            // Guardar el usuario
            usuarioDao.guardar(usuario);

            // Redirigir al login
            return "login?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar el usuario", "Error al registrar el usuario."));
            return null;
        }
    }


    // Login de usuario
    public String login() {
        Usuario usuarioEncontrado = usuarioDao.buscarPorUsername(usuario.getUsername());
        if (usuarioEncontrado != null && BCrypt.checkpw(usuario.getPassword(), usuarioEncontrado.getPassword())) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuarioEncontrado);
            return "index?faces-redirect=true"; // Redirige a una página de inicio
        } else {
            // Agregar mensaje de error si las credenciales no son válidas
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña incorrectos", "Usuario o contraseña incorrectos."));
            return null;
        }
    }

    public void verificarSesion() throws IOException {
        Usuario usuarioSesion = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        if (usuarioSesion == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }
}
