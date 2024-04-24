package com.example.blackjack;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HelloController {



    private DatagramSocket socket=null;
    @FXML private TextField belepes;
    @FXML private TextField server;
    @FXML private Label ertek;
    @FXML private Label tet;
    @FXML private VBox bc;
    @FXML private Pane pnJatek;
    @FXML private Button g;
    @FXML private Button cs;
    @FXML private Button k;
    @FXML private Button ht;
    @FXML private Button st;
    @FXML private ImageView asztal;
    @FXML private Slider slide;
    public int bet=0;
    public int penz=0;
    public int betss=0;
    public LinkedList<ImageView >kartyak= new LinkedList<>();
    public LinkedList<ImageView >oszto= new LinkedList<>();
    public LinkedList<ImageView >jatekos= new LinkedList<>();
    public int n=610,o=534;
    public int r=728,e=307;
    public String[] tabla= {"1","2","3","4","5"};
    public int[] cordx= {610,907,791,978,453,346,236};
    public int[] cordy= {636,503,574,402,596,537,402};

    private final String KONAMI_CODE = "UUDDLRLRBA";
    private int index = 0;
    private MediaPlayer mediaPlayer;
    private List<Media> playlist;
    private int current;
    public String[] musicFile = {"Lady.mp3","better.mp3","gimmi.mp3"};
    public int t=0;

    public void onmusicclick(){
        if((int)slide.getValue()==0){
            mediaPlayer.setVolume(100);
        }else if((int)slide.getValue()==1){
            mediaPlayer.setVolume(0);
        }
    }
    public void szam(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        current = (current + 1) % playlist.size();
        mediaPlayer = new MediaPlayer(playlist.get(current));
        mediaPlayer.play();

    }

    public void initialize(){
        bc.setOnMouseClicked(this::handleBackgroundClick);
        playlist= new ArrayList<>();
        current=0;
        playlist.add(new Media(getClass().getResource("Lady.mp3").toString()));
        playlist.add(new Media(getClass().getResource("better.mp3").toString()));
        playlist.add(new Media(getClass().getResource("gimmi.mp3").toString()));
        mediaPlayer = new MediaPlayer(playlist.get(current));
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(0);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        try {
            socket=new DatagramSocket(678);
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
    @FXML
    private void handleBackgroundClick(MouseEvent event) {
        bc.requestFocus();
    }

    @FXML
    private void onKeyPressed(javafx.scene.input.KeyEvent event) {
        if (!bc.isFocused()) return;
        if (event.getText().equalsIgnoreCase(Character.toString(KONAMI_CODE.charAt(index)))) {
            index++;
            if (index == KONAMI_CODE.length()) {
                System.out.println("Konami Code Entered!");
                openSecretStage();
                index = 0;
            }
        } else {
            index = 0;
        }
    }

    private void openSecretStage() {
        ImageView cheat= new ImageView(new Image(getClass().getResourceAsStream("cheat.png")));
        Stage secretStage = new Stage();
        StackPane root = new StackPane();
        cheat.setFitHeight(700);
        cheat.setFitWidth(500);
        root.getChildren().add(cheat);
        Scene scene = new Scene(root, 500, 700);
        secretStage.setScene(scene);
        secretStage.setTitle("Cheatsheat");
        secretStage.getIcons().add(new Image(getClass().getResourceAsStream("card.png")));
        secretStage.show();
    }
    //Belépés
    public void join(){
        kuld("join:"+belepes.getText(), server.getText(),678);
        k.setDisable(false);
        cs.setDisable(true);
        belepes.setDisable(true);
        bc.requestFocus();
    }


    //Kilépés
    public void onKilepclick(){
        kuld("exit",server.getText(),678);
        k.setDisable(true);
        cs.setDisable(false);
        n=580;o=514;
        g.setDisable(false);
        for(int i=0;i<oszto.size();i++){
            pnJatek.getChildren().remove(oszto.get(i));
        }
        oszto.clear();
        for(int i=0;i<kartyak.size();i++){
            pnJatek.getChildren().remove(kartyak.get(i));
        }
        kartyak.clear();
        for(int i=0;i<jatekos.size();i++){
            pnJatek.getChildren().remove(jatekos.get(i));
        }
        jatekos.clear();
        penz=0;
        r=728;
        tet.setText("0 Ft");
        belepes.setText(ertek.getText().split(" ")[0]);
    }

    private void kuld(String uzenet, String ip, int port) {
        try {
            byte[] adat = uzenet.getBytes("UTF-8");
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
        System.out.println("messsege"+uzenet);
        if(s[0].equals("joined")){
            bet=Integer.parseInt(s[1]);
            ertek.setText(bet+" Ft");
        }

        if(s[0].equals("start")) {
            for(int i=0;i<Integer.parseInt(s[1]);i++){
                ImageView p=new ImageView(new Image(getClass().getResourceAsStream("Male.png")));
                jatekos.add(p);p.setFitWidth(128);p.setFitHeight(128);p.setLayoutX(cordx[i]);p.setLayoutY(cordy[i]);
                pnJatek.getChildren().add(p);
            }
            for (ImageView imageView : oszto) {
                pnJatek.getChildren().remove(imageView);
            }
            oszto.clear();
            for (ImageView imageView : kartyak) {
                pnJatek.getChildren().remove(imageView);
            }
            kartyak.clear();
            g.setDisable(false);
        }

        if(s[0].equals("s")){
            if(oszto.size()==0){
                ImageView asd=new ImageView(new Image(getClass().getResourceAsStream(s[1]+".png")));
                asd.setLayoutX(r);asd.setLayoutY(e);asd.setFitHeight(130);asd.setFitWidth(110);
                pnJatek.getChildren().add(asd);
                ImageView as=new ImageView(new Image(getClass().getResourceAsStream("card back black.png")));
                as.setLayoutX(r-120);as.setLayoutY(e);as.setFitHeight(130);as.setFitWidth(110);
                pnJatek.getChildren().add(as);
                oszto.add(asd);
                oszto.add(as);
            }else if(oszto.size()==2){
                oszto.get(1).setImage(new Image(getClass().getResourceAsStream(s[1]+ ".png")));
                oszto.add(new ImageView());
            }else {
                r=r-120;
                ImageView asd=new ImageView(new Image(getClass().getResourceAsStream(s[1]+ ".png")));
                asd.setLayoutX(r-120);asd.setLayoutY(e);asd.setFitHeight(130);asd.setFitWidth(110);
                oszto.add(asd);
                pnJatek.getChildren().add(asd);
            }
        }

        if(s[0].equals("paid")){
            ertek.setText(s[1]+" Ft");
            bet=bet+Integer.parseInt(s[1]);
        }
        if(s[0].equals("k")){
            ImageView a=new ImageView(new Image(getClass().getResourceAsStream(s[1]+ ".png")));
            a.setLayoutX(610+40*kartyak.size());a.setLayoutY(534-40*kartyak.size());
            a.setFitHeight(130);a.setFitWidth(110);
            pnJatek.getChildren().add(a);
            kartyak.add(a);
            ht.setDisable(false);
            st.setDisable(false);
        }
        if(s[0].equals("end")){
            n=580;o=514;
            r=728;
            penz=0;
            tet.setText("0 Ft");
            ht.setDisable(true);
            st.setDisable(true);
        }
        if(s[0].equals("balance")){
            bet=bet+Integer.parseInt(s[1]);
            ertek.setText(bet+" Ft");
        }
    }

    //Asztalok

    public int a=1;
    public void onRigthclick(){
        if(a==5)a=0;asztal.setImage(new Image(getClass().getResourceAsStream("asztal"+tabla[a]+".png")));a++;
    }
    public void onLeftclick(){
        if(a==0)a=5;a--;asztal.setImage(new Image(getClass().getResourceAsStream("asztal"+tabla[a]+".png")));
    }



    //Gombok

    public void onHitclick(){
        kuld("hit",server.getText(),678);
    }
    public void onStandclick(){
        kuld("stand",server.getText(),678);
    }


    //Tét adás
    public void sendTet(){
        g.setDisable(true);
        ertek.setText(Integer.parseInt(ertek.getText().split(" ")[0])-Integer.parseInt(tet.getText().split(" ")[0])+" Ft");
        betss=penz;
        bc.requestFocus();
        kuld("bet:"+penz,server.getText(),678);
    }


    // Pénzek

    public void on100click() {
        if (bet >= 100) {
            bet -= 100;
            penz+=100;
            tet.setText(penz + " Ft");
        }
    }
    public void onEgyclick(){
        if(bet>=1) {
            bet -= 1;
            penz+=1;
            tet.setText(penz + " Ft");
        }
    }
    public void on25click(){
        if(bet>=25) {
            bet -= 25;
            penz+=25;
            tet.setText(penz + " Ft");
        }
    }
    public void on50click(){
        if(bet>=50) {
            bet -= 50;
            penz+=50;
            tet.setText(penz + " Ft");
        }
    }
    public void onResetClick(){
        penz=0;
        tet.setText("0 Ft");
    }
    public void onujraclick(){
        penz=betss;
        bet-=betss;
        tet.setText(betss+" Ft");
    }
    public void onDoubleclick(){
        penz=penz*2;
        bet-=penz;
        tet.setText(penz+" Ft");
    }
}
