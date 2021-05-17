/**
 * Jointure est une classe qui contient :
 * <ol>
 * <li> un bloc a inserer 
 * <li> un sel
 * <li> un hash
 * <ol>
 *
 * @autor Sohayla RABHI et Hajar BOUZIANE
*/
public class Jointure {
	private Bloc blocAinserer;
	private int sel;
	private int hash;
	
	/**
	 * Constructeur qui cree une Jointure (qui est un noeud de la Blockchaine)
	 * 
	 * @param blocAinserer Un bloc a inserer
	 * @param sel Le sel de la Jointure
	 * param hash Le hash de la Jointure
	 */
	public Jointure(Bloc blocAinserer, int sel, int hash) {
		this.blocAinserer = blocAinserer;
		this.sel = sel;
		this.hash = hash;
	}
	
	/**
	 * Fonction qui renvoie le bloc a inserer d'une Jointure
	 *
	 * @return Un bloc a inserer
	*/
	public Bloc getBlocAinserer() {
		return blocAinserer;
	}
	
	/**
	 * Fonction qui modifie le bloc a inserer d'une Jointure
	 *
	 * @param blocAinserer Le bloc a inserer
	*/
	public void setBlocAinserer(Bloc blocAinserer) {
		this.blocAinserer = blocAinserer;
	}
	
	/**
	 * Fonction qui renvoie le sel d'une Jointure
	 *
	 * @return Un sel d'une Jointure
	*/
	public int getSel() {
		return sel;
	}
	
	/**
	 * Fonction qui modifie le sel d'une Jointure
	 *
	 * @param sel Le sel d'une Jointure
	*/
	public void setSel(int sel) {
		this.sel = sel;
	}
	
	/**
	 * Fonction qui renvoie le hash d'une Jointure
	 *
	 * @return Le hash d'une Jointure
	*/
	public int getHash() {
		return hash;
	}
	
	/**
	 * Fonction qui modifie le hash d'une Jointure
	 *
	 * @param hash Le hash d'une Jointure
	*/
	public void setHash(int hash) {
		this.hash = hash;
	}

	/**
	 * Fonction qui renvoie le hashCode d'une Jointure
	 *
	 * @return Le hashCode d'une Jointure
	*/
	@Override
	public int hashCode() {
		int resultat = 1;
		resultat = resultat * 31 + ((this.blocAinserer == null) ? 0 : this.blocAinserer.hashCode());
		resultat = resultat * 31 + this.sel;
		resultat = resultat * 31 + this.hash;
		return resultat;
	}	
	
	/**
	 * Fonction qui teste l'egalite entre deux objets de type Jointure
	 *
	 * @param obj Un objet de type Jointure
	 * @return Renvoie true Si obj est identique a une instance de la classe Jointure Sinon false
	*/
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if(! (obj instanceof Jointure))
			return false;
		
		Jointure j = (Jointure) obj;
		if((j.blocAinserer == null && this.blocAinserer != null) 
				|| (j.blocAinserer != null && this.blocAinserer == null))
					return false;
		if(!(j.blocAinserer.equals(this.blocAinserer)))
			return false;
		if(j.sel != this.sel)
			return false;
		if(j.hash != this.hash)
			return false;
		return true;
	}
	
	/**
	 * Fonction qui affiche le bloc a inserer, le sel et le hash d'une Jointure 
	 *
	 * @return Une chaine de caracteres qui contient la description d'une Jointure
	*/
	@Override
	public String toString() {
		return this.blocAinserer.toString() + "\n" + "\n\t[sel : " + sel + " | hash : " + hash + "]\n"; 
	}
	
	public static void main(String[] args) {
		//test affichage
		System.out.println("-_-_-_-_-_-_ Test affichage : \n");
		int[] tabCrypto1 = {2,5,1};
		Etat e1 = new Etat(tabCrypto1);
		Transaction t1 = new Transaction(2,1,0);
		Bloc b1 = new Bloc(e1, t1);
		Jointure j1 = new Jointure (b1, 1, 0);
		System.out.println(j1.toString());
		
		//test egalite
		System.out.println("\n-_-_-_-_-_-_ Test egalite : ");
		int[] tabCrypto2 = {7,45,0};
		Etat e2 = new Etat(tabCrypto2);
		int[] tabCrypto3 = {7,45,0};
		Etat e3 = new Etat(tabCrypto3);
		int[] tabCrypto4 = {2,0,1};
		Etat e4 = new Etat(tabCrypto4);
		Transaction t2 = new Transaction(24,2,8);
		Transaction t3 = new Transaction(24,2,8);
		Transaction t4 = new Transaction(24,2,0);
		Bloc b2 = new Bloc(e2, t2);
		Bloc b3 = new Bloc(e3, t3);
		Bloc b4 = new Bloc(e4, t4);
		Jointure j2 = new Jointure (b2, 1, 0);
		Jointure j3 = new Jointure (b3, 1, 0);
		Jointure j4 = new Jointure (b4, 10, 3);
				
		System.out.println(j2.equals(j3));
		System.out.println(j3.equals(j4));
				
		//test hashCode
		System.out.println("\n-_-_-_-_-_-_ Test hashCode : ");
		System.out.println(j2.hashCode());
		System.out.println(j3.hashCode());
		System.out.println(j4.hashCode());
	}

}
