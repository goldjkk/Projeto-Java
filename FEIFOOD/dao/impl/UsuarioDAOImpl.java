package dao.impl;
import config.ConnectionFactory; import dao.UsuarioDAO; import model.Usuario;
import java.sql.*; import java.util.Optional;
public class UsuarioDAOImpl implements UsuarioDAO {
    public void salvar(Usuario u){
        String sql="INSERT INTO feifood.usuario (nome,email,senha_hash,perfil) VALUES (?,?,?,?)";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,u.getNome()); ps.setString(2,u.getEmail()); ps.setString(3,u.getSenhaHash()); ps.setString(4,u.getPerfil().name());
            ps.executeUpdate(); try(ResultSet rs=ps.getGeneratedKeys()){ if(rs.next()) u.setId(rs.getInt(1)); }
        } catch(SQLException e){ throw new RuntimeException("Erro ao salvar usuário", e); }
    }
    public Optional<Usuario> buscarPorEmail(String email){
        String sql="SELECT id,nome,email,senha_hash,perfil FROM feifood.usuario WHERE email=?";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,email); try(ResultSet rs=ps.executeQuery()){
                if(!rs.next()) return Optional.empty();
                Usuario u=new Usuario(); u.setId(rs.getInt("id")); u.setNome(rs.getString("nome")); u.setEmail(rs.getString("email"));
                u.setSenhaHash(rs.getString("senha_hash")); u.setPerfil(Usuario.Perfil.valueOf(rs.getString("perfil"))); return Optional.of(u);
            }
        } catch(SQLException e){ throw new RuntimeException("Erro ao buscar usuário", e); }
    }
}
