package proiektua;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;

public class HeaderBerdindu {

		private static void main(String[] args) throws Exception {
			//Para igualar los atributos de train y test, necesitamos el hiztegi (tienen todos los atributos del train) 
			//y al test añadirle los atributos.
			args = new String[] {"/home/eneko/Escritorio/TextMining/hiztegia.txt", "/home/eneko/Escritorio/TextMining/dev.arff", "/home/eneko/Escritorio/TextMining/devBow.arff"};
			if(args.length==3) {
				DataSource source = new DataSource(args[1]);
				Instances devData = source.getDataSet(); 
				devData.setClassIndex(2);
				
				FixedDictionaryStringToWordVector filterDictionary = new FixedDictionaryStringToWordVector();
				filterDictionary.setInputFormat(devData);
				filterDictionary.setDictionaryFile(args[0]);
				Instances devBow = Filter.useFilter(devData, filterDictionary);
				
				ArffSaver as = new ArffSaver();
		        as.setInstances(devBow);
		        as.setFile(new File(args[2]));
		        as.writeBatch();
				
			}else {
				System.out.println("3 parametro sartu behar dira: hiztegia.txt dev.arff devBow.arff");
			}
		}
}

