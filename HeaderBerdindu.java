package proiektua;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;

public class HeaderBerdindu {

		public static void main(String[] args) throws Exception {
			//Para igualar los atributos de train y test, necesitamos el hiztegi (tienen todos los atributos del train) 
			//y al test añadirle los atributos.
			args = new String[] {"/home/eneko/Escritorio/TextMining/hiztegia.txt", "/home/eneko/Escritorio/TextMining/test.arff", "/home/eneko/Escritorio/TextMining/devBow.arff"};
			if(args.length==3) {
				DataSource source = new DataSource(args[1]);
				Instances dev = source.getDataSet(); 
				dev.setClassIndex(2);
				
				FixedDictionaryStringToWordVector hiztegia = new FixedDictionaryStringToWordVector();
				hiztegia.setDictionaryFile(new File(args[0]));
				hiztegia.setInputFormat(dev);
				dev = Filter.useFilter(dev, hiztegia);
				
				ArffSaver as = new ArffSaver();
		        as.setInstances(dev);
		        as.setDestination(new File(args[2]));
		        as.setFile(new File(args[2]));
		        as.writeBatch();
				
			}else {
				System.out.println("3 parametro sartu behar dira: hiztegia.txt dev.arff devBow.arff");
			}
		}
}
