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
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;
import util.ObjectEncoder;

public class HttpClient {

    private final String REGISTER = "user/register"; // Post, no authToken
    private final String LOGIN = "user/login"; // Post, no authToken
    private final String CLEAR = "clear"; // Post, no authToken
    private final String FILL = "fill"; // Post, no authToken
    private final String LOAD = "load"; // Post, no authToken
    private final String ONE_PERSON = "person/"; // Get, yes authToken
    private final String ALL_PERSONS = "person"; // Get, yes authToken
    private final String ONE_EVENT = "event/"; // Get, yes authToken
    private final String ALL_EVENTS = "event"; // Get, yes authToken

    private boolean hasRequestBody;
    private HttpURLConnection connection;

    public LoginResult login(LoginRequest loginRequest) {
        this.hasRequestBody = true;
        LoginResult loginResult = new LoginResult();
        Object obj = getResult(loginRequest, loginResult.getClass(), "/user/login", null);
        return (LoginResult) obj;
    }

    public PersonResult getFamily(String authToken) {
        this.hasRequestBody = false;
        PersonResult personResult = new PersonResult();

        Object obj = getResult(null, personResult.getClass(), "/person", authToken);
        return (PersonResult) obj;
    }

    public EventResult getEvents(String authToken) {
        this.hasRequestBody = false;
        EventResult eventResult = new EventResult();

        Object obj = getResult(null, eventResult.getClass(), "/event", authToken);
        return (EventResult) obj;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        this.hasRequestBody = true;
        RegisterResult registerResult = new RegisterResult();
        Object obj = getResult(registerRequest, registerResult.getClass(), "/user/register", null);
        return (RegisterResult) obj;
    }

    public Object getResult(Object request, Class<?> classType, String urlPath, String authToken) {
        ObjectEncoder objectEncoder = new ObjectEncoder();

        try {
            setConnection(urlPath, authToken);
            connection.connect();

            if(this.hasRequestBody) {
                writeRequestBody(objectEncoder.serialize(request), connection.getOutputStream());
                connection.getOutputStream().close();
            }

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return objectEncoder.deserialize(connection.getInputStream(), classType);
            }

        } catch (Exception e) {
            Log.e("HttpClient", e.getMessage(), e);
        }

        return null;
    }

    public void setConnection(String urlPath, String authToken) {

        try {
            URL url = new URL("http://10.0.2.2:8080" + urlPath); //192.168.1.48
            connection = (HttpURLConnection) url.openConnection();
            setRequestMethod(urlPath);
            connection.addRequestProperty("Accept", "application/json");


            if(authToken != null) {
                connection.addRequestProperty("Authorization", authToken);
            }

        } catch(IOException e) {
            Log.e("HttpClient", e.getMessage(), e);
        }
    }

    public void setRequestMethod(String urlPath) throws ProtocolException {

        StringBuilder s = new StringBuilder(urlPath);
        s.deleteCharAt(0); // Delete leading "/" from urlPath
        urlPath = s.toString();

        switch (urlPath) {
            case ALL_EVENTS:
            case ALL_PERSONS:
            case ONE_EVENT:
            case ONE_PERSON:
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);
                break;
            default:
                connection.setRequestMethod("POST");

                connection.setDoOutput(true);
                break;
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

