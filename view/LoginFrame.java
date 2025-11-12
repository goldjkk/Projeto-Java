package view;
import controller.AuthController; import dao.impl.UsuarioDAOImpl; import model.Usuario;
import java.util.Optional; import javax.swing.*;
public class LoginFrame extends JFrame {
    private final JTextField txtEmail=new JTextField(22);
    private final JPasswordField txtSenha=new JPasswordField(22);
    private final JButton btnEntrar=new JButton("Entrar");
    private final JButton btnRegistrar=new JButton("Registrar");
    private final AuthController auth=new AuthController(new UsuarioDAOImpl());
    public LoginFrame(){
        super("FEIFood - Login"); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p=new JPanel(); p.add(new JLabel("Email:")); p.add(txtEmail); p.add(new JLabel("Senha:")); p.add(txtSenha); p.add(btnEntrar); p.add(btnRegistrar);
        setContentPane(p); pack(); setLocationRelativeTo(null);
        btnEntrar.addActionListener(e->onLogin()); btnRegistrar.addActionListener(e->onRegistrar());
    }
    private void onLogin(){
        Optional<Usuario> u=auth.login(txtEmail.getText(), new String(txtSenha.getPassword()));
        if(u.isPresent()){ JOptionPane.showMessageDialog(this,"Bem-vindo, "+u.get().getNome()); new MainFrame(u.get()).setVisible(true); dispose(); }
        else JOptionPane.showMessageDialog(this,"Credenciais inválidas.");
    }
    private void onRegistrar(){
        String nome=JOptionPane.showInputDialog(this,"Seu nome:"); if(nome==null||nome.isBlank()) return;
        auth.registrar(nome, txtEmail.getText(), new String(txtSenha.getPassword()));
        JOptionPane.showMessageDialog(this,"Conta criada! Faça login.");
    }
}
