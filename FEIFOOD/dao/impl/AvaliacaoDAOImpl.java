package dao.impl;
import config.ConnectionFactory; import dao.AvaliacaoDAO; import model.Avaliacao;
import java.sql.*;
public class AvaliacaoDAOImpl implements AvaliacaoDAO {
    public void salvar(Avaliacao a){
        String sql="INSERT INTO feifood.avaliacao (pedido_id, estrelas, comentario) VALUES (?,?,?) ON CONFLICT (pedido_id) DO UPDATE SET estrelas=EXCLUDED.estrelas, comentario=EXCLUDED.comentario";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,a.getPedidoId()); ps.setInt(2,a.getEstrelas()); ps.setString(3,a.getComentario()); ps.executeUpdate();
        } catch(SQLException e){ throw new RuntimeException("Erro ao salvar avaliação", e); }
    }
}
