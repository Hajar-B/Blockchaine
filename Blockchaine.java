import java.util.ArrayList;

/**
 * Blockchaine est une classe qui contient une liste. Cette derniere est composee de  maillons qui sont des Jointures
 *
 * @autor Sohayla RABHI et Hajar BOUZIANE
*/
public class Blockchaine {
	public ArrayList<Jointure> Bchaine;
	
	/**
	 * Constructeur qui cree une Blockchaine. La liste est vide lorsqu'elle est initialisee.
	 * 
	 */
	public Blockchaine() {
		this.Bchaine = new ArrayList<Jointure>();
	}
	
	/**
	 * Fonction qui renvoie la liste de jointures d'une Blockchaine
	 *
	 * @return La liste de jointures
	*/
	public ArrayList<Jointure> getBchaine() {
		return Bchaine;
	}

	/**
	 * Fonction qui modifie la liste de jointures d'une Blockchaine
	 *
	 * @param bchaine La nouvelle liste de jointures
	*/
	public void setBchaine(ArrayList<Jointure> bchaine) {
		Bchaine = bchaine;
	}
	
	/**
	 * Fonction qui renvoie le hashCode d'une Blockchaine
	 *
	 * @return Le hashCode d'une Blockchaine
	*/
	@Override
	public int hashCode() {
		int resultat = 1;
		if(this.Bchaine == null)
			return resultat * 31 + 0;
		for(int i=0; i<this.Bchaine.size(); i++)
			resultat = resultat * 31 + this.Bchaine.get(i).hashCode();
		return resultat;
	}

	/**
	 * Fonction qui teste l'egalite entre deux objets de type Blockchaine
	 *
	 * @param obj Un objet de type Blockchaine
	 * @return Renvoie true Si obj est identique a une instance de la classe Blockchaine Sinon false
	*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Blockchaine other = (Blockchaine) obj;
		if (Bchaine == null) {
			if (other.Bchaine != null)
				return false;
		} else if (!Bchaine.equals(other.Bchaine))
			return false;
		return true;
	}
	
	/**
	 * Fonction qui affiche une Blockchaine. Elle affiche toutes les Jointures (les blocs suivi de leur sel et hash).
	 *
	 * @return Une chaine de caracteres qui contient la description d'une Blockchaine.
	*/
	@Override
	public String toString() {
		String chaine = "\t * Blockchaine * \n";
		for(int i=0; i<this.Bchaine.size(); i++) 
			chaine = chaine + 
					"\n-------------------------------\n\tBlock numero "+ i 
					+"\n-------------------------------\n" + 
					this.Bchaine.get(i).toString() + " " +this.Bchaine.get(i).getBlocAinserer().hashCode();
		return chaine;
	}
		
	/**
	 * Fonction qui ajoute une Jointure a la Blockchaine:
	 * <ol>
	 * <li> Si la liste est vide alors on ajoute la jointure telle qu'elle est.
	 * <li> Sinon, on retire 'somme' au payeur et on la donne au receveur.
	 * <ol>
	 * 
	*/
	public void addBloc(Jointure j) {
		
		if(this.Bchaine.size() > 0) {
			int somme = j.getBlocAinserer().getTransactionEffectuee().getSomme();
			int payeur = j.getBlocAinserer().getTransactionEffectuee().getPayeur();
			int receveur = j.getBlocAinserer().getTransactionEffectuee().getReceveur();
			int[] tabCrypto = j.getBlocAinserer().getEtatFinal().getTabCrypto();
			tabCrypto[receveur] = tabCrypto[receveur] + somme;
			tabCrypto[payeur] = tabCrypto[payeur] - somme;
		}
		this.Bchaine.add(j);
	}
	
	/**
	 * Fonction qui verifie qu'un bloc est coherent a la Blockchaine. 
	 * <ol>
	 * <li> Si la Blockchaine est vide ET que la transaction du bloc est NULL, alors le bloc est coherent.
	 * <li> Si la Blockchaine n'est pas vide, on recupere le tableau d'entiers du dernier Etat insere.
	 * <li> Avec ce tableau, on verifie que le payeur a suffisamment de monnaie pour effectuer la transaction du bloc
	 * <li> S'il a suffisamment de monnaie, alors le bloc est coherent sinon il ne l'est pas. 
	 * <ol>
	 * 
	 * @param bloc Bloc a verifier avant de l'inserer dans la Blockchaine
	 * @return True si le bloc est coherent, sinon False
	*/
	public boolean verif(Bloc bloc) {
		if(this.Bchaine.size() == 0) {
			if(bloc.getTransactionEffectuee() == null)
				return true;
			else
				return false;
		}
		// --------------------- on recupere le bloc precedent
		Bloc precBloc = this.Bchaine.get(this.Bchaine.size()-1).getBlocAinserer();
		//on recupere la transaction de ce precedent bloc
		Etat EtatPrecBloc = precBloc.getEtatFinal();
		//on recupere le tableau
		int[] tabPrecCrypto = EtatPrecBloc.getTabCrypto();
		
		// --------------------- on recupere l'etat final du bloc a verifier
		Etat etatBloc = bloc.getEtatFinal();
		int[] tabCrypto = etatBloc.getTabCrypto();
		
		//on recupere la transaction du bloc a verifier
		Transaction transactionBloc = bloc.getTransactionEffectuee();
		//on recupere la somme, le payeur, le receveur du bloc a verifier
		int sommeBloc = transactionBloc.getSomme();
		int payeurBloc = transactionBloc.getPayeur();
		int receveurBloc = transactionBloc.getReceveur();
		
		if(sommeBloc > tabPrecCrypto[payeurBloc])
			return false;
		
		return true;
	}
	
	/**
	 * Fonction qui calcule le hash de la jointure que l'on veut inserer dans la Blockchaine. On fait la somme :
	 * <ol>
	 * <li> d'un sel
	 * <li> du hash du bloc
	 * <li> du hash de la precedente jointure
	 * <ol>
	 * 
	 * @param b Bloc a inserer dans la Blockchaine
	 * @param sel Un sel
	 * @return Le hash de la jointure a inserer
	*/
	public int calculHash(Bloc b, int sel) {
		int resultat = 1;
		resultat = resultat * 31 + sel;
		resultat = resultat * 31 + b.hashCode();
		//on prend le dernier element parce que le bloc n'est pas suppose etre insere
		resultat = resultat * 31 + ((this.Bchaine == null || this.Bchaine.size() == 0) ? 0 : this.Bchaine.get(this.Bchaine.size()-1).getHash());
		return resultat;
	}
	
	/**
	 * Fonction qui teste si un bloc b est inserable :
	 * <ol>
	 * <li> On calcule le hash de la jointure qui contient le bloc b a inserer
	 * <li> On convertit le hash en binaire 
	 * <li> On effetctue un affichage sur 32 bits
	 * <li> on compte le nombre de 0 successif qui se situe avant le premier '1'
	 * <li> On compare le compteur a la difficulte : Si ils sont egaux, alors le bloc b est inserable sinon il ne l'est pas.
	 * <ol>
	 * 
	 * @param b Bloc a inserer dans la Blockchaine
	 * @param difficulte Une difficulte
	 * @param sel Un sel
	 * @return True si le bloc b est inserable sinon False
	*/
	public boolean inserable(Bloc b, int difficulte, int sel) {
		int i = 0;
		int count = 0;
		
		int hash = calculHash(b, sel);
		String hashToBinary = Integer.toBinaryString(hash);
		String formatBinaryHash = String.format("%32s", hashToBinary).replaceAll(" ","0");
		
		while(i<32) {
			if(formatBinaryHash.charAt(i) == '1'){
				break;
			}
			count=count+1;
			i++;
		}

		if(difficulte == count)
			return true;
		return false;
	}

	public static void main(String[] args) {
	
		Blockchaine b = new Blockchaine();
		int[] tabCrypto1 = {2,5,1};
		Etat e1 = new Etat(tabCrypto1);
		Bloc b1 = new Bloc(e1, null);
		System.out.println("Verif : bloc b1 coherent ? " + b.verif(b1));
		System.out.println("Inserable : bloc b1 inserable ? " + b.inserable(b1, 6, 4000));
		Jointure j1 = new Jointure (b1, 6, 4000);
		b.addBloc(j1);
		//System.out.println(b.toString());
		
		int[] tabCrypto2 = {4,3,1};
		Etat e2 = new Etat(tabCrypto2);
		Transaction t2 = new Transaction(2,1,0);
		Bloc b2 = new Bloc(e2, t2);
		System.out.println("Verif : bloc b2 coherent ? " + b.verif(b2));
		System.out.println("Inserable : bloc b2 inserable ? " + b.inserable(b2, 3, 4000));
		System.out.println("Inserable : bloc b2 inserable ? " + b.inserable(b2, 6, 4000));
		Jointure j2 = new Jointure (b2, 6, 4000);
		b.addBloc(j2);
		System.out.println(b.toString());
		/*
		int[] tabCrypto1 = {2,5,1};
		Etat e1 = new Etat(tabCrypto1);
		int[] tabCrypto2 = {7,45,0};
		Etat e2 = new Etat(tabCrypto2);
		int[] tabCrypto3 = {4,3,1};
		Etat e3 = new Etat(tabCrypto3);
		int[] tabCrypto4 = {2,0,1};
		Etat e4 = new Etat(tabCrypto4);
		
		//Transaction t1 = new Transaction(2,1,0);
		Transaction t2 = new Transaction(24,2,8);
		Transaction t3 = new Transaction(2,1,0);
		Transaction t4 = new Transaction(24,2,0);
		
		Bloc b1 = new Bloc(e1, null);
		Bloc b2 = new Bloc(e2, t2);
		Bloc b3 = new Bloc(e3, t3);
		Bloc b4 = new Bloc(e4, t4);
		
		Jointure j1 = new Jointure (b1, 1, 6);
		Jointure j2 = new Jointure (b2, 11, 3);
		Jointure j3 = new Jointure (b3, 34, 0);
		Jointure j4 = new Jointure (b4, 10, 3);
		
		Blockchaine b = new Blockchaine();
		//b.addBloc(j1);
		//b.addBloc(j2);
		//b.addBloc(j3);
		//b.addBloc(j4);
		
		//System.out.println(b.toString());	
		//System.out.println(b.equals(b));
		//System.out.println(b.hashCode());
		System.out.println("Bloc inserable b1? " + b.verif(j1.getBlocAinserer()));
		b.addBloc(j1);	
		System.out.println("Bloc inserable b2? " + b.verif(j2.getBlocAinserer()));
		System.out.println("Bloc inserable b3? " + b.verif(j3.getBlocAinserer()));
		b.addBloc(j3);
		System.out.println("Bloc inserable b4? " + b.verif(j4.getBlocAinserer()));
		System.out.println(b.toString());
		*/
	}

}
