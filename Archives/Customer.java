package Chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Customer {
	public static void main(String[] args) {
		
		FrameworkCustomer miframe = new FrameworkCustomer();
		
		miframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
	}
}

// This is the framework structure
class FrameworkCustomer extends JFrame{
	
	public FrameworkCustomer(){
		
		setBounds(400,200,250,350);
		
		SheetFrameworkCustomer mysheet = new SheetFrameworkCustomer();
		
		add(mysheet);
		
		setVisible(true);
		
		addWindowListener(new SendOnline());
		
	}
	
}
class SheetFrameworkCustomer extends JPanel implements Runnable{
	
	public SheetFrameworkCustomer(){
		// Front part of the chat
		
		String usernick = JOptionPane.showInputDialog("Give me your nickname: ");
		
		JLabel n_nick = new JLabel ("Nick: ");
		
		add(n_nick);
		
		nickname = new JLabel();
		
		nickname.setText(usernick);
		
		add(nickname);
		
		JLabel text = new JLabel("Online: ");
		
		add(text);
		
		ip = new JComboBox();
		
		add(ip);
		//Body of chat
		Staticfield = new JTextArea(12,20);
		
		add(Staticfield);
		
		field1 =new JTextField(20);
		
		add(field1);
		//Button and events
		mybutton = new JButton("Send");
		
		SendText myevent = new SendText();
		
		mybutton.addActionListener(myevent);
		
		add(mybutton);
		
		Thread mythread = new Thread(this);
		
		mythread.start();
	}
	
	private class SendText implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			//ipconfig for ip direction
			
			Staticfield.append(" \n" + field1.getText() );
			
			try {
				
				Socket mysocket = new Socket("192.168.1.70",9999);
				
				SendPackage data = new SendPackage();
				
				data.setNickname(nickname.getText());
				
				data.setIp(ip.getSelectedItem().toString());
				
				data.setMessage(field1.getText());
				
				ObjectOutputStream datapackage = new ObjectOutputStream(mysocket.getOutputStream());
				
				datapackage.writeObject(data);
				
				mysocket.close();
				
				/*Invalido porque usamos objects no text
				DataOutputStream out = new DataOutputStream(mysocket.getOutputStream());
				
				out.writeUTF(field1.getText());
				
				out.close();*/
				
			} catch (IOException e1) {
				
				System.out.println(e1.getMessage());
				
			}
			
		}
		
	}
	
	public void run() {
		try {
		
			ServerSocket servercustomer = new ServerSocket(9090);
			
			Socket customer;
			
			SendPackage receivedpackage;
			
			while(true) {
				
				customer = servercustomer.accept();
				
				ObjectInputStream indata = new ObjectInputStream(customer.getInputStream());
				
				receivedpackage = (SendPackage) indata.readObject();
				
				if(!receivedpackage.getMessage().equals("Online"))
				
					Staticfield.append(" \n" + receivedpackage.getNickname() + ": " 
							+ receivedpackage.getMessage());
				
				else {
					// It used to check that the link works
					//Staticfield.append(" \n" + );+
					
					ArrayList <String> IpsMenu = new ArrayList <String>();
					
					IpsMenu =receivedpackage.getIps();
					
					ip.removeAllItems();
					
					for(String element: IpsMenu) {
						
						ip.addItem(element);
					}
				}
					
			}
			
		}catch(Exception e) {
			
			System.out.println("Error");
			
		}
		
		
	}
	
	private JTextField field1;
	
	private JComboBox ip;
	
	private JLabel nickname;
	
	private JTextArea Staticfield;
	
	private JButton mybutton;

	
}
// Send signal online
class SendOnline extends WindowAdapter{
	
	public void windowOpened(WindowEvent e) {
		
		try {
			
			Socket mysocket2 = new  Socket("192.168.1.70",9999);
			
			SendPackage data = new SendPackage ();
			
			data.setMessage("Online");
			
			ObjectOutputStream datapackage2 = new ObjectOutputStream(mysocket2.getOutputStream());
			
			datapackage2.writeObject(data);
			
		}catch (Exception e2) {
			
		}
		
	}
}

// This is a class to has the nickname, message and ip and implements serializable

class SendPackage implements Serializable {
	
	public String getNickname() {
		
		return nickname;
		
	}

	public void setNickname(String nickname) {
		
		this.nickname = nickname;
		
	}

	public String getIp() {
		
		return ip;
		
	}

	public void setIp(String ip) {
		
		this.ip = ip;
		
	}

	public String getMessage() {
		
		return message;
		
	}

	public void setMessage(String message) {
		
		this.message = message;
		
	}

	public ArrayList<String> getIps() {
		return Ips;
	}

	public void setIps(ArrayList<String> ips) {
		Ips = ips;
	}

	private String nickname , ip, message;
	
	private ArrayList<String> Ips;
}



