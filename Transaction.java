public class Transaction {
	private int somme;
	private int payeur;
	private int receveur;
	
	public Transaction(int somme, int payeur, int receveur) {
		this.somme = somme;
		this.payeur = payeur;
		this.receveur = receveur;
	}
	
	public int getSomme() {
		return somme;
	}

	public void setSomme(int somme) {
		this.somme = somme;
	}

	public int getPayeur() {
		return payeur;
	}

	public void setPayeur(int payeur) {
		this.payeur = payeur;
	}

	public int getReceveur() {
		return receveur;
	}

	public void setReceveur(int receveur) {
		this.receveur = receveur;
	}
	
	@Override
	public int hashCode() {
		int resultat = 1;
		resultat = resultat * 31 + somme;
		resultat = resultat * 31 + payeur;
		resultat = resultat * 31 + receveur;
		return resultat;
	}
	
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
