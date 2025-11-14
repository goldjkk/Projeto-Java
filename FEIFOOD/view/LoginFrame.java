package view;

import controller.AuthController;
import dao.impl.UsuarioDAOImpl;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginFrame extends JFrame {
    private final JTextField email = new JTextField(22);
    private final JPasswordField senha = new JPasswordField(22);
    private final JButton btnEntrar = new JButton("Entrar");
    private final JButton btnRegistrar = new JButton("Registrar");
    private final AuthController auth = new AuthController(new UsuarioDAOImpl());

    public LoginFrame() {
        super("FEIFood - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        var p = new JPanel();
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Senha:")); p.add(senha);
        p.add(btnEntrar); p.add(btnRegistrar);

        btnEntrar.addActionListener(e -> onLogin());
        btnRegistrar.addActionListener(e -> onRegistrar());

        setContentPane(p);
        pack();
        setLocationRelativeTo(null);
    }

    private void onLogin() {
        String em = email.getText().trim();
        String pw = new String(senha.getPassword());

        if (em.isBlank() || pw.isBlank()) {
            JOptionPane.showMessageDialog(this, "Preencha email e senha.");
            return;
        }

        Optional<Usuario> u = auth.login(em, pw);
        if (u.isPresent()) {
            new MainFrame(u.get()).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciais inválidas.");
        }
    }

    private void onRegistrar() {
        // Form simples de cadastro (nome, email, senha, confirmar)
        var txtNome  = new JTextField(22);
        var txtEmail = new JTextField(22);
        var txtPass1 = new JPasswordField(22);
        var txtPass2 = new JPasswordField(22);

        JPanel form = new JPanel(new GridLayout(0,1,4,4));
        form.add(new JLabel("Nome completo:")); form.add(txtNome);
        form.add(new JLabel("Email:"));         form.add(txtEmail);
        form.add(new JLabel("Senha:"));         form.add(txtPass1);
        form.add(new JLabel("Confirmar senha:"));form.add(txtPass2);

        int ok = JOptionPane.showConfirmDialog(this, form, "Criar conta",
                                               JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return;

        String nome = txtNome.getText().trim();
        String em   = txtEmail.getText().trim();
        String p1   = new String(txtPass1.getPassword());
        String p2   = new String(txtPass2.getPassword());

        if (nome.isBlank() || em.isBlank() || p1.isBlank() || p2.isBlank()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }
        if (!p1.equals(p2)) {
            JOptionPane.showMessageDialog(this, "As senhas não conferem.");
            return;
        }

        try {
            // usa o controller existente
            auth.registrar(nome, em, p1);
            JOptionPane.showMessageDialog(this, "Conta criada! Faça login.");

            // já preenche a tela de login com o que acabou de registrar
            email.setText(em);
            senha.setText(p1);
            email.requestFocusInWindow();
        } catch (Exception ex) {
            // caso DAO lance exceção de email duplicado, etc.
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao registrar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
