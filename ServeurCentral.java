import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServeurCentral {
	private Socket mineur;
	private int poolSize;
	private int port;
	private ServerSocket server;
	private ExecutorService pool;
	
	public ServeurCentral(int port, int poolSize) throws IOException {
		this.port = port;
		this.poolSize = poolSize;
		server = new ServerSocket(this.port);
		pool = Executors.newFixedThreadPool(this.poolSize);
	}
	
	public void manageRequest() throws IOException {
		System.out.println("Bienvenue sur le Serveur Central.\nEn attente de mineurs...");
		while(true) {
			mineur = server.accept();
			System.out.println("Connexion etablie avec " + mineur.getLocalSocketAddress());
			ServeurCentralHandler mineurThread = new ServeurCentralHandler(mineur);
			pool.execute(mineurThread);
		}
	}
	
	public static void main(String[] args) throws IOException{
		ServeurCentral sv = new ServeurCentral(3333,10);
		sv.manageRequest();
	}

}
