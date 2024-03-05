
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

class Ates {

    private int x;
    private int y;

    public Ates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}

public class Oyun extends JPanel implements KeyListener, ActionListener {


    Timer timer = new Timer(5, this);

    private int gecenSure = 0;
    private int enIyiSure = 10000;
    private int harcananAtes = 0;
    private BufferedImage img;
    private Image bg_img;
    private ArrayList<Ates> atesler = new ArrayList<Ates>();

    private int atesdirY = 5;

    private int topX = 0;

    private int topdirX = 2;

    private int uzayGemisiX = 0;

    private int dirUzayX = 20;

    public boolean kontrolEt() {
        for (Ates ates : atesler) {

            if (new Rectangle(ates.getX(), ates.getY(), 10, 20).intersects(new Rectangle(topX, 0,20,20))) {
                return true;

            }

        }
        return false;

    }

    public Oyun() {
        try {
            img = ImageIO.read(new FileImageInputStream(new File("uzaygemisi.png")));
            // Yeni arka plan resmini yükleme
            bg_img = ImageIO.read(new FileImageInputStream(new File("bg_uzay.jpg"))); // Arka plan resminin dosya yolunu doğru şekilde belirtin
        } catch (IOException ex) {
            Logger.getLogger(Oyun.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        enIyiSure = enIyiSureOku();


    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        g.drawImage(bg_img, 0, 0, getWidth(), getHeight(), this);
        gecenSure += 5;

        g.setColor(Color.red);
        g.fillOval(topX, 0, 20, 20);

        g.drawImage(img, uzayGemisiX, 490, img.getWidth() / 10, img.getHeight() / 10, this);

        for (Ates ates : atesler) {
            if (ates.getY() < 0) {
                atesler.remove(ates);
            }
        }
        g.setColor(Color.blue);
        for (Ates ates : atesler) {
            g.fillRect(ates.getX(), ates.getY(), 10, 20);
        }
        if(kontrolEt()){
            timer.stop();
            if(gecenSure < enIyiSure){
                enIyiSureKaydet(gecenSure);
            }
            String message = "Kazandınız...\n"+
                    "Harcanan Ateş : " + harcananAtes +"\n"+
                    "Geçen Süre : " + gecenSure/1000.0 + " saniye\n"+
                    "En İyi Süre : " + enIyiSureOku() +"\n" +

                    "Tekrar oynamak ister misiniz?";

            int option = JOptionPane.showConfirmDialog(this, message,"Oyun Bitti",JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION){
                resetGame();
            }else if(option == JOptionPane.NO_OPTION){
                System.exit(0);
            }else{
                do{
                    option = JOptionPane.showConfirmDialog(this, message,"Oyun Bitti",JOptionPane.YES_NO_OPTION);
                }while(option== JOptionPane.CLOSED_OPTION);
            }
        }

    }
    private void resetGame(){
        gecenSure = 0;
        harcananAtes = 0;
        atesler.clear();
        topX = 0;
        uzayGemisiX = 0;
        // Timer'ı yeniden başlat
        timer.restart();
    }
    private void enIyiSureKaydet(int sure){
        try{
            FileWriter writer = new FileWriter("enIyiSure.txt",true);
            writer.write(sure + "\n");
            writer.close();
        }catch (IOException e ){
            e.printStackTrace();
        }

    }
    private int enIyiSureOku() {
        int enIyiSure = 10000;
        try {
            Scanner scanner = new Scanner(new File("enIyiSure.txt"));
            while (scanner.hasNextLine()) {
                int sure = Integer.parseInt(scanner.nextLine());
                if (sure < enIyiSure) {
                    enIyiSure = sure;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("enIyiSure.txt dosyası bulunamadı.");
        }
        return enIyiSure;
    }




    @Override
    public void repaint() {
        super.repaint(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_LEFT) {
            if (uzayGemisiX <= 0) {
                uzayGemisiX = 0;
            } else {
                uzayGemisiX -= dirUzayX;
            }

        } else if (c == KeyEvent.VK_RIGHT) {
            if (uzayGemisiX >= 720) {
                uzayGemisiX = 720;

            } else {
                uzayGemisiX += dirUzayX;
            }

        } else if (c == KeyEvent.VK_SPACE) {
            atesler.add(new Ates(uzayGemisiX + 15, 470));

            harcananAtes++;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Ates ates : atesler) {
            ates.setY(ates.getY() - atesdirY);
        }

        topX += topdirX;
        if (topX >= 750) {
            topdirX = -topdirX;
        }
        if (topX <= 0) {
            topdirX = -topdirX;
        }
        repaint();

    }

}
