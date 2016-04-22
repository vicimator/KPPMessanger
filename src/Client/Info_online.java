package Client;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Info_online extends JFrame 
{
	
	private Icon warnIcon = UIManager.getIcon("OptionPane.informationIcon");
	
	Info_online() 
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
		msgLabel.setText("<html><center>User with this nickname is currently online. It's impossible to login one account in different windows.</center></html>");
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
					Info_online frame = new Info_online();
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
