package proiektua;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.Reorder;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;


public class ParametroEkorketa {

    public static void main(String[] args) throws Exception {
        //args = new String[]{"C:\\Users\\aimar\\OneDrive\\Escritorio\\DatuakProba", "C:\\Users\\aimar\\OneDrive\\Escritorio\\Hiztegia", "C:\\Users\\aimar\\OneDrive\\Escritorio\\trainProiektua.arff"};
        DataSource source = new DataSource("C:\\Program Files\\Weka-3-8-6\\data\\filtered.arff");
        //DataSource source = new DataSource(args[0]);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes()-1);


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

        System.out.println("Trainen instantzia kopurua: " + train.numInstances());
        System.out.println("Trainaren instantzia kopurua: " + test.numInstances());
        System.out.println("Atributu kopurua: " + data.numAttributes());

        // Get the class attribute
        int classIndex = data.classIndex();
        Attribute classAttribute = data.attribute(classIndex);

        // Get the attribute statistics for the class attribute
        AttributeStats stats = data.attributeStats(classIndex);

        System.out.println("\n###### DATA OSOA Klase bakoitzaren balio kopurua ######");
        System.out.println("TEST klase balio ezberdinak: " + classAttribute.numValues());
        // Print the count of instances for each class
        for (int i = 0; i < classAttribute.numValues(); i++) {
            String className = classAttribute.value(i);
            int count = stats.nominalCounts[i];
            System.out.println(className + ": " + count);
        }

        // Get the class attribute
        int classIndexTrain = train.classIndex();
        System.out.println("Train classindex: " + classIndexTrain);
        Attribute classAttributeTrain = train.attribute(classIndexTrain);

        // Get the attribute statistics for the class attribute
        AttributeStats statsTrain = train.attributeStats(classIndex);

        System.out.println("\n###### TRAIN Klase bakoitzaren balio kopurua ######");
        System.out.println("TRAIN klase balio ezberdinak: " + classAttributeTrain.numValues());
        // Print the count of instances for each class
        for (int i = 0; i < classAttributeTrain.numValues(); i++) {
            String className = classAttributeTrain.value(i);
            int count = statsTrain.nominalCounts[i];
            System.out.println(className + ": " + count);
        }

        //Klase minoritarioaren indizea
        int klaseMin = 99999;
        int klaseMinInd = -1;


        // Get the class attribute
        int classIndexTest = test.classIndex();
        System.out.println("Test classindex: " + classIndexTest);
        Attribute classAttributeTest = test.attribute(classIndexTest);

        // Get the attribute statistics for the class attribute
        AttributeStats statsTest = test.attributeStats(classIndex);

        System.out.println("\n###### TEST Klase bakoitzaren balio kopurua ######");
        System.out.println("TEST klase balio ezberdinak: " + classAttributeTest.numValues());
        // Print the count of instances for each class
        for (int i = 0; i < classAttributeTest.numValues(); i++) {
            String className = classAttributeTest.value(i);
            int count = statsTest.nominalCounts[i];
            System.out.println(className + ": " + count);
            if (klaseMin > statsTest.nominalCounts[i]){
                klaseMin = statsTest.nominalCounts[i];
                klaseMinInd = i;
            }
        }

        System.out.println("Klase minoritarioaren izena: " + classAttributeTest.value(klaseMinInd)
                + " eta indizea: " + klaseMinInd + " agerpenak: " + statsTest.nominalCounts[klaseMinInd]);


        System.out.println("\n\n");

        /*
        //Arff train errepresentazio bektorialera pasa.
        StringToWordVector stw = new StringToWordVector();
        stw.setAttributeIndices("last");        //Azken atributua
        stw.setLowerCaseTokens(true);
        stw.setWordsToKeep(7000); // Hitz kopurua
        stw.setInputFormat(train);

        //ENEKO: "sex", "module" y "site" son atributos del arff y
        // no se pueden añadir porque estan duplicados

        Instances trainBektore = Filter.useFilter(train, stw);


        //Reorder para poner la clase al final
        Reorder reorder = new Reorder();
        reorder.setAttributeIndices("2,last-1");
        reorder.setInputFormat(train);
        Instances trainOrdenatuta = Filter.useFilter(trainBektore, reorder);

        ArffSaver as = new ArffSaver();
        as.setInstances(trainOrdenatuta);
        as.setFile(new File(args[2]));
        as.writeBatch();

        ArffSaver asTest = new ArffSaver();
        asTest.setInstances(test);
        asTest.setFile(new File(args[3]));
        asTest.writeBatch();
        */

        //########################### PARAMETRO EKORKETA #############################
        SMO smo = new SMO();

        //------------------- Kernelak hasieratu -------------------
        PolyKernel pk = new PolyKernel();
        RBFKernel rbf = new RBFKernel();

        //------------------- Aldagaiak hasieratu PE-rako ----------------
        int ber = 10;

        // MAX/MIN -> C
        int minC = -3;
        int maxC = 2;

        // MAX/MIN -> Gamma
        int minGamma = -3;
        int maxGamma = 2;

        // MAX/MIN -> PolyKernel exp
        int minPK = 1;
        int maxPK = 5;

        // Balioak hasieratu
        double gamma = -1;
        double c = -1;

        //Balio hoberenak hasieratu
        double bestFMeasure = -1;
        double bestC = -1;
        int bestKernel = -1;
        int bestPKExp = -1;
        double bestGamma = -1;

        //Ebaluazio hoberena gordetzeko
        Evaluation bestEval = null;

        //TODO --> for barruko System.out.print() arazketa mezuak kendu datu
        // guztiekin parametro ekorketa egiterakoan

        //Ctrl + F kontsolan bilatzeko eguneraketa if-ean sartzen den bakoitzean

        //C -> x10  0.01->100
        for (int cExp = minC; cExp <= maxC; cExp++) {
            c = Math.pow(ber,cExp);
            System.out.println("|1 for|----- C iteratzen .... -----");
            System.out.println("C-ren balioa: " + c);
            smo.setC(c);

            //KernelZenb 0->1 0 = PolyKernel; 1 = RBFKernel
            for (int kernelZenb = 0; kernelZenb < 2; kernelZenb++) {
                System.out.println("|2 for| ----- Kernel iteratzen .... -----");
                System.out.println("Kernel-ren balioa: " + kernelZenb);

                //PolyKernel bada
                if (kernelZenb == 0) {
                    smo.setKernel(pk);

                    //PolyKernel Exponentea +1 1->5
                    for (int pkExp = minPK; pkExp <= maxPK; pkExp++) {
                        System.out.println("|3.1 for| ----- PKExp iteratzen .... -----");
                        System.out.println("PKExp-ren balioa: " + pkExp);
                        pk.setExponent(pkExp);

                        //Train-rekin entrenatu SMO algoritmoa classifierra egiteko
                        smo.buildClassifier(train);

                        //Ebaluazio instantzia berria egin behar da bata
                        //bestearen gainean ez idazteko
                        Evaluation eval = new Evaluation(train);
                        eval.evaluateModel(smo,test);

                        //System.out.println(eval.unweightedMacroFmeasure());
                        System.out.println(eval.fMeasure(klaseMinInd));
                        System.out.println(eval.precision(klaseMinInd));
                        System.out.println(eval.recall(klaseMinInd));

                        //FMeasure hobeagoa aurkitu badu
                        if (bestFMeasure < eval.fMeasure(klaseMinInd)){
                            bestFMeasure=eval.fMeasure(klaseMinInd);
                            bestC = c;
                            bestKernel = kernelZenb;
                            bestPKExp =pkExp;
                            bestEval = eval;
                            System.out.println("\n\nEguneratu dira balio optimoak");
                            System.out.println("####################################");
                            System.out.println("C balioa: " + bestC);
                            System.out.println("Kernel balioa: " + bestKernel);
                            System.out.println("PKExp balioa: " + bestPKExp);
                            System.out.println("FMeasure balioa" + bestFMeasure);
                            System.out.println("####################################\n\n");
                        }
                    }
                }

                //RBFKernel bada
                if (kernelZenb == 1) {
                    smo.setKernel(rbf);
                    //RBF-ren Gamma x10  0.01->100
                    for (int gammaExp = minGamma; gammaExp < maxGamma; gammaExp++) {
                        gamma = Math.pow(ber,gammaExp);
                        System.out.println("|3.2 for| ----- Gamma iteratzen .... -----");
                        System.out.println("Gamma-ren balioa: " + gamma);
                        rbf.setGamma(gamma);
                        smo.buildClassifier(train);

                        //Ebaluazio instantzia berria egin behar da bata
                        //bestearen gainean ez idazteko
                        Evaluation eval = new Evaluation(train);
                        //Train-rekin egindako classifier test-rekin cross validatu
                        eval.evaluateModel(smo,test);

                        //System.out.println(eval.unweightedMacroFmeasure());
                        System.out.println(eval.fMeasure(klaseMinInd));
                        System.out.println(eval.precision(klaseMinInd));
                        System.out.println(eval.recall(klaseMinInd));

                        //FMeasure hobeagoa aurkitu badu
                        if (bestFMeasure < eval.fMeasure(klaseMinInd)){
                            bestFMeasure=eval.fMeasure(klaseMinInd);
                            bestC = c;
                            bestKernel = kernelZenb;
                            bestGamma = gamma;
                            bestEval = eval;
                            System.out.println("\n\nEguneratu dira balio optimoak");
                            System.out.println("####################################");
                            System.out.println("C balioa: " + bestC);
                            System.out.println("Kernel balioa: " + bestKernel);
                            System.out.println("Gamma balioa: " + bestGamma);
                            System.out.println("FMeasure balioa: " + bestFMeasure);
                            System.out.println("####################################\n\n");
                        }
                    }
                }
            }
        }

        //----------------------Ekoerketaren balioak inprimatu----------------------------
        System.out.println("\n\nAmaitu da ekorketa, balio OPTIMOAK:");
        System.out.println("C balio OPTIMOA: " + bestC);
        System.out.println("Kernel balio OPTIMOA: " + bestKernel);
        if(bestKernel==0){
            System.out.println("PKExp balio OPTIMOA: " + bestPKExp);
        } else {
            System.out.println("Gamma balio OPTIMOA: " + bestGamma);
        }

        System.out.println("\n/// Lortutako Fmeasure OPTIMOA ///");
        System.out.println("FMeasure OPTIMOA: " + bestFMeasure);
        System.out.println("\n##### SUMMARY #####");
        bestEval.toSummaryString();
        System.out.println("\n##### CONFUSION MATRIX #####");
        bestEval.confusionMatrix();

        System.out.println("\n\n##### EBALUAZIO EZ ZINTZOA: OPTIMISTA ######");
        smo.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(smo,data);
        System.out.println("\n##### SUMMARY #####");
        eval.toSummaryString();
        System.out.println("\n##### CONFUSION MATRIX #####");
        eval.confusionMatrix();

        System.out.println("\n\n##### K-fold cross validation #####");
        smo.buildClassifier(data);
        eval = new Evaluation(data);
        eval.crossValidateModel(smo,data,10,new Random(1));
        System.out.println("\n##### SUMMARY #####");
        eval.toSummaryString();
        System.out.println("\n##### CONFUSION MATRIX #####");
        eval.confusionMatrix();



        //-------------- Test Blind ---------------
        System.out.println("\n\n##### Test blind predikzioak #####");
        Evaluation iragarpenEval=new Evaluation(data);
        iragarpenEval.evaluateModel(smo, test);
        int i = 0;
        //BufferedWriter bw1=new BufferedWriter(new FileWriter("/home/lsi/Escritorio/test_blind_predictions.txt"));
        BufferedWriter bw1=new BufferedWriter(new FileWriter(args[4]));
        for (Prediction p: iragarpenEval.predictions()) {
            String iragarpena= data.attribute(data.numAttributes()-1).value((int)p.predicted());
            String erreala= data.attribute(data.numAttributes()-1).value((int)p.actual());


            System.out.println("Atributua: " + train.attribute(i).name() + "-rako: "
                    + iragarpena + " iragarri da. Erreala ->" + erreala);
            bw1.write("Attributua" + train.attribute(i).name() + "-rako: "
                    + iragarpena + " iragarri da " + erreala);
            bw1.newLine();
            i++;
        }
        bw1.flush();
        bw1.close();
    }
}

