package view;
import controller.PedidoController; import dao.impl.PedidoDAOImpl; import model.*;
import javax.swing.*; import javax.swing.table.DefaultTableModel; import java.awt.*;
public class PanelPedido extends JPanel {
    private final Usuario logado; private final PanelBuscaAlimentos painelBusca=new PanelBuscaAlimentos();
    private final PedidoController pedidoController=new PedidoController(new PedidoDAOImpl());
    private final JTable tabela=new JTable();
    private final DefaultTableModel model=new DefaultTableModel(new Object[]{"ID","Nome","Qtd","Preço Unit.","Subtotal"},0){ public boolean isCellEditable(int r,int c){return false;} };
    private final JLabel lblTotal=new JLabel("Total: R$ 0,00");
    private final JButton btnNovo=new JButton("Novo Pedido");
    private final JButton btnAdd=new JButton("Adicionar");
    private final JButton btnRemover=new JButton("Remover");
    private final JButton btnSalvar=new JButton("Salvar Itens");
    private final JButton btnExcluir=new JButton("Excluir Pedido");
    private final JButton btnAvaliar=new JButton("Avaliar (0-5)");
    public PanelPedido(Usuario logado){
        this.logado=logado; setLayout(new BorderLayout());
        JPanel top=new JPanel(); top.add(btnNovo); top.add(btnAdd); top.add(btnRemover); top.add(btnSalvar); top.add(btnExcluir); top.add(btnAvaliar); add(top,BorderLayout.NORTH);
        JSplitPane split=new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelBusca, createItensPanel()); split.setResizeWeight(0.5); add(split,BorderLayout.CENTER);
        btnNovo.addActionListener(e->novoPedido()); btnAdd.addActionListener(e->adicionarSelecionado()); btnRemover.addActionListener(e->removerSelecionado());
        btnSalvar.addActionListener(e->salvarItens()); btnExcluir.addActionListener(e->excluirPedido()); btnAvaliar.addActionListener(e->avaliarPedido());
        setEnabledState(false);
    }
    private JPanel createItensPanel(){ JPanel p=new JPanel(new BorderLayout()); tabela.setModel(model); p.add(new JScrollPane(tabela),BorderLayout.CENTER);
        JPanel south=new JPanel(new FlowLayout(FlowLayout.RIGHT)); south.add(lblTotal); p.add(south,BorderLayout.SOUTH); return p; }
    private void setEnabledState(boolean enabled){ btnAdd.setEnabled(enabled); btnRemover.setEnabled(enabled); btnSalvar.setEnabled(enabled); btnExcluir.setEnabled(enabled); btnAvaliar.setEnabled(enabled); }
    private void novoPedido(){ pedidoController.novoPedido(logado); model.setRowCount(0); atualizarTotal(); setEnabledState(true); JOptionPane.showMessageDialog(this,"Pedido criado!"); }
    private void adicionarSelecionado(){
        if(pedidoController.getPedido()==null){ JOptionPane.showMessageDialog(this,"Crie um pedido primeiro."); return; }
        Alimento a=painelBusca.getSelecionado(); if(a==null){ JOptionPane.showMessageDialog(this,"Selecione um alimento na tabela de busca."); return; }
        String sQtd=JOptionPane.showInputDialog(this,"Quantidade:","1"); if(sQtd==null) return; int qtd=Integer.parseInt(sQtd);
        pedidoController.adicionar(a,qtd); addOrUpdateRow(a,qtd); atualizarTotal();
    }
    private void addOrUpdateRow(Alimento a,int qtd){
        for(int i=0;i<model.getRowCount();i++){ if(((Integer)model.getValueAt(i,0)).equals(a.getId())){ int q=(int)model.getValueAt(i,2)+qtd; model.setValueAt(q,i,2); model.setValueAt(q*(double)model.getValueAt(i,3),i,4); return; } }
        model.addRow(new Object[]{ a.getId(), a.getNome(), qtd, a.getPreco(), qtd*a.getPreco() });
    }
    private void removerSelecionado(){ int row=tabela.getSelectedRow(); if(row<0) return; Integer id=(Integer)model.getValueAt(row,0);
        Alimento a=new dao.impl.AlimentoDAOImpl().buscarPorId(id).orElse(null); if(a==null) return; pedidoController.remover(a); model.removeRow(row); atualizarTotal();
    }
    private void salvarItens(){ if(pedidoController.getPedido()==null) return; pedidoController.salvarItens(); JOptionPane.showMessageDialog(this,"Itens salvos no banco!"); }
    private void excluirPedido(){ if(pedidoController.getPedido()==null) return; int opt=JOptionPane.showConfirmDialog(this,"Excluir pedido?","Confirmação",JOptionPane.YES_NO_OPTION);
        if(opt==JOptionPane.YES_OPTION){ pedidoController.excluirPedido(); model.setRowCount(0); atualizarTotal(); setEnabledState(false); } }
    private void avaliarPedido(){ if(pedidoController.getPedido()==null) return; String s=JOptionPane.showInputDialog(this,"Nota (0-5):","5"); if(s==null) return; int estrelas=Math.max(0, Math.min(5, Integer.parseInt(s)));
        String comentario=JOptionPane.showInputDialog(this,"Comentário (opcional):",""); dao.AvaliacaoDAO avDAO=new dao.impl.AvaliacaoDAOImpl();
        model.Avaliacao av=new model.Avaliacao(); av.setPedidoId(pedidoController.getPedido().getId()); av.setEstrelas(estrelas); av.setComentario(comentario);
        avDAO.salvar(av); JOptionPane.showMessageDialog(this,"Avaliação registrada!"); }
    private void atualizarTotal(){ double total=0.0; for(int i=0;i<model.getRowCount();i++){ total+=(double)model.getValueAt(i,4);} lblTotal.setText(String.format("Total: R$ %.2f", total)); }
}
