package MainRealDrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver {
	/*
	 * Wait to receive data from the host with ip : fromIP on port : port
	 * And save it under the path : filePath
	 */
	private ServerSocket server = null;
	private Socket client = null;
	private String fromIP = null;
	private String filePath = null;
	
	public FileReceiver(String fromIP, int port, String filePath){
		this.fromIP = fromIP;
		this.filePath = filePath;
		try {
			this.server = new ServerSocket();
			this.server.bind(new InetSocketAddress(port));
			System.out.println("FileReceiver started on : "+port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receive(){
		try {
			this.client = server.accept();
			System.out.println("Receiving client from : "+client.getInetAddress().getHostAddress());
			if(!client.getInetAddress().getHostAddress().equals(fromIP)){
				System.out.println("Error IP mismatch");
			}
			byte[] bufR = new byte[512];
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			
			InputStream is = client.getInputStream();
			
			int totalReceived = 0;
			int lenBufR = 0;
			while(lenBufR != -1){
				lenBufR = is.read(bufR);
				if(lenBufR == -1){
					break;
				}
				totalReceived += lenBufR;
				fos.write(bufR, 0, lenBufR);
				//String reponse = new String(bufR, 0 ,lenBufR);
				//System.out.println("("+lenBufR+" bytes) : "+reponse);
			}
			System.out.println("FileReceived : "+totalReceived+" bytes received");
			fos.close();
			client.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
