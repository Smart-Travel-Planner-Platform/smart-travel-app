package com.smarttravel.gui;

import com.smarttravel.observer.WeatherObserver;
import com.smarttravel.repository.CityRepository;
import com.smarttravel.model.City;
import com.smarttravel.model.WeatherState;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Hava durumu dağılımını pasta grafiği olarak gösteren panel

/*Bu Sınıf Ne İşe Yarar?
Oransal Görselleştirme: Toplam şehirlerin yüzde kaçının
 güneşli veya yağmurlu olduğunu anlık olarak gösterir.

Veri Senkronizasyonu: Observer deseni sayesinde,
 Geliştirici 1'in Thread'i hava durumunu güncellediği anda
  hiçbir butona basmaya gerek kalmadan grafik dilimleri yer değiştirir.

Arayüz Uyumu: DESIGN.md dosyasındaki "Status Indicators" mantığına uygun olarak,
 aktif hava durumlarını parlak renklerle (Cyan/Secondary) temsil eder.

Arayüzden Tetiklenme Senaryosu
Arayüzdeki (index.html) "Weather Split" başlığı altındaki grafik bu kodun karşılığıdır:

Arka Plan Thread'i Çalıştığında:

WeatherReportProvider tüm kayıtlı pencerelere "Veri yenilendi" mesajı gönderir.

PieChartPanel.update() tetiklenir ve paintComponent içindeki
 özetleme (mapping) işlemi saniyeler içinde yeni sayıları çıkarır.

Ekranda Ne Görülür?

Eğer aniden 3 şehir daha yağmurlu olursa,
 koyu mavi (Teal) dilim genişlerken güneşli (Cyan) dilim daralır.

Bu görsel değişim, kullanıcının seyahat planındaki şehirlerin genel
 hava durumunu analiz etmesini sağlar. */
public class PieChartPanel extends JPanel implements WeatherObserver {
    
    private CityRepository repository;

    public PieChartPanel() {
        this.repository = CityRepository.getInstance();
        this.setPreferredSize(new Dimension(400, 300));
        this.setBackground(new Color(238, 248, 250, 100)); // Aqua-Futurism şeffaflığı
    }

    // Gözlemci (Observer) güncelleme metodu
    @Override
    public void update() {
        repaint(); // Veri değişince grafiği tazele
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<City> cities = repository.getCities();
        if (cities.isEmpty()) return;

        // Şehirleri hava durumuna göre grupla ve sayılarını bul
        Map<WeatherState, Long> counts = cities.stream()
            .collect(Collectors.groupingBy(City::getCurrentWeatherState, Collectors.counting()));

        int lastAngle = 0;
        int totalCities = cities.size();
        int x = 100, y = 50, width = 200, height = 200;

        // Her hava durumu tipi için bir dilim çiz
        for (WeatherState state : WeatherState.values()) {
            long count = counts.getOrDefault(state, 0L);
            int angle = (int) ((count * 360) / totalCities);

            // Temaya uygun renkler seç (SUNNY: Sarı/Turuncu, RAINY: Mavi vb.)
            g2d.setColor(getWeatherColor(state));
            g2d.fillArc(x, y, width, height, lastAngle, angle);
            
            lastAngle += angle;
        }

        // Grafiğin yanına küçük renk anahtarları (legend) çiz
        drawLegend(g2d, counts, totalCities);
    }

    private Color getWeatherColor(WeatherState state) {
        return switch (state) {
            case SUNNY -> new Color(131, 240, 255); // Cyan (Secondary)
            case RAINY -> new Color(0, 101, 111);  // Dark Teal
            case CLOUDY -> new Color(111, 121, 123); // Gray
            case SNOWY -> new Color(255, 255, 255); // White
        };
    }

    private void drawLegend(Graphics2D g2d, Map<WeatherState, Long> counts, int total) {
        int ly = 50;
        for (WeatherState state : WeatherState.values()) {
            g2d.setColor(getWeatherColor(state));
            g2d.fillRect(320, ly, 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.drawString(state + ": " + counts.getOrDefault(state, 0L), 340, ly + 12);
            ly += 25;
        }
    }
}