package com.example.familymap.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import model.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import util.ObjectEncoder;

public class HttpClient {


    private String REGISTER = "user/register"; // Post, no authToken
    private String LOGIN = "user/login"; // Post, no authToken
    private String CLEAR = "clear"; // Post, no authToken
    private String FILL = "fill"; // Post, no authToken
    private String LOAD = "load"; // Post, no authToken
    private String ONE_PERSON = "person/"; // Get, yes authToken
    private String ALL_PERSONS = "person"; // Get, yes authToken
    private String ONE_EVENT = "event/"; // Get, yes authToken
    private String ALL_EVENTS = "event"; // Get, yes authToken

    private boolean hasRequestBody;
    private HttpURLConnection connection;

    public LoginResult login(LoginRequest loginRequest) {
        this.hasRequestBody = true;
        LoginResult loginResult = new LoginResult();
        Object obj = getResult(loginRequest, loginResult, "/user/login", null);
        return (LoginResult) obj;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        this.hasRequestBody = true;
        LoginResult loginResult = new LoginResult();
        Object obj = getResult(registerRequest, loginResult, "/user/register", null);
        return (RegisterResult) obj;
    }

    public Object getResult(Object request, Object result, String urlPath, AuthToken authToken) {
        ObjectEncoder objectEncoder = new ObjectEncoder();

        try {
            connection = getConnection(urlPath, authToken);
            setRequestMethod(urlPath);
            connection.connect();

            if(this.hasRequestBody) {
                writeRequestBody(objectEncoder.serialize(request), connection.getOutputStream());
                connection.getOutputStream().close();
            }

            return objectEncoder.deserialize(connection.getInputStream(), result.getClass());

//            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                result = objectEncoder.deserialize(connection.getInputStream(), result.getClass());
//
//            } else {
//                return objectEncoder.deserialize(connection.getInputStream(), result.getClass());
//            }

        } catch (Exception e) {
            Log.e("HttpClient", e.getMessage(), e);
        }

        return null;
    }

    public HttpURLConnection getConnection(String urlPath, AuthToken authToken) {

        HttpURLConnection connectionToReturn = null;

        try {
            URL url = new URL("http://localhost:8080" + urlPath);
            connectionToReturn = (HttpURLConnection) url.openConnection();
            setRequestMethod(urlPath);

            connectionToReturn.addRequestProperty("Accept", "application/json");

            if(authToken != null) {
                connectionToReturn.addRequestProperty("Authorization", authToken.getToken());
            }

        } catch(IOException e) {
            Log.e("HttpClient", e.getMessage(), e);
        }

        return connectionToReturn;
    }

    public String getBase(String urlPath) {
        String[] urlParts = urlPath.split("/");
        return urlParts[0];
    }

    public void setRequestMethod(String urlPath) throws ProtocolException {
        StringBuilder s = new StringBuilder(urlPath);
        s.deleteCharAt(0); // Delete leading "/" from urlPath
        urlPath = s.toString();

        String base = getBase(urlPath);

        if(base.equals(LOGIN) || base.equals(REGISTER) || base.equals(CLEAR) ||
                base.equals(FILL) || base.equals(LOAD)) {
            connection.setRequestMethod("GET");
        } else if (base.equals(ALL_EVENTS) || base.equals(ALL_PERSONS)) {
            connection.setRequestMethod("POST");
        } else if (base.contains(ONE_EVENT) || base.contains(ONE_PERSON)) {
            connection.setRequestMethod("POST");
        }
    }








    public String getUrl(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();
                return responseBodyData;
            }
        }
        catch (Exception e) {
            Log.e("HttpClient", e.getMessage(), e);
        }

        return null;
    }

    public void writeRequestBody(String json, OutputStream outputStream) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(json);
            outputStreamWriter.flush();
        } catch(IOException e) {
            Log.e("HttpClient", "Problem in writeRequestBody function");
        }
    }
}

