import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServeurCentralHandler implements Runnable {
	private Socket mineur;
	private DataInputStream in;
	private Multicast mult;
	
	public ServeurCentralHandler(Socket mineur) throws IOException {
		this.mineur = mineur;
		in = new DataInputStream(this.mineur.getInputStream());
		mult = new Multicast();
	}
	
	@Override
	public void run() {
		while(true){
			try{
				String request = in.readUTF();
				System.out.println(request);
				if(request.equals("1 1 1")){
					System.out.println("Début du multicast");
					byte[] message = ("Le serveur vous parle ...").getBytes();
					mult.send(message);
					System.out.println("Fin du multicast");
				}
			} catch(IOException e) {
				System.out.println(e);
			}
		}
		
	}
	
}
