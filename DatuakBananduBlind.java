package proiektua;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

public class DatuakBananduBlind {
	
		public static void main(String[] args) throws Exception {
			args = new String[] {"/home/eneko/Escritorio/TextMining/datuak.txt", "/home/eneko/Escritorio/TextMining/train.arff", "/home/eneko/Escritorio/TextMining/test.txt"};
			//Hacer 15-85 para el test y el train y luego del 85 sacas el 70-30 de train y test.
			DataSource source = new DataSource(args[0]);
			Instances data = source.getDataSet();
			data.setClassIndex(2);
			
			Resample resample = new Resample();
	        resample.setInputFormat(data);
	        resample.setSampleSizePercent(85);
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
			
			//Class missing value al test
			for (int i = 0; i < test.numInstances(); i++) {
				test.instance(i).setClassMissing();
			}
		}
}
