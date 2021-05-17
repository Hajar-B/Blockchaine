/**
 * Transaction est une classe qui contient un triplet d'entiers :
 * <ol>
 * <li> la somme 
 * <li> le payeur
 * <li> le receveur
 * <ol>
 *
 * @autor Sohayla RABHI et Hajar BOUZIANE
*/
public class Transaction {
	private int somme;
	private int payeur;
	private int receveur;
	
	/**
	 * Constructeur qui cree une Transaction
	 * 
	 * @param somme un entier
	 * @param payeur un entier
	 * @param receveur un entier
	 */
	public Transaction(int somme, int payeur, int receveur) {
		this.somme = somme;
		this.payeur = payeur;
		this.receveur = receveur;
	}
	
	/**
	 * Fonction qui renvoie la somme de la Transaction
	 *
	 * @return La somme de la Transaction
	*/
	public int getSomme() {
		return somme;
	}
	
	/**
	 * Fonction qui modifie la somme de la Transaction
	 *
	 * @param somme La somme de la Transaction
	*/
	public void setSomme(int somme) {
		this.somme = somme;
	}
	
	/**
	 * Fonction qui renvoie le payeur de la Transaction
	 *
	 * @return Le payeur de la Transaction
	*/
	public int getPayeur() {
		return payeur;
	}
	
	/**
	 * Fonction qui modifie le payeur de la Transaction 
	 *
	 * @param payeur Le payeur de la Transaction
	*/
	public void setPayeur(int payeur) {
		this.payeur = payeur;
	}
	
	/**
	 * Fonction qui renvoie le receveur de la Transaction
	 *
	 * @return Le receveur de la Transaction
	*/
	public int getReceveur() {
		return receveur;
	}
	
	/**
	 * Fonction qui modifie le receveur de la Transaction
	 *
	 * @param receveur Le receveur de la Transaction
	*/
	public void setReceveur(int receveur) {
		this.receveur = receveur;
	}
	
	/**
	 * Fonction qui renvoie le hashCode d'une Transaction
	 *
	 * @return Le hashCode d'une Transaction
	*/
	@Override
	public int hashCode() {
		int resultat = 1;
		resultat = resultat * 31 + somme;
		resultat = resultat * 31 + payeur;
		resultat = resultat * 31 + receveur;
		return resultat;
	}
	
	/**
	 * Fonction qui teste l'egalite entre deux objets de type Transaction
	 *
	 * @param obj Un objet de type Transaction
	 * @return Renvoie true Si obj est identique a une instance de la classe Transaction Sinon false
	*/
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if(! (obj instanceof Transaction))
			return false;
		Transaction t = (Transaction) obj;
		if(this.somme != t.somme)
			return false;
		if(this.payeur != t.payeur)
			return false;
		if(this.receveur != t.receveur)
			return false;
		return true;
	}
	
	/**
	 * Fonction qui affiche le triplet d'entiers d'une Transaction
	 *
	 * @return Une chaine de caracteres qui contient la description d'une Transaction
	*/
	@Override
	public String toString() {
		String chaine = "\t * Transaction * \t\n";
		chaine = chaine + "[somme : "+ this.somme + "] ";
		chaine = chaine + "[payeur : "+ this.payeur + "] ";
		chaine = chaine + "[receveur :"+ this.receveur + "] ";
		return chaine;
	}
	
	public static void main(String[] args) {
		//affichage d'une transaction
		System.out.println("-_-_-_-_-_-_ Test affichage : \n");
		Transaction t1 = new Transaction(2,1,0);
		System.out.println(t1.toString());
		
		//test egalite
		System.out.println("\n-_-_-_-_-_-_ Test egalite : ");
		Transaction t2 = new Transaction(24,2,8);
		Transaction t3 = new Transaction(24,2,8);
		Transaction t4 = new Transaction(24,2,0);
		
		System.out.println(t2.equals(t3));
		System.out.println(t3.equals(t4));
		
		//test hashCode
		System.out.println("\n-_-_-_-_-_-_ Test hashCode : ");
		System.out.println(t2.hashCode());
		System.out.println(t3.hashCode());
		System.out.println(t4.hashCode());
	}

}
