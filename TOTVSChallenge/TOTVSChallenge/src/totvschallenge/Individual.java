/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totvschallenge;

/**
 *
 * @author rafaelabud
 */
public class Individual {
    String Table;
    String ValueRange;
    String Product;
    int Fitness;
    
    public Individual(String Table, String faixaDevalor, String Product)
    {
        this.Table = Table;
        this.ValueRange = faixaDevalor;
        this.Product = Product;
    }
    
    public String getTable()
    {
        return Table;
    }

    public String getValueRange()
    {
        return ValueRange;
    }

    public String getProduct()
    {
        return Product;
    }
    
    public int getFitness()
    {
        return Fitness;
    }    

    public void setProduct(String Product)
    {
        this.Product = Product;
    }

    public void setTable(String Table)
    {
        this.Table = Table;
    }
    
    public void setValueRange(String ValueRange)
    {
        this.ValueRange = ValueRange;
    }
    
    public void setfitness(int Fitness)
    {
        this.Fitness = Fitness;
    }
    
}
