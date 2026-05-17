package com.smarttravel.gui;

import com.smarttravel.composite.*;
import com.smarttravel.command.*;
import com.smarttravel.model.City;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/*
 * SINAV NOTU (PLANNER PANEL - PROJENİN BEYNİ):
 * Bu sınıf hocanın en çok dikkat edeceği 3 tasarımı yönetir:
 * 1. COMPOSITE: JTree (Ağaç) yapısı ile ana klasörler ve içindeki yaprak aktiviteler recursive olarak çizilir.
 * 2. COMMAND: Undo, Redo, Move Up, Move Down, Remove gibi işlemlerin Command yöneticisiyle yapılması.
 * 3. HASHMAP MANTIĞI: Her şehrin kendine özel bir planının hafızada (RAM) tutulması.
 */
public class PlannerPanel extends JPanel {

    // Ağaç görselleştirmesi için Swing bileşenleri
    private JTree activityTree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootTreeNode;
    
    // İşlemleri geri/ileri almak için merkezi komut yöneticisi (Invoker)
    private CommandManager commandManager;

    // SINAV NOTU (VERİ YAPISI - HASHMAP):
    // Kullanıcı Ankara'ya plan yapıp İstanbul'a geçtiğinde Ankara'nın silinmemesi gerekir.
    // Bu yüzden "Şehir Adı" -> "O Şehrin Planı" şeklinde bir eşleştirme (Key-Value) kullanıyoruz.
    private Map<String, ActivityPlan> cityPlans;
    private ActivityPlan activePlan; // O an ekranda işlem yapılan şehrin planı
    
    // Şehrin nüfus, alan ve hava durumu bilgilerini tutmak için City nesnesi
    private City currentCity; 

    // Arayüz elemanları
    private JCheckBox chkMuseum, chkCenter, chkShopping, chkPark;
    private JTextField txtLabel, txtCost, txtHours, txtPlanName;
    private JTextArea txtPreview;
    private JButton btnAddSelected, btnAddCustom, btnAddPlanNode;
    private JButton btnRemove, btnMoveUp, btnMoveDown, btnClear, btnUndo, btnRedo;

    // Alt toplam ve üst başlık etiketleri
    private JLabel lblPreviewTotal;
    private JLabel lblPreviewHeader;

    public PlannerPanel() {
        this.cityPlans = new HashMap<>();
        this.commandManager = new CommandManager();
        setLayout(new GridLayout(1, 2, 10, 10));
        createMiddleColumn();
        createRightColumn();
    }

    // SINAV NOTU (AKTİF ŞEHİR DEĞİŞİMİ):
    // Artık sadece isim (String) değil, seçilen City nesnesini komple alıyoruz.
    public void setActiveCity(City city) {
        this.currentCity = city; 
        String cityName = city.getName(); 
        
        // Eğer bu şehre ilk defa tıklanıyorsa, ona sıfırdan bir "Kök Plan" oluştur ve Map'e kaydet.
        if (!cityPlans.containsKey(cityName)) {
            cityPlans.put(cityName, new ActivityPlan(cityName + " Root Plan"));
        }
        
        // Önceden varsa veya yeni oluşturulduysa, onu "Aktif Plan" olarak belirle.
        this.activePlan = cityPlans.get(cityName);
        
        // Ekrana nüfus, hava durumu ve seçili checkbox verilerini yansıt
        updatePreviewText();
        
        // Şehir değiştiği için Command (Geri Alma) geçmişini sıfırla ki, 
        // Ankara'nın işlemini İstanbul'da yanlışlıkla geri almayalım.
        this.commandManager = new CommandManager(); 
        
        // Ekrandaki JTree ağacını yeni aktif şehre göre tekrar çizdir
        refreshTreeUI();
    }

    private void createMiddleColumn() {
        JPanel middleCol = new JPanel(new BorderLayout(5, 5));
        middleCol.setBorder(BorderFactory.createTitledBorder("Planner"));

        JPanel pnlActivities = new JPanel(new GridLayout(6, 1));
        
        lblPreviewHeader = new JLabel("Preview for Active City");
        pnlActivities.add(lblPreviewHeader);
        
        pnlActivities.add(new JLabel("Available Activities:"));
        
        chkMuseum = new JCheckBox("Visit Museum (2.0 h, $18.0)");
        chkCenter = new JCheckBox("Visit City Center (1.5 h, $0.0)");
        chkShopping = new JCheckBox("Visit Shopping Mall (2.0 h, $25.0)");
        chkPark = new JCheckBox("Walk in the Park (1.0 h, $7.0)");
        
        pnlActivities.add(chkMuseum); pnlActivities.add(chkCenter);
        pnlActivities.add(chkShopping); pnlActivities.add(chkPark);

        // Checkbox'lara tıklanmasını dinleyen listener
        java.awt.event.ItemListener checkboxListener = e -> updatePreviewText();
        chkMuseum.addItemListener(checkboxListener);
        chkCenter.addItemListener(checkboxListener);
        chkShopping.addItemListener(checkboxListener);
        chkPark.addItemListener(checkboxListener);

        // Custom Activity text kutuları oluşturma kısmı
        JPanel pnlCustom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlCustom.setBorder(BorderFactory.createTitledBorder("Add New Activity Option"));
        pnlCustom.add(new JLabel("Label:")); txtLabel = new JTextField(8); pnlCustom.add(txtLabel);
        pnlCustom.add(new JLabel("Cost:")); txtCost = new JTextField(4); pnlCustom.add(txtCost);
        pnlCustom.add(new JLabel("Hours:")); txtHours = new JTextField(4); pnlCustom.add(txtHours);
        btnAddCustom = new JButton("Add Custom Activity Option"); pnlCustom.add(btnAddCustom);

        JPanel topHalf = new JPanel(new BorderLayout());
        topHalf.add(pnlActivities, BorderLayout.NORTH);
        topHalf.add(pnlCustom, BorderLayout.SOUTH);
        middleCol.add(topHalf, BorderLayout.NORTH);

        txtPreview = new JTextArea("Please select a city from the left list first.");
        txtPreview.setEditable(false);
        middleCol.add(new JScrollPane(txtPreview), BorderLayout.CENTER);

        JPanel pnlAddBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        lblPreviewTotal = new JLabel("Preview total: 0.0 hours / $0.0");
        pnlAddBtn.add(lblPreviewTotal);
        
        btnAddSelected = new JButton("Add Selected Activities");
        pnlAddBtn.add(btnAddSelected);
        middleCol.add(pnlAddBtn, BorderLayout.SOUTH);
        add(middleCol);

        // SINAV NOTU (COMMAND DESENİ - DINAMIK EKLEME):
        // Kullanıcı ekle butonuna bastığında aktiviteler ağaçta o an hangi composite (klasör) 
        // seçiliyse onun altına eklenir. Hiçbir şey seçilmediyse Root'a eklenir.
        btnAddSelected.addActionListener(e -> {
            if (activePlan == null) {
                JOptionPane.showMessageDialog(this, "Please select a city first!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Adım 1: Eklenecek hedef klasörü (Composite düğümü) buluyoruz
            ActivityPlan targetPlan = activePlan;
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) activityTree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode != rootTreeNode) {
                Object userObj = selectedNode.getUserObject();
                if (userObj instanceof ComponentNodeWrapper) {
                    TravelComponent comp = ((ComponentNodeWrapper) userObj).getComponent();
                    if (comp instanceof ActivityPlan) {
                        targetPlan = (ActivityPlan) comp; // Seçili düğüm bir klasörse hedef odur
                    } else {
                        // Eğer seçili düğüm bir yaprak aktiviteyse, onun bağlı olduğu üst klasörü hedef seç
                        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                        if (parentNode != null && parentNode != rootTreeNode) {
                            targetPlan = (ActivityPlan) ((ComponentNodeWrapper) parentNode.getUserObject()).getComponent();
                        }
                    }
                }
            }

            // Adım 2: Seçili olan her aktiviteyi hedef klasöre Command ile ekle
            boolean added = false;
            if (chkMuseum.isSelected()) {
                commandManager.executeCommand(new AddActivityCommand(targetPlan, new Activity("Visit Museum", 18.0, 2.0)));
                added = true;
            }
            if (chkCenter.isSelected()) {
                commandManager.executeCommand(new AddActivityCommand(targetPlan, new Activity("Visit City Center", 0.0, 1.5)));
                added = true;
            }
            if (chkShopping.isSelected()) {
                commandManager.executeCommand(new AddActivityCommand(targetPlan, new Activity("Visit Shopping Mall", 25.0, 2.0)));
                added = true;
            }
            if (chkPark.isSelected()) {
                commandManager.executeCommand(new AddActivityCommand(targetPlan, new Activity("Walk in the Park", 7.0, 1.0)));
                added = true;
            }

            if (added) {
                chkMuseum.setSelected(false); chkCenter.setSelected(false);
                chkShopping.setSelected(false); chkPark.setSelected(false);
                updatePreviewText();
            }
            refreshTreeUI(); 
        });

        btnAddCustom.addActionListener(e -> {
            if (activePlan == null) {
                JOptionPane.showMessageDialog(this, "Please select a city first!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String label = txtLabel.getText().trim();
            if (label.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a label!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double cost;
            double hours;
            try {
                cost = Double.parseDouble(txtCost.getText().trim());
                hours = Double.parseDouble(txtHours.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for Cost and Hours!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ActivityPlan targetPlan = activePlan;
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) activityTree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode != rootTreeNode) {
                Object userObj = selectedNode.getUserObject();
                if (userObj instanceof ComponentNodeWrapper) {
                    TravelComponent comp = ((ComponentNodeWrapper) userObj).getComponent();
                    if (comp instanceof ActivityPlan) {
                        targetPlan = (ActivityPlan) comp;
                    } else {
                        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                        if (parentNode != null && parentNode != rootTreeNode) {
                            targetPlan = (ActivityPlan) ((ComponentNodeWrapper) parentNode.getUserObject()).getComponent();
                        }
                    }
                }
            }

            commandManager.executeCommand(new AddActivityCommand(targetPlan, new Activity(label, cost, hours)));
            txtLabel.setText("");
            txtCost.setText("");
            txtHours.setText("");
            refreshTreeUI();
        });
    }

    private void updatePreviewText() {
        if (activePlan == null || currentCity == null) {
            txtPreview.setText("Please select a city from the left list first.");
            if (lblPreviewTotal != null) lblPreviewTotal.setText("Preview total: 0.0 hours / $0.0");
            if (lblPreviewHeader != null) lblPreviewHeader.setText("Preview for Active City");
            return;
        }

        String cityName = currentCity.getName();
        
        if (lblPreviewHeader != null) {
            lblPreviewHeader.setText("Preview for " + cityName + " | Weather: " + currentCity.getCurrentWeatherState() + " | Temp: " + currentCity.getCurrentTemperature() + "°C");
        }

        StringBuilder previewStr = new StringBuilder();
        previewStr.append("Active city: ").append(cityName).append("\n");
        previewStr.append("Population: ").append(currentCity.getPopulation()).append("\n");
        previewStr.append("Area: ").append(currentCity.getArea()).append(" km²\n");
        previewStr.append("Current weather: ").append(currentCity.getCurrentWeatherState()).append(", ").append(currentCity.getCurrentTemperature()).append("°C\n\n");

        double totalHours = 0.0;
        double totalCost = 0.0;
        StringBuilder decoratorSummary = new StringBuilder("Base plan for visiting ").append(cityName);
        StringBuilder selectedActivitiesList = new StringBuilder();

        if (chkCenter.isSelected()) {
            selectedActivitiesList.append("- Visit City Center [1.5 h, $0.0]\n");
            totalHours += 1.5; totalCost += 0.0;
            decoratorSummary.append(", Visit City Center");
        }
        if (chkMuseum.isSelected()) {
            selectedActivitiesList.append("- Visit Museum [2.0 h, $18.0]\n");
            totalHours += 2.0; totalCost += 18.0;
            decoratorSummary.append(", Visit Museum");
        }
        if (chkShopping.isSelected()) {
            selectedActivitiesList.append("- Visit Shopping Mall [2.0 h, $25.0]\n");
            totalHours += 2.0; totalCost += 25.0;
            decoratorSummary.append(", Visit Shopping Mall");
        }
        if (chkPark.isSelected()) {
            selectedActivitiesList.append("- Walk in the Park [1.0 h, $7.0]\n");
            totalHours += 1.0; totalCost += 7.0;
            decoratorSummary.append(", Walk in the Park");
        }

        if (totalHours == 0.0 && totalCost == 0.0) {
            previewStr.append("Waiting for activities...\n");
        } else {
            previewStr.append("Selected activities to be added under the active composite node:\n");
            previewStr.append(selectedActivitiesList.toString()).append("\n");
            previewStr.append("Active composite target: ").append(activePlan.getName()).append("\n\n");
            previewStr.append("Decorator-based preview summary:\n");
            previewStr.append(decoratorSummary.toString()).append("\n");
            previewStr.append("Total time: ").append(totalHours).append(" hours\n");
            previewStr.append("Total cost: $").append(totalCost).append("\n");
        }

        txtPreview.setText(previewStr.toString());
        if (lblPreviewTotal != null) {
            lblPreviewTotal.setText("Preview total: " + totalHours + " hours / $" + totalCost);
        }
    }

    private void createRightColumn() {
        JPanel rightCol = new JPanel(new BorderLayout(5, 5));
        rightCol.setBorder(BorderFactory.createTitledBorder("Activity Tree for Active City"));

        JPanel pnlAddNode = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlAddNode.setBorder(BorderFactory.createTitledBorder("Add Composite Plan Node"));
        pnlAddNode.add(new JLabel("Plan name:"));
        txtPlanName = new JTextField(15); pnlAddNode.add(txtPlanName);
        btnAddPlanNode = new JButton("Add Plan Node"); pnlAddNode.add(btnAddPlanNode);
        rightCol.add(pnlAddNode, BorderLayout.NORTH);

        rootTreeNode = new DefaultMutableTreeNode("No City Selected");
        treeModel = new DefaultTreeModel(rootTreeNode);
        activityTree = new JTree(treeModel);
        rightCol.add(new JScrollPane(activityTree), BorderLayout.CENTER);

        JPanel pnlCommands = new JPanel(new GridLayout(2, 3, 5, 5));
        btnRemove = new JButton("Remove Selected Node"); btnMoveUp = new JButton("Move Up");
        btnMoveDown = new JButton("Move Down"); btnClear = new JButton("Clear Active City Tree");
        btnUndo = new JButton("Undo"); btnRedo = new JButton("Redo");

        pnlCommands.add(btnRemove); pnlCommands.add(btnMoveUp); pnlCommands.add(btnMoveDown);
        pnlCommands.add(btnClear); pnlCommands.add(btnUndo); pnlCommands.add(btnRedo);
        rightCol.add(pnlCommands, BorderLayout.SOUTH);
        add(rightCol);

        // =====================================================================================
        // --- ADDACTIONLISTENER ENTEGRASYONU (ADD PLAN NODE - COMPOSITE TETİKLEYİCİSİ) ---
        // =====================================================================================
        btnAddPlanNode.addActionListener(e -> {
            if (activePlan == null) {
                JOptionPane.showMessageDialog(this, "Please select a city first!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String nodeName = txtPlanName.getText().trim();
            if (nodeName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a plan name!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Hangi alt klasör seçildiyse yeni planı onun içine oluştur, seçilmediyse ana plana (Root) ekle
            ActivityPlan targetPlan = activePlan;
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) activityTree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode != rootTreeNode) {
                Object userObj = selectedNode.getUserObject();
                if (userObj instanceof ComponentNodeWrapper) {
                    TravelComponent comp = ((ComponentNodeWrapper) userObj).getComponent();
                    if (comp instanceof ActivityPlan) {
                        targetPlan = (ActivityPlan) comp;
                    } else {
                        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                        if (parentNode != null && parentNode != rootTreeNode) {
                            targetPlan = (ActivityPlan) ((ComponentNodeWrapper) parentNode.getUserObject()).getComponent();
                        }
                    }
                }
            }

            // Yeni alt plan klasörümüzü (Composite) Command deseniyle güvenli şekilde ekliyoruz
            ActivityPlan subPlan = new ActivityPlan(nodeName);
            commandManager.executeCommand(new AddActivityCommand(targetPlan, subPlan));
            
            txtPlanName.setText(""); // Kutuyu temizle
            refreshTreeUI();         // Ağacı güncelle
        });

        btnClear.addActionListener(e -> {
            if(activePlan != null) {
                commandManager.executeCommand(new ClearPlanCommand(activePlan));
                refreshTreeUI();
            }
        });

        btnUndo.addActionListener(e -> {
            commandManager.undo();
            refreshTreeUI();
        });

        btnRedo.addActionListener(e -> {
            commandManager.redo();
            refreshTreeUI();
        });
        
        // Derinlemesine klasörlerden güvenli silme operasyonu
        btnRemove.addActionListener(e -> {
            if (activePlan == null) return;
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) activityTree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode != rootTreeNode) {
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                ActivityPlan parentPlan = activePlan;
                if (parentNode != rootTreeNode) {
                    parentPlan = (ActivityPlan) ((ComponentNodeWrapper) parentNode.getUserObject()).getComponent();
                }
                
                TravelComponent comp = ((ComponentNodeWrapper) selectedNode.getUserObject()).getComponent();
                commandManager.executeCommand(new RemoveActivityCommand(parentPlan, comp));
                refreshTreeUI();
            }
        });

        // Derinlemesine klasörlerden güvenli yukarı taşıma operasyonu
        btnMoveUp.addActionListener(e -> {
            if (activePlan == null) return;
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) activityTree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode != rootTreeNode) {
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                int index = parentNode.getIndex(selectedNode);
                if (index > 0) {
                    ActivityPlan parentPlan = activePlan;
                    if (parentNode != rootTreeNode) {
                        parentPlan = (ActivityPlan) ((ComponentNodeWrapper) parentNode.getUserObject()).getComponent();
                    }
                    commandManager.executeCommand(new MoveActivityCommand(parentPlan, index, index - 1));
                    refreshTreeUI();
                }
            }
        });

        // Derinlemesine klasörlerden güvenli aşağı taşıma operasyonu
        btnMoveDown.addActionListener(e -> {
            if (activePlan == null) return;
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) activityTree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode != rootTreeNode) {
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                int index = parentNode.getIndex(selectedNode);
                if (index >= 0 && index < parentNode.getChildCount() - 1) {
                    ActivityPlan parentPlan = activePlan;
                    if (parentNode != rootTreeNode) {
                        parentPlan = (ActivityPlan) ((ComponentNodeWrapper) parentNode.getUserObject()).getComponent();
                    }
                    commandManager.executeCommand(new MoveActivityCommand(parentPlan, index, index + 1));
                    refreshTreeUI();
                }
            }
        });
    }

    // SINAV NOTU (RECURSIVE COMPOSITE TREE REFRESH):
    // Klasörlerin içindeki alt klasörleri ve aktiviteleri sonsuz derinlikte 
    // tarayıp ekrandaki JTree'ye hiyerarşik basan özyineli mimari.
    private void refreshTreeUI() {
        if (activePlan == null) return;
        
        rootTreeNode.removeAllChildren(); 
        populateTreeNodes(rootTreeNode, activePlan); // Özyineli doldurmayı tetikle
        
        // Kök düğümün ismini ve toplam polimorfik maliyetlerini güncelle
        rootTreeNode.setUserObject(activePlan.getName() + " [" + activePlan.getTotalDuration() + " h, $" + activePlan.getTotalCost() + "]");
        treeModel.reload(); 
        
        // Kullanıcı kolaylığı için tüm klasörleri otomatik genişlet (Açık getir)
        for (int i = 0; i < activityTree.getRowCount(); i++) {
            activityTree.expandRow(i);
        }
    }

    // Recursive (Özyineli) yardımcı fonksiyon
    private void populateTreeNodes(DefaultMutableTreeNode uiNode, ActivityPlan compositePlan) {
        for (TravelComponent comp : compositePlan.getComponents()) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new ComponentNodeWrapper(comp));
            uiNode.add(childNode);
            
            // Eğer bu component bir alt plan (Klasör/Composite) ise, onun çocuklarını da içeriye tarayarak ekle
            if (comp instanceof ActivityPlan) {
                populateTreeNodes(childNode, (ActivityPlan) comp);
            }
        }
    }

    private class ComponentNodeWrapper {
        private TravelComponent comp;
        public ComponentNodeWrapper(TravelComponent comp) { this.comp = comp; }
        public TravelComponent getComponent() { return comp; }
        
        @Override
        public String toString() {
            if (comp instanceof ActivityPlan) {
                return comp.getName() + " [" + comp.getTotalDuration() + " h, $" + comp.getTotalCost() + "]";
            }
            return comp.getName() + " [Cost: $" + comp.getTotalCost() + ", " + comp.getTotalDuration() + "h]";
        }
    }
}