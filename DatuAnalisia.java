package proiektua;

import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class DatuAnalisia {
    public static void main(String[] args) throws Exception {
        //args = new String[]{"C:\\Users\\aimar\\OneDrive\\Escritorio\\DatuakProba", "C:\\Users\\aimar\\OneDrive\\Escritorio\\Hiztegia", "C:\\Users\\aimar\\OneDrive\\Escritorio\\trainProiektua.arff"};
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("C:\\Program Files\\Weka-3-8-6\\data\\filtered.arff");
        //DataSource source = new DataSource(args[0]);
        Instances train = source.getDataSet();
        train.setClassIndex(train.numAttributes() - 1);


        //Kopurua gordetzeko
        int booleanKop = 0;
        int nominalKop = 0;
        int numericKop = 0;
        int stringKop = 0;

        for (int i = 0; i < train.numAttributes(); i++) {
            if (train.attribute(i).isNominal()) {
                System.out.println("Mota: Nominala -> " + train.attribute(i).name() + " izena du eta "
                        + train.numDistinctValues(i) + " balio ezberdin ditu");
                nominalKop = nominalKop + 1;
            } else if (train.attribute(i).isNumeric()) {
                System.out.println("Mota: Numerikoa -> " + train.attribute(i).name() + " izena du eta "
                        + train.numDistinctValues(i) + " balio ezberdin ditu");
                numericKop = numericKop +1;
            } else if (train.attribute(i).isString()){
                System.out.println("Mota: String -> " + train.attribute(i).name() + " izena du eta "
                        + train.numDistinctValues(i) + " balio ezberdin ditu");
                stringKop = stringKop + 1;
            } else if (train.attribute(i).toString().equals("True") || train.attribute(i).toString().equals("False")){
                System.out.println("Mota: Boolean -> " + train.attribute(i).name() + " izena du eta "
                        + train.numDistinctValues(i) + " balio ezberdin ditu");
                booleanKop = booleanKop + 1;
            }
        }

        System.out.println("----------------");
        System.out.println("Nominal kopurua: " + nominalKop);
        System.out.println("Numeric kopurua: " + numericKop);
        System.out.println("String kopurua: " + stringKop);
        System.out.println("Boolean kopurua: " + booleanKop);

        System.out.println("Atributu kopurua: " + train.numAttributes());
        System.out.println("Instantzia kopurura: " + train.numInstances());

        System.out.println("Klase minoritarioa positibotzat hartuta:");

        int klaseMin = 99999;
        int klaseMinInd = -1;
        int instEzMinKop = 0;

        int classIndexTrain = train.classIndex();
        System.out.println("Train classindex: " + classIndexTrain);
        Attribute classAttributeTrain = train.attribute(classIndexTrain);

        // Get the attribute statistics for the class attribute
        AttributeStats statsTrain = train.attributeStats(classIndexTrain);

        System.out.println("\n###### TRAIN Klase bakoitzaren balio kopurua ######");
        System.out.println("TRAIN klase balio ezberdinak: " + classAttributeTrain.numValues());
        // Print the count of instances for each class
        for (int i = 0; i < classAttributeTrain.numValues(); i++) {
            String className = classAttributeTrain.value(i);
            int count = statsTrain.nominalCounts[i];
            System.out.println(className + ": " + count);
            if (klaseMin > statsTrain.nominalCounts[i]){
                klaseMin = statsTrain.nominalCounts[i];
                klaseMinInd = i;
            }
        }

        instEzMinKop = train.numInstances() - statsTrain.nominalCounts[klaseMinInd];
        System.out.println("Klase minoritarioaren izena: " + classAttributeTrain.value(klaseMinInd)
                + " eta indizea: " + klaseMinInd + " agerpenak: " + statsTrain.nominalCounts[klaseMinInd]);
        System.out.println("Klase ez minoritarioaren agerpenak: " + instEzMinKop);
        System.out.println("Instantzia totalak: " + train.numInstances());

    }
}