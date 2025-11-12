package view;
import controller.EstatisticasController; import javax.swing.*; import java.awt.*;
public class PanelEstatisticas extends JPanel {
    public PanelEstatisticas(EstatisticasController controller){
        setLayout(new GridLayout(1,3,10,10));
        JTable t1=new JTable(controller.topBemAvaliados());
        JTable t2=new JTable(controller.topMalAvaliados());
        JTable t3=new JTable(controller.totais());
        add(wrap("Top 5 Bem Avaliados", t1)); add(wrap("Top 5 Mal Avaliados", t2)); add(wrap("Totais do Sistema", t3));
    }
    private JPanel wrap(String titulo,JTable t){ JPanel p=new JPanel(new BorderLayout()); p.add(new JLabel(titulo, SwingConstants.CENTER),BorderLayout.NORTH); p.add(new JScrollPane(t),BorderLayout.CENTER); return p; }
}
