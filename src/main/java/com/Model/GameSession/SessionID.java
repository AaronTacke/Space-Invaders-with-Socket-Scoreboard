package com.Model.GameSession;

import java.util.regex.Pattern;

public class SessionID{
    private long sessionID;

    //Creates a SessionID using an ip and a port
    public SessionID(String ip, int port){
        String numbers[] = ip.split(Pattern.quote("."));
        sessionID = 0;
        for(String number : numbers){
            sessionID *=256;
            sessionID += Integer.parseInt(number);
        }
        sessionID *= 65536;
        sessionID += port;
    }

    //Creates a SessionID using the SessionID itself
    public SessionID(String sessionID){
        char chars[] = sessionID.toCharArray();
        this.sessionID = 0;
        for(int i = 0; i < chars.length; i++){
            this.sessionID *= 62;
            this.sessionID += charToInt(chars[i]);
        }
    }

    private int charToInt(char c){
        if(c>='a' && c<='z') return c-'a';
        else if(c>='A' && c<='Z') return 26+c-'A';
        else if(c>='0' && c<='9') return 26+26+c-'0';
        else return 0;
    }

    private char intToChar(int i){
        if(i<0) return '\0';
        else if(i<26) return (char)('a'+i);
        else if(i<26+26) return (char)('A'+i-26);
        else if(i<26+26+10) return (char)('0'+i-(26+26));
        else return '\0';
    }

    //Returns the IP that is stored in the SessionID
    public String getIP(){
        long sessionID = this.sessionID / 65536;
        int numbers[] = new int[4];
        for(int i = 0; i < numbers.length; i++){
            numbers[3-i] = (int)(sessionID%256);
            sessionID /= 256;
        }
        return numbers[0]+"."+numbers[1]+"."+numbers[2]+"."+numbers[3];
    }

    //Returns the Port that is stored in the SessionID
    public int getPort(){
        return (int)(sessionID%65536);
    }

    //Returns the sessionID itself
    public String getSessionID(){
        StringBuilder sb = new StringBuilder();
        long sessionID = this.sessionID;
        while(sessionID>0){
            sb.append(intToChar((int)(sessionID%62)));
            sessionID /= 62;
        }
        sb.reverse();
        return sb.toString();
    }

    //Overrides toString to print SessionID easily.
    @Override
    public String toString() {
        return "SessionID: "+getSessionID();
    }
}