package Client; // Подробно об окне ошибок описано в файле Error_db.java

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Info_private extends JFrame 
{
	
	private Icon warnIcon = UIManager.getIcon("OptionPane.informationIcon");
	
	Info_private() 
	{
		initGUI();
	}
	
	private void initGUI()
	{
		
		setTitle("Information");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(280, 160);
		setLocationRelativeTo(null);
		setVisible(true);
		getContentPane().setLayout(null);
		
		JLabel msgLabel = new JLabel();
		msgLabel.setFont(new Font("Book Antiqua", Font.PLAIN, 13));
		msgLabel.setText("<html><center>Actually, you can't write private messages to yourself.</center></html>");
		msgLabel.setBounds(50, 11, 217, 85);
		getContentPane().add(msgLabel);
		
		JButton btnAgree = new JButton("Got it");
		btnAgree.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAgree.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent a) 
			{
				dispose();
			}
		});
		btnAgree.setBounds(90, 87, 100, 35);
		getContentPane().add(btnAgree);
		
		JLabel warnLabel = new JLabel();
		warnLabel.setBounds(17, 30, 32, 44);
		warnLabel.setIcon(warnIcon);
		getContentPane().add(warnLabel);

	}
	
public static void main(String[] args) 
{
		EventQueue.invokeLater(new Runnable(){
			
			public void run() 
			{
				try 
				{
					Info_private frame = new Info_private();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
}
