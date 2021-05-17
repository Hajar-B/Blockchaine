import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * Classe qui gere les message envoye en multicast
 *
 * @autor Sohayla RABHI et Hajar BOUZIANE
*/
public class Multicast {
	private DatagramSocket serverSocket;
	private String ip;
	private int port;
	
	public Multicast() throws IOException{
		this.ip = "225.4.5.6";
		this.port = 3333;
		serverSocket = new DatagramSocket();
	}
	
	public void send(byte[] message) throws IOException{
		DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getByName(ip), port);
		serverSocket.send(packet);	
	}
}
