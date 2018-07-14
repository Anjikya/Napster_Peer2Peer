
import java.io.IOException;
import java.io.*;
import java.net.*;

public class ClientServer implements Runnable {
String FileDirectory;
int port_no;
ServerSocket serverSocket=null;
Socket socket=null;
	
	ClientServer(int port,String SharedDir)
	{
		port_no=port;
		FileDirectory=SharedDir;
	}
	
	public void run()							//To run the thread of client acting as server
	{
	try{
	
		serverSocket=new ServerSocket(port_no);
		System.out.println("Client server created");
		
		}catch(IOException ie)
	{
			ie.printStackTrace();
	}
	try{
		socket=serverSocket.accept();
		}catch(IOException io)
	{
			io.printStackTrace();
	}
	new ClientDownload(socket,FileDirectory);
	}

	}


class ClientDownload extends Thread
{
	protected Socket socket;
	String FileDirectory;
	int port;
	String filename;
	ClientDownload(Socket socket,String FileDirectory)
	{
		this.socket=socket;
		this.FileDirectory=FileDirectory;
	}
	
	public void run()
	{
		try{
			
			InputStream is=socket.getInputStream();		    //Connecting Client acting as a server to the file requesting Client	
			ObjectInputStream ois=new ObjectInputStream(is);
			OutputStream os=socket.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			filename=(String)ois.readObject();					//Fileaname to be downloaded
			String FileLocation;
			while(true)
			{
				   File myFile = new File(FileDirectory+"//"+filename);
	                long length = myFile.length();
	                byte [] mybytearray = new byte[(int)length];		//Sending file length of the file to be downloaded to the client
	                oos.writeObject((int)myFile.length());
	                oos.flush();
	                FileInputStream fileInSt=new FileInputStream(myFile);
	                BufferedInputStream objBufInStream = new BufferedInputStream(fileInSt); 
											   	//transferring the contents of the file as stream of bytes
	                objBufInStream.read(mybytearray,0,(int)myFile.length());
	                System.out.println("sending file of " +mybytearray.length+ " bytes");
	                oos.write(mybytearray,0,mybytearray.length);
	                oos.flush(); 
			}
		}catch(Exception e)
		{
			
			e.printStackTrace();
			
		}
	}
	
}

