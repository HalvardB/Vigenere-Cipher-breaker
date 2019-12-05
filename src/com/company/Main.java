package com.company;

import edu.duke.FileResource;

import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        VigenereBreaker vb = new VigenereBreaker();


        FileResource file = new FileResource("data/secretmessage2.txt");
        String encrypted = file.asString();
        vb.breakVigenere();

        /*
        // vb.tester();

        int[] keys = vb.tryKeyLength(encrypted, 38, 'e');
        VigenereCipher vc = new VigenereCipher(keys);
        String decryptedString = vc.decrypt(encrypted);

        FileResource dir = new FileResource("dictionaries/English");
        HashSet<String> dictionaries = vb.readDirectory(dir);

        int wordCount = vb.countWords(decryptedString, dictionaries);

        System.out.println("Real words is: " + wordCount);

         */
    }
}
