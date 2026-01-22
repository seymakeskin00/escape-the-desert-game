import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ColKesfiOyunu extends JFrame {
    // Görsel Nesneleri
    private Image arkaplan, parsonem;
    private JPanel anaPanel;
    private JTextArea hikayeMetni;
    private JLabel kaynakLabel;
    private JButton btn1, btn2;

    // Oyun Değişkenleri
    private int su = 100;
    private int enerji = 100;
    private String mevcutSahne = "anaMenu";

    public ColKesfiOyunu() {
        // 1. Görselleri Yükle (Klasör yoluna dikkat: /res/...)
        try {
            arkaplan = new ImageIcon(getClass().getResource("/res/col_arkaplan.jpg")).getImage();
            parsonem = new ImageIcon(getClass().getResource("/res/parsonem.jpg")).getImage();
        } catch (Exception e) {
            System.out.println("Görsel yükleme hatası! Dosya isimlerini kontrol et.");
        }

        // 2. Pencere Ayarları
        setTitle("Çöl Keşfi - Hayatta Kalma");
        setSize(800, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // 3. Özel Çizim Paneli (Arkaplan ve Parşömen Çizimi)
        anaPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(arkaplan, 0, 0, getWidth(), getHeight(), this);
                if (!mevcutSahne.equals("anaMenu")) {
                    g.drawImage(parsonem, 80, 130, 640, 380, this);
                }
            }
        };
        anaPanel.setLayout(null);
        setContentPane(anaPanel);

        // 4. GUI Bileşenlerini Oluştur
        bilesenleriHazirla();
        sahneDegistir("anaMenu");

        setVisible(true);
    }

    private void bilesenleriHazirla() {
        // Kaynak Tabelası (Üst Kısım)
        kaynakLabel = new JLabel("Su: " + su + " | Enerji: " + enerji);
        kaynakLabel.setBounds(100, 30, 600, 70);
        kaynakLabel.setFont(new Font("Serif", Font.BOLD, 36));
        kaynakLabel.setForeground(new Color(255, 215, 0));
        kaynakLabel.setHorizontalAlignment(SwingConstants.CENTER);
        anaPanel.add(kaynakLabel);

        // Hikaye Metni (Parşömen Üzeri)
        hikayeMetni = new JTextArea();
        hikayeMetni.setBounds(160, 0, 500, 280); 

     // Metni dikeyde de biraz daha yukarıdan başlatmak için Insets (kenar boşluğu) ekleyelim:
        hikayeMetni.setMargin(new Insets(10, 10, 10, 10));
     // Hem KALIN (Bold) hem EĞİK (Italic) yapmak için:
        hikayeMetni.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 22));

        // Rengi de biraz daha eski bir mürekkep tonuna çekelim (Koyu Kahve):
        hikayeMetni.setForeground(new Color(60, 30, 0));

        // Yazıyı biraz daha yukarı alıp kutu içinde ferahlatmak için:
        hikayeMetni.setBounds(150, 150, 500, 320); 
        hikayeMetni.setMargin(new Insets(20, 30, 20, 30));
        hikayeMetni.setLineWrap(true);
        hikayeMetni.setWrapStyleWord(true);
        hikayeMetni.setOpaque(false);
        hikayeMetni.setEditable(false);
        anaPanel.add(hikayeMetni);

        // Butonlar
        btn1 = tasarimliButon(250, 540);
        btn2 = tasarimliButon(250, 610);
        
        btn1.addActionListener(e -> butonTiklandi(1));
        btn2.addActionListener(e -> butonTiklandi(2));

        anaPanel.add(btn1);
        anaPanel.add(btn2);
    }

    private JButton tasarimliButon(int x, int y) {
        JButton b = new JButton();
        b.setBounds(x, y, 300, 55);
        b.setFont(new Font("Tahoma", Font.BOLD, 16));
        b.setBackground(new Color(180, 130, 70));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createRaisedBevelBorder());
        return b;
    }

    private void butonTiklandi(int secim) {
        switch (mevcutSahne) {
            case "anaMenu": if (secim == 1) sahneDegistir("baslangic"); else System.exit(0); break;
            case "baslangic": sahneDegistir("karar1"); break;
            case "karar1": 
                if (secim == 1) { su -= 20; } else { enerji -= 25; } 
                sahneDegistir("karar2"); break;
            case "karar2": 
                if (secim == 1) { su += 15; enerji -= 10; } else { su -= 10; } 
                sahneDegistir("karar3"); break;
            case "karar3": 
                if (secim == 1) { enerji -= 20; } else { su -= 25; } 
                sahneDegistir("karar4"); break;
            case "karar4": 
                if (secim == 1) sahneDegistir("kazandiniz"); else sahneDegistir("kaybettiniz"); break;
            default: System.exit(0);
        }
        kaynaklariGuncelle();
    }

    private void sahneDegistir(String yeniSahne) {
        mevcutSahne = yeniSahne;
        kaynakLabel.setVisible(!yeniSahne.equals("anaMenu"));

        if (yeniSahne.equals("anaMenu")) {
            hikayeMetni.setText("\n\n   ÇÖL KEŞFİ\n\n   Hayatta kalabilecek misin?");
            btn1.setText("Başla"); btn2.setText("Çıkış");
        } else if (yeniSahne.equals("baslangic")) {
            hikayeMetni.setText("Kum fırtınası dindi. Yanında sadece bitmek üzere olan bir matara su var.");
            btn1.setText("Kuzeye Doğru İlerle"); btn2.setVisible(false);
        } else if (yeniSahne.equals("karar1")) {
            btn2.setVisible(true);
            hikayeMetni.setText("KARAR 1: Önünde dik bir kum tepesi ve yan tarafta karanlık bir mağara var.");
            btn1.setText("Mağaraya gir (Serin)"); btn2.setText("Tepeleri aş (Yorucu)");
        } else if (yeniSahne.equals("karar2")) {
            hikayeMetni.setText("KARAR 2: Mağara çıkışında bir bedevi kampı gördün. Su istemeli misin?");
            btn1.setText("Yardım İste"); btn2.setText("Görmezden Gel");
        } else if (yeniSahne.equals("karar3")) {
            hikayeMetni.setText("KARAR 3: Yeni bir fırtına yaklaşıyor! Antik bir kalıntı gördün.");
            btn1.setText("Kalıntıya sığın"); btn2.setText("Yola devam et");
        } else if (yeniSahne.equals("karar4")) {
            hikayeMetni.setText("KARAR 4: Ufukta bir vaha göründü! Ama bu bir serap olabilir.");
            btn1.setText("Tüm gücünle koş"); btn2.setText("Temkinli ilerle");
        } else if (yeniSahne.equals("kazandiniz")) {
            hikayeMetni.setText("TEBRİKLER! Vahaya ulaştın ve kurtuldun.");
            btn1.setText("Kapat"); btn2.setVisible(false);
        } else if (yeniSahne.equals("kaybettiniz")) {
            hikayeMetni.setText("KAYBETTİN... Çölün sıcaklığına yenik düştün.");
            btn1.setText("Kapat"); btn2.setVisible(false);
        }
        repaint(); // Arkaplanı ve parşömeni yeniden çiz
    }

    private void kaynaklariGuncelle() {
        kaynakLabel.setText("Su: " + su + " | Enerji: " + enerji);
        if ((su <= 0 || enerji <= 0) && !mevcutSahne.equals("kazandiniz")) {
            sahneDegistir("kaybettiniz");
        }
    }

    public static void main(String[] args) { new ColKesfiOyunu(); }
}