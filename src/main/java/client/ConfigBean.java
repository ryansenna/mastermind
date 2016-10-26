/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 * This class will bundle the configurations for the Mastermind client.
 * @author Railanderson Sena
 */
public class ConfigBean {

    private String serverNumber;
    private int portNum;
    
    public ConfigBean () {
        serverNumber = "";
        portNum = 0;
    }

    public ConfigBean(String serverNumber, int portNum) {
        this.serverNumber = serverNumber;
        this.portNum = portNum;
    }

    public String getServerNumber() {
        return serverNumber;
    }


    public int getPortNum() {
        if(portNum == 0)
            return 7;// return 7 if the port # has not been defined.
        
        return portNum;
    }

    public void setServerNumber(String serverNumber) {
        this.serverNumber = serverNumber;
    }

    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }
   
}
