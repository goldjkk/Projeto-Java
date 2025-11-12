package view;
import controller.EstatisticasController; import model.Usuario; import javax.swing.*;
public class MainFrame extends JFrame {
    private final JTabbedPane tabs=new JTabbedPane();
    public MainFrame(Usuario logado){
        super("FEIFood"); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setSize(980,640); setLocationRelativeTo(null);
        tabs.add("Buscar Alimentos", new PanelBuscaAlimentos());
        tabs.add("Meu Pedido", new PanelPedido(logado));
        tabs.add("Estatísticas", new PanelEstatisticas(new EstatisticasController()));
        setContentPane(tabs);
    }
}
