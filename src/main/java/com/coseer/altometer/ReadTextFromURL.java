package com.nlu.altometer;
import com.nlu.platform.tools.types.Uncompacted;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ReadTextFromURL {

        public void GetText(String url1) {
            System.out.println("Within ReadText");
            try {

                URL url = new URL(url1);

                // read text returned by server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    BufferedWriter writer = new BufferedWriter(new FileWriter("legion.txt",true));
                    writer.append(line);
                    writer.close();
                }
                in.close();

            } catch (MalformedURLException e) {
                System.out.println("Malformed URL: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("I/O Error: " + e.getMessage());
            }
        }
}
