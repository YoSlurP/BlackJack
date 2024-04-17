package com.example.blackjack;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.*;
import java.util.LinkedList;

public class HelloController {



    private DatagramSocket socket=null;
    @FXML private TextField belepes;
    @FXML private TextField server;
    @FXML private Label ertek;
    @FXML private Label tet;
    @FXML private VBox bc;
    @FXML private Button g;
    public int penz=0;
    public int bet=0;
    public LinkedList<String >kartyak= new LinkedList<>();
    public LinkedList<String >oszto= new LinkedList<>();
    public int n=580,o=514;
    public int l=0;


    public void initialize(){
        bc.setStyle("-fx-background-color: linear-gradient(to right bottom, #015294, #02f1f5);");
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
        }
        if(s[0].equals("start")){
            g.setDisable(true);
        }
        if(s[0].equals("paid")){
            ertek.setText(s[1]+" Ft");
        }
        if(s[0].equals("s")){
            ImageView asd=new ImageView(s[1].charAt(0) + s[1].charAt(1) + ".png");
            ImageView as=new ImageView("card back black.png");
            as.setLayoutX(649);as.setLayoutY(209);as.setFitHeight(80);as.setFitWidth(70);
            asd.setLayoutX(532);asd.setLayoutY(209);asd.setFitHeight(80);asd.setFitWidth(70);
            oszto.add(s[1].charAt(0) + s[1].charAt(1) + ".png");
        }
        if(s[0].equals("k")){
            ImageView a=new ImageView(s[1].charAt(0) + s[1].charAt(1) + ".png");
            a.setLayoutX(n);a.setLayoutY(o);
            a.setFitHeight(80);a.setFitWidth(70);
            kartyak.add(s[1].charAt(0) + s[1].charAt(1) + ".png");
        }
        if (s[0].equals("k") || kartyak.size()<2 ){
            ImageView a=new ImageView(s[1].charAt(0) + s[1].charAt(1) + ".png");
            a.setLayoutX(n+30);a.setLayoutY(o+30);n=n+30;o=o+30;
            a.setFitHeight(80);a.setFitWidth(70);
            kartyak.add(s[1].charAt(0) + s[1].charAt(1) + ".png");
        }
        if (s[0].equals("k") || kartyak.size()>2 ){
            ImageView a=new ImageView(s[1].charAt(0) + s[1].charAt(1) + ".png");
            a.setLayoutX(n+30);a.setLayoutY(o+30);n=o+30;o=o+30;
            a.setFitHeight(80);a.setFitWidth(70);
            kartyak.add(s[1].charAt(0) + s[1].charAt(1) + ".png");
        }
        if(s[0].equals("s")|| oszto.size()>2){
            ImageView k =new ImageView(s[1].charAt(0) + s[1].charAt(1) + ".png");
            k.setLayoutX(649);
            k.setLayoutY(209);
            k.setFitHeight(80);
            k.setFitWidth(70);
        }
        if(s[0].equals("end")){
            n=580;o=514;
        }
        if(s[0].equals("balance")){

        }

    }
    public void onResetClick(){
        tet.setText("0 Ft");
    }
    public void onHitclick(){
        kuld("hit",server.getText(),678);
    }
    public void onStandclick(){
        kuld("stand",server.getText(),678);
    }

    public void sendTet(){
        kuld("bet:"+bet,server.getText(),678);

    }
    public void onKilepclick(){
        kuld("exit",server.getText(),678);
    }

    public void on100click(){
        bet+=100;
        tet.setText(bet+" Ft");
    }
    public void onEgyclick(){
        bet+=1;
        tet.setText(bet+" Ft");
    }
    public void on25click(){
        bet+=25;
        tet.setText(bet+" Ft");
    }
    public void on50click(){
        bet+=50;
        tet.setText(bet+" Ft");
    }
}