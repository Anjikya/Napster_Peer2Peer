import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server {										//Thread class to handle multiple clients 
	public static void main(String args[]) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		int port=5002;
		ArrayList<FileProperties> filess=new ArrayList<FileProperties>(); 

		try {
			serverSocket = new ServerSocket(port);

		} catch (IOException io) {
			io.printStackTrace();
		}
		while (true) {
			try {
				System.out.println("Server waiting for Client");
				socket = serverSocket.accept();
			} catch (IOException e) {
				System.out.println("IO?error: " + e.getMessage());
			}

			new EchoThread(socket,filess).start();
		}
	}
}

class EchoThread implements Runnable {
	protected Socket server;

	int count = 0;
	int value = 1;
	ArrayList<FileProperties> filess=new ArrayList<FileProperties>();       //ArrayList that maps to FileProperties class to store registered files of clients 

	int[] files_per_peer = new int[20];
	Data d = new Data();
	Peer p = new Peer();

	public EchoThread(Socket server,ArrayList<FileProperties> filess) 
 	{
		this.server = server;
		this.filess=filess;
		
	}
	public void run() {
		
		
		try {
			OutputStream os = server.getOutputStream();
			InputStream is = server.getInputStream();
			ObjectOutputStream oos;
			ObjectInputStream ois;

	
			
				System.out.println("Just Connected to client on port no." + server.getRemoteSocketAddress() + "....");
				
				while (value == 1) {

					ois = new ObjectInputStream(is);
					d = (Data) ois.readObject();					
					value = d.choice;
					if (value == 2)
						break;
					if(register(d.peer_id, d.filename)==1)									//register() to register files of clients
						System.out.println("Registering File "+d.filename+" of peer_id "+d.peer_id+" to the server");

				}

				if (value == 2) {
					System.out.println("Search invoked");									//search() returning list of peer_ids
					search(d.filename);
					oos = new ObjectOutputStream(os);
					oos.writeObject(p);
					oos.flush();
					oos.close();
				}

		
			} catch (ClassNotFoundException c) {
				System.out.println("Data class not found");
				c.printStackTrace();
				
			} catch (IOException ie) {
				ie.printStackTrace();
				
			}

		
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public int register(int peer_id, String filename) {										//Definition of register()
		FileProperties f=new FileProperties();
		f.peer_id=peer_id;
		f.Filename=filename;
		filess.add(f);
		//count = files_per_peer[peer_id];
		//reg[peer_id][count++] = filename;
		return 1;
	}

	public void search(String filename) {												//Definition of search()
		FileProperties ff;
		int k=0;
		for(int i=0;i<filess.size();i++)
		{
			ff=filess.get(i);
			if(ff.Filename.equals(filename))
				p.files[k++] = ff.peer_id;
		}
		
		
	
	}

}
