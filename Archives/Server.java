package Chat;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Server {
	public static void main(String[] args) {
		
		FrameworkServer miframe = new FrameworkServer();
		
		miframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class FrameworkServer extends JFrame implements Runnable{
	
	public FrameworkServer (){
		
		setBounds(800,200,400,350);
		
		JPanel mysheet = new JPanel();
		
		add(mysheet);
		
		textarea = new JTextArea();
		
		mysheet.add(textarea, BorderLayout.CENTER);
		
		setVisible(true);
		
		Thread mythread = new Thread (this);
		
		mythread.start();
	}
	
	private JTextArea textarea;


	public void run() {
		
		try {
			
			ServerSocket server = new ServerSocket(9999);
			
			String nickname, ip, message;
			
			ArrayList <String> Iplist = new ArrayList <String>();
			
			SendPackage receivedpackage;
			
			while(true) {
				
				Socket mysocket = server.accept();
				
				ObjectInputStream datapackage = new ObjectInputStream(mysocket.getInputStream());
				
				receivedpackage = (SendPackage) datapackage.readObject();
				
				nickname = receivedpackage.getNickname();
				
				ip = receivedpackage.getIp();
				
				message = receivedpackage.getMessage();
				
				// Checks if the user is connect or not
				if(!message.equals("Online")) {
				
				textarea.append(" \n" + nickname + ": " + message + " for " + ip);
				
				Socket sendestiny = new Socket(ip,9090);
				
				ObjectOutputStream packagereset = new ObjectOutputStream(sendestiny.getOutputStream());
				
				packagereset.writeObject(receivedpackage);
				
				packagereset.close();
				
				sendestiny.close();
				
				mysocket.close();
				
				}else {					
					// Online detected and important notes
					//getInetAdress return the address to which  the socket is connected
					//InetAddress getHostAdrees returns ip in string
					
					InetAddress localization = mysocket.getInetAddress();
					
					String userip = localization.getHostAddress();
					
					System.out.println("Online: " + userip);
					
					Iplist.add(userip);
					 
					receivedpackage.setIps(Iplist);
					 
					for( String element:Iplist) {
						
						 System.out.println("Array: " + element);
						
						 Socket sendestiny = new Socket(element,9090);
							
						 ObjectOutputStream packagereset = new ObjectOutputStream(
								 sendestiny.getOutputStream());
							
						 packagereset.writeObject(receivedpackage);
							
						 packagereset.close();
						
						 sendestiny.close();
						
						 mysocket.close();
							
					 }
					
				}
				
				/*Invalido pq es objeto no texto
				 * DataInputStream in = new DataInputStream(mysocket.getInputStream());
				
				String textmesage = in.readUTF();
			
				;*/
			
			}
			
		} catch (IOException | ClassNotFoundException e) {
			
			System.out.println(e.getMessage());
			
		}
		
	}
	
}
