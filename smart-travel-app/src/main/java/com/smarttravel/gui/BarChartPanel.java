package com.smarttravel.gui;

import com.smarttravel.observer.WeatherObserver;
import com.smarttravel.repository.CityRepository;
import com.smarttravel.model.City;
import javax.swing.*;
import java.awt.*;
import java.util.List;

// Geliştirici 1'in yazdığı WeatherObserver arayüzünü implement ederek güncellemeleri dinler

/*Bu Sınıf Ne İşe Yarar?
Dinamik Görselleştirme: Geliştirici 1'in arka plandaki Thread'i
 hava durumunu rastgele değiştirdiğinde, bu sınıf "update" sinyalini alır ve grafiği saniyeler içinde günceller.

Veri İzleme: Kullanıcının şehirlerdeki anlık sıcaklık
 değişimlerini bir bakışta görmesini sağlar.

Tasarım Entegrasyonu: DESIGN.md dosyasında belirtilen
 degrade geçişlerini ve modern arayüz renklerini (Cyan/Secondary) Swing bileşenlerine
  yansıtır.

Arayüzden Tetiklenme Senaryosu
Arayüzdeki (index.html) "Weekly Temperature Trend" bölümü bu kodun karşılığıdır:

Thread Çalıştığında:

WeatherReportProvider (Geliştirici 1) tüm observer'lara "Hava durumu değişti!" der.

BarChartPanel.update() metodu otomatik olarak koşturulur.

Ekranda Ne Görülür?

Grafikteki çubuklar (barlar), şehirlerin sıcaklıkları arttıkça yükselir, azaldıkça kısalır.

Bu değişim, index.html dosyasındaki statik grafik görüntüsünün Java
 tarafındaki canlı versiyonudur. */
public class BarChartPanel extends JPanel implements WeatherObserver {
    
    // Grafiğin çizilebilmesi için şehir verilerine erişim sağlar
    private CityRepository repository;

    public BarChartPanel() {
        this.repository = CityRepository.getInstance(); // Singleton üzerinden veriyi al
        this.setPreferredSize(new Dimension(400, 300));
        this.setBackground(new Color(238, 248, 250, 100)); // Aqua-Futurism cam efekti
    }

    // Geliştirici 1'in Thread mekanizması çalıştığında bu metod tetiklenir
    @Override
    public void update() {
        // Veri değiştiği için paneli yeniden çizdirir
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Kenar yumuşatma aktif (Daha modern bir görünüm için)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<City> cities = repository.getCities();
        int x = 50; // Başlangıç X koordinatı
        int width = 30; // Bar genişliği

        // Her bir şehir için sıcaklık değerine göre bir bar (çubuk) çizer
        for (City city : cities) {
            int height = (int) city.getCurrentTemperature() * 3; // Sıcaklığı görsel
            //  boyuta çevir
            
            // Aqua-Futurism temasına uygun degrade renk (Cyan to Blue)
            GradientPaint gp = new GradientPaint(x, 250 - height, new Color(0, 101, 111), 
                                                x, 250, new Color(131, 240, 255));
            g2d.setPaint(gp);
            
            // Barı çiz (Yukarıdan aşağı koordinat sistemine göre ayarlı)
            g2d.fillRect(x, 250 - height, width, height);
            
            // Şehir isimlerini barın altına yaz
            g2d.setColor(new Color(39, 48, 50));
            g2d.drawString(city.getName(), x, 270);
            
            x += 60; // Bir sonraki bar için boşluk bırak
        }
    }
}