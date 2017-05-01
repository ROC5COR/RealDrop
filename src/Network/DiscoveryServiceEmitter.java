package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class DiscoveryServiceEmitter extends Thread{
	
	private DatagramSocket socket;
	private int port = 20451;
	DatagramPacket dpE;
	InetSocketAddress adrDest;
	private String discoveryMessage;
	
	public DiscoveryServiceEmitter(String broadcastAddr, String message){
		this.discoveryMessage = message;
		try {
			socket = new DatagramSocket();
	        socket.setBroadcast(true);
	        adrDest = new InetSocketAddress(broadcastAddr, this.port);
	    } catch (Exception e) {
	        System.err.println("DSE : Connection failed. " + e.getMessage());
	    }
	    //listen();
		
	}
	
	public void run(){
		System.out.println("Start broadcasting");
	    broadcastPolicy1();
	}
	
	public void broadcastPolicy1(){
		
		byte[] buf= new String(this.discoveryMessage).getBytes();
		
		while(true){

			dpE = new DatagramPacket(buf, buf.length, adrDest);
			try {
				socket.send(dpE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Broadcast Sent");
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//socket.close(); //Never closed
		
	}
	
	
}
