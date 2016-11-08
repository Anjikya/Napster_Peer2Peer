import java.io.*;
import java.net.*;
import java.util.*;

public class Data implements Serializable  			//Serialized object to send data from clients to server 
{
	public int peer_id;
	public String filename;
	public int choice;
	public int[] files=new int[20];
}
