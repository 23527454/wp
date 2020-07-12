package com.fiacs.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zgx
 */
public class IPUtil {

    public static String getIp(){
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
           return ip4.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
