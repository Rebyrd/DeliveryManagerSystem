package DeliveryManagerSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    private Oracle oracle;
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("送货管理系统");
        frame.setContentPane(new GUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public GUI()
    {
        try{
            oracle=new Oracle();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        登录Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textField1.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"账号为空","error",JOptionPane.OK_CANCEL_OPTION);
                    return;
                }
                if(passwordField1.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"密码为空","error",JOptionPane.OK_CANCEL_OPTION);
                    return;
                }
                if(textField1.getText().equals((String)"123456")){
                    if(passwordField1.getText().equals((String)"123456")){
                        frame.dispose();
                        App.entry(oracle);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null,"登录失败，请检查账号密码。","error",JOptionPane.OK_CANCEL_OPTION);
            }
        });
    }

    private JPanel mainPanel;
    private JButton 登录Button;
    private JTextField textField1;
    private JPasswordField passwordField1;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
