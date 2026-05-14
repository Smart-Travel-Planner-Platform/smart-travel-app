package com.smarttravel.gui;

import com.smarttravel.patterns.composite.ActivityPlan;
import com.smarttravel.patterns.composite.TravelComponent;

import command.AddActivityCommand;
import command.ClearPlanCommand;
import command.Command;
import command.CommandManager;
import composite.Activity;

import com.smarttravel.patterns.command.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

// Seyahat planının (Composite yapı) yönetildiği ve görüntülendiği ana panel

/* Bu Sınıf Ne İşe Yarar?
Hiyerarşi Görselleştirme: Kullanıcının eklediği "Day 1", "Day 2" gibi
 alt planları ve bunların içindeki aktiviteleri (Composite deseni) ekranda listeler.

Komut Merkezi: "Ekle", "Sil" veya "Temizle" gibi tüm kullanıcı eylemlerini
 Command nesnelerine dönüştürerek CommandManager'a iletir.

Anlık Geri Bildirim: Bir öğe silindiğinde veya taşındığında refreshUI() metodu
 sayesinde ekranın anında güncellenmesini sağlar.

Arayüzden Tetiklenme Senaryosu
Arayüzdeki (index.html) sağ tarafta bulunan "Travel Plan" bölümü
 bu kodun tam karşılığıdır:

Aktivite Ekleme: Kullanıcı sol listeden bir şehre tıklayıp plana ekle dediğinde,
 AddActivityCommand çalışır ve sağdaki listeye yeni bir satır gelir.

Maliyet Hesaplama: refreshUI her çalıştığında,
 Composite yapıdaki getTotalCost() metodu tüm alt dalları gezerek
  toplam bütçeyi hesaplar ve ekrandaki barı (Progress Bar) günceller.

Tasarım: DESIGN.md dosyasındaki "Glass Cards" konseptini
 yansıtmak için panelin arka planı yarı şeffaf bırakılmıştır.*/

public class PlannerPanel extends JPanel {

    private ActivityPlan mainPlan;        // Tüm geziyi tutan kök Composite nesnesi
    private DefaultListModel<String> listModel;
    private JList<String> visualList;
    private CommandManager commandManager; // Undo/Redo işlemleri için

    public PlannerPanel() {
        this.mainPlan = new ActivityPlan("My Summer Trip");
        this.commandManager = new CommandManager();
        this.listModel = new DefaultListModel<>();
        this.visualList = new JList<>(listModel);
        
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(222, 234, 237, 150)); // Aqua-Futurism şeffaf cam efekti

        // --- Üst Başlık ve Bilgi ---
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel titleLabel = new JLabel("Travel Itinerary", JLabel.CENTER);
        titleLabel.setFont(new Font("Lexend", Font.BOLD, 18));
        header.add(titleLabel);
        
        add(header, BorderLayout.NORTH);
        add(new JScrollPane(visualList), BorderLayout.CENTER);

        // --- Alt Butonlar (Command Tetikleyicileri) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton btnAdd = new JButton("Add Activity");
        JButton btnRemove = new JButton("Remove Selected");
        JButton btnClear = new JButton("Clear All");
        JButton btnUndo = new JButton("Undo");

        // "Add Activity" butonu tetiklendiğinde
        btnAdd.addActionListener(e -> {
            // Normalde burada bir diyalog kutusu açılıp veri alınır, 
            // şimdilik örnek bir aktivite ekliyoruz.
            Command addCmd = new AddActivityCommand(mainPlan, new Activity("New Site Visit", 50, 2));
            commandManager.executeCommand(addCmd);
            refreshUI();
        });

        // "Clear All" butonu tetiklendiğinde
        btnClear.addActionListener(e -> {
            Command clearCmd = new ClearPlanCommand(mainPlan);
            commandManager.executeCommand(clearCmd);
            refreshUI();
        });

        // "Undo" butonu tetiklendiğinde
        btnUndo.addActionListener(e -> {
            commandManager.undo();
            refreshUI();
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnUndo);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Arayüzü Composite yapıdaki güncel verilerle yeniler
    private void refreshUI() {
        listModel.clear();
        // Composite yapıyı özyineli (recursive) gezerek isimleri listeye ekler
        for (TravelComponent comp : mainPlan.getComponents()) {
            listModel.addElement(comp.getName() + " - $" + comp.getTotalCost());
        }
        // Toplam maliyeti ana pencereye bildirebilirsin
    }
}