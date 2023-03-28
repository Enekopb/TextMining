package proiektua;

import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class Iragarpenak {
	
	public static void main (String[] args) throws Exception{
		if(args.length!=3) {
			System.out.println("3 argumentu sartu behar dituzu: .arff fitxategia, modeloa, eta irteera fitxategia");
		}
		else {
			DataSource source=new DataSource(args[0]);
            Instances data=source.getDataSet();
            data.setClassIndex(data.numAttributes()-1);
            
            Classifier model = (Classifier) SerializationHelper.read(args[1]);
            Evaluation eval = new Evaluation (data);
            eval.evaluateModel(model, data);

            PrintWriter pw = new PrintWriter (args[2]);
            pw.println("Iragarpenak: ");
            pw.println();
            pw.println("Nº\tReal\tPredicted");
            pw.println("\n");
            
            for(int i=0; i< eval.predictions().size(); i++){
            	double iragarria = eval.predictions().get(i).predicted();
                pw.println((i+1)+"\t"+data.instance(i).stringValue(data.classIndex())+"\t"+data.classAttribute().value((int)iragarria)+"\n");
            }     
            pw.close();
		}
	}
}
