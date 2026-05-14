package com.smarttravel.gui;

import com.smarttravel.strategy.*;

import strategy.SortByArea;
import strategy.SortByName;
import strategy.SortByPopulation;
import strategy.SortStrategy;

import com.smarttravel.iterator.*;
import com.smarttravel.repository.CityRepository;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// Sıralama ve Filtreleme kontrollerinin yönetildiği panel

/*Bu Sınıf Ne İşe Yarar?
Strateji Seçici (Strategy Selector): Kullanıcı "Nüfusa göre sırala" dediğinde,
 arka planda SortByPopulation sınıfının çalışmasını sağlar.

Veri Filtreleme (Filtering): Iterator deseni aracılığıyla sadece belirli hava
 durumuna sahip şehirlerin ekranda kalmasını yönetir.

Entegrasyon Merkezi: Geliştirici 1'in Singleton deposundaki veriyi,
 Geliştirici 2'nin Algoritmalarıyla işleyip senin GUI panellerinde gösterilmesini sağlar.

Arayüzden Tetiklenme Senaryosu
Arayüzdeki (index.html) üst bölümdeki seçim alanları bu kodun karşılığıdır:

ComboBox Değiştiğinde:

ControlPanel içindeki ActionListener anında devreye girer.

Geliştirici 2'nin yazdığı SortStrategy sınıflarından biri örneklenir (instantiate).

Sıralanmış yeni liste CityListPanel (görselindeki dosya) içine
 gönderilerek ekranın sol tarafındaki şehir listesi güncellenir.

Tasarım Uyumu:

DESIGN.md dosyasındaki "Tactile Inputs" maddesine uygun olarak,
 bu ComboBox'ların görünümleri Swing tarafında özelleştirilmelidir. */
public class ControlPanel extends JPanel {
    
    private JComboBox<String> sortCombo;     // Geliştirici 2'nin Strategy'leri için
    private JComboBox<String> filterCombo;   // Geliştirici 2'nin Iterator'ları için
    private CityRepository repository;       // Geliştirici 1'in verisi için

    public ControlPanel() {
        this.repository = CityRepository.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        // Aqua-Futurism temasına uygun cam efekti ve düzen
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        this.setBackground(new Color(222, 234, 237, 180)); 

        // --- SIRALAMA (STRATEGY PATTERN) ---
        add(new JLabel("Sort by:"));
        String[] sortOptions = {"Name", "Population", "Area"};
        sortCombo = new JComboBox<>(sortOptions);
        
        // Geliştirici 2'nin yazdığı Strategy sınıflarını tetikleyen dinleyici
        sortCombo.addActionListener(e -> {
            String selected = (String) sortCombo.getSelectedItem();
            SortStrategy strategy;
            
            // Seçime göre ilgili somut stratejiyi belirle
            if (selected.equals("Population")) strategy = new SortByPopulation();
            else if (selected.equals("Area")) strategy = new SortByArea();
            else strategy = new SortByName();
            
            // Veriyi sırala ve arayüzü güncelle
            repository.setCities(strategy.sort(repository.getCities()));
            // Ana penceredeki listeyi yenilemek için gerekli tetikleyici buraya gelir
        });
        add(sortCombo);

        // --- FİLTRELEME (ITERATOR PATTERN) ---
        add(new JLabel("Weather Filter:"));
        String[] filterOptions = {"All", "Sunny", "Rainy", "Cloudy"};
        filterCombo = new JComboBox<>(filterOptions);
        
        // Geliştirici 2'nin Iterator sınıflarını kullanan dinleyici
        filterCombo.addActionListener(e -> {
            // Burada seçilen hava durumuna göre ilgili Iterator çağrılır
            // Örn: new SunnyIterator(repository.getCities())
            // Filtrelenmiş şehirler CityListPanel'e gönderilir.
        });
        add(filterCombo);
    }
}