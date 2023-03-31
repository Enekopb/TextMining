package getModel;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class getModel {
    public static void main(String[] args) throws Exception {
        if(args.length != 3){
            System.out.println("3 parametro sartu behar dira: 1 -> train .arff-a, 2 -> dev .arff-a eta 3 -> modeloa gordeko den direktorioa");
            System.out.println("Komandoaren erabilpena -> java -jar getModel.jar <1 param> <2 param> <3 param>");
        } else {
            DataSource sourceTrain = new DataSource(args[0]);
            DataSource sourceDev = new DataSource(args[1]);

            Instances train = sourceTrain.getDataSet();
            train.setClassIndex(train.numAttributes() - 1);

            Instances test = sourceDev.getDataSet();
            test.setClassIndex(test.numAttributes() - 1);

            //Datu guztiak biltzen ditugu
            Instances mergedData = new Instances(train);
            for (int i = 0; i < test.numInstances(); i++) {
                mergedData.add(test.instance(i));
            }

            //Klase minoritarioaren indizea eta klase balioaren maiztasuna hasieratu
            int klaseMin = 99999;
            int klaseMinInd = -1;

            // Get the class attribute
            int classIndexTest = test.classIndex();
            Attribute classAttributeTest = test.attribute(classIndexTest);

            // Test-aren AttributeStats lortu klase minoritario ateratzeko
            AttributeStats statsTest = test.attributeStats(classIndexTest);

            // Klaseen artean minoritarioa aurkitu eta gorde
            for (int i = 1; i < classAttributeTest.numValues(); i++) {
                if (klaseMin > statsTest.nominalCounts[i]) {
                    klaseMin = statsTest.nominalCounts[i];
                    klaseMinInd = i;
                }
            }

            //########################### PARAMETRO EKORKETA #############################
            SMO smo = new SMO();

            //------------------- Kernela hasieratu -------------------
            PolyKernel pk = new PolyKernel();

            //------------------- Aldagaiak hasieratu PE-rako ----------------
            int ber = 10;

            // MIN/MAX -> C iterazioak
            int minC = -2;
            int maxC = 2;

            // MIN/MAX -> PolyKernel exp iterazioak
            int minPK = 1;
            int maxPK = 5;

            // Balioak hasieratu
            double c;

            //Balio hoberenak hasieratu
            double bestFMeasure = -1;
            double bestC = 0.1;
            int bestPKExp = 1;

            //Classifier hoberena gordetzeko (modeloa)
            Classifier bestSMO = null;

            System.out.println("------- Parametro ekorketa hasiko da -------");
            //C x10  0.01->100
            for (int cExp = minC; cExp <= maxC; cExp++) {
                c = Math.pow(ber, cExp);
                System.out.println("|1. for|----- C iteratzen .... -----");
                System.out.println("C-ren balioa: " + c);
                smo.setC(c);

                //PolyKernel Exponentea +1 1->5
                for (int pkExp = minPK; pkExp <= maxPK; pkExp++) {
                    System.out.println("|2. for| ----- PKExp iteratzen .... -----");
                    System.out.println("PKExp-ren balioa: " + pkExp);
                    pk.setExponent(pkExp);
                    smo.setKernel(pk);

                    //Train-rekin entrenatu SMO algoritmoa classifierra egiteko
                    smo.buildClassifier(train);

                    //Ebaluazio instantzia berria egin behar da bata
                    //bestearen gainean ez idazteko
                    Evaluation eval = new Evaluation(test);
                    eval.evaluateModel(smo, test);

                    System.out.println("Uneko FMeasure: " + eval.fMeasure(klaseMinInd));
                    System.out.println("Uneko PctCorrect " + eval.pctCorrect());

                    //FMeasure hobeagoa aurkitu badu
                    if (bestFMeasure < eval.fMeasure(klaseMinInd)) {
                        bestFMeasure = eval.fMeasure(klaseMinInd);
                        bestC = c;
                        bestPKExp = pkExp;
                        bestSMO = smo;
                        System.out.println("\n\nEguneratu dira balio optimoak");
                        System.out.println("####################################");
                        System.out.println("C balioa: " + bestC);
                        System.out.println("PKExp balioa: " + bestPKExp);
                        System.out.println("Lortutako FMeasure balioa" + bestFMeasure);
                        System.out.println("####################################\n\n");
                    }
                }
            }

            //----------------------Ekoerketaren balioak inprimatu----------------------------

            System.out.println("\n\nAmaitu da ekorketa, balio OPTIMOAK:");
            System.out.println("C balio OPTIMOA: " + bestC);
            System.out.println("PKExp balio OPTIMOA: " + bestPKExp);

            //------------------------ Parametro optimoak hautatu ------------------------
            System.out.println("\nParametro optimoak ezartzen...");
            smo.setC(bestC);
            pk.setExponent(bestPKExp);
            smo.setKernel(pk);

            //-------------------------- Modelo definitiboa eraiki ----------------------------
            System.out.println("\nModelo osoa eraikitzen...");
            smo.buildClassifier(mergedData);
            SerializationHelper.write(args[2], bestSMO);

            System.out.println("\nProgramaren exekuzioa amaituko da");
        }
    }
}
