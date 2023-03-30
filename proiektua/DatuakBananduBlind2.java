package proiektua;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.Reorder;

public class DatuakBananduBlind2 {
	
		public static void main(String[] args) throws Exception {
			//Adibidez:
			args = new String[] {"C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\datuak.arff", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\trainRAW.arff", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\testRAW.arff"};
			if(args.length==3) {
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
				
				//Class missing value test-ean 
				for (int i = 0; i < test.numInstances(); i++) {
					test.instance(i).setClassMissing();
				}
					
		        ArffSaver as = new ArffSaver();
		        as.setInstances(train);
		        as.setFile(new File(args[1]));
		        as.writeBatch();
		        
		        ArffSaver asT = new ArffSaver();
		        asT.setInstances(test);
		        asT.setFile(new File(args[2]));
		        asT.writeBatch();

			}
			else {
				System.out.println("3 parametro sartu behar dira: datuak.arff train.arff test.arff");
			}
		}
}
