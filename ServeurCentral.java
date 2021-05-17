import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

/**
 * ServeurCentral est une classe qui cree un serveur qui ecoute sur le port 3333. Il accepte les connexions des Mineurs et valide l'insertion d'un bloc dans la Blockchaine.
 *
 * @autor Sohayla RABHI et Hajar BOUZIANE
*/
public class ServeurCentral {
	private Socket mineur;
	private int poolSize;
	private int port;
	private ServerSocket server;
	private ExecutorService pool;
	private static ArrayList<ServeurCentralHandler> listeMineur = new ArrayList<>();
	
	/**
	 * Constructeur qui cree un ServeurCentral. 
	 * 
	 * @param port Le port d'ecoute pour des connexions de Mineurs 
	 * @param poolSize La taiile du pool de threads 
	 * @throws IOException 
	 */
	public ServeurCentral(int port, int poolSize) throws IOException {
		this.port = port;
		this.poolSize = poolSize;
		server = new ServerSocket(this.port);
		pool = Executors.newFixedThreadPool(this.poolSize);
	}
	
	/**
	 * Fonction qui attend les connexions des Mineurs et execute le run de la classe ServeurCentralHandler.
	 *
	 * @throws IOException
	*/
	public void manageRequest() throws IOException {
		System.out.println("Bienvenue sur le Serveur Central.\nEn attente de mineurs...");
		while(true) {
			mineur = server.accept();
			System.out.println("Connexion etablie avec " + mineur.getLocalSocketAddress());
			ServeurCentralHandler mineurThread = new ServeurCentralHandler(mineur, listeMineur);
			listeMineur.add(mineurThread);
			pool.execute(mineurThread);
		}
	}
	
	public static void main(String[] args) throws IOException{
		ServeurCentral sv = new ServeurCentral(3333,10);		
		sv.manageRequest();
	}

}
