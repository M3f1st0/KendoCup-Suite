/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FunctionalityClasses;

import java.io.Serializable;

/**
 *
 * @author Panagiotis Bitharis
 */
public class ConnectionSettings implements Serializable{
    private String serverAddress;
    private String portNumber;
    private String username;
    private String password;
    
    public ConnectionSettings(String servAddr, String port, String uname, String pass){
        if(!servAddr.isEmpty() && !port.isEmpty() && !uname.isEmpty() && !pass.isEmpty()){
            serverAddress = servAddr;
            portNumber = port;
            username = uname;
            password = pass;
        }else{
            throw new IllegalArgumentException("Empty Fields");
        }
    }

    /**
     * @return the serverAddress
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * @return the portNumber
     */
    public String getPortNumber() {
        return portNumber;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    
}
