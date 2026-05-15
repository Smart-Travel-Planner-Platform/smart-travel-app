package com.smarttravel.gui;

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

    private ControlPanel controlPanel;
    private BarChartPanel chartPanel;
    private PlannerPanel plannerPanel;
    private CityListPanel cityListPanel;

    public MainAppWindow(WeatherReportProvider weatherProvider) {
        setTitle("Smart Travel Planner - Aqua Futurism");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        getContentPane().setBackground(new Color(238, 248, 250));
        setLayout(new BorderLayout(10, 10)); // Ekranı Kuzey, Güney, Doğu, Batı, Merkez olarak böler
        
        initializeComponents(weatherProvider);
    }

    private void initializeComponents(WeatherReportProvider weatherProvider) {
        // Üst panel (Strategy ve Iterator komutlarını içerir)
        controlPanel = new ControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Sol panel (Şehirlerin listelendiği yer)
        cityListPanel = new CityListPanel();
        add(new JScrollPane(cityListPanel), BorderLayout.WEST);
        
        // Orta ve Sağ panel (Aktivitelerin eklendiği Composite ve Command merkezi)
        plannerPanel = new PlannerPanel();
        add(new JScrollPane(plannerPanel), BorderLayout.CENTER);
        
        // Alt panel (Observer deseninin dinleyici grafikleri)
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 10, 0)); 
        chartPanel = new BarChartPanel();
        PieChartPanel pieChartPanel = new PieChartPanel();
        
        chartsPanel.add(chartPanel);
        chartsPanel.add(pieChartPanel);
        add(chartsPanel, BorderLayout.SOUTH);
        
        // SINAV NOTU (OBSERVER DESENİ):
        // Motor (Subject), grafiklere (Observer) bağlanır. 
        // Böylece hava durumu değiştiğinde grafikler otomatik haberdar olur (Loose Coupling - Gevşek Bağlılık).
        weatherProvider.attach(chartPanel);
        weatherProvider.attach(pieChartPanel);
    }

    // SINAV NOTU (ENCAPSULATION - KAPSULLEME):
    // Diğer panellerin MainAppWindow içindeki değişkenlere doğrudan (public) erişmesini engelleriz.
    // Sadece getter metotlarıyla kontrollü bir erişim sağlarız.
    public CityListPanel getCityListPanel() {
        return cityListPanel;
    }

    public PlannerPanel getPlannerPanel() {
        return plannerPanel;
    }
}