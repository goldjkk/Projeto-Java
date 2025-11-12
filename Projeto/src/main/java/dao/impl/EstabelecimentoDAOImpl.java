package dao.impl;
import config.ConnectionFactory; import dao.EstabelecimentoDAO; import model.Estabelecimento;
import java.sql.*; import java.util.*;
public class EstabelecimentoDAOImpl implements EstabelecimentoDAO {
    public void salvar(Estabelecimento e){
        String sql="INSERT INTO feifood.estabelecimento (nome,cnpj,cidade,ativo) VALUES (?,?,?,?)";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,e.getNome()); ps.setString(2,e.getCnpj()); ps.setString(3,e.getCidade()); ps.setBoolean(4,e.isAtivo());
            ps.executeUpdate(); try(ResultSet rs=ps.getGeneratedKeys()){ if(rs.next()) e.setId(rs.getInt(1)); }
        } catch(SQLException ex){ throw new RuntimeException("Erro ao salvar estabelecimento", ex); }
    }
    public void atualizar(Estabelecimento e){
        String sql="UPDATE feifood.estabelecimento SET nome=?, cnpj=?, cidade=?, ativo=? WHERE id=?";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,e.getNome()); ps.setString(2,e.getCnpj()); ps.setString(3,e.getCidade()); ps.setBoolean(4,e.isAtivo()); ps.setInt(5,e.getId());
            ps.executeUpdate();
        } catch(SQLException ex){ throw new RuntimeException("Erro ao atualizar estabelecimento", ex); }
    }
    public List<Estabelecimento> listarAtivos(){
        String sql="SELECT id,nome,cnpj,cidade,ativo FROM feifood.estabelecimento WHERE ativo=TRUE ORDER BY nome";
        List<Estabelecimento> list=new ArrayList<>();
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()){
            while(rs.next()){ Estabelecimento e=new Estabelecimento(); e.setId(rs.getInt("id")); e.setNome(rs.getString("nome")); e.setCnpj(rs.getString("cnpj")); e.setCidade(rs.getString("cidade")); e.setAtivo(rs.getBoolean("ativo")); list.add(e); }
        } catch(SQLException ex){ throw new RuntimeException("Erro ao listar estabelecimentos", ex); }
        return list;
    }
    public Optional<Estabelecimento> buscarPorId(int id){
        String sql="SELECT id,nome,cnpj,cidade,ativo FROM feifood.estabelecimento WHERE id=?";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()){
                if(!rs.next()) return Optional.empty(); Estabelecimento e=new Estabelecimento();
                e.setId(rs.getInt("id")); e.setNome(rs.getString("nome")); e.setCnpj(rs.getString("cnpj"));
                e.setCidade(rs.getString("cidade")); e.setAtivo(rs.getBoolean("ativo")); return Optional.of(e);
            }
        } catch(SQLException ex){ throw new RuntimeException("Erro ao buscar estabelecimento", ex); }
    }
}
