import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServeurCentralHandler implements Runnable {
	private Socket mineur;
	private DataInputStream in;
	private Multicast mult;
	private Blockchaine bck;
	private int difficulte;
	private int[] tabCrypto;
	private long start;
	
	
	public ServeurCentralHandler(Socket mineur) throws IOException {
		this.mineur = mineur;
		in = new DataInputStream(this.mineur.getInputStream());
		mult = new Multicast();
		
		bck = new Blockchaine();
		tabCrypto = new int[] {6,2,3,4,5};
		Etat e = new Etat(tabCrypto);
		Bloc b = new Bloc(e,null);
		Jointure j = new Jointure(b,0,0);
		bck.addBloc(j);
		difficulte = 3;
		
		System.out.println("===================> Initialisation de la blockchaine");
		System.out.println(bck.toString());
		System.out.println("<=================================================>");
		
		start = System.currentTimeMillis();
	}
	
	public int getDifficulte(){
		return difficulte;
	}
	
	public void setDifficulte(int difficulte){
		this.difficulte = difficulte;
	}
	
	public int[] getTabCrypto(){
		return tabCrypto;
	}
	
	@Override
	public void run() {
		while(true){
			try{
				//recupere les 4 entiers envoyes par le mineur : somme, payeur, receveur et sel
				String request = in.readUTF();
				System.out.println(request);
				
				//On stocke les entiers dans un tableau
				String mots[] = request.split(" ");
				int[] transaction = new int[4];
				for(int i=0; i<4;i++)
					transaction[i]=Integer.parseInt(mots[i]);
				
				//on test si la trasaction est cohérente et insérable
				int test = maj(transaction, getDifficulte());
				if(test == 1){
					// 1. j'insère le bloc dans la bloc chaine
					// 2. je l'affiche pour vérifier que c'est bon
					// 3. j'envoie les 5 entiers aux mineurx : somme, payeur, receveur, sel et difficulte
					
					//1.
					Etat e = new Etat(getTabCrypto());
					Transaction t = new Transaction(transaction[0],transaction[1],transaction[2]);
					Bloc b = new Bloc(e,t);
					Jointure j = new Jointure(b,transaction[3], bck.calculHash(b,transaction[3]));
					bck.addBloc(j);
					//2.
					System.out.println("===================> Mise à jour de la blockchaine");
					System.out.println(bck.toString());
					System.out.println("<=================================================>");
					//3.
					String sendToMineur = "";
					sendToMineur = transaction[0] + " " +
							transaction[1] + " " +
							transaction[2] + " " +
							transaction[3] + " " +
							getDifficulte();
					byte[] message = (sendToMineur).getBytes();
					mult.send(message);
					System.out.println("La transaction a ete envoyee a tous les mineurs");
						
				}
			} catch(IOException e) {
				System.out.println(e);
			}
		}
		
	}
	
	public int maj(int[] tr_sel, int difficulte) {
		int res = 0;
		
		boolean isFinished = false;
		//initialisation du time
		long fin = System.currentTimeMillis();
		long temps_ecoule = fin -start;
		
		//on test si le bloc est coherent et inserable :
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
			bck.addBloc(j);
			isFinished = true; 
		}
		
		if(isFinished && temps_ecoule <600000){
			res = 1;
			if(getDifficulte() < 32)
				setDifficulte(getDifficulte()+1);
			isFinished = false;
			start = System.currentTimeMillis();
		}
		
		if(!isFinished && temps_ecoule%600000==0){
			System.out.println("Plus de 10 minutes se sont ecoules");
			setDifficulte(getDifficulte()-1);
		}
		
		return res;
	}
	
}
