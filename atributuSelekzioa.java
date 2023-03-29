package proiektua;

import java.io.File;
import java.io.PrintWriter;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;


public class atributuSelekzioa {

	public static void main(String[] args) throws Exception {
		args = new String[]{"/home/eneko/Escritorio/TextMining/train.arff", "/home/eneko/Escritorio/TextMining/filtered.arff", "/home/eneko/Escritorio/TextMining/hiztegia.txt"};
		if(args.length==3 || args.length==4){
			DataSource source = new DataSource(args[0]);
			Instances data = source.getDataSet();
			data.setClassIndex(data.numAttributes()-1);
			
			InfoGainAttributeEval ig = new InfoGainAttributeEval();	//Atributu ebaluatzailea 
			
			Ranker ranker = new Ranker();		//atributuak ordenatzeko
			if (args.length==4 && Integer.parseInt(args[3])<=300) {
				ranker.setNumToSelect(Integer.parseInt(args[3]));			//atributu kopurua limitatzeko
			}
			else {
				ranker.setNumToSelect(300);
			}
			ranker.setThreshold(0.1);			
			
			AttributeSelection as = new AttributeSelection();
			as.setInputFormat(data);
			as.setEvaluator(ig);
			as.setSearch(ranker);
			Instances filteredData = Filter.useFilter(data, as);
			
			ArffSaver saver = new ArffSaver();
			saver.setInstances(filteredData);
			saver.setFile(new File(args[1]));
			saver.writeBatch();
						
			  
			PrintWriter pw = new PrintWriter(args[2]);				//hiztegia gordetzeko
			
			for (int i=0; i<filteredData.numAttributes()-1; i++) {
				String s = filteredData.attribute(i).name();
	            pw.println(s + ",1");
	        }
	        pw.close();
	        	        
		}
		else {
			System.out.println("3 parametro sartu behar dira: train.arff filtered.arff hiztegia (atributu limitea aukerazkoa da.)");
		}
	}

}
