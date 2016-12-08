/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totvschallenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author rafaelabud
 */
public class TOTVSChallenge {

    /**
     * @param args the command line arguments
     */
    public static int numberOfgenerations = 10;
    public static String pathFile = "/Users/rafaelabud/Documents/TOTVSChallenge/";
    public static int numberOfIndividuals = 100;
    
    public static void main(String[] args) throws FileNotFoundException, IOException, JSONException, InterruptedException {
        
        String everything;
        BufferedReader br = new BufferedReader(new FileReader(pathFile+"sample.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } finally {
            br.close();
        }
        JSONArray obj = new JSONArray(everything);
        String[][] DataSample = new String[obj.length()][2];
        ArrayList<ArrayList> ListOfProducts = new ArrayList<ArrayList>();
        for(int numberOfSales = 0;numberOfSales<obj.length();numberOfSales++)
        {
            ArrayList<String> Products = new ArrayList<String>();
            JSONObject List = new JSONObject(obj.get(numberOfSales).toString());
            JSONObject Complemento = List.getJSONObject("complemento"); 
            String valorTotal = Complemento.get("valorTotal").toString();
            DataSample[numberOfSales][0] = valorTotal;
            
            JSONArray dets = List.getJSONArray("dets"); 
            for(int numberOfProducts = 0;numberOfProducts<dets.length();numberOfProducts++)
            {
                String Item = dets.get(numberOfProducts).toString();
                JSONObject ProductList = new JSONObject(Item);
                JSONObject Prod = new JSONObject(ProductList.get("prod").toString());
                Products.add(Prod.get("xProd").toString()); 
            }            
            ListOfProducts.add(Products);
            JSONObject infAdic = List.getJSONObject("infAdic");
            String Table = infAdic.get("infCpl").toString();
            DataSample[numberOfSales][1] = Table;            
        }

        //Criação dos indivíduos
        ArrayList<Individual> Individuals = new ArrayList<Individual>();        
        for(int IndividualsIndex = 0;IndividualsIndex<numberOfIndividuals;IndividualsIndex++)
        {
            Integer Interval = 0;
            if (IndividualsIndex%2==0)
                Interval = 1;
            Individual Ind = new Individual(DataSample[IndividualsIndex][1],Interval.toString(),ListOfProducts.get(IndividualsIndex).get(0).toString());            
            int fitness = evaluatesIndividual(ListOfProducts, DataSample,Ind);
            Ind.setfitness(fitness);
            Individuals.add(Ind);
        }
        for(int generationIndex = 0;generationIndex<numberOfgenerations;generationIndex++)
        {
            //verifica quem é o melhor da população
            int BestIndex = -1;
            int BestFitness = 0;
            for(int SecondIndividualsIndex = 0;SecondIndividualsIndex<100;SecondIndividualsIndex++)
            {
                Individual Ind = Individuals.get(SecondIndividualsIndex);
                if (Ind.getFitness()>BestFitness)
                {
                    BestFitness = Ind.getFitness();
                    BestIndex = SecondIndividualsIndex;
                }
            }
            Individual Best = Individuals.get(BestIndex);
            int ChoosedIndex = 0;
            if (BestIndex==0)
                ChoosedIndex = 1;
            Individual Choosed = Individuals.get(ChoosedIndex);
            ArrayList<Integer> NextGeneration = new ArrayList<Integer>();
            for(int ThirdNumberOfIndividuals = 0;ThirdNumberOfIndividuals<100;ThirdNumberOfIndividuals++)
            {
                if (ThirdNumberOfIndividuals!=BestIndex && ThirdNumberOfIndividuals!=ChoosedIndex)
                    NextGeneration.add(ThirdNumberOfIndividuals);
            }
            ArrayList<Individual> NexGenerationIndividuals = new ArrayList<Individual>();     
            NexGenerationIndividuals.add(Best);
            NexGenerationIndividuals.add(Choosed);
            for(int r = 0;r<NextGeneration.size();r++)
            {
                Individual A = Individuals.get(r);
                Individual B = Individuals.get(r+1);

                Individual C = new Individual(A.getTable(), B.getValueRange(), B.getProduct());
                Individual D = new Individual(B.getTable(), A.getValueRange(), A.getProduct());

                int fitnessC = evaluatesIndividual(ListOfProducts, DataSample, C);
                int fitnessD = evaluatesIndividual(ListOfProducts, DataSample, C);

                C.setfitness(fitnessC);
                D.setfitness(fitnessD);

                NexGenerationIndividuals.add(C);
                NexGenerationIndividuals.add(D);
            }
            Individuals.clear();
            Individuals.addAll(NexGenerationIndividuals);
        }
        Individual Winner = Individuals.get(0);
        System.out.println("The winner is "+Winner.getTable()+" "
                +Winner.getValueRange()+" "+Winner.getProduct());
        
    }

    public static int evaluatesIndividual(ArrayList<ArrayList> ListOfProducts, String[][]DataSample, Individual Ind)
    {
            int count = 0;
            for(int a = 0;a<ListOfProducts.size();a++)
            {
                ArrayList<String> Products = ListOfProducts.get(a);
                String Table = DataSample[a][1];
                String Valor = DataSample[a][0];
                if (Table.equals(Ind.getTable()))
                {
                    if (Products.contains(Ind.getProduct()))
                    {
                        if (Ind.getValueRange().equals(verifiesInterval(Double.parseDouble(Valor))))
                        {
                            count++;
                        }
                    }
                }
            }
            return count;
    }
    
    public static String verifiesInterval(Double num)
    {
        String[] Intervals = new String[10];
        Intervals[0] = "9,74;69,657";
        Intervals[1] = "69,657;129,574";
        Intervals[2] = "129,574;189,491";
        Intervals[3] = "189,491;249,408";
        Intervals[4] = "249,408;309,325";
        Intervals[5] = "309,325;369,242";
        Intervals[6] = "369,242;429,159";
        Intervals[7] = "429,159;489,076";
        Intervals[8] = "489,076;548,993";
        Intervals[9] = "548,993;608,91";  
        
        for(Integer t = 0;t<9;t++)
        {
            Double intInf = Double.parseDouble(Intervals[t].split(";")[0].replace(",", "."));
            Double intSup = Double.parseDouble(Intervals[t].split(";")[1].replace(",", "."));
            if (num>=intInf && num<=intSup)
                return t.toString();
        }
        return "-1";
    }
    
}
