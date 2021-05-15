import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.Scanner;

public class Mineur {
	private String ip;
	private int port;
	private Socket socket;
	//private MulticastSocket multicast;
	//private InetAddress group;
	private DataOutputStream out;
	private DataInputStream in;
	
	public Mineur(String ip, int port) throws IOException {
		this.ip = ip;
		this.port = port;
		this.socket = new Socket(this.ip, this.port);
		
		//multicast = new MulticastSocket(this.port);
		//group = InetAddress.getByName("225.4.5.6");
		//multicast.joinGroup(this.group);
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(System.in);

	}
	/*
	public void printMessage() throws IOException {
		byte[] message = new byte[256];
		DatagramPacket packet = new DatagramPacket(message, message.length);
		multicast.receive(packet);
		System.out.println(new String(packet.getData()));
	}
	*/
	public void manageRequest() throws IOException {
		while(true){
			Scanner s = new Scanner(System.in);
			String nom = s.nextLine();
			out.writeUTF(nom);
		}
	}
	
	public static void main(String[] args) throws IOException {
		Mineur min = new Mineur("localhost",3333);
		min.manageRequest();		
	}

}
