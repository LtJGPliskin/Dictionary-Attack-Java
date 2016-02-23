/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaryattack;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.io.*;
import java.io.FileReader;
import java.util.Scanner;
/**
 *
 * @author boucher
 */
public class DictionaryAttack {

    /**
     * @param args the command line arguments
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException, IOException {
       DictionaryAttack a = new DictionaryAttack();
       long start;                                      //used to hold the start time of the program
    Scanner sc = new Scanner(System.in);                //used to read in user input
    System.out.println("Input File Directory like so: /Users/pulsargreymane/Downloads/passwords.txt");//prompt user
    String t = sc.next();                               //holds user input
       BufferedReader br = new BufferedReader(new FileReader(t));//reads users desired file
       StringBuilder sb = new StringBuilder();                  //constructs a stringbuilder
       String line = br.readLine();                             //reads the buffered reader file
       String everything = "";                                  //creates an empty file
    while (line != null)                                        //reads the buffered reader to a string builder
    {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
    }
    br.close();
       everything = sb.toString();                              //converts the string builder to a string
       String[] dictionary = everything.split("\n");            //parses out the dictionary by the enter keys
       String[] guessMatrix = {"6f047ccaa1ed3e8e05cde1c7ebc7d958", "275a5602cd91a468a0e10c226a03a39c",  //matrix that holds the desired keys
           "b4ba93170358df216e8648734ac2d539", "dc1c6ca00763a1821c5af993e0b6f60a", "8cd9f1b962128bd3d3ede2f5f101f4fc", 
           "554532464e066aba23aee72b95f18ba2"};
      
       start = System.currentTimeMillis();                      //starts the clock
          for(int i = 0; i < dictionary.length; i++)            //for each word in the dictionary the desired outputs will be searched for
       {
       String input = dictionary[i];                            //holds the current dictionary word
       String inputHex = "";                                    //holds the compiled dictionary hex format
       String[] passwordHex;                                    //used to hold the matrix of hexs in a "ab" format per array slot
       byte[] bytesOfMessage = input.getBytes("UTF-8");         //gets the current dictionaries bytes
       MessageDigest md = MessageDigest.getInstance("MD5");     //gets the byte format for MD5
       byte[] stringDigest = md.digest(bytesOfMessage);         //converts the UTF-5 into MD5
       
       passwordHex = a.stringFromHex(stringDigest);             //returns a string format in "ab" format
       passwordHex = a.checkF(passwordHex);                     //removes additional f's and keeps f's within the first two format
       passwordHex = a.append0(passwordHex);                    //appends 0's to the hex format if the "ab" format is a single number
       inputHex = a.mergeHex(passwordHex);                      //merges the 16 "ab" array format into a 32 hex string 
       a.matches(inputHex, guessMatrix, input, start, i);          //checks the hex string to see if it's inside the desired 
        }
       
    }
    public void matches(String input, String[] guess, String answer, long start, int w)//compares the array of answers with the current dictionary word
    {
        long end;                                               //holds the final time of when the the word is found
        for (String gues : guess)                                //runs through the array of wanted passwords and outputs answers
        {
            if (input == null ? gues == null : input.equals(gues)) {
                System.out.println();
                System.out.println("The password is " + answer);
                System.out.println("The hashvalue is " + input);
                end = System.currentTimeMillis();
                System.out.println("It takes the program " +((end - start) / 1000) + " seconds to find");
                System.out.println("It was word number: " + w + " of the password dictionary");
                break;
            }
        }
    }
    public String mergeHex(String[] passwordHex)//merges the 16x2 format into a single string of size 32
    {
        String a = "";                          //holds the single string
        for (String passwordHex1 : passwordHex) //appends the 16x2 string array to single string
        {
            a += passwordHex1;
        }
        return a;                               //returns the single string
    }
    public String[] stringFromHex(byte[] a) //converts the byte to a string array
    {
        String[] b;                         //holds the hex array
        b = new String[a.length];           //assures the hex array is the right size
         for(int i = 0; i < a.length; i++)
       {
           if(b[i] == null)
           {
               b[i] = "";
           }
       b[i] += Integer.toHexString(a[i]);   //converts the byte to a string in hex format
       
       }
        return b;                           //returns the hex array
    }
    
    public String[] append0(String[] passwordHex)   //appends 0 to the hex array if the hex format is of length 1
    {
        for(int i = 0; i < passwordHex.length; i++) //runs through the hex string of size 16 and checks if it is of size 16x2
       {
           if(passwordHex[i].length() == 1)         //if the hex is a single string, appends 0 to the front ex. 9 becomes 09
       {
           String temp = "0";
           temp+= passwordHex[i];
           passwordHex[i] = temp;
       }
       }
        return passwordHex;                         //returns the string
    }
    public String[] checkF(String[] passwordHex)    //removes additional f's that appear in the hex format
    {
    int t = 0;                                      //holds the number of times f appears
    boolean first;                                  //holds a check if the first character is f
        for(int i = 0; i < passwordHex.length; i++)
       {
       if(passwordHex[i].length() > 2)                  //only runs if the hex is longer than 2
            {
            
                first = false;                          //sets that the first letter of the hex is not f
                String[] q = passwordHex[i].split("");  //parses out the hex string
                if("f".equals(q[6]))                    //assures that the first letter is f
                {
                    first = true;                       //sets that the first letter of the hex is f
                }
                 passwordHex[i] = passwordHex[i].replaceAll("f", "");//removes all f's
                 if(passwordHex[i].length() == 1 && first == false) //if the first letter wasn't f and the string is now of length 1
                 {
                     passwordHex[i] += "f";                         //appends f to the back end of the hex
                 }
                 else if(first == true)                             //if the first letter was f, this appends f to the front of the string
                    {
                        String temp = passwordHex[i];
                        passwordHex[i] = "f";
                        passwordHex[i] += temp;
                    }
            }
       }
    return passwordHex;                             //returns the hex string without unnecesary f's
    }
}
  