package Client; // Работаем в пакете клиента

import java.awt.EventQueue; // Необходимые библиотечки
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Error_db extends JFrame 
{
	
	private Icon warnIcon = UIManager.getIcon("OptionPane.errorIcon"); // Добавляем иконку приложения
	
	public Error_db() 
	{
		initGUI(); // Конструктор содержит лишь метод отрисовки окна
	}
	
	private void initGUI()
	{
		
		setTitle("Error"); // Заголовок ошибки
		setResizable(false); // Изменять размер окна запрещено
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // При закрытии закрыть прототип только этого окна
		setSize(280, 160); // Фиксированный размер окна
		setLocationRelativeTo(null); // Поместить по центру экрана
		setVisible(true); // Сделать видимым
		getContentPane().setLayout(null); // Лэйаут делаем типа Абсолют (задавать придется координаты объектов при добавлении)
		
		JLabel msgLabel = new JLabel();
		msgLabel.setFont(new Font("Book Antiqua", Font.PLAIN, 13));
		msgLabel.setText("<html>Problem with database connection.</html>");
		msgLabel.setBounds(61, 37, 203, 39); // Описание ошибки + ее добавление на панель
		getContentPane().add(msgLabel);
		
		JButton btnAgree = new JButton("Got it"); // Кнопка, что все окей, я понял в чем беда
		btnAgree.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAgree.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent a) 
			{
				dispose(); // При нажатии на нее окно закрывается
			}
		});
		btnAgree.setBounds(160, 87, 104, 34);
		getContentPane().add(btnAgree); // Биндим Enter на эту кнопку
		
		JLabel warnLabel = new JLabel();
		warnLabel.setBounds(10, 32, 32, 44);
		warnLabel.setIcon(warnIcon); // Добавляем иконочку для красоты 
		getContentPane().add(warnLabel);

	}
	
public static void main(String[] args) 
{
		EventQueue.invokeLater(new Runnable(){
			
			public void run()  // Отрисовка окна + делаем ег овидимым
			{
				try 
				{
					Error_db frame = new Error_db();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace(); // всевозможные отчеты о возможных ошибках
				}
			}
		});
	}
}
