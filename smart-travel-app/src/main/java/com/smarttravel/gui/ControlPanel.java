package com.smarttravel.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.smarttravel.iterator.CloudyIterator;
import com.smarttravel.iterator.RainyIterator;
import com.smarttravel.iterator.SnowyIterator;
import com.smarttravel.iterator.SunnyIterator;
import com.smarttravel.iterator.WeatherIterator;
import com.smarttravel.repository.CityRepository;
import com.smarttravel.strategy.SortByArea;
import com.smarttravel.strategy.SortByName;
import com.smarttravel.strategy.SortByPopulation;
import com.smarttravel.strategy.SortStrategy;

/* * HOCAYA NOT: ControlPanel Sınıfı
 * Bu sınıf, kullanıcının "Sıralama (Strategy)" ve "Filtreleme (Iterator)" işlemlerini 
 * arayüz üzerinden tetiklediği ana kontrol merkezidir.
 */
public class ControlPanel extends JPanel {
    
    private JComboBox<String> sortCombo;
    private JComboBox<String> filterCombo;
    private CityRepository repository; // Singleton deseninden gelen veri deposu

    public ControlPanel() {
        // Singleton pattern: Şehirlerin tek bir merkezden (repository) çekilmesi sağlanıyor.
        this.repository = CityRepository.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        // Hocanın PDF'indeki gibi üst üste 2 satırlık bir grid (ızgara) tasarımı oluşturuyoruz.
        this.setLayout(new GridLayout(2, 1, 5, 5));
        this.setBackground(new Color(238, 248, 250)); 
        this.setBorder(BorderFactory.createTitledBorder("Controls"));

        // ====================================================================
        // 1. SATIR: STRATEGY DESENİ (Sıralama İşlemleri)
        // ====================================================================
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.setOpaque(false);
        topRow.add(new JLabel("Sort Cities:"));
        
        String[] sortOptions = {"Name", "Population", "Area"};
        sortCombo = new JComboBox<>(sortOptions);
        sortCombo.setPreferredSize(new Dimension(800, 25)); // Görseli uzatmak için
        
        // Kullanıcı combobox'tan bir sıralama türü seçtiğinde tetiklenen olay (Listener)
        sortCombo.addActionListener(e -> {
            String selected = (String) sortCombo.getSelectedItem();
            SortStrategy strategy; // Strategy Arayüzü (Interface)
            
            // HOCAYA CEVAP (STRATEGY): Çalışma zamanında (runtime) if-else if bloklarıyla
            // kullanıcının isteğine göre uygun sıralama algoritması (somut strateji) dinamik olarak seçilir.
            if (selected.equals("Population")) strategy = new SortByPopulation();
            else if (selected.equals("Area")) strategy = new SortByArea();
            else strategy = new SortByName();
            
            // SwingUtilities ile bu panelin içinde bulunduğu Ana Pencereyi (MainAppWindow) buluyoruz
            MainAppWindow mainWin = (MainAppWindow) SwingUtilities.getWindowAncestor(this);
            // Seçilen stratejiyi sol taraftaki listeye gönderip verileri sıralatıyoruz.
            if (mainWin != null && mainWin.getCityListPanel() != null) {
                mainWin.getCityListPanel().refreshAllCities(strategy);
            }
        });
        topRow.add(sortCombo);
        add(topRow);

        // ====================================================================
        // 2. SATIR: ITERATOR DESENİ (Filtreleme İşlemleri)
        // ====================================================================
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomRow.setOpaque(false);
        bottomRow.add(new JLabel("Filter by Weather:"));
        
        String[] filterOptions = {"ALL", "SUNNY", "RAINY", "CLOUDY", "SNOWY"};
        filterCombo = new JComboBox<>(filterOptions);
        filterCombo.setPreferredSize(new Dimension(770, 25));
        
        filterCombo.addActionListener(e -> {
            String selected = (String) filterCombo.getSelectedItem();
            WeatherIterator it = null; // Bizim yazdığımız özel Iterator arayüzü
            
            // HOCAYA CEVAP (ITERATOR): İlgili hava durumuna göre koleksiyonun (listenin) 
            // iç yapısını dışarıya sızdırmadan güvenli bir şekilde dolaşmamızı sağlayan Iterator nesnesi üretilir.
            if (selected.equals("SUNNY")) it = new SunnyIterator(repository.getCities());
            else if (selected.equals("RAINY")) it = new RainyIterator(repository.getCities());
            else if (selected.equals("CLOUDY")) it = new CloudyIterator(repository.getCities());
            else if (selected.equals("SNOWY")) it = new SnowyIterator(repository.getCities());
            
            MainAppWindow mainWin = (MainAppWindow) SwingUtilities.getWindowAncestor(this);
            // Üretilen Iterator sol panele gönderilir, sol panel bu iterator'ın hasNext() metoduyla şehirleri çeker.
            if (mainWin != null && mainWin.getCityListPanel() != null) {
                mainWin.getCityListPanel().refreshFilteredCities(it);
            }
        });
        bottomRow.add(filterCombo);
        add(bottomRow);
    }
}