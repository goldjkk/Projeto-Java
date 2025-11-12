package dao.impl;
import config.ConnectionFactory; import dao.PedidoDAO; import model.*;
import java.sql.*; import java.util.List;
public class PedidoDAOImpl implements PedidoDAO {
    public int criar(Pedido p){
        String sql="INSERT INTO feifood.pedido (usuario_id,status) VALUES (?,?)";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,p.getUsuario().getId()); ps.setString(2,p.getStatus()); ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys()){ if(rs.next()){ p.setId(rs.getInt(1)); return p.getId(); } }
        } catch(SQLException e){ throw new RuntimeException("Erro ao criar pedido", e); }
        throw new RuntimeException("Falha ao obter ID do pedido");
    }
    public void atualizarStatus(int pedidoId,String status){
        String sql="UPDATE feifood.pedido SET status=?, atualizado_em=NOW() WHERE id=?";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,status); ps.setInt(2,pedidoId); ps.executeUpdate(); }
        catch(SQLException e){ throw new RuntimeException("Erro ao atualizar status do pedido", e); }
    }
    public void remover(int pedidoId){
        String sql="DELETE FROM feifood.pedido WHERE id=?";
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setInt(1,pedidoId); ps.executeUpdate(); }
        catch(SQLException e){ throw new RuntimeException("Erro ao excluir pedido", e); }
    }
    public void salvarItens(int pedidoId, List<PedidoItem> itens){
        String del="DELETE FROM feifood.pedido_item WHERE pedido_id=?";
        String ins="INSERT INTO feifood.pedido_item (pedido_id, alimento_id, quantidade, preco_unitario) VALUES (?,?,?,?)";
        try(Connection c=ConnectionFactory.getConnection()){
            c.setAutoCommit(false);
            try(PreparedStatement d=c.prepareStatement(del)){ d.setInt(1,pedidoId); d.executeUpdate(); }
            try(PreparedStatement i=c.prepareStatement(ins)){
                for(PedidoItem item: itens){
                    i.setInt(1,pedidoId); i.setInt(2,item.getAlimento().getId());
                    i.setInt(3,item.getQuantidade()); i.setDouble(4,item.getPrecoUnitario()); i.addBatch();
                }
                i.executeBatch();
            }
            c.commit();
        } catch(SQLException e){ throw new RuntimeException("Erro ao salvar itens do pedido", e); }
    }
}
