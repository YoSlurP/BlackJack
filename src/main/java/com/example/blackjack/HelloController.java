package com.example.blackjack;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import javafx.scene.input.MouseEvent;
import java.net.*;
import java.util.LinkedList;
import java.util.Map;

public class HelloController {



    private DatagramSocket socket=null;
    @FXML private TextField belepes;
    @FXML private TextField server;
    @FXML private Label ertek;
    @FXML private Label tet;
    @FXML private Label countS;
    @FXML private Label countK;
    @FXML private VBox bc;
    @FXML private Pane pnJatek;
    @FXML private Button g;
    @FXML private ImageView asztal;
    public int bet=0;
    public LinkedList<ImageView >kartyak= new LinkedList<>();
    public LinkedList<ImageView >oszto= new LinkedList<>();
    public LinkedList<ImageView >jatekos= new LinkedList<>();
    public int n=610,o=534;
    public int r=708,e=307;
    public int l=0;
    public String[] tabla= {"1","2","3","4","5"};
    public int[] cordx= {978,907,791,610,453,346,236};
    public int[] cordy= {402,503,574,636,596,537,402};

    private final String KONAMI_CODE = "UUDDLRLRBA";
    private int index = 0;

    public void initialize(){
        bc.setOnMouseClicked(this::handleBackgroundClick);
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

    public void join(){
        kuld("join:"+belepes.getText(), server.getText(),678);
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
        if(s[0].equals("joined")){
            ertek.setText(s[1]+" Ft");
            bet=Integer.parseInt(s[1]);
        }

        if(s[0].equals("start")) {
            g.setDisable(true);
            for(int i=0;i<Integer.parseInt(s[1]);i++){
                ImageView p=new ImageView(new Image(getClass().getResourceAsStream("Male.png")));
                jatekos.add(p);
                p.setFitWidth(128);p.setFitHeight(128);
                p.setLayoutX(cordx[i]);
                p.setLayoutY(cordy[i]);
                pnJatek.getChildren().add(p);
            }
        }

        if(s[0].equals("s")){
            if(oszto.size()==0){
                ImageView asd=new ImageView(new Image(getClass().getResourceAsStream(s[1].charAt(0) + s[1].charAt(1) + ".png")));
                asd.setLayoutX(r);asd.setLayoutY(e);asd.setFitHeight(100);asd.setFitWidth(90);
                pnJatek.getChildren().add(asd);
                ImageView as=new ImageView(new Image(getClass().getResourceAsStream("card back black.png")));
                as.setLayoutX(r-80);as.setLayoutY(e);as.setFitHeight(100);as.setFitWidth(90);
                pnJatek.getChildren().add(as);
                oszto.add(asd);
                oszto.add(as);
                if(s[1].charAt(1)=='A'){
                    s[1].equals("1/11");
                }
                countS.setText(String.valueOf(Integer.parseInt(countS.getText()+s[1])));
            }else if(oszto.size()==2){
                oszto.get(1).setImage(new Image(getClass().getResourceAsStream(s[1].charAt(0) + s[1].charAt(1) + ".png")));
                oszto.add(new ImageView());
            }else {
                r=r-80;
                ImageView asd=new ImageView(new Image(getClass().getResourceAsStream(s[1].charAt(0) + s[1].charAt(1) + ".png")));
                asd.setLayoutX(r-80);asd.setLayoutY(e);asd.setFitHeight(100);asd.setFitWidth(90);
                countS.setText(String.valueOf(Integer.parseInt(countS.getText()+s[1])));
                pnJatek.getChildren().add(asd);
            }
        }

        if(s[0].equals("paid")){
            ertek.setText(s[1]+" Ft");
        }
        if(s[0].equals("k")){
            ImageView a=new ImageView(new Image(getClass().getResourceAsStream(s[1].charAt(0) + s[1].charAt(1) + ".png")));
            a.setLayoutX(610+40*kartyak.size());a.setLayoutY(534+40*kartyak.size());
            a.setFitHeight(100);a.setFitWidth(90);
            pnJatek.getChildren().add(a);
            countK.setText(String.valueOf(Integer.parseInt(countK.getText()+s[1])));
            kartyak.add(a);
        }
        if(s[0].equals("end")){
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
            countS.setText("0");
            countK.setText("0");

        }
        if(s[0].equals("balance")){
            ertek.setText(s[1]+ertek.getText()+" Ft");
        }

    }
    public int a=1;
    public void onRigthclick(){
        if(a==5){
            a=0;
        }
        asztal.setImage(new Image(getClass().getResourceAsStream("asztal"+tabla[a]+".png")));
        a++;


    }
    public void onLeftclick(){
        if(a==0){
            a=5;
        }
        a--;
        asztal.setImage(new Image(getClass().getResourceAsStream("asztal"+tabla[a]+".png")));


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

    public void on100click() {
        if (Integer.parseInt(ertek.getText().split(" ")[0]) > 100) {
            bet -= 100;
            tet.setText(bet + " Ft");
        }
    }
    public void onEgyclick(){
        if(Integer.parseInt(ertek.getText().split(" ")[0])>1) {
            bet -= 1;
            tet.setText(bet + " Ft");
        }
    }
    public void on25click(){
        if(Integer.parseInt(ertek.getText().split(" ")[0])>25) {
            bet -= 25;
            tet.setText(bet + " Ft");
        }
    }
    public void on50click(){
        if(Integer.parseInt(ertek.getText().split(" ")[0])>50) {
            bet -= 50;
            tet.setText(bet + " Ft");
        }
    }
}
