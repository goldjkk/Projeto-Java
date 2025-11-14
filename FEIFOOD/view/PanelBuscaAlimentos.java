package view;

import dao.impl.AlimentoDAOImpl;
import model.Alimento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelBuscaAlimentos extends JPanel {
    private final JTextField busca = new JTextField(30);
    private final DefaultTableModel m = new DefaultTableModel(
        new Object[]{"ID","Nome","Descrição","Preço","Tipo","Estabelecimento"},0){
        @Override public boolean isCellEditable(int r,int c){ return false; }
        @Override public Class<?> getColumnClass(int c){ return new Class[]{Integer.class,String.class,String.class,Double.class,String.class,String.class}[c]; }
    };
    private final AlimentoDAOImpl dao = new AlimentoDAOImpl();

    public PanelBuscaAlimentos(){
        setLayout(new BorderLayout());
        var top = new JPanel(); top.add(new JLabel("Nome:")); top.add(busca);
        var btn = new JButton("Buscar"); btn.addActionListener(e -> listar()); top.add(btn);
        add(top,BorderLayout.NORTH);
        add(new JScrollPane(new JTable(m)),BorderLayout.CENTER);
        SwingUtilities.invokeLater(this::listar);
    }

    private void listar(){
        m.setRowCount(0);
        List<Alimento> it = dao.buscarPorNome(busca.getText().trim(),200,0);
        it.forEach(a -> m.addRow(new Object[]{
            a.getId(), a.getNome(), a.getDescricao(), a.getPreco(), a.getTipo(),
            a.getEstabelecimento()!=null ? a.getEstabelecimento().getNome() : ""
        }));
    }
}
