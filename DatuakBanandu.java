package proiektua;
import java.io.File;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.Reorder;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemovePercentage;
import weka.core.converters.ArffSaver;
import weka.filters.unsupervised.instance.SparseToNonSparse;



public class DatuakBanandu {

	public static void main(String[] args) throws Exception {
		args = new String[]{"/home/eneko/Escritorio/DatuakProba.arff", "/home/eneko/Escritorio/train.arff", "/home/eneko/Escritorio/test.arff"};
			if(args.length==3) {
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
	        Instances test = Filter.useFilter(data, resample);
	
			System.out.println("Trainen instantzia kopurua:"+train.numInstances());
			System.out.println("Testaren instantzia kopurua:"+test.numInstances());
	
			//Arff train errepresentazio bektorialera pasa.
			StringToWordVector filter = new StringToWordVector();
			filter.setAttributeIndices("last");		//Azken atributua
			filter.setInputFormat(train);
			filter.setLowerCaseTokens(true);
	        filter.setWordsToKeep(7000); // Hitz kopurua
	        Instances train2 = Filter.useFilter(data, filter);
			
			
			//Reorder para poner la clase al final
			Reorder reorder = new Reorder();
	        reorder.setAttributeIndices("first-2,4-last,3");
	        reorder.setInputFormat(train2);
	        Instances train3 = Filter.useFilter(train2, reorder);
	        train3.setClassIndex(train.numAttributes()-1);
	
	        ArffSaver as = new ArffSaver();
	        as.setInstances(train3);
	        as.setFile(new File(args[1]));
	        as.writeBatch();
	        
	        ArffSaver asT = new ArffSaver();
	        asT.setInstances(test);
	        asT.setFile(new File(args[2]));
	        asT.writeBatch();
			}
	       
	}

}
