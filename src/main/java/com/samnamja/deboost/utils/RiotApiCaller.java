package com.samnamja.deboost.utils;

public class RiotApiCaller {
    private static final String API_KEY = "RGAPI-3b565c76-e06f-42d1-9ee1-7766538cd66d";

    public void callRiotApi() {
        System.out.println(API_KEY);
    }
    public static void main(String[] args) {
        RiotApiCaller riotApiCaller = new RiotApiCaller();
        riotApiCaller.callRiotApi();
    }
}
