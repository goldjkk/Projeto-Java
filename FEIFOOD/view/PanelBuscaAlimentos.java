package view;
import controller.*; import dao.impl.AlimentoDAOImpl; import model.Alimento;
import javax.swing.*; import javax.swing.table.DefaultTableModel; import java.awt.*; import java.util.List;
public class PanelBuscaAlimentos extends JPanel {
    private final JTextField txtBusca=new JTextField(30);
    private final JButton btnBuscar=new JButton("Buscar");
    private final JTable tabela=new JTable();
    private final DefaultTableModel model=new DefaultTableModel(new Object[]{"ID","Nome","Descrição","Preço","Tipo","Estabelecimento"},0){ public boolean isCellEditable(int r,int c){return false;} };
    private final AlimentoController controller=new AlimentoControllerImpl(new AlimentoDAOImpl());
    public PanelBuscaAlimentos(){
        setLayout(new BorderLayout());
        JPanel top=new JPanel(); top.add(new JLabel("Nome:")); top.add(txtBusca); top.add(btnBuscar); add(top,BorderLayout.NORTH);
        tabela.setModel(model); add(new JScrollPane(tabela), BorderLayout.CENTER);
        btnBuscar.addActionListener(e->buscar());
    }
    private void buscar(){
        model.setRowCount(0);
        List<Alimento> itens=controller.buscarPorNome(txtBusca.getText().trim(),100,0);
        for(Alimento a:itens){
            model.addRow(new Object[]{ a.getId(), a.getNome(), a.getDescricao(), a.getPreco(), a.getTipo(), a.getEstabelecimento()!=null?a.getEstabelecimento().getNome():"" });
        }
    }
    public Alimento getSelecionado(){
        int row=tabela.getSelectedRow(); if(row<0) return null; Integer id=(Integer)model.getValueAt(row,0);
        return new AlimentoDAOImpl().buscarPorId(id).orElse(null);
    }
}
