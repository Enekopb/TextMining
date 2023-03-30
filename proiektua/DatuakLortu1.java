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
				String gs_text = klaseakLortu(args[0]);
				gs_text = gs_text.replaceAll("\\[|\\]|/", "");
				PrintWriter pw = new PrintWriter(args[1]);
				pw.println("@relation verbalAutopsy\n");
				pw.println("@attribute module_attr {'', 'neonate', 'child', 'adult'}");
				pw.println("@attribute site_attr {'', 'ap', 'dar', 'up', 'pemba', 'bohol', 'mexico'}");
				pw.println("@attribute gs_text34 {''," + gs_text + "}");
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

		private static String klaseakLortu(String text) throws FileNotFoundException {
			String line;
			ArrayList<String> lista = new ArrayList<String>();
			Scanner scanner = new Scanner(new File(text));
			line=scanner.nextLine().toLowerCase();
			while(scanner.hasNextLine()) {
				line=scanner.nextLine().toLowerCase();
				String[] lerroBanatuta = line.split(";");
				if(!lista.contains("\'" + lerroBanatuta[3] + "\'")) {
					lista.add("\'" + lerroBanatuta[3] + "\'");
				}

			}
			return lista.toString();
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
