package com.smarttravel;

import com.smarttravel.gui.MainAppWindow;
import com.smarttravel.observer.WeatherReportProvider;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        
        // 1. Geliştirici 1'in (Senin) Motoru başlatılıyor
        WeatherReportProvider weatherProvider = new WeatherReportProvider();

        // 2. Geliştirici 3'ün (Karya'nın) Arayüzü başlatılıyor
        SwingUtilities.invokeLater(() -> {
            try {
                // Arayüze hava durumu motorunu bağlıyoruz ki grafikler canlansın
                MainAppWindow mainWindow = new MainAppWindow(weatherProvider);
                mainWindow.setVisible(true);
            } catch (Exception e) {
                System.out.println("Arayüz başlatılırken bir hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        });

        // 3. Arka planda 3 saniyede bir verileri değiştirecek Thread'i çalıştır
        Thread weatherThread = new Thread(weatherProvider);
        weatherThread.start();
    }
}