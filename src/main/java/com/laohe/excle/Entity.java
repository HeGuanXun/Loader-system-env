package com.laohe.excle;

/**
 * @author blackcompany
 * @date 2017-06-29
 * 
 */
public class Entity {

    private String blackCompany;

    public Entity() {
    }
    public Entity(String blackCompany) {
        this.blackCompany = blackCompany;
    }
    
    @Override
    public String toString() {
        return "Entity [ blackCompany=" + blackCompany+ "]";
    }

    public String getBlackCompany() {
        return blackCompany;
    }

    public void setBlackCompany(String blackCompany) {
        this.blackCompany = blackCompany;
    }
}