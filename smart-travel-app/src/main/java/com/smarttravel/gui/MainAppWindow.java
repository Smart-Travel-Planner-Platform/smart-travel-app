package com.smarttravel.gui;

import com.smarttravel.repository.CityRepository;
import com.smarttravel.observer.WeatherReportProvider;
import javax.swing.*;
import java.awt.*;

// Uygulamanın ana penceresi - Tüm GUI bileşenlerinin birleştiği merkez

/*Bu Sınıf Ne İşe Yarar?
Orkestra Şefliği: Projedeki tüm farklı desenlerin (Pattern)
 görsel karşılıklarını bir araya getirir.

Entegrasyon: WeatherReportProvider nesnesine chartPanel'i attach ederek,
 hava durumu değiştiğinde grafiklerin güncellenmesini sağlar.

Hiyerarşik Düzen: BorderLayout kullanarak; navigasyonu üste,
 listeyi sola ve ana çalışma alanını (Planlayıcı) merkeze yerleştirir.

Arayüzden Tetiklenme Senaryosu
Arayüzdeki (index.html) genel yerleşim bu kodun karşılığıdır:

Uygulama Açıldığında:

main metodunda CityRepository çalışır ve JSON verileri belleğe yüklenir.

MainAppWindow tüm alt panelleri (Control, Chart vb.) oluşturur.

Görselleştirme:

Pencerenin altındaki BarChartPanel,
 Geliştirici 1'in simülasyonundan gelen verilerle çubukları çizmeye başlar.

Sol taraftaki listeden bir şehir seçilip "Ekle" dendiğinde,
 bu pencere içindeki PlannerPanel güncellenir.

Tasarım:

DESIGN.md dosyasındaki "Glass-Panel" efekti,
 Swing'de opaklığı ayarlanmış (rgba) arka plan renkleriyle bu sınıfta taklit edilir. */
public class MainAppWindow extends JFrame {

    // Senin yazdığın alt paneller
    private ControlPanel controlPanel;
    private BarChartPanel chartPanel;
    private PlannerPanel plannerPanel;
    private CityListPanel cityListPanel;

    public MainAppWindow() {
        // Pencere temel ayarları
        setTitle("Smart Travel Planner - Aqua Futurism");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Ekranın ortasında aç
        
        // Tasarım dokümanındaki "Sunlit Abyss" arka plan rengi
        getContentPane().setBackground(new Color(238, 248, 250));
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
    }

    private void initializeComponents() {
        // 1. Üst Kısım: Kontrol Paneli (Sıralama ve Filtreleme)
        controlPanel = new ControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        // 2. Sol Kısım: Şehir Listesi
        cityListPanel = new CityListPanel();
        add(new JScrollPane(cityListPanel), BorderLayout.WEST);

        // 3. Orta/Sağ Kısım: Seyahat Planı (Composite Yapısı)
        plannerPanel = new PlannerPanel();
        add(new JScrollPane(plannerPanel), BorderLayout.CENTER);

        // 4. Alt Kısım: Grafik Paneli (Observer Yapısı)
        chartPanel = new BarChartPanel();
        add(chartPanel, BorderLayout.SOUTH);

        // --- KRİTİK BAĞLANTI (Observer) ---
        // Geliştirici 1'in yazdığı sisteme grafik panelini "dinleyici" olarak ekle
        WeatherReportProvider.getInstance().attach(chartPanel);
    }

    public static void main(String[] args) {
        // Uygulamayı başlatırken Singleton Repository'yi tetikle
        CityRepository.getInstance(); 

        // Arayüzü oluştur ve görünür yap
        SwingUtilities.invokeLater(() -> {
            new MainAppWindow().setVisible(true);
        });
    }
}