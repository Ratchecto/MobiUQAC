package com.rebillard.mobiuqac;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class GetSource {

    /**
     * Permet d'obtenir le code source d'une page web
     *
     * @param sUrl (String)
     * @return code source (String)
     * @throws Exception
     */
    public static String getCode(String sUrl) throws Exception {

        URL url = new URL(sUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        InputStream in = connection.getInputStream();

        InputStreamReader isw = new InputStreamReader(in);

        String codeSource = "", line = "";

        int data = isw.read();
        while (data != -1) {
            char current = (char) data;
            codeSource += current;
            data = isw.read();
        }


        return codeSource;
    }


}