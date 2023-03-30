package proiektua;
import java.io.File;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.Reorder;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;
import weka.core.converters.ArffSaver;



public class DatuakBanandu3 {

	public static void main(String[] args) throws Exception {
		//Adibidez:
		args = new String[]{"C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\trainRAW.arff", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\train.arff", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\dev.arff","C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\hiztegia.txt"};
			if(args.length==4) {
			DataSource source = new DataSource(args[0]);
			Instances data = source.getDataSet();
			data.setClassIndex(2);
			
			Resample resample = new Resample();
	        resample.setInputFormat(data);
	        resample.setSampleSizePercent(70);
	        resample.setRandomSeed(21);
	        resample.setBiasToUniformClass(0);
	        resample.setNoReplacement(true);
	        resample.setInvertSelection(false);
	        //train egin
	        Instances train = Filter.useFilter(data, resample);
	
	        resample.setInputFormat(data);
	        resample.setInvertSelection(true);
	        //test egin
	        Instances dev = Filter.useFilter(data, resample);
	
			System.out.println("Trainen instantzia kopurua:"+train.numInstances());
			System.out.println("Testaren instantzia kopurua:"+dev.numInstances());
	
			StringToWordVector filter = new StringToWordVector();
			filter.setAttributeIndices("last");		//Azken atributua
			filter.setDictionaryFileToSaveTo(new File(args[3]));
			filter.setLowerCaseTokens(true);
			filter.setTFTransform(false);
			filter.setIDFTransform(false);
	        filter.setWordsToKeep(10000); // Hitz kopurua
	        filter.setPeriodicPruning(-1.0);
	        filter.setOutputWordCounts(false);
			filter.setInputFormat(train);
	        train = Filter.useFilter(train, filter);
	        
			Reorder reorder = new Reorder();
	        reorder.setAttributeIndices("first-2,4-last,3");
	        reorder.setInputFormat(train);
	        train = Filter.useFilter(train, reorder);
	        train.setClassIndex(train.numAttributes()-1);
	        
	        Reorder reorder2 = new Reorder();
	        reorder2.setAttributeIndices("first-2,4-last,3");
	        reorder2.setInputFormat(dev);
	        dev = Filter.useFilter(dev, reorder2);
	        dev.setClassIndex(dev.numAttributes()-1);
	
	        ArffSaver as = new ArffSaver();
	        as.setInstances(train);
	        as.setFile(new File(args[1]));
	        as.writeBatch();
	        
	        ArffSaver asT = new ArffSaver();
	        asT.setInstances(dev);
	        asT.setFile(new File(args[2]));
	        asT.writeBatch();
			}
			else {
				System.out.println("3 argumentu sartu behar dituzu: Datuak.arff fitxategia, train, eta dev fitxategia");
			}
	       
	}

}