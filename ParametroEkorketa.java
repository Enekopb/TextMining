package proiektua;

import weka.core.Debug.Random;
import weka.core.SelectedTag;
import weka.core.Tag;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.meta.CVParameterSelection;

public class ParametroEkorketa {
	public static void main(String[] args) {
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
        
        // MAX/MIN -> Tolerantzia
        int minTol = -3;
        int maxTol = -1;
        
        // MAX/MIN -> Gamma
        int minGamma = -3;
        int maxGamma = 2;
        
        // MAX/MIN -> PolyKernel exp
        int minPK = 1;
        int maxPK = 5;

        // Balioak hasieratu
        double tol = -1;
        double gamma = -1;
        double c = -1;

        //Balio hoberenak hasieratu
        double bestFmeasure = -1;
        double bestC = -1;
        double bestTol = -1;
        int bestKernel = -1;
        int bestPKExp = -1;
        double bestGamma = -1;
        
        //TODO --> for barruko System.out.print() arazketa mezuak kendu datu
        // guztiekin parametro ekorketa egiterakoan
        
        //Ctrl + F kontsolan bilatzeko eguneraketa if-ean sartzen den bakoitzean

        //C -> x10  0.01->100
        for (int cExp = minC; cExp <= maxC; cExp++) {
            c = Math.pow(ber,cExp);
            System.out.println("|1 for|----- C iteratzen .... -----");
            System.out.println("C-ren balioa: " + c);
            smo.setC(c);

            //Tolerance  x10  0.001->0.1
            for (int tolExp = minTol; tolExp <= maxTol; tolExp++) {
                tol = Math.pow(ber,tolExp);
                System.out.println("|2 for| ----- Tolerantzia iteratzen .... -----");
                System.out.println("Tolerantzia-ren balioa: " + tol);
                smo.setToleranceParameter(tol);

                //KernelZenb 0->1 0 = PolyKernel; 1 = RBFKernel
                for (int kernelZenb = 0; kernelZenb < 2; kernelZenb++) {
                    System.out.println("|3 for| ----- Kernel iteratzen .... -----");
                    System.out.println("Kernel-ren balioa: " + kernelZenb);
                    
                    //PolyKernel bada
                    if (kernelZenb == 0) {
                        smo.setKernel(pk);
                        
                        //PolyKernel Exponentea +1 1->5
                        for (int pkExp = minPK; pkExp <= maxPK; pkExp++) {
                            System.out.println("|4.1 for| ----- PKExp iteratzen .... -----");
                            System.out.println("PKExp-ren balioa: " + pkExp);
                            pk.setExponent(pkExp);
                            
                            //Train-rekin entrenatu SMO algoritmoa classifierra egiteko 
                            smo.buildClassifier(train);

                            //Ebaluazio instantzia berria egin behar da bata 
                            //bestearen gainean ez idazteko
                            Evaluation eval = new Evaluation(train);
                            eval.crossValidateModel(smo,test,10,new Random(34));
                            
                            //FMeasure hobeagoa aurkitu badu
                            if (bestFmeasure < eval.weightedFMeasure()){
                                bestFmeasure=eval.weightedFMeasure();
                                bestC = c;
                                bestKernel = kernelZenb;
                                bestTol = tol;
                                bestPKExp =pkExp;
                                System.out.println("\n\nEguneratu dira balio optimoak");
                                System.out.println("####################################");
                                System.out.println("C balioa: " + bestC);
                                System.out.println("Tolerantzia balioa: " + bestTol);
                                System.out.println("Kernel balioa: " + bestKernel);
                                System.out.println("PKExp balioa: " + bestPKExp);
                                System.out.println("Precision hoberena" + bestFmeasure);
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
                            System.out.println("|4.2 for| ----- Gamma iteratzen .... -----");
                            System.out.println("Gamma-ren balioa: " + gamma);
                            rbf.setGamma(gamma);
                            smo.buildClassifier(train);
                            
                            //Ebaluazio instantzia berria egin behar da bata 
                            //bestearen gainean ez idazteko
                            Evaluation eval = new Evaluation(train);
                            //Train-rekin egindako classifier test-rekin cross validatu
                            eval.crossValidateModel(smo,test,10,new Random(34));
                            
                            //FMeasure hobeagoa aurkitu badu
                            if (bestFmeasure < eval.weightedFMeasure()){
                                bestFmeasure=eval.weightedFMeasure();
                                bestC = c;
                                bestKernel = kernelZenb;
                                bestTol = tol;
                                bestGamma = gamma;
                                System.out.println("\n\nEguneratu dira balio optimoak");
                                System.out.println("####################################");
                                System.out.println("C balioa: " + bestC);
                                System.out.println("Tolerantzia balioa: " + bestTol);
                                System.out.println("Kernel balioa: " + bestKernel);
                                System.out.println("Gamma balioa: " + bestGamma);
                                System.out.println("Fmeasure hoberena" + bestFmeasure);
                                System.out.println("####################################\n\n");
                            }
                        }
                    }
                }
            }
        }

        //----------------------Ekoerketaren balioak inprimatu----------------------------
        System.out.println("\n\nAmaitu da ekorketa, balio OPTIMOAK:");
        System.out.println("C balio OPTIMOA: " + bestC);
        System.out.println("Tolerantzia balio OPTIMOA: " + bestTol);
        System.out.println("Kernel balio OPTIMOA: " + bestKernel);
        if(bestKernel==0){
            System.out.println("PKExp balio OPTIMOA: " + bestPKExp);
        } else {
            System.out.println("Gamma balio OPTIMOA: " + bestGamma);
        }

        System.out.println("\n/// Lortutako Fmeasure OPTIMOA ///");
        System.out.println("FMeasure OPTIMOA: " + bestFmeasure);	}
}
