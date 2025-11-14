package view;

import config.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.sql.*;

public class PanelPedido extends JPanel {
    // busca
    private final JTextField filtro = new JTextField(28);
    private final DefaultTableModel mbusca = new DefaultTableModel(
        new Object[]{"ID","Nome","Descrição","Preço","Tipo","Estabelecimento"},0){
        @Override public boolean isCellEditable(int r,int c){return false;}
        @Override public Class<?> getColumnClass(int c){return new Class[]{Integer.class,String.class,String.class,Double.class,String.class,String.class}[c];}
    };
    private final JTable tBusca = new JTable(mbusca);

    // carrinho
    private final DefaultTableModel mitem = new DefaultTableModel(
        new Object[]{"ID","Nome","Qtd","Preço Unit.","Subtotal"},0){
        @Override public boolean isCellEditable(int r,int c){return c==2;}
        @Override public Class<?> getColumnClass(int c){return new Class[]{Integer.class,String.class,Integer.class,Double.class,Double.class}[c];}
    };
    private final JTable tItem = new JTable(mitem);
    private final JLabel total = new JLabel("Total: R$ 0,00");

    // ações
    private final JButton novo = new JButton("Novo Pedido");
    private final JButton add  = new JButton("Adicionar");
    private final JButton rem  = new JButton("Remover");
    private final JButton save = new JButton("Salvar Itens");
    private final JButton del  = new JButton("Excluir Pedido");
    private final JButton rate = new JButton("Avaliar (0-5)");

    private Integer usuarioId, pedidoId;
    private final Runnable onChange;

    public PanelPedido(Integer uid, Runnable onChange){
        this.usuarioId = uid==null?1:uid; this.onChange = onChange;
        setLayout(new BorderLayout(8,8));

        var bar = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        for (var b : new JButton[]{novo,add,rem,save,del,rate}) bar.add(b);
        var top = new JPanel(); var buscar = new JButton("Buscar");
        buscar.addActionListener(e -> buscar()); top.add(new JLabel("Nome:")); top.add(filtro); top.add(buscar);

        var north = new JPanel(new BorderLayout()); north.add(bar,BorderLayout.NORTH); north.add(top,BorderLayout.SOUTH);
        add(north,BorderLayout.NORTH); add(new JScrollPane(tBusca),BorderLayout.CENTER);

        var south = new JPanel(new BorderLayout());
        south.add(new JSeparator(),BorderLayout.NORTH);
        south.add(new JScrollPane(tItem),BorderLayout.CENTER);
        var rod = new JPanel(new FlowLayout(FlowLayout.RIGHT)); rod.add(total); south.add(rod,BorderLayout.SOUTH);
        add(south,BorderLayout.SOUTH);

        novo.addActionListener(e -> novoPedido());
        add .addActionListener(e -> addItem());
        rem .addActionListener(e -> remItem());
        save.addActionListener(e -> salvar());
        del .addActionListener(e -> excluir());
        rate.addActionListener(e -> avaliar());

        tBusca.addMouseListener(new MouseAdapter(){ @Override public void mouseClicked(java.awt.event.MouseEvent e){ if(e.getClickCount()==2 && add.isEnabled()) addItem(); }});
        mitem.addTableModelListener(e -> { if(e.getColumn()==2) recalcular(); });

        setEnabledPedido(false);
        SwingUtilities.invokeLater(this::buscar);
    }

    private void setEnabledPedido(boolean on){
        novo.setEnabled(!on); add.setEnabled(on); rem.setEnabled(on); save.setEnabled(on); del.setEnabled(on); rate.setEnabled(on && mitem.getRowCount()>0);
    }

    // -------- busca ----------
    private void buscar(){
        mbusca.setRowCount(0);
        String sql = """
          SELECT a.id,a.nome,a.descricao,a.preco,a.tipo,e.nome
          FROM feifood.alimento a
          LEFT JOIN feifood.estabelecimento e ON e.id=a.estabelecimento_id
          WHERE a.ativo AND LOWER(a.nome) LIKE LOWER(?)
          ORDER BY a.nome LIMIT 200
        """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,"%"+filtro.getText().trim()+"%");
            try(ResultSet r=ps.executeQuery()){
                while(r.next()) mbusca.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getDouble(4),r.getString(5),r.getString(6)});
            }
        } catch(Exception ex){ erro("Buscar",ex); }
    }

    // -------- pedido ----------
    private void novoPedido(){
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO feifood.pedido (usuario_id,criado_em) VALUES (?,now()) RETURNING id")){
            ps.setInt(1,usuarioId);
            try(ResultSet r=ps.executeQuery()){ if(r.next()) pedidoId=r.getInt(1); }
            mitem.setRowCount(0); setEnabledPedido(true);
            JOptionPane.showMessageDialog(this,"Pedido #"+pedidoId+" aberto.");
        } catch(Exception ex){ erro("Novo pedido",ex); }
    }

    private void addItem(){
        int i = tBusca.getSelectedRow(); if(i<0){ msg("Selecione um alimento."); return; }
        Integer id = (Integer)mbusca.getValueAt(i,0);
        Double  pr = (Double) mbusca.getValueAt(i,3);
        for(int r=0;r<mitem.getRowCount();r++){
            if(id.equals(mitem.getValueAt(r,0))){ mitem.setValueAt((Integer)mitem.getValueAt(r,2)+1,r,2); recalcular(); rate.setEnabled(true); return; }
        }
        mitem.addRow(new Object[]{id, mbusca.getValueAt(i,1), 1, pr, pr});
        recalcular(); rate.setEnabled(true);
    }

    private void remItem(){
        int i=tItem.getSelectedRow(); if(i>=0){ mitem.removeRow(i); recalcular(); rate.setEnabled(mitem.getRowCount()>0); }
    }

    private void recalcular(){
        double tot=0; for(int r=0;r<mitem.getRowCount();r++){
            int q=(Integer)mitem.getValueAt(r,2); double p=(Double)mitem.getValueAt(r,3), s=q*p;
            mitem.setValueAt(s,r,4); tot+=s;
        }
        total.setText(String.format("Total: R$ %.2f",tot));
    }

    private boolean salvar(){
        if(pedidoId==null){ msg("Abra um pedido primeiro."); return false; }
        if(mitem.getRowCount()==0){ msg("Adicione itens antes de salvar."); return false; }
        String sql = """
          INSERT INTO feifood.pedido_item (pedido_id,alimento_id,quantidade,preco_unitario)
          VALUES (?,?,?,?)
          ON CONFLICT (pedido_id,alimento_id) DO UPDATE
          SET quantidade = feifood.pedido_item.quantidade + EXCLUDED.quantidade,
              preco_unitario = EXCLUDED.preco_unitario
        """;
        try (Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            for(int r=0;r<mitem.getRowCount();r++){
                ps.setInt(1,pedidoId);
                ps.setInt(2,(Integer)mitem.getValueAt(r,0));
                ps.setInt(3,(Integer)mitem.getValueAt(r,2));
                ps.setDouble(4,(Double)mitem.getValueAt(r,3));
                ps.addBatch();
            }
            ps.executeBatch();
            msg("Itens salvos!");
            if(onChange!=null) onChange.run();
            return true;
        } catch(Exception ex){ erro("Salvar itens",ex); return false; }
    }

    private void excluir(){
        if(pedidoId==null) return;
        if(JOptionPane.showConfirmDialog(this,"Excluir este pedido?","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        try (Connection c=ConnectionFactory.getConnection()){
            for (var sql : new String[]{
                "DELETE FROM feifood.pedido_item WHERE pedido_id=?",
                "DELETE FROM feifood.avaliacao  WHERE pedido_id=?",
                "DELETE FROM feifood.pedido     WHERE id=?"
            }){
                try (PreparedStatement ps=c.prepareStatement(sql)){ ps.setInt(1,pedidoId); ps.executeUpdate(); }
            }
            mitem.setRowCount(0); pedidoId=null; setEnabledPedido(false);
            if(onChange!=null) onChange.run();
        } catch(Exception ex){ erro("Excluir pedido",ex); }
    }

    private void avaliar(){
        if(pedidoId==null || mitem.getRowCount()==0){ msg("Adicione itens e salve antes de avaliar."); return; }
        if(!salvar()) return; // garante persistência antes do join das estatísticas
        var s = JOptionPane.showInputDialog(this,"Nota (0–5):","Avaliar",JOptionPane.QUESTION_MESSAGE);
        if(s==null) return;
        int nota; try{ nota=Integer.parseInt(s.trim()); if(nota<0||nota>5) throw new NumberFormatException(); } catch(Exception e){ msg("Informe um número entre 0 e 5."); return; }
        String sql = """
          INSERT INTO feifood.avaliacao (pedido_id,estrelas,comentario,criado_em)
          VALUES(?, ?, NULL, now())
          ON CONFLICT (pedido_id) DO UPDATE SET estrelas=EXCLUDED.estrelas, comentario=EXCLUDED.comentario, criado_em=now()
        """;
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,pedidoId); ps.setInt(2,nota); ps.executeUpdate();
            msg("Avaliação registrada!");
            if(onChange!=null) onChange.run();
        } catch(Exception ex){ erro("Avaliar",ex); }
    }

    // util
    private void msg(String s){ JOptionPane.showMessageDialog(this,s); }
    private void erro(String a, Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Erro ao "+a+": "+e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE); }
}
