package controller;
import dao.UsuarioDAO; import model.Usuario;
import java.util.Optional;
public class AuthController {
    private final UsuarioDAO usuarioDAO;
    public AuthController(UsuarioDAO usuarioDAO){ this.usuarioDAO=usuarioDAO; }
    public Optional<Usuario> login(String email,String senha){ return usuarioDAO.buscarPorEmail(email).filter(u->u.getSenhaHash().equals(hash(senha))); }
    public void registrar(String nome,String email,String senha){ Usuario u=new Usuario(); u.setNome(nome); u.setEmail(email); u.setSenhaHash(hash(senha)); u.setPerfil(Usuario.Perfil.CLIENTE); usuarioDAO.salvar(u); }
    private String hash(String s){ return Integer.toHexString(s.hashCode()); }
}
