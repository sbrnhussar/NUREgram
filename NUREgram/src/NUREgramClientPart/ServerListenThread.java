package NUREgramClientPart;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ServerListenThread extends Thread{
	private MainWindowClient main_window;
	private Socket socket;
	
	ServerListenThread(MainWindowClient set_window, Socket set_socket){
		main_window = set_window;
		socket = set_socket;
	}
	public void run() {
		
		try{
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			while(!isInterrupted()) {
				String answer = input.readUTF();
				if((answer.split(";", 2)[0]).compareTo("take_online") != 0) {
					main_window.set_output_area(answer);
				}
				else {
					main_window.set_clients(answer.split(";", 2)[1]);
				}
				main_window.set_caret();
			}
		}
		catch (UnknownHostException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e2)
		{
			//e2.printStackTrace();
			JOptionPane.showMessageDialog(main_window, "Server is down", "Server error", JOptionPane.PLAIN_MESSAGE);
			main_window.dispose();
			interrupt();
			EnterWindowClient enter_window = new EnterWindowClient();
			enter_window.setVisible(true);
		}
	}
}
