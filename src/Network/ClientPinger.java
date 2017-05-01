package Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientPinger {
	
	private int portManagerPort = -1;
	public ClientPinger(int portManagerPort){
		this.portManagerPort = portManagerPort;
	}
	public int ping(String ipAddr){
		System.out.println("Will ping : "+ipAddr);
		
		Socket socket = new Socket();
		System.out.println("Getting RemoteTransfertPort");
		
		InetSocketAddress addrDest = new InetSocketAddress(ipAddr,this.portManagerPort);
		
		try {
			socket.connect(addrDest,2000);
			System.out.println("Client : "+ipAddr+"is online");
			return 1;
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				System.out.println("Socket ok");
			}
			System.out.println("Client : "+ipAddr+" seems to be offline"+e.getMessage());
			return 0;
		}		
	}
}
