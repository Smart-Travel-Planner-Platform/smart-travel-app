package com.smarttravel.gui;

import com.smarttravel.model.City;
import com.smarttravel.repository.CityRepository;
import com.smarttravel.strategy.SortStrategy;
import com.smarttravel.iterator.WeatherIterator;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class CityListPanel extends JPanel {
    private JList<String> allCitiesList;
    private DefaultListModel<String> allCitiesModel;
    
    private JList<String> filteredCitiesList;
    private DefaultListModel<String> filteredCitiesModel;

    public CityListPanel() {
        setLayout(new GridLayout(1, 2, 10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 1. Liste: Tüm Şehirler (Strategy ile sıralanır)
        allCitiesModel = new DefaultListModel<>();
        allCitiesList = new JList<>(allCitiesModel);
        JPanel allPanel = new JPanel(new BorderLayout());
        allPanel.add(new JLabel("All Cities (resorted only when sort type changes)"), BorderLayout.NORTH);
        allPanel.add(new JScrollPane(allCitiesList), BorderLayout.CENTER);

        // 2. Liste: Filtreli Şehirler (Iterator ile filtrelenir)
        filteredCitiesModel = new DefaultListModel<>();
        filteredCitiesList = new JList<>(filteredCitiesModel);
        JPanel filteredPanel = new JPanel(new BorderLayout());
        filteredPanel.add(new JLabel("Cities by Weather"), BorderLayout.NORTH);
        filteredPanel.add(new JScrollPane(filteredCitiesList), BorderLayout.CENTER);

        add(allPanel);
        add(filteredPanel);

        // =====================================================================================
        // SINAV NOTU (EVENT LISTENER - OLAY DİNLEYİCİSİ):
        // Kullanıcının arayüzde bir şehre tıkladığını (seçtiğini) yakalayan mekanizmadır.
        // Hoca "Şehri seçtiğimizi PlannerPanel nereden biliyor?" diye sorarsa bu bloğu göster.
        // =====================================================================================
        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void valueChanged(ListSelectionEvent e) {
                // getValueIsAdjusting(): Kullanıcı tıklama işlemini tam bitirdiğinde (fareyi bıraktığında) çalışır.
                if (!e.getValueIsAdjusting()) {
                    JList<String> sourceList = (JList<String>) e.getSource();
                    
                    if (sourceList.getSelectedValue() != null) {
                        // Eğer soldaki listeden seçim yapıldıysa, sağdakinin seçimini kaldır (Çakışmayı önler)
                        if (sourceList == allCitiesList) filteredCitiesList.clearSelection();
                        else allCitiesList.clearSelection();

                        // Ekrandaki uzun metni parçala (Örn: "Ankara (Pop: 5000" -> sadece "Ankara" kısmını al)
                        String selectedText = sourceList.getSelectedValue();
                        String cityName = selectedText.split(" \\(")[0];

                        // SINAV NOTU (SWING UTILITIES):
                        // CityListPanel, PlannerPanel'i doğrudan tanımaz. Önce ikisinin de babası olan
                        // MainAppWindow'u bulur, oradan PlannerPanel'e ulaşıp "Aktif şehri değiştir" emrini verir.
                        MainAppWindow mainWin = (MainAppWindow) SwingUtilities.getWindowAncestor(CityListPanel.this);
                        if (mainWin != null && mainWin.getPlannerPanel() != null) {
                            mainWin.getPlannerPanel().setActiveCity(cityName);
                        }
                    }
                }
            }
        };

        // Yazdığımız dinleyiciyi her iki listeye de ekliyoruz
        allCitiesList.addListSelectionListener(selectionListener);
        filteredCitiesList.addListSelectionListener(selectionListener);

        refreshAllCities(null); 
    }

    public void refreshAllCities(SortStrategy strategy) {
        List<City> cities = new ArrayList<>(CityRepository.getInstance().getCities());
        if (strategy != null) strategy.sort(cities);
        allCitiesModel.clear();
        for (City c : cities) {
            allCitiesModel.addElement(c.getName() + " (Pop: " + c.getPopulation() + ", Area: " + c.getArea() + " km², Temp: " + c.getCurrentTemperature() + "°C, " + c.getCurrentWeatherState().toString() + ")");
        }
    }

    public void refreshFilteredCities(WeatherIterator iterator) {
        filteredCitiesModel.clear();
        if (iterator == null) {
            for (City c : CityRepository.getInstance().getCities()) {
                filteredCitiesModel.addElement(c.getName() + " (Pop: " + c.getPopulation() + ", Area: " + c.getArea() + " km², Temp: " + c.getCurrentTemperature() + "°C, " + c.getCurrentWeatherState().toString() + ")");
            }
            return;
        }
        while (iterator.hasNext()) {
            City c = iterator.next();
            filteredCitiesModel.addElement(c.getName() + " (Pop: " + c.getPopulation() + ", Area: " + c.getArea() + " km², Temp: " + c.getCurrentTemperature() + "°C, " + c.getCurrentWeatherState().toString() + ")");
        }
    }
}