package proiektua;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DatuakLortu1 {
	//Datuak .csv edo .txt .arff-ra pasatu.
	
		public static void main(String[] args) throws IOException {
			args= new String[] {"C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\0_PHMRC_VAI_redacted_free_text.csv", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\datuak.arff"};
			if(args.length==2) {
				errenkadaHutsakKendu(args[0]);
				PrintWriter pw = new PrintWriter(args[1]);
				pw.println("@relation verbalAutopsy\n");
				pw.println("@attribute module_attr {'', 'neonate', 'child', 'adult'}");
				pw.println("@attribute site_attr {'', 'ap', 'dar', 'up', 'pemba', 'bohol', 'mexico'}");
				pw.println("@attribute gs_text34 {'', 'certain infectious and parasitic diseases', 'neoplasm', 'diabetes', 'epilepsy', 'diseases of the circulatory system', 'diseases of respiratory system', 'diseases of the digestive system', 'renal failure', 'pregnancy, childbirth and the puerperium', 'congenital malformation', 'injury, poisoning and external causes', 'external causes of morbidity and mortality'}");
				pw.println("@attribute sex_attr {'', '1', '2', '8', '9'}");
				pw.println("@attribute age_years_attr integer");
				pw.println("@attribute age_months_attr integer");
				pw.println("@attribute age_days_attr integer");
				pw.println("@attribute open_response string\n");
				pw.println("@data");
				String line;
				Scanner scanner = new Scanner(new File(args[0]));
				line=scanner.nextLine().toLowerCase();
				while (scanner.hasNextLine()) {
					line=scanner.nextLine().toLowerCase();
					line = line.replaceAll("[`'!?*-+=/_\"$&]|\\[|\\]|\\(|\\)", "");
					String[] lerroBanatuta = line.split(";");
					if(line.length()!=0) {
						for (int i = 0; i < lerroBanatuta.length; i++) {
							if(lerroBanatuta[i]=="") {
								lerroBanatuta[i]="?";
							}
						}
						lerroBanatuta[3] = agrupatuKlasea(lerroBanatuta[3]);
						if(lerroBanatuta.length==4) {
							System.out.println(lerroBanatuta[0] + ". linea");
							System.out.println(lerroBanatuta.length + " luzera da.");
						}
						if(lerroBanatuta.length==9) {
							lerroBanatuta[8] = lerroBanatuta[8].replaceAll("0|1|2|3|4|5|6|7|8|9", "");
							pw.println(lerroBanatuta[1] + "," + lerroBanatuta[2] + ",\"" + lerroBanatuta[3] + "\"," + lerroBanatuta[4] + "," + lerroBanatuta[5] + "," + lerroBanatuta[6] + "," + lerroBanatuta[7] + ",\"" + lerroBanatuta[8] + "\"");
						}
						else if(lerroBanatuta.length==5) {
							pw.println(lerroBanatuta[1] + "," + lerroBanatuta[2] + ",\"" + lerroBanatuta[3] + "\"," + lerroBanatuta[4] + ",?" + ",?" + ",?" + ",\" \"");
						}
						else if(lerroBanatuta.length==6) {
							pw.println(lerroBanatuta[1] + "," + lerroBanatuta[2] + ",\"" + lerroBanatuta[3] + "\"," + lerroBanatuta[4] + "," + lerroBanatuta[5] + ",?" + ",?" + ",\" \"");
						}
						else if(lerroBanatuta.length==7) {
							pw.println(lerroBanatuta[1] + "," + lerroBanatuta[2] + ",\"" + lerroBanatuta[3] + "\"," + lerroBanatuta[4] + "," + lerroBanatuta[5] + "," + lerroBanatuta[6] + ",?" + ",\" \"");
						}
						else if(lerroBanatuta.length==8) {
							pw.println(lerroBanatuta[1] + "," + lerroBanatuta[2] + ",\"" + lerroBanatuta[3] + "\"," + lerroBanatuta[4] + "," + lerroBanatuta[5] + "," + lerroBanatuta[6] + "," + lerroBanatuta[7] + ",\" \"");
						}
						else if(lerroBanatuta.length==10) {
							lerroBanatuta[8]= lerroBanatuta[8].replaceAll("0|1|2|3|4|5|6|7|8|9+", "");
							lerroBanatuta[9]= lerroBanatuta[9].replaceAll("0|1|2|3|4|5|6|7|8|9+", "");
							pw.println(lerroBanatuta[1] + "," + lerroBanatuta[2] + ",\"" + lerroBanatuta[3] + "\"," + lerroBanatuta[4] + "," + lerroBanatuta[5] + "," + lerroBanatuta[6] + "," + lerroBanatuta[7] + ",\"" + lerroBanatuta[8] + lerroBanatuta[9] + "\"");
						}
						else if(lerroBanatuta.length==11) {
							lerroBanatuta[8]= lerroBanatuta[8].replaceAll("0|1|2|3|4|5|6|7|8|9", "");
							lerroBanatuta[9]= lerroBanatuta[9].replaceAll("0|1|2|3|4|5|6|7|8|9", "");
							lerroBanatuta[10]= lerroBanatuta[10].replaceAll("0|1|2|3|4|5|6|7|8|9", "");
							pw.println(lerroBanatuta[1] + "," + lerroBanatuta[2] + ",\"" + lerroBanatuta[3] + "\"," + lerroBanatuta[4] + "," + lerroBanatuta[5] + "," + lerroBanatuta[6] + "," + lerroBanatuta[7] + ",\"" + lerroBanatuta[8] + lerroBanatuta[9] + lerroBanatuta[10] + "\"");
						}
					}
					
				}
				scanner.close();
				pw.close();
			}
			else {
				System.out.println("2 argumentu sartu behar dituzu: Datuak.csv datuak.arff");
			}
		}
	
		private static String agrupatuKlasea(String unekoKlasea) {
			String hitzBerria = unekoKlasea;
			if(unekoKlasea.equals("diarrheadysentery") || unekoKlasea.equals("other infectious diseases") || unekoKlasea.equals("aids") || unekoKlasea.equals("sepsis") || unekoKlasea.equals("meningitis") || unekoKlasea.equals("meningitissepsis") || unekoKlasea.equals("malaria") || unekoKlasea.equals("encephalitis") || unekoKlasea.equals("measles") || unekoKlasea.equals("hemorrhagic fever") || unekoKlasea.equals("tb") || unekoKlasea.equals("other non-communicable diseases") || unekoKlasea.equals("other cardiovascular diseases")) {
				hitzBerria = "certain infectious and parasitic diseases";
			}
			else if(unekoKlasea.equals("leukemialymphomas") || unekoKlasea.equals("colorectal cancer") || unekoKlasea.equals("lung cancer") || unekoKlasea.equals("cervical cancer") || unekoKlasea.equals("breast cancer") || unekoKlasea.equals("stomach cancer") || unekoKlasea.equals("prostate cancer") || unekoKlasea.equals("esophageal cancer") || unekoKlasea.equals("other cancers")) {
				hitzBerria = "neoplasm";
			}
			else if(unekoKlasea.equals("stroke") || unekoKlasea.equals("acute myocardial infarction")) {
				hitzBerria = "diseases of the circulatory system";
			}
			else if(unekoKlasea.equals("pneumonia") || unekoKlasea.equals("asthma") || unekoKlasea.equals("copd")){
				hitzBerria = "diseases of respiratory system";
			}
			else if(unekoKlasea.equals("cirrhosis") || unekoKlasea.equals("other digestive diseases") ) {
				hitzBerria = "diseases of the digestive system";
			}
			else if(unekoKlasea.equals("preterm delivery") || unekoKlasea.equals("stillbirth") || unekoKlasea.equals("maternal") || unekoKlasea.equals("birth asphyxia") ) {
				hitzBerria = "pregnancy, childbirth and the puerperium";
			}
			else if(unekoKlasea.equals("bite of poisoning animal") || unekoKlasea.equals("poisonings")|| unekoKlasea.equals("bite of venomous animal") ) {
				hitzBerria = "injury, poisoning and external causes";
			}
			else if(unekoKlasea.equals("road traffic") || unekoKlasea.equals("falls") || unekoKlasea.equals("homicide") || unekoKlasea.equals("fires")|| unekoKlasea.equals("drowning")|| unekoKlasea.equals("suicide")|| unekoKlasea.equals("violent death")|| unekoKlasea.equals("other injuries") || unekoKlasea.equals("other defined causes of child deaths")) {
				hitzBerria = "external causes of morbidity and mortality";
			}
			return hitzBerria;
	}

		private static void errenkadaHutsakKendu(String text) throws IOException {
			File file = new File(text);
	    	Scanner scanner = new Scanner(file);
	    	
	    	String textuBerria = "";
	    	String linea = scanner.nextLine();
	    	textuBerria += linea;
	    	
	    	while(scanner.hasNextLine()){
	    		linea = scanner.nextLine();
	    		    		
	            // Errenkada hutsak kentzeko
	    		//4035 adult eta 2006 child: Eskuz kendu
	    		if(linea.length()==0 || !Character.isDigit(linea.charAt(0))) {
	    			linea = linea.replaceAll("\n", "").replaceAll("\r", "");
	    		}
	    		else {
	                textuBerria += "\n" + linea;
	    		}
	    	}
	    	FileWriter fileWriter = new FileWriter(text);
	        fileWriter.write(textuBerria);
	        fileWriter.close();
		}

}
