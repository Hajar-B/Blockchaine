import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

/**
 * Mineur est une classe qui cree un Mineur. Il laisse une connexion ouverte pour les clients qui souhaitent effectuer une transaction. Une fois qu'il recoit la transaction de la part du client il cree un bloc et recherche un sel qui lui permettra d'inserer le bloc avec la difficulte actuelle. 
 * Il ecoute en permanence les informations envoyees en multicast par le serveur central et insere la jointure contenant la transaction et le sel que le serveur central a envoye. Avant qu'il insere la jointure on verifie que le bloc est coherent et inserable.
 *
 * @autor Sohayla RABHI et Hajar BOUZIANE
*/
public class Mineur {
	private String ip;
	private int port;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private MulticastSocket multicast;
	private InetAddress group;
	
	private ArrayList<String> transactionEnAttente;
	private Blockchaine bck;
	private int difficulte;
	private int[] tabCrypto;
	
	/**
	 * Constructeur qui cree un Mineur. 
	 * 
	 * @param ip L'adresse ip avec laquelle il va se connecter 
	 * @param port Le port avec lequel il va se connecter
	 * @throws IOException 
	 */
	public Mineur(String ip, int port) throws IOException {
		this.ip = ip;
		this.port = port;
		this.socket = new Socket(this.ip, this.port);
		
		multicast = new MulticastSocket(this.port);
		group = InetAddress.getByName("225.4.5.6");
		multicast.joinGroup(this.group);
		
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(System.in);
		transactionEnAttente = new ArrayList<>();
		
		bck = new Blockchaine();
		tabCrypto = new int[] {6,2,3,4,5};
		Etat e = new Etat(tabCrypto);
		Bloc b = new Bloc(e,null);
		Jointure j = new Jointure(b,0,0);
		bck.addBloc(j);
		difficulte = 3;
	}
	
	/**
	 * Fonction qui renvoie la transaction en attente
	 *
	 * @return La transaction en attente
	*/
	public ArrayList<String> getTransactionEnAttente(){
		return transactionEnAttente;
	}
	
	/**
	 * Fonction qui modifie la transaction en attente 
	 *
	 * @param transactionEnAttente La nouvelle transaction en attente
	*/
	public void setTransactionEnAttente(ArrayList<String> transactionEnAttente){
		this.transactionEnAttente = transactionEnAttente;
	}
	
	/**
	 * Fonction qui renvoie la Blockchaine
	 *
	 * @return La Blockchaine
	*/	
	public Blockchaine getBck(){
		return bck;
	}
	
	/**
	 * Fonction qui modifie la Blockchaine
	 *
	 * @param bck La nouvelle Blockchaine
	*/
	public void setBck(Blockchaine bck){
		this.bck = bck;
	}
	
	/**
	 * Fonction qui renvoie la difficulte
	 *
	 * @return La difficulte
	*/
	public int getDifficulte(){
		return difficulte;
	}
	
	/**
	 * Fonction qui modifie la difficulte courante
	 *
	 * @param difficulte La nouvelle difficulte
	*/
	public void setDifficulte(int difficulte){
		this.difficulte = difficulte;
	}
	
	/**
	 * Fonction qui communique avec le serveur central, qui recupere les transactions saisies par le client et agit en consequence. 
	 *
	 * Si la liste de transaction en attente est vide il attend des transactions sinon il va traiter les transactions qui sont en attente dans cette liste. 
	 * Une fois que la prochaine transaction est recuperee elle est supprimee de la liste. 
	 * On recupere la transaction que le client a saisi puis on recherche un sel avec cette derniere. Puis on envoie au serveur central la transaction avec le sel qu'il a trouve.
	 *
	 * @throws IOException
	*/
	public void manageRequest(MineurHandler minh) throws IOException {
		while(true){
			try{
				setTransactionEnAttente(minh.getTransactionEnAttente());
				for(int i=0; i<getTransactionEnAttente().size(); i++){
					System.out.println("=>"+transactionEnAttente.get(i));
				}
				if(getTransactionEnAttente().size() == 0){
					Thread.sleep(10000);
					System.out.println("Il n'y a plus de transaction en attente");
				}
				else{
					Thread.sleep(10000);
					//on recupere la prochaine transaction
					String transactionEnCours = transactionEnAttente.get(0);
					System.out.println("Tansaction en cours de traitement : "+transactionEnCours);
					//on la supprime de la liste d'attente
					transactionEnAttente.remove(0);
					//on cherche un sel
					String mots[] = transactionEnCours.split(" ");
					//on a la somme, le payeur et le receveur
					int[] transaction = new int[3];
					for(int i=0; i<3; i++)
						transaction[i] = Integer.parseInt(mots[i]);
						
						
					if(((transaction[1] >=0 && transaction[1]<tabCrypto.length) 
						&& (transaction[1] >=0 && transaction[1]<tabCrypto.length))){
						
						if(transaction[1] != transaction[2]){
							int sel = recherche_Sel(transaction,getDifficulte());
							
							//une fois le sel trouve, on cree une chaine qui contient:
							// la somme, le payeur, le receveur et le sel pour les envoyer au serveur
							String sendToServeur = "" + transaction[0] + 
										" " + transaction[1] + 
										" " + transaction[2] + 
										" " + sel;
							out.writeUTF(sendToServeur);
						}
						else
							System.out.println("Vous avez saisi un payeur et un receveur qui sont identique");
					}			
					else
						System.out.println("Vous avez saisi un payeur ou un receveur qui n'existe pas");
					
				}
			} catch(InterruptedException e){
				System.out.println(e);
			}
			
		} 
	}
	
	/**
	 * Fonction qui va rechercher un sel avec une transaction et une difficulte. 
	 * 
	 * On genere un sel qui est un nombre aleatoire. Ensuite, on cree un bloc avec la transaction et on calcule son hash. Si le nombre de zeros successifs que contient le hash est identique a la difficulte alors on renvoie ce sel. Autrement on s'il y a trop de zeros il incremente le sel sinon il le decremente.
	 *
	 * @param transaction Le tableau qui contient la transaction 
	 * @param difficulte La difficulte 
	 * @return Le sel qu'il a trouve
	 */
	public int recherche_Sel(int[] transaction, int difficulte){
		int count=0;
		Random rand = new Random();
		int sel = rand.nextInt(100000000 - 0 + 1) +0;
		
		int taille = bck.Bchaine.size();
		Etat e = bck.getBchaine().get(taille-1).getBlocAinserer().getEtatFinal();
		Transaction t = new Transaction(transaction[0], transaction[1], transaction[2]);
		Bloc b = new Bloc(e,t);
		while(difficulte != count){
			count = 0;
			int h = bck.calculHash(b,sel);
			String hBinary = Integer.toBinaryString(h);
			String hformat = String.format("%32s",hBinary).replaceAll(" ","0");
			for(int i=0; i<32; i++){
				if(hformat.charAt(i) != '1')
					count = count+1;
				else
					break;
			}
			if(count>difficulte){
				sel = sel+1;
			}
			else
				sel = sel-1;
		}
		return sel;
	}
	
	
	/**
	 * Fonction qui recoit et affiche le message envoye par le serveur central en multicast
	 *
	 * @throws IOException 
	 * @return Le message
	 */
	public String printMessage() throws IOException {
		byte[] message = new byte[256];
		DatagramPacket packet = new DatagramPacket(message, message.length);
		multicast.receive(packet);
		String m = new String(packet.getData());
		System.out.println("* Le serveur a valide la transaction : " + m);
		return m;
	}
	
	
	public static void main(String[] args) throws IOException {
		Mineur min = new Mineur("localhost",3333);
		MineurHandler minh = new MineurHandler();
		Thread t = new Thread(minh);
		t.start();
		
		//thread qui met a jour la blockchaine quand le mineur se connecte pour la premier fois au serveur
		Thread t3 = new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					DataInputStream key = new DataInputStream(min.socket.getInputStream());
					
					Blockchaine bc = new Blockchaine();
					int[] tabCrypto = new int[] {6,2,3,4,5};
					Etat et = new Etat(tabCrypto);
					Bloc b = new Bloc(et,null);
					Jointure jo = new Jointure(b,0,0);
					bc.addBloc(jo);
					min.setBck(bc);
					
					String tailleBck = key.readUTF();
					int taille = Integer.parseInt(tailleBck);
					
					for(int index = 0; index <taille; index++){
						String infos = key.readUTF();						
						et = bc.getBchaine().get(index).getBlocAinserer().getEtatFinal();
												
						String mots[] = infos.split(" ");
						int transactionSelHash[] = new int[6];

						for(int index2 = 0; index2<transactionSelHash.length;index2++)
							transactionSelHash[index2] = Integer.parseInt(mots[index2]);
						Transaction t = new Transaction(transactionSelHash[0], transactionSelHash[1], transactionSelHash[2]);
						b = new Bloc(et, t);
						jo = new Jointure(b, transactionSelHash[3], transactionSelHash[4]);
						bc.addBloc(jo);
						
						min.setBck(bc);

					}
				} catch(IOException e){
					System.out.println(e);
				}				
			}
		});
		t3.start();	
		
		//thread qui insere la transaction envoye en multicast par le serveur
		Thread t2 = new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){
					try {
						String message = min.printMessage();
						String mots[] = message.split(" ");
						int[] transaction = new int[5];
						for(int i=0; i<5;i++){
							transaction[i]=Integer.parseInt(mots[i]);
						}
						//transaction[4] = Integer.valueOf(mots[4]);
						int taille = min.getBck().getBchaine().size()-1;
						int[] tab = min.getBck().getBchaine().get(taille).getBlocAinserer().getEtatFinal().getTabCrypto();
						int[] newTab = new int[tab.length];
						for(int i=0; i<tab.length;i++){
							newTab[i] = tab[i];
						}
						
						Etat e = new Etat(newTab);
						Transaction t = new Transaction(transaction[0],transaction[1],transaction[2]);
						Bloc b = new Bloc(e,t);
						Jointure j = new Jointure(b,transaction[3], min.getBck().calculHash(b,transaction[3]));
						if(min.getBck().verif(b) && min.getBck().inserable(b,min.getDifficulte(),transaction[3]))
							min.getBck().addBloc(j);
						min.setDifficulte(transaction[4]);
						System.out.println("===================> Mise à jour de la blockchaine");
						System.out.println(min.getBck().toString());
						System.out.println("<=================================================>");		
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t2.start();
		
		min.manageRequest(minh);		
	}

}
