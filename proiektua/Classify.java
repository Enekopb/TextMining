package Classify;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Classify {
    public static void main(String[] args) throws Exception {
        if(args.length != 5){
            System.out.println("5 parametro sartu behar dira: 1 -> testblind .arff-a,\n" +
                    "2 ->  smo modeloak egindako predikzioak, 3 -> naivebayes predictions.txt gorde nahi denpath-a\n" +
                    "4 -> smo modeloa gordetzen den fitxategia eta 5 -> naiveBayes modeloa gerdetzen den fitxategia\n");
            System.out.println("Komandoaren erabilpena -> java -jar classify.jar <1 param> <2 param> <3 param> <4 param> <5 param>");
        } else {
            System.out.println("--- Bi modeloak kargatzen... ---");
            Classifier smo = (Classifier) SerializationHelper.read(args[3]);
            Classifier baseline = (Classifier) SerializationHelper.read(args[4]);

            DataSource sourceBlind = new DataSource(args[0]);

            Instances testBlind = sourceBlind.getDataSet();
            testBlind.setClassIndex(testBlind.numAttributes() - 1);
            int classIndex = testBlind.classIndex();

            //-------------- Test Blind ---------------
            System.out.println("\n\n##### Test blind predikzioak #####");
            Attribute classAttribute = testBlind.attribute(classIndex);

            System.out.println("--- SMO predikzioak ---");
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(args[1]));
            for (int i = 0; i < testBlind.numInstances(); i++) {
                Instance instantzia = testBlind.instance(i);
                instantzia.setClassMissing();
                double balioa = smo.classifyInstance(instantzia);
                String predikzioa = classAttribute.value((int) balioa);

                System.out.println(i + " instantziarako predikzioa = " + predikzioa);
                bw1.write(i + " instantziarako predikzioa = " + predikzioa);
                bw1.newLine();
            }
            bw1.flush();
            bw1.close();

            System.out.println("\n--- NaiveBayes predikzioak ---");
            bw1 = new BufferedWriter(new FileWriter(args[2]));
            for (int i = 0; i < testBlind.numInstances(); i++) {
                Instance instantzia = testBlind.instance(i);
                instantzia.setClassMissing();
                double balioa = baseline.classifyInstance(instantzia);
                String predikzioa = classAttribute.value((int) balioa);

                System.out.println(i + " instantziarako predikzioa = " + predikzioa);
                bw1.write(i + " instantziarako predikzioa = " + predikzioa);
                bw1.newLine();
            }
            bw1.flush();
            bw1.close();
        }
    }
}
