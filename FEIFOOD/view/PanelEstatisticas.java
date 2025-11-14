package view;

import config.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PanelEstatisticas extends JPanel {
    private final DefaultTableModel bem  = tableModel();
    private final DefaultTableModel mal  = tableModel();
    private final DefaultTableModel tot  = new DefaultTableModel(new Object[]{"Tipo","Total"},0){
        @Override public boolean isCellEditable(int r,int c){return false;}
        @Override public Class<?> getColumnClass(int c){return c==1?Integer.class:String.class;}
    };

    public PanelEstatisticas(Object ignored){
        setLayout(new BorderLayout(8,8));
        var left = new JPanel(new GridLayout(1,2,8,8));
        left.add(box("Top 5 Bem Avaliados", new JTable(bem)));
        left.add(box("Top 5 Mal Avaliados", new JTable(mal)));
        add(left,BorderLayout.CENTER);
        add(box("Totais do Sistema", new JTable(tot)),BorderLayout.EAST);
        recarregar();
    }

    private static DefaultTableModel tableModel(){
        return new DefaultTableModel(new Object[]{"Alimento","Média","Qtd"},0){
            @Override public boolean isCellEditable(int r,int c){return false;}
            @Override public Class<?> getColumnClass(int c){return new Class[]{String.class,Double.class,Integer.class}[c];}
        };
    }
    private static JPanel box(String t,JComponent c){
        var p=new JPanel(new BorderLayout()); p.add(new JLabel(t,SwingConstants.CENTER),BorderLayout.NORTH); p.add(new JScrollPane(c)); return p;
    }

    public void recarregar(){
        fillTotais();
        fillTop(bem,true);
        fillTop(mal,false);
    }

    private void fillTotais(){
        tot.setRowCount(0);
        try (Connection c = ConnectionFactory.getConnection();
             Statement s = c.createStatement()) {
            tot.addRow(new Object[]{"Usuários",        int1(s,"SELECT COUNT(*) FROM feifood.usuario")});
            tot.addRow(new Object[]{"Estabelecimentos",int1(s,"SELECT COUNT(*) FROM feifood.estabelecimento")});
            tot.addRow(new Object[]{"Alimentos",       int1(s,"SELECT COUNT(*) FROM feifood.alimento WHERE ativo")});
        } catch (Exception e) { e.printStackTrace(); }
    }
    private static int int1(Statement s,String sql) throws SQLException { try(ResultSet r=s.executeQuery(sql)){ return r.next()? r.getInt(1):0; } }

    private void fillTop(DefaultTableModel model, boolean best){
        model.setRowCount(0);
        String sql = """
            SELECT a.nome, ROUND(AVG(av.estrelas)::numeric,1) AS media, COUNT(*) AS qtd
            FROM feifood.avaliacao av
            JOIN feifood.pedido p ON p.id=av.pedido_id
            JOIN feifood.pedido_item pi ON pi.pedido_id=p.id
            JOIN feifood.alimento a ON a.id=pi.alimento_id
            GROUP BY a.nome
            ORDER BY media %s, qtd DESC
            LIMIT 5
        """.formatted(best? "DESC":"ASC");
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) model.addRow(new Object[]{rs.getString(1), rs.getDouble(2), rs.getInt(3)});
        } catch (Exception e) { e.printStackTrace(); }
    }
}
