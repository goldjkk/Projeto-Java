package dao; import model.Usuario; import java.util.Optional; public interface UsuarioDAO { void salvar(Usuario u); Optional<Usuario> buscarPorEmail(String email); }
