package chatApp;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class chatApp_client extends Frame implements Runnable, ActionListener{
	
	//creating objects
	
	TextField textField;//type a message
	TextArea textArea;//display the message
	Button send;
	
	Socket socket;
	DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;
	
	Thread chat;
	
	//constructor
	
	chatApp_client()
	{
		textField=new TextField("",45);
		textArea=new TextArea("",20,40);
		send=new Button("Send");
		
		send.addActionListener(this);
		
		//to close window on clicking exit
		
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent windowEvent)
			{
			System.exit(0);
			}
		});
		
		//Establishing connection & read or write  message
		
		try
		{
		socket=new Socket("localhost",10000);
		
		dataInputStream=new DataInputStream(socket.getInputStream());
		dataOutputStream=new DataOutputStream(socket.getOutputStream());
		}
		catch(Exception e)
		{
			
		}
		//adding the elements
		add(textArea);
		textArea.setEditable(false);
		add(textField);
		add(send);
		
		//thread definitions
		chat=new Thread(this);
		chat.setDaemon(true);//executed without interfering with user's operation - when all user thread die jvm terminates this thread automatically
		chat.start();//calls runnable method to execute code specified in the run() method in a  separate thread
		
		//setting up the frame
		setSize(500,500);
		setTitle("Client");
		setLayout(new FlowLayout(FlowLayout.LEFT,20,0));
		setVisible(true);
		
		
	}
	
	//when send is clicked operations to be performed
	
	public void actionPerformed(ActionEvent e)
	{
		//display client message in chat box
		
		String msg=textField.getText();
		textArea.append("Client:"+msg+"\n");
		textField.setText("");
		
		try 
		{
			dataOutputStream.writeUTF(msg);//writes string to output stream(sends message to server)
			dataOutputStream.flush();//allows continued write or append
		}
		catch (IOException e1) 
		{
			
		}	
	}
	//main method
	public static void main(String [] args)
	{
		new chatApp_client();//instance of class
	}
	
	//thread run method(start a new thread)
	public void run()
	{
		while(true)
		{
			try
			{
				//display server message in chat box
				
				String msg=dataInputStream.readUTF();//(read message from server)
				textArea.append("Server:"+msg+"\n");
			}
			catch(Exception e)
			{
				
			}
		}
	}

}
