package com.company;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;
import edu.duke.*;

public class VigenereBreaker {

    public void breakVigenere () {
        FileResource file = new FileResource("data/secretmessage4.txt");
        String encrypted = file.asString();

        String [] dictionaries = {"Danish", "Dutch", "English", "French", "German", "Italian", "Portuguese" , "Spanish"};
        HashMap<String, HashSet<String>> languages = new HashMap<String, HashSet<String>>();


        for(int i = 0; i < dictionaries.length; i++){
            FileResource dic = new FileResource("dictionaries/" + dictionaries[i]);
            HashSet<String> dictionary = readDirectory(dic);
            languages.put(dictionaries[i], dictionary);
            System.out.println("Done with " + dictionaries[i]);
        }

        breakForAllLangs(encrypted, languages);
    }

    public void breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages){
        int highestWordCountSoFar = 0;
        String highestCountLanguage = null;
        String decryptedMessage = null;

        for(String language : languages.keySet()){
            HashSet<String> dictionary = languages.get(language);
            String decrypted = breakForLanguage(encrypted, dictionary);
            int count = countWords(decrypted, dictionary);
            if(count > highestWordCountSoFar){
                highestWordCountSoFar = count;
                highestCountLanguage = language;
                decryptedMessage = decrypted;
            }
        }

        System.out.println("Language is: " + highestCountLanguage);
        System.out.println(decryptedMessage);
    }

    public String sliceString(String message, int whichSlice, int totalSlices) {

        StringBuilder slicedString = new StringBuilder();

        for (int i = whichSlice; i < message.length(); i += totalSlices){
            slicedString.append(message.charAt(i));
        }

        String finishedString = slicedString.toString();
        // System.out.println("String: " + finishedString);

        return finishedString;
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cc = new CaesarCracker();

        for(int i = 0; i < klength; i++){
            String currentSlice = sliceString(encrypted, i, klength);
            key[i] = cc.getKey(currentSlice);
            // System.out.println("key" + i + " = " + key[i]);
        }
        return key;
    }

    public HashSet<String> readDirectory(FileResource fr){
        HashSet<String> directory = new HashSet<String>();
        for(String word : fr.lines()){
            word = word.toLowerCase();
            directory.add(word);
        }
        return directory;
    }

    public int countWords(String message, HashSet<String> directory){
        String [] stringArray = message.split("\\W+");
        int realWordCount = 0;

        for(String word : stringArray){
            if(directory.contains(word.toLowerCase())){
                realWordCount++;
            }
        }
        return realWordCount;
    }

    public String breakForLanguage(String encrypted, HashSet<String> directory){
        int wordCount = 0;
        int[] finalKeys = null;
        char mostCommonChar = mostCommonCharIn(directory);

        for (int i = 1; i < 100; i++){

            int[] keys = tryKeyLength(encrypted, i, mostCommonChar);

            VigenereCipher vc = new VigenereCipher(keys);
            String decrypted = vc.decrypt(encrypted);

            int currentWords = countWords(decrypted, directory);

            if(currentWords > wordCount){
                wordCount = currentWords;
                finalKeys = keys;
            }
        }

        VigenereCipher vc = new VigenereCipher(finalKeys);
        String decrypted = vc.decrypt(encrypted);

        System.out.println("Real words: " + wordCount);
        System.out.println("Key length: " + finalKeys.length);

        return decrypted;
    }

    public char mostCommonCharIn(HashSet<String> dictionary){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();

        // Count each character
        for(String word : dictionary){
            for (int i = 0; i < word.length(); i++){
                char currChar = word.charAt(i);
                if(map.containsKey(currChar)){
                    map.put(currChar, map.get(currChar) + 1);
                } else {
                    map.put(currChar, 1);
                }
            }
        }

        // Find the character with the highest count
        int highestSoFar = 0;
        char highestCharSoFar = 'e';

        for(char character : map.keySet()){
            int currentCount = map.get(character);

            if(currentCount > highestSoFar){
                highestSoFar = currentCount;
                highestCharSoFar = character;
            }
        }

        return highestCharSoFar;
    }



    public void tester(){
        String testSlice = sliceString("abcdefghijklm", 2, 4);
        System.out.println("Test answer is: " + testSlice);

        FileResource file = new FileResource("data/athens_keyflute.txt");
        String message = file.asString();

        int[] keys = tryKeyLength(message, 5, 'e');
        System.out.println("Test keys is: " + keys);

        VigenereCipher vc = new VigenereCipher(keys);
        String decrypted = vc.decrypt(message);

        System.out.println(decrypted);


    }
    
}
