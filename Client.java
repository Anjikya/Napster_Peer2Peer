import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
	public static void main(String args[]) {
		int id;
		int c = 1;
		String f_name;
		int portofserver;
		int portasserver;
		Scanner scan = new Scanner(System.in);
		String serverName = args[0];
		//int port = Integer.parseInt(args[1]);
		int port=5002;
		Data d = new Data();
		Peer p = new Peer();
		int peer;
		String sharedDir;
		try {
			System.out.println("Connecting to:" + serverName + "on port" + port);
			Socket client = new Socket(serverName, port);							//Creating the socket for connecting to server
			System.out.println("Connected to" + client.getRemoteSocketAddress());
			OutputStream out = client.getOutputStream();
			ObjectOutputStream oos;
			InputStream in = client.getInputStream();
			ObjectInputStream ois;
			System.out.println("ENter the shared directory");						//Shared Directory of the client containing its files
			sharedDir=scan.nextLine();
			System.out.println("Enter the portno where client act as server");		//Port Number at which this client will work as server
			portasserver=scan.nextInt();
			ClientServer cs=new ClientServer(portasserver,sharedDir);				//Start a thread to make any client work as a server
			cs.start();
			//System.out.println("enter 1 to register or 2 search for a file");
			int ch = 1;
			if (ch == 1) {
				
				File newfind;
				File directoryObj = new File(sharedDir);
				String[] filesList = directoryObj.list();
				for (int j = 0; j < filesList.length; j++) {						//Loop to registering all files 
					newfind = new File(filesList[j]);								//in the directory to the server
					oos = new ObjectOutputStream(out);
					d.filename = newfind.getName();
					 d.peer_id=Integer.parseInt(args[1]);
					 d.choice=1;
					oos.writeObject(d);
					System.out.println("Registering Files "+newfind.getName()+" from "+sharedDir+"to the Server");
				}
			}

			System.out.println("enter 1 to search for a file or 2 to exit??");
			c=scan.nextInt();
			if (c == 1) {
				try {
					System.out.println("enter the file to be searched:");
					scan.nextLine();
					d.filename = scan.nextLine();
					d.choice = 2;
					oos = new ObjectOutputStream(out);						//Send the filename to be searched to the server			
					oos.writeObject(d);
					oos.flush();
					System.out.println("Peers having the file "+d.filename+ " are:::");
					ois = new ObjectInputStream(in);
					p = (Peer) ois.readObject();
					for (int i = 0; i < p.files.length; i++)
						if(p.files[i]!=0)
						System.out.println(p.files[i]);
					System.out.println("Enter the peer_id to download the file:: "+d.filename);
					peer=scan.nextInt();
					System.out.println("Enter the the peers respective port no. at which it will act as server");
					portofserver=scan.nextInt();
					//ClientasServer(peer,portofserver,d.filename,sharedDir); 												//Downloading the file from Peer						
					System.out.println("Downloaded file "+d.filename+"from peer_id: "+peer+"at port no"+portofserver);

					client.close();
					}catch(IOException io)
				{
						io.printStackTrace();
				}
			}
					else
					{	
						System.out.println("Terminating the client");
						System.exit(1);
					}
				} catch (ClassNotFoundException cnf) {
					cnf.printStackTrace();
				} catch (IOException ie) {
					ie.printStackTrace();
				}
			}
			
	public static void ClientasServer(int clientasserverpeerid,int clientasserverportno,String filename,String sharedDir)   
	{
		try{											 //Connecting to the peer Server having the file to be downloaded	
			Socket clientasserversocket=new Socket("localhost",clientasserverportno);
			ObjectOutputStream ooos=new ObjectOutputStream(clientasserversocket.getOutputStream());
			ooos.flush();
			ObjectInputStream oois=new ObjectInputStream(clientasserversocket.getInputStream());
			ooos.writeObject(filename);
			int readbytes=(int)oois.readObject();
			System.out.println("bytes transferred: "+readbytes);
			byte[] b=new byte[readbytes];
			oois.readFully(b);
			OutputStream fileos=new FileOutputStream(sharedDir+"//"+filename);
			BufferedOutputStream bos=new BufferedOutputStream(fileos);
			bos.write(b, 0,(int) readbytes);
	        System.out.println(filename+" file has be downloaded to your directory "+sharedDir);
	        bos.flush();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
