
import javax.swing.JFrame;

public class OyunEkrani extends JFrame {

    public OyunEkrani(String uzay_Oyunu) {

    }

    public static void main(String[] args) {

        OyunEkrani ekran = new OyunEkrani("Uzay Oyunu");

        ekran.setResizable(false);
        ekran.setFocusable(false);
        ekran.setSize(800, 600);
        ekran.setLocationRelativeTo(null);
        ekran.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Oyun oyun = new Oyun();

        ekran.requestFocus();
        oyun.addKeyListener(oyun);

        oyun.setFocusable(true);
        oyun.setFocusTraversalKeysEnabled(false);

        ekran.add(oyun);
        ekran.setVisible(true);

    }

}
