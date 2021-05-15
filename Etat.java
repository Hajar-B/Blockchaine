public class Etat {
	private int[] tabCrypto;
	
	public Etat(int[] tabCrypto) {
		this.tabCrypto = tabCrypto;
	}

	public int[] getTabCrypto() {
		return tabCrypto;
	}
	
	@Override
	public int hashCode() {
		int resultat = 1;
		
		for(int i=0; i < this.tabCrypto.length; i++)
			resultat = resultat * 31 + this.tabCrypto[i];
		
		return resultat;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if(! (obj instanceof Etat))
			return false;
		Etat e = (Etat) obj;
		if(this.tabCrypto.length == e.tabCrypto.length) {
			for(int i=0; i<e.tabCrypto.length;i++) {
				if(e.tabCrypto[i] != this.tabCrypto[i])
					return false;
			}
		}
		else
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		String chaine = "\t * Etat * \t\n";
		for(int i=0; i<this.tabCrypto.length; i++) {
			chaine = chaine + "[personne : "+ i +" | monnaie : "+ this.tabCrypto[i]+"]\n";
		}
		return chaine;
	}

	public static void main(String[] args) {
		//affichage d'un etat
		System.out.println("-_-_-_-_-_-_ Test affichage : \n");
		int[] tabCrypto1 = {2,5,1};
		Etat e1 = new Etat(tabCrypto1);
		System.out.println(e1.toString());
		
		//test egalite
		System.out.println("-_-_-_-_-_-_ Test egalite : ");
		int[] tabCrypto2 = {7,45,0};
		Etat e2 = new Etat(tabCrypto2);
		
		int[] tabCrypto3 = {7,45,0};
		Etat e3 = new Etat(tabCrypto3);
		
		int[] tabCrypto4 = {2,0,1};
		Etat e4 = new Etat(tabCrypto4);
		
		System.out.println(e2.equals(e3));
		System.out.println(e3.equals(e4));
		
		//test hashCode
		System.out.println("\n-_-_-_-_-_-_ Test hashCode : ");
		System.out.println(e2.hashCode());
		System.out.println(e3.hashCode());
		System.out.println(e4.hashCode());
	}

}
