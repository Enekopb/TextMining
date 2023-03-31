package proiektua;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;


public class atributuSelekzioa4 {

	public static void main(String[] args) throws Exception {
		args = new String[]{"C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\train.arff", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\trainFSS.arff", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\hiztegia.txt","C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\hiztegiaFSS.txt"};
		if(args.length==3 || args.length==4){
			DataSource source = new DataSource(args[0]);
			Instances data = source.getDataSet();
			data.setClassIndex(data.numAttributes()-1);
			
			InfoGainAttributeEval ig = new InfoGainAttributeEval();	//Atributu ebaluatzailea 
			
			Ranker ranker = new Ranker();		//atributuak ordenatzeko
			ranker.setNumToSelect(300);
			ranker.setThreshold(0.1);			
			
			AttributeSelection as = new AttributeSelection();
			as.setEvaluator(ig);
			as.setSearch(ranker);
			as.setInputFormat(data);
			Instances filteredData = Filter.useFilter(data, as);
			
			/*SparseToNonSparse filterNonSparse = new SparseToNonSparse();
            filterNonSparse.setInputFormat(filteredData);
            filteredData = Filter.useFilter(filteredData,filterNonSparse);*/
			
			ArffSaver saver = new ArffSaver();
			saver.setInstances(filteredData);
			saver.setFile(new File(args[1]));
			saver.writeBatch();						
			  
			FileWriter fw = new FileWriter(new File(args[3]));			//hiztegiaFSS gordetzeko
						
			/*ArrayList<String> lista = new ArrayList<String>();
			lista.add("module_attr");
			lista.add("age_years_attr");
			lista.add("site_attr");
			lista.add("sex_attr");
			lista.add("age_days_attr");
			lista.add("age_months_attr");
			lista.add("gs_text34");*/
			for (int i = 0; i < filteredData.numAttributes()-1; i++) {
				Scanner sc = new Scanner(new File(args[2]));
				String line = sc.nextLine();
				String s = filteredData.attribute(i).name();
				while(sc.hasNextLine()) {
					line = sc.nextLine();
					String[] hitza = line.split(",");
					if(s.equals(hitza[0])) {
			            fw.write(line + "\n");
					}
				}
			}
			fw.close();	        	        
		}
		else {
			System.out.println("4 parametro sartu behar dira: train.arff filtered.arff hiztegia.txt hiztegiaFSS.txt");
		}
	}

}