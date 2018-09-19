package NUREgramClientPart;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class EnterWindowClient extends JFrame{
	private JLabel nickname_label = new JLabel("Enter your nickname:");
	private JButton enter_button = new JButton("Enter");
	private JTextField nickname_field;
	private JTextField IP_field;

	public EnterWindowClient() {
		this.setTitle("NUREgram");
		this.setBounds(100, 100, 257, 162);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		enter_button.setBounds(151, 91, 80, 23);
		this.getRootPane().setDefaultButton(enter_button);
		enter_button.addActionListener(new EnterActionListener());
		getContentPane().add(enter_button);
		
		nickname_label.setBounds(10, 11, 199, 14);
		getContentPane().add(nickname_label);
		
		nickname_field = new JTextField();
		nickname_field.setBounds(10, 32, 221, 20);
		getContentPane().add(nickname_field);
		nickname_field.setColumns(10);
		
		IP_field = new JTextField("localhost");
		IP_field.setBounds(117, 63, 102, 20);
		getContentPane().add(IP_field);
		IP_field.setColumns(10);
		
		JLabel IP_label = new JLabel("Enter server IP:");
		IP_label.setBounds(20, 66, 140, 14);
		getContentPane().add(IP_label);
		
		JLabel prod_label = new JLabel("Sushko ITCS-16-6 prod.");
		prod_label.setBounds(10, 95, 140, 14);
		getContentPane().add(prod_label);
	}
	
	class EnterActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(nickname_field.getText() != "") {
								
				try {					
					Socket socket = new Socket(IP_field.getText(), 4096);
					DataOutputStream output = new DataOutputStream(socket.getOutputStream());
					
					output.writeUTF(nickname_field.getText());
					output.flush();
					
					MainWindowClient main_window = new MainWindowClient(socket, nickname_field.getText());
					main_window.setVisible(true);
					
					dispose();
					
					ServerListenThread listener = new ServerListenThread(main_window, socket);
					listener.start();
				}		
				catch (UnknownHostException e1)
				{
					//e1.printStackTrace();
				}
				catch (IOException e2)
				{
					//e2.printStackTrace();
					JOptionPane.showMessageDialog(enter_button, "Incorrect IP, try again.", "IP error", JOptionPane.PLAIN_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(enter_button, "Enter your nickname", "Empty nickname", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
}
