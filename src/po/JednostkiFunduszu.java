/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.Serializable;


/**
 *
 * @author Piter
 */
public class JednostkiFunduszu implements Serializable {
    
    private FunduszInwestycyjny fundusz;
    private Integer liczbaJednostek;

    public JednostkiFunduszu(FunduszInwestycyjny fundusz, Integer liczbaJednostek) {
        this.fundusz = fundusz;
        this.liczbaJednostek = liczbaJednostek;
    }
    
    public String getNazwaPodgladu() {
        String string = fundusz.getNazwaPodmiotu() + " w liczbie: " + liczbaJednostek;
        return string;
    }

    public FunduszInwestycyjny getFundusz() {
        return fundusz;
    }

    public void setFundusz(FunduszInwestycyjny fundusz) {
        this.fundusz = fundusz;
    }

    public Integer getLiczbaJednostek() {
        return liczbaJednostek;
    }

    public void setLiczbaJednostek(Integer liczbaJednostek) {
        this.liczbaJednostek = liczbaJednostek;
    }
    
    
    
}
