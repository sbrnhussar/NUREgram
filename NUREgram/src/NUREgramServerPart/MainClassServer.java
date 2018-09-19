package NUREgramServerPart;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;


public class MainClassServer {

	public static void main(String[] args) {
		try
		{
			ServerSocket server = new ServerSocket(4096);
			
			MainWindowServer main_window = new MainWindowServer();
			
			main_window.setVisible(true);
						
			while(true) {
				main_window.set_text("Wait connection...");
				Socket client = server.accept();
				main_window.set_text("New connection accepted, new client's address - " + client.getInetAddress());
				(new ClientThread(client, main_window)).add_thread();
			}
		}
		catch (IOException e)
		{
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Server is already running", "Server error", JOptionPane.PLAIN_MESSAGE);
        }
	}
}
