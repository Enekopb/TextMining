package proiektua;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;

public class reorderTrainFSS5 {
	
	public static void main(String[] args) throws Exception {
		args = new String[] {"C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\trainFSS.arff", "C:\\Users\\eneko\\OneDrive\\Escritorio\\weka\\trainFSSReorder.arff"};
		DataSource source = new DataSource(args[0]);
		Instances data = source.getDataSet(); 
		data.setClassIndex(data.numAttributes()-1);
		
		Reorder reorder = new Reorder();
        reorder.setAttributeIndices("1,3,38,2,96,4-37,39-95,97-last");
        reorder.setInputFormat(data);
        data = Filter.useFilter(data, reorder);
		
        ArffSaver as = new ArffSaver();
        as.setInstances(data);
        as.setDestination(new File(args[1]));
        as.setFile(new File(args[1]));
        as.writeBatch();
	}
}
