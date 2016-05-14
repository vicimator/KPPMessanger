package Client;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;

public class About extends JFrame {

	private JPanel contentPane;

	public About() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/Images/logo.jpg")));
		setTitle("About KPPMessanger");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);
		
		JLabel image = new JLabel();
		image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/kppLogo.jpg")));
		image.setBounds(60, 30, 180, 82);
		contentPane.add(image);
		
		JLabel desc = new JLabel("<html>\r\nKPPMessanger it's <i>\"easy-to-use\"</i> manager, which includes all common stuff to comunicate. <br /> <br />\r\nThis application was made by <strong>Vindict Team</strong>: <br /> <br />\r\n<strong>Developers</strong>: Vladyslav Stopin, <br />Bogdan Gorbatenko, Ann Kanevska <br /> <br />\r\n<strong>GitHub</strong>: KPPMessanger <br /> <br />\r\n<center><i><small>Current version: 0.67</small></i></center>\r\n</html>");
		desc.setBounds(27, 135, 245, 156);
		contentPane.add(desc);
	}
}
