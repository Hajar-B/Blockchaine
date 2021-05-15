import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServeurCentralHandler implements Runnable {
	private Socket mineur;
	private DataInputStream in;
	
	
	public ServeurCentralHandler(Socket mineur) throws IOException {
		this.mineur = mineur;
		in = new DataInputStream(this.mineur.getInputStream());
		
	}
	
	@Override
	public void run() {
		while(true){
			try{
				String request = in.readUTF();
				System.out.println(request);
			} catch(IOException e) {
				System.out.println(e);
			}
		}
		
	}
	
}
