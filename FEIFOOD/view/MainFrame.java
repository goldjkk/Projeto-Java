package view;

import controller.EstatisticasController;
import model.Usuario;

import javax.swing.*;

public class MainFrame extends JFrame {
    private final JTabbedPane tabs = new JTabbedPane();
    private final PanelEstatisticas stats = new PanelEstatisticas(new EstatisticasController());
    private final Integer uid;

    public MainFrame() { this(null); }
    public MainFrame(Usuario user) {
        uid = user != null ? user.getId() : 1;
        setTitle("FEIFood"); setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000,700); setLocationRelativeTo(null);

        tabs.addTab("Buscar Alimentos", new PanelBuscaAlimentos());
        tabs.addTab("Meu Pedido", new PanelPedido(uid, stats::recarregar));
        tabs.addTab("Estatísticas", stats);
        tabs.addChangeListener(e -> {
            if ("Estatísticas".equals(tabs.getTitleAt(tabs.getSelectedIndex()))) stats.recarregar();
        });
        setContentPane(tabs);
    }

    public static void main(String[] a){ SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true)); }
}
