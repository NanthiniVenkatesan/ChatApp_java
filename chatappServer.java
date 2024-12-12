	package chatApp;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class chatApp_server extends Frame implements Runnable,ActionListener{
	TextField textField;
	TextArea textArea;
	Button send;
	
	ServerSocket serverSocket;
	Socket socket;
	DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;
	
	Thread chat;
	
	chatApp_server()
	{
		textField=new TextField("",45);
		textArea=new TextArea("",20,40);
		send=new Button("Send");
		
		send.addActionListener(this);
		
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent windowEvent)
			{
			System.exit(0);
			}
		});
		
		try
		{
		serverSocket=new ServerSocket(10000);
		socket=serverSocket.accept();
		System.out.println("client connected");
		
		dataInputStream=new DataInputStream(socket.getInputStream());
		dataOutputStream=new DataOutputStream(socket.getOutputStream());
		}
		catch(Exception e)
		{
			
		}
		
		
		add(textArea);
		textArea.setEditable(false);
		add(textField);
		add(send);
		
		chat=new Thread(this);
		chat.setDaemon(true);
		chat.start();
		
		setSize(500,500);
		setTitle("server");
		setLayout(new FlowLayout(FlowLayout.LEFT,20,0));
		setVisible(true);
		
		
	}
	public void actionPerformed(ActionEvent e)
	{
		String msg=textField.getText();
		textArea.append("Server:"+msg+"\n");
		textField.setText("");
		
		try 
		{
			dataOutputStream.writeUTF(msg);
			dataOutputStream.flush();
		}
		catch (IOException e1) 
		{
			
		}
	}
	public static void main(String [] args)
	{
		new chatApp_server();
	}
	public void run()
	{
		while(true)
		{
			try
			{
				String msg=dataInputStream.readUTF();
				textArea.append("Client:"+msg+"\n");
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
}
