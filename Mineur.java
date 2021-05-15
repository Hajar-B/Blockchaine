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
	private DataOutputStream out;
	private DataInputStream in;
	private MulticastSocket multicast;
	private InetAddress group;
	
	public Mineur(String ip, int port) throws IOException {
		this.ip = ip;
		this.port = port;
		this.socket = new Socket(this.ip, this.port);
		
		multicast = new MulticastSocket(this.port);
		group = InetAddress.getByName("225.4.5.6");
		multicast.joinGroup(this.group);
		
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(System.in);

	}
		
	
	// dans ce tableau, je met les 2 chiffres que le client envoie dans la transaction. Pour tester si la transaction est correcte, il suffit de prendre les 3 chiffres stocké dans ce tableau. 
	// Si besoin, on peut mettre le tableau dans une variable global de la classe ou juste le faire passer en argument d'une fonction qui teste si la transaction est correct et pour faire le calcul du sel
	public void manageRequest() throws IOException {
		while(true){
			Scanner s = new Scanner(System.in);
			String nom = s.nextLine();
			if(nom.length() == 5){
				String mots[] = nom.split(" ");
				int[] transaction = new int[3];
				for(int i=0; i<3; i++)
					transaction[i] = Integer.parseInt(mots[i]);
				for(int i=0; i<3; i++)
					System.out.print(transaction[i] + " ");
				System.out.println(" ");
				out.writeUTF(nom);
			}
			else
				System.out.println("Veuillez saisir 3 entiers separe par des espaces pour votre transaction");
		}
	}

	public void printMessage() throws IOException {
		byte[] message = new byte[256];
		DatagramPacket packet = new DatagramPacket(message, message.length);
		multicast.receive(packet);
		System.out.println(new String(packet.getData()));
	}
	
	public static void main(String[] args) throws IOException {
		Mineur min = new Mineur("localhost",3333);
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){
					try {
						min.printMessage();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
		min.manageRequest();		
	}

}
