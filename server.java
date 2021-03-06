package gradgg;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Server {
	static DatagramSocket sendsocket;
	static InetAddress address = null;
	static byte[] buf;
	
	public static Map<String,String> userList = new HashMap<>();
	
	
	public static void main(String[] args) throws IOException
	{
		sendsocket = new DatagramSocket();
		address = InetAddress.getByName("127.0.0.1");
		
		try(DatagramSocket socket = new DatagramSocket(5010))
		{
			buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			
			while(true) {
				buf = new byte[256];
				packet = new DatagramPacket(buf,buf.length);
				
				socket.receive(packet);
				
				String message = new String(buf);
				System.out.println(message);
				
				
				if(message.contains("port"))
				{
					String[] impo = message.substring(4).split("&&");
					userList.put(impo[0],impo[1]);
					send("user Enter - username : " + impo[1]);
					System.out.println("port : " + impo[0]);
				}
				else if(message.contains("exit"))
				{
					String[] impo = message.split("&&");
					userList.remove(impo[0]);
					send("user out - username : " + impo[1]);
				}
				else
					send(message);
				
			}
		}
	}
	
	public static void send(String message)
	{
		byte[] buffer = (message).getBytes();
		
		try {
			Set<String> user = userList.keySet();
			Iterator<String> iterator = user.iterator();
			while(iterator.hasNext())
			{
				sendsocket.send(new DatagramPacket(buffer,buffer.length,address,Integer.parseInt(iterator.next())));
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
	}
}
