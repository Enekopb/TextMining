package proiektua;

import java.io.File;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Reorder;


public class HeaderBerdinduTest6 {
	public static void main(String[] args) throws Exception {
		args = new String[] {};
		//NaiveBayes erabiliz train-rekin entrenatu eta dev ebaluatu, gero SVM-rekin konparatzeko.
		args = new String[] {"C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\hiztegiaFSS.txt", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\testRAW.arff", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\testBoW.arff"};
		if(args.length==3) {
			DataSource source = new DataSource(args[1]);
			Instances dev = source.getDataSet(); 
			dev.setClassIndex(2);
							
			Remove remove = new Remove();
			remove.setAttributeIndices("6");
			remove.setInputFormat(dev);
			dev = Filter.useFilter(dev, remove);
			
			FixedDictionaryStringToWordVector hiztegia = new FixedDictionaryStringToWordVector();
			hiztegia.setDictionaryFile(new File(args[0]));
			hiztegia.setInputFormat(dev);
			dev = Filter.useFilter(dev, hiztegia);
			
			Reorder reorder = new Reorder();
	        reorder.setAttributeIndices("first-2,4-last,3");
	        reorder.setInputFormat(dev);
	        dev = Filter.useFilter(dev, reorder);
	        dev.setClassIndex(dev.numAttributes()-1);
							
			ArffSaver as = new ArffSaver();
	        as.setInstances(dev);
	        as.setDestination(new File(args[2]));
	        as.setFile(new File(args[2]));
	        as.writeBatch();
			
		}else {
			System.out.println("3 parametro sartu behar dira: hiztegiaFSS.txt test.arff testBow.arff");
		}
	}
}
