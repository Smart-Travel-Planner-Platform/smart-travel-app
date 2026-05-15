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
    
    private CityRepository repository;

    public BarChartPanel() {
        this.repository = CityRepository.getInstance(); 
        this.setPreferredSize(new Dimension(400, 300));
        
        // GLITCH ÇÖZÜMÜ VE MOCK-UP UYUMU: 
        // Şeffaflığı (100) kaldırdık ve hocanın ekranındaki gibi beyaz/açık gri bir arka plan yaptık.
        // Ayrıca panelin sol üst köşesine hocanın PDF'indeki gibi "City Temperatures" başlığını ekledik.
        this.setBackground(Color.WHITE); 
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    public void update() {
        // HOCAYA CEVAP (OBSERVER): Subject (Motor) veri değiştirdiğinde 
        // bu panelin kendini anında yeni verilerle tekrar çizmesini sağlar.
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Ekranın önceki karelerini (frame) temizleyen çok kritik metot
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // HOCAYA NOT: Sol üstteki başlık yazısı
        g2d.setColor(Color.BLACK);
        g2d.drawString("City Temperatures", 10, 15);

        List<City> cities = repository.getCities();
        if (cities == null || cities.isEmpty()) return;

        int x = 20; // Başlangıç X koordinatını biraz daha sola çektik
        int width = 35; // Çubukları biraz kalınlaştırdık

        // Her bir şehir için iterasyon yapıp grafiği çizdiriyoruz
        for (City city : cities) {
            // Çubuk yüksekliğini sıcaklıkla orantılı yapıyoruz
            int height = (int) (city.getCurrentTemperature() * 4); 
            // Çizim çok aşağı taşmasın diye bir sınır (limit) koyuyoruz
            if (height < 0) height = 0; 
            
            // MOCK-UP UYUMU: Hocanın PDF'indeki (Figure 1) kırmızımsı çubuk rengi 
            g2d.setColor(new Color(255, 102, 102)); 
            g2d.fillRect(x, 270 - height, width, height);
            
            // MOCK-UP UYUMU: Çubukların hemen üstüne sıcaklık sayısını yazdır 
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString(String.format("%.1f", city.getCurrentTemperature()), x + 2, 270 - height - 5);
            
            // Şehir isimlerini barın altına yazdır (Sığması için ilk 5-6 harfini alıp kısaltıyoruz)
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String cityName = city.getName();
            if(cityName.length() > 6) cityName = cityName.substring(0, 5) + "..";
            g2d.drawString(cityName, x, 285);
            
            x += 50; // Bir sonraki bar için boşluk (X koordinatını kaydır)
        }
    }
}