package com.smarttravel.gui;

import com.smarttravel.model.City;
import com.smarttravel.repository.CityRepository;
import com.smarttravel.observer.WeatherObserver; // Observer deseninin arayüzü

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/* * HOCAYA AÇIKLAMA NOTU (OBSERVER DESENİ):
 * Bu sınıf Observer (Gözlemci) tasarım deseninin "Concrete Observer" (Somut Gözlemci) kısmıdır.
 * implements WeatherObserver diyerek sisteme "Ben hava durumunu dinleyen bir dinleyiciyim" diyoruz.
 * Arka planda çalışan Thread (Motor) her 3 saniyede bir verileri değiştirdiğinde, 
 * bizim haberimiz bile olmadan otomatik olarak bu sınıfın içindeki update() metodu tetiklenir 
 * ve grafik kendi kendini yeniden çizer.
 */
public class PieChartPanel extends JPanel implements WeatherObserver {

    private Map<String, Integer> weatherCounts; // Hangi havadan kaç tane var onu tutacak liste
    private Map<String, Color> weatherColors;   // Havalara göre pasta dilimi renkleri

    public PieChartPanel() {
        this.weatherCounts = new HashMap<>();
        this.weatherColors = new HashMap<>();
        
        // Grafikteki dilimlerin renklerini belirliyoruz
        weatherColors.put("SUNNY", new Color(255, 204, 51));   // Güneşli: Sarı
        weatherColors.put("CLOUDY", new Color(102, 153, 255)); // Bulutlu: Mavi
        weatherColors.put("RAINY", new Color(102, 204, 102));  // Yağmurlu: Yeşil
        weatherColors.put("SNOWY", new Color(204, 204, 255));  // Karlı: Beyaz/Buz
        
        setPreferredSize(new Dimension(400, 250));
        setBackground(Color.WHITE);
        
        // İlk açılışta verileri bir kere hesapla
        calculateWeatherDistribution();
    }

    /* * HOCAYA AÇIKLAMA NOTU (VERİ İŞLEME):
     * Singleton olan CityRepository'den tüm şehirleri çekiyoruz.
     * Listedeki her şehrin havasına bakıp (Örn: 3 Güneşli, 2 Yağmurlu) sayıları topluyoruz.
     */
    private void calculateWeatherDistribution() {
        weatherCounts.clear();
        List<City> cities = CityRepository.getInstance().getCities();
        
        for (City city : cities) {
            String weather = city.getCurrentWeatherState().toString();
            // Eğer o hava durumu daha önce sayılmadıysa 1 olarak başla, sayıldıysa üstüne 1 ekle
            weatherCounts.put(weather, weatherCounts.getOrDefault(weather, 0) + 1);
        }
    }

    /* * HOCAYA AÇIKLAMA NOTU (GÜNCELLEME TETİKLEYİCİSİ):
     * İşte Observer deseninin kalbi burası! Motor (Subject) "Veriler değişti!" diye bağırdığında,
     * bu metot otomatik olarak çalışır. Önce yeni sayıları hesaplar, sonra repaint() diyerek
     * ekrana "Kendini yeni verilerle tekrar çiz" emrini verir.
     */
    @Override
    public void update() {
        calculateWeatherDistribution();
        repaint(); // Swing'e grafiği yeniden çizmesi gerektiğini söyler
    }

    /* * HOCAYA AÇIKLAMA NOTU (ÇİZİM METODU):
     * Java Swing'de her türlü şekil (pasta, çizgi, kare) paintComponent metodu ezilerek (Override) çizilir.
     * Burada oran orantı kurarak 360 derecelik daireyi hava durumu sayılarına göre dilimlere bölüyoruz.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Çizgilerin tırtıklı değil, yumuşak ve pürüzsüz görünmesini sağlar
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int totalCities = CityRepository.getInstance().getCities().size();
        if (totalCities == 0) return; // Şehir yoksa çizim yapma

        // Grafiğin ekrandaki konumu ve boyutu
        int width = getWidth();
        int height = getHeight();
        int chartSize = Math.min(width, height) - 40;
        int x = (width - chartSize) / 2 - 50; // Grafiği biraz sola kaydırıyoruz ki yazılara yer kalsın
        int y = (height - chartSize) / 2;

        int startAngle = 0; // Çizime 0 dereceden başla
        int legendY = 30;   // Sağ taraftaki bilgi yazılarının (Legend) başlangıç yüksekliği

        // Her bir hava durumu için pastadan bir dilim kes
        for (Map.Entry<String, Integer> entry : weatherCounts.entrySet()) {
            String weather = entry.getKey();
            int count = entry.getValue();
            
            // Oran orantı: (Bu havadaki şehir sayısı / Toplam şehir) * 360 derece
            int angle = (int) Math.round((double) count / totalCities * 360);

            // Dilimin içini boya
            g2d.setColor(weatherColors.getOrDefault(weather, Color.GRAY));
            g2d.fillArc(x, y, chartSize, chartSize, startAngle, angle);
            
            // Sağ tarafa küçük renkli kareler ve yanına "SUNNY (3)" gibi bilgi metinleri yazdır
            g2d.fillRect(x + chartSize + 30, legendY - 10, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawString(weather + " (" + count + ")", x + chartSize + 45, legendY);
            
            legendY += 20; // Bir sonraki yazıyı bir alt satıra kaydır
            startAngle += angle; // Sonraki dilimin başlayacağı açıyı ayarla
        }
    }
}