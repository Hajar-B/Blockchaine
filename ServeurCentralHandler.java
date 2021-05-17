import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


/**
 * ServeurCentralHandler est une classe qui implemente le run d'un Thread. Cette classe permet :
 * <ol>
 * <li> Il initialise un blockchaine
 * <li> Il attend de recevoir les transactions envoyes par les mineurs
 * <li> Il verifie que les blocs crees a partir de ces transaction soient coherents et inserables
 * <li> Il incremente une difficulte si moins de 10 minutes se sont ecoulees lors du traitement d'une transaction, sinon il la decremente
 * <li> Si le bloc est coherent et inserable, il l'ajoute a la Blockchaine et il renvoie la transaction avec le sel et la nouvelle difficulte
 * <ol>
 *
 * @autor Sohayla RABHI et Hajar BOUZIANE
*/
public class ServeurCentralHandler implements Runnable {
	private Socket mineur;
	private DataInputStream in;
	private DataOutputStream out;
	private Multicast mult;
	private Blockchaine bck;
	private int difficulte;
	private int[] tabCrypto;
	private long start;
	private static ArrayList<ServeurCentralHandler> listeMineur = new ArrayList<>();

	
	/**
	 * Constructeur qui cree un ServeurCentralHandler. 
	 *
	 * <ol>
	 * <li> Si la liste de mineur est vide, alors on insere un sel bloc a la blockchaine (avec un transaction NULL).
	 * <li> Sinon, on insere a chaque nouveaux mineurs, tous les blocs qui ont ete insere a la blockchaine du serveur central avant qu'ils soient connectes.
	 * <li> on initialise le temps ainsi que la difficulte
	 * <ol> 
	 *
	 * @param mineur Un socket 
	 * @param listeMineur La liste des mineurs qui sont connectees au serveur central
	 * @throws IOException 
	 */
	public ServeurCentralHandler(Socket mineur, ArrayList<ServeurCentralHandler> listeMineur) throws IOException {
		this.mineur = mineur;
		in = new DataInputStream(this.mineur.getInputStream());
		out = new DataOutputStream(this.mineur.getOutputStream());
		mult = new Multicast();
		
		if(listeMineur.size() == 0){
			bck = new Blockchaine();
			tabCrypto = new int[] {6,2,3,4,5};
			Etat e = new Etat(tabCrypto);
			Bloc b = new Bloc(e,null);
			Jointure j = new Jointure(b,0,0);
			bck.addBloc(j);
		}
		else{
			bck = misajourbckmineur(listeMineur.get(0));
		}
		difficulte = 3;
		this.listeMineur = listeMineur;
		start = System.currentTimeMillis();
	}
	
	/**
	 * Fonction qui renvoie la difficulte courante
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
	 * Fonction qui renvoie le premier tableau de cryptomonnaie
	 *
	 * @return Le tableau de cryptomonnaie
	*/
	public int[] getTabCrypto(){
		return tabCrypto;
	}
	
	/**
	 * Fonction qui renvoie la blockchaine
	 *
	 * @return La blockchaine
	*/
	public Blockchaine getBck(){
		return bck;
	}
	
	/**
	 * Fonction qui modifie la blockchaine
	 *
	 * @param bck La nouvelle blockchaine
	*/
	public void setBck(Blockchaine bck){
		this.bck = bck;
	}
	
	/**
	 * Fonction qui met a jour la blockchaine de chaque nouveaux mineurs qui se connectent au serveur Central. 
	 * De plus, elle envoie cette meme blockchaine au mineur pour qu'il puisse avoir chacun une blockchaine qui soit initialisee comme celle de serveur central.
	 *
	 * @param m Le premier mineur qui se soit connecte au serveur central
	 * @return La nouvelle blockchaine du nouveau mineur
	*/
	public Blockchaine misajourbckmineur(ServeurCentralHandler m){
		Blockchaine bck = new Blockchaine();
		Blockchaine b = m.getBck();
		String sendToMineur = new String(); 
		sendToMineur = Integer.toString(b.getBchaine().size());
		try{
			out.writeUTF(sendToMineur);
		}catch(IOException e){
			System.out.println(e);
		}
		for(int i = 0; i<b.getBchaine().size();i++){
			bck.addBloc(b.getBchaine().get(i));
			
			if(i>0){
				sendToMineur = Integer.toString(b.getBchaine().get(i).getBlocAinserer().getTransactionEffectuee().getSomme()) + " " +
						Integer.toString(b.getBchaine().get(i).getBlocAinserer().getTransactionEffectuee().getPayeur()) + " " +
						Integer.toString(b.getBchaine().get(i).getBlocAinserer().getTransactionEffectuee().getReceveur()) + " " +
						Integer.toString(b.getBchaine().get(i).getSel()) + " " +
						Integer.toString(b.getBchaine().get(i).getHash()) + " " +
						Integer.toString(b.Bchaine.get(i).getBlocAinserer().hashCode()) + " " ;
				try{
					out.writeUTF(sendToMineur);
				}catch(IOException e){
					System.out.println(e);
				}	
			}
			
		}		
		return bck;
	}
		
	/**
	 * Fonction qui communique avec le mineur et agit en consequence.
	 * <ol>
	 * <li> On recupere les 4 entiers envoyes par un mineur (somme, payeur, receveur et sel)
	 * <li> On teste si la transaction est coherente et inserable apres l'avoir insere dans bloc de la fonction maj()
	 * <li> Si c'est coherent et inserable, alors on cree une jointure et on l'insere a la blockchaine
	 * <li> On ajoute cette jointure a tous les mineurs connectes au serveur central
	 * <li> On affiche l'etat de la blockchaine
	 * <li> On envoie (avec une connexion multicast) 5 entiers a tous les mineurs (somme, payeur, receveur, sel et difficulte)
	 * <ol>
	*/
	@Override
	public void run() {
		while(true){
			try{
				//recupere les 4 entiers envoyes par le mineur : somme, payeur, receveur et sel
				String request = in.readUTF();
				System.out.println("Reception d'une transaction potentiellement coherente et inserable : " + request);
				
				//On stocke les entiers dans un tableau
				String mots[] = request.split(" ");
				int[] transaction = new int[4];
				for(int i=0; i<4;i++)
					transaction[i]=Integer.parseInt(mots[i]);
				
				//on test si la trasaction est cohérente et insérable
				int test = maj(transaction, getDifficulte());
				System.out.println("test = " + test);
				if(test == 1){
					// 1. j'insère le bloc dans la bloc chaine
					// 2. je l'affiche pour vérifier que c'est bon
					// 3. j'envoie les 5 entiers aux mineurx : somme, payeur, receveur, sel et difficulte
					
					//1.
					int taille = bck.Bchaine.size()-1;
					int[] tab = bck.getBchaine().get(taille).getBlocAinserer().getEtatFinal().getTabCrypto();
					int[] newTab = new int[tab.length];
					for(int i=0; i<tab.length;i++){
						newTab[i] = tab[i];
					}
								
					Etat e = new Etat(newTab);
					Transaction t = new Transaction(transaction[0],transaction[1],transaction[2]);
					Bloc b = new Bloc(e,t);
					Jointure j = new Jointure(b,transaction[3], bck.calculHash(b,transaction[3]));
					
					for(int i=0; i<listeMineur.size(); i++){
							listeMineur.get(i).getBck().addBloc(j);
					}
					
					//2.
					System.out.println("===================> Mise à jour de la blockchaine");
					System.out.println(bck.toString());
					System.out.println("<=================================================>");
					
					//3.			
					String sendToMineur = new String(); 
					sendToMineur = Integer.toString(transaction[0]) + " " +
								Integer.toString(transaction[1]) + " " +
								Integer.toString(transaction[2]) + " " +
								Integer.toString(transaction[3]) + " " +
								Integer.toString(getDifficulte()) + " ";
					
					byte[] message = (sendToMineur).getBytes();
					mult.send(message);
					System.out.println("La transaction a ete envoyee a tous les mineurs");			
				}
				
			} catch(IOException e) {
				System.out.println(e);
			}
		}
		
	}
	
	
	/**
	 * Fonction qui verifie si avec la transaction que le mineur a envoye au serveur, que le bloc est coherent et inserable. Si le dernier bloc a ete insere il y a moins de 10 minutes alors on incremente la difficulte sinon on la decremente.
	 *
	 * @param tr_sel Le tableau qui contient la transaction 
	 * @param difficulte La difficulte avant la verification du bloc 
	 * @return 1 si le bloc est coherent et inserable sinon 0
	 */
	public int maj(int[] tr_sel, int difficulte) {
		int res = 0;
		
		boolean isFinished = false;
		//initialisation du time
		long fin = System.currentTimeMillis();
		long temps_ecoule = fin -start;
		
		//on teste si le bloc est coherent et inserable :
		//1. on recupere le dernier etat
		//2. on cree une nouvelle transaction avec le tableau tr_sel
		//3. on cree un bloc avec etat + transaction
		//4. on test le bloc
		//5. si succes : on cree la jointure et on l'insere Sinon on attend un autre sel
		int taille = bck.Bchaine.size();
		// 1.
		Etat e = bck.getBchaine().get(taille-1).getBlocAinserer().getEtatFinal();
		// 2.
		Transaction t = new Transaction(tr_sel[0], tr_sel[1], tr_sel[2]);
		// 3.
		Bloc b = new Bloc(e,t);
		// 4 et 5.
		if(bck.verif(b) && bck.inserable(b,getDifficulte(),tr_sel[3])){
			int hash = bck.calculHash(b,tr_sel[3]);
			Jointure j = new Jointure(b,tr_sel[3],hash);
			isFinished = true; 
		}
		
		if(isFinished && temps_ecoule <600000){
			res = 1;
			if(getDifficulte() < 32)
				setDifficulte(getDifficulte()+1);
			isFinished = false;
			start = System.currentTimeMillis();
		}
		
		if(isFinished && temps_ecoule >= 600000){
			res = 1;
			setDifficulte(getDifficulte()-1);
			isFinished = false;
			start = System.currentTimeMillis();
		}
		
		return res;
	}
	
}
