package stockage;

public class StudentStatut {
	
   private static String idEtudiant;
	
	public StudentStatut(String idEtudiant) {
		this.idEtudiant = idEtudiant;
	}
	 
	public static void setIdStudent(String id) {
	      idEtudiant = id;
	}
	
	 public static String getIdStudent() {
		 return idEtudiant;
	}
	
}
