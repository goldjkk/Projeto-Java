package dao.impl;
import config.ConnectionFactory; import dao.AlimentoDAO; import model.*;
import java.sql.*; import java.util.*;
public class AlimentoDAOImpl implements AlimentoDAO {
    private Estabelecimento mapEstabelecimento(ResultSet rs) throws SQLException { Estabelecimento e=new Estabelecimento(); e.setId(rs.getInt("estabelecimento_id")); e.setNome(rs.getString("estabelecimento_nome")); return e; }
    private Alimento mapAlimento(ResultSet rs) throws SQLException {
        String tipo=rs.getString("tipo"); Alimento a;
        if ("BEBIDA".equals(tipo)) { Bebida b=new Bebida(); b.setTeorAlcoolico(rs.getObject("teor_alcoolico")==null?0.0:rs.getDouble("teor_alcoolico")); a=b; }
        else { Comida c=new Comida(); c.setVegetariano(rs.getObject("vegetariano")!=null && rs.getBoolean("vegetariano")); a=c; }
        a.setId(rs.getInt("id")); a.setNome(rs.getString("nome")); a.setDescricao(rs.getString("descricao")); a.setPreco(rs.getDouble("preco")); a.setAtivo(rs.getBoolean("ativo")); a.setEstabelecimento(mapEstabelecimento(rs)); return a;
    }
    public void salvar(Alimento a){
        String sql="INSERT INTO feifood.alimento (estabelecimento_id,nome,descricao,preco,tipo,vegetariano,teor_alcoolico,ativo) VALUES (?,?,?,?,?,?,?,?)";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,a.getEstabelecimento().getId()); ps.setString(2,a.getNome()); ps.setString(3,a.getDescricao()); ps.setDouble(4,a.getPreco()); ps.setString(5,a.getTipo());
            if(a instanceof Comida){ ps.setObject(6, ((Comida)a).isVegetariano()); ps.setNull(7,Types.NUMERIC);} else { ps.setNull(6,Types.BOOLEAN); ps.setObject(7, ((Bebida)a).getTeorAlcoolico()); }
            ps.setBoolean(8,a.isAtivo()); ps.executeUpdate(); try(ResultSet rs=ps.getGeneratedKeys()){ if(rs.next()) a.setId(rs.getInt(1)); }
        } catch(SQLException e){ throw new RuntimeException("Erro ao salvar alimento", e); }
    }
    public void atualizar(Alimento a){
        String sql="UPDATE feifood.alimento SET estabelecimento_id=?, nome=?, descricao=?, preco=?, tipo=?, vegetariano=?, teor_alcoolico=?, ativo=? WHERE id=?";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,a.getEstabelecimento().getId()); ps.setString(2,a.getNome()); ps.setString(3,a.getDescricao()); ps.setDouble(4,a.getPreco()); ps.setString(5,a.getTipo());
            if(a instanceof Comida){ ps.setObject(6, ((Comida)a).isVegetariano()); ps.setNull(7,Types.NUMERIC);} else { ps.setNull(6,Types.BOOLEAN); ps.setObject(7, ((Bebida)a).getTeorAlcoolico()); }
            ps.setBoolean(8,a.isAtivo()); ps.setInt(9,a.getId()); ps.executeUpdate();
        } catch(SQLException e){ throw new RuntimeException("Erro ao atualizar alimento", e); }
    }
    public Optional<Alimento> buscarPorId(int id){
        String sql = "SELECT a.*, e.nome AS estabelecimento_nome FROM feifood.alimento a JOIN feifood.estabelecimento e ON e.id=a.estabelecimento_id WHERE a.id=?";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()){ if(!rs.next()) return Optional.empty(); return Optional.of(mapAlimento(rs)); }
        } catch(SQLException e){ throw new RuntimeException("Erro ao buscar alimento", e); }
    }
    public List<Alimento> buscarPorNome(String termo,int limit,int offset){
        String sql = "SELECT a.*, e.nome AS estabelecimento_nome FROM feifood.alimento a JOIN feifood.estabelecimento e ON e.id=a.estabelecimento_id WHERE a.ativo=TRUE AND LOWER(a.nome) LIKE LOWER(?) ORDER BY a.nome LIMIT ? OFFSET ?";
        List<Alimento> list=new ArrayList<>();
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,"%"+termo+"%"); ps.setInt(2,limit); ps.setInt(3,offset);
            try(ResultSet rs=ps.executeQuery()){ while(rs.next()) list.add(mapAlimento(rs)); }
        } catch(SQLException e){ throw new RuntimeException("Erro ao buscar alimentos", e); }
        return list;
    }
}
