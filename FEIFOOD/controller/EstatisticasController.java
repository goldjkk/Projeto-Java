package controller;
import config.ConnectionFactory; import java.sql.*; import javax.swing.table.DefaultTableModel;
public class EstatisticasController {
    public DefaultTableModel topBemAvaliados(){
        String sql = "SELECT a.nome, ROUND(AVG(av.estrelas)::numeric,2) AS media, COUNT(*) qtd FROM feifood.avaliacao av JOIN feifood.pedido p ON p.id=av.pedido_id JOIN feifood.pedido_item pi ON pi.pedido_id=p.id JOIN feifood.alimento a ON a.id=pi.alimento_id GROUP BY a.nome ORDER BY media DESC, qtd DESC LIMIT 5";
        return tabela(sql, new String[]{ "Alimento","Média","Qtd" });
    }
    public DefaultTableModel topMalAvaliados(){
        String sql = "SELECT a.nome, ROUND(AVG(av.estrelas)::numeric,2) AS media, COUNT(*) qtd FROM feifood.avaliacao av JOIN feifood.pedido p ON p.id=av.pedido_id JOIN feifood.pedido_item pi ON pi.pedido_id=p.id JOIN feifood.alimento a ON a.id=pi.alimento_id GROUP BY a.nome ORDER BY media ASC, qtd DESC LIMIT 5";
        return tabela(sql, new String[]{ "Alimento","Média","Qtd" });
    }
    public DefaultTableModel totais(){
        String sql = "SELECT 'Usuarios' as tipo, COUNT(*) as total FROM feifood.usuario UNION ALL SELECT 'Estabelecimentos', COUNT(*) FROM feifood.estabelecimento UNION ALL SELECT 'Alimentos', COUNT(*) FROM feifood.alimento";
        return tabela(sql, new String[]{ "Tipo","Total" });
    }
    private DefaultTableModel tabela(String sql, String[] cols){
        DefaultTableModel model=new DefaultTableModel(cols,0){ public boolean isCellEditable(int r,int c){return false;} };
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()){
            int n=rs.getMetaData().getColumnCount(); while(rs.next()){ Object[] row=new Object[n]; for(int i=1;i<=n;i++) row[i-1]=rs.getObject(i); model.addRow(row); }
        } catch(SQLException e){ throw new RuntimeException("Erro ao carregar estatísticas", e); }
        return model;
    }
}
