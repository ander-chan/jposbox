/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.takepos.jposbox;

import java.awt.PopupMenu;
import java.net.InetAddress;

/**
 *
 * @author ander
 */
public class IpItem extends PopupMenu {

    private final String key;
    private final String value;

    @Override
    public String toString() {
        return  key ;
    }

    public IpItem(String format, String addr) {
        this.key = format;
        this.value = addr;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
}
