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
	
	public ArrayList<String> getTransactionEnAttente(){
		return transactionEnAttente;
	}
	
	public void setTransactionEnAttente(ArrayList<String> transactionEnAttente){
		this.transactionEnAttente = transactionEnAttente;
	}
		
	public Blockchaine getBck(){
		return bck;
	}
	
	public void setBck(Blockchaine bck){
		this.bck = bck;
	}
	
	public int getDifficulte(){
		return difficulte;
	}
	
	public void setDifficulte(int difficulte){
		this.difficulte = difficulte;
	}

	public void manageRequest(MineurHandler minh) throws IOException {
		while(true){
			try{
				setTransactionEnAttente(minh.getTransactionEnAttente());
				for(int i=0; i<getTransactionEnAttente().size(); i++){
					System.out.println("=>"+transactionEnAttente.get(i));
				}
				if(getTransactionEnAttente().size() == 0){
					Thread.sleep(5000);
					System.out.println("Il n'y a plus de transaction en attente");
				}
				else{
					Thread.sleep(5000);
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
	
	public void rechercheInsertion(int[] transaction, int difficulte){
		int sel = recherche_Sel(transaction, difficulte);
		int taille = bck.Bchaine.size();
		
		Etat e = bck.getBchaine().get(taille-1).getBlocAinserer().getEtatFinal();
		Transaction t = new Transaction(transaction[0], transaction[1], transaction[2]);
		Bloc b = new Bloc(e,t);
		Jointure j = new Jointure(b,sel, bck.calculHash(b,sel));
		if(bck.verif(b) && bck.inserable(b,difficulte,sel))
			System.out.println("Le bloc est inserable");
	} 
	
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
