package com.example.blackjack;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class HelloController {



    private DatagramSocket socket=null;
    @FXML private TextField belepes;
    @FXML private TextField server;
    @FXML private Label ertek;
    public int penz=0;
    public int bet=0;


    public void initialize(){
        try {
            socket=new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread fogadas=new Thread(new Runnable() {
            @Override
            public void run() {
                fogad();
            }
        });
        fogadas.setDaemon(true);
        fogadas.start();

    }



    public void join(){
        kuld("join:", server.getText(),678);
    }
    private void kuld(String uzenet, String ip, int port) {
        try {
            byte[] adat = uzenet.getBytes("utf-8");
            InetAddress ipv4 = Inet4Address.getByName(ip);
            DatagramPacket packet = new DatagramPacket(adat, adat.length, ipv4, port);
            socket.send(packet);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void fogad() { // Külön szálon!
        byte[] adat = new byte[256];
        DatagramPacket packet = new DatagramPacket(adat, adat.length);
        while (true) {
            try {
                socket.receive(packet);
                String uzenet = new String(adat, 0, packet.getLength(), "utf-8");
                String ip = packet.getAddress().getHostAddress();
                int port = packet.getPort();
                Platform.runLater(() -> onFogad(uzenet, ip, port));
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    private void onFogad(String uzenet, String ip, int port) {
        String[] s=uzenet.split(":");
        if(s[0].equals("joined")){
            ertek.setText(s[1]+" Ft");
        }else if(s[0].equals("start")){

        }else if(s[0].equals("paid")){

        }else if(s[0].equals("k")){

        }
    }



    public void onHitclick(){
        kuld("hit",server.getText(),678);
    }
    public void onStandclick(){
        kuld("stand",server.getText(),678);
    }

    public void on100click(){
        bet+=100;
    }
    public void onEgyclick(){
        bet+=1;

    }
    public void on25click(){
        bet+=25;

    }
    public void on50click(){
        bet+=50;

    }
}