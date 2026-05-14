package com.smarttravel.command;/*KARYA */

import com.smarttravel.composite.ActivityPlan;
import com.smarttravel.composite.TravelComponent;

/**
 * Command Tasarım Örüntüsü - Somut Komut (Concrete Command) Sınıfı.
 * Bu sınıf, seyahat planına yeni bir aktivite veya alt plan ekleme işlemini kapsüller.
 */
public class AddActivityCommand implements Command {
    private ActivityPlan plan;           // İşlemin yapılacağı hedef plan (Receiver)
    private TravelComponent component;    // Eklenecek olan bileşen (Şehir veya Aktivite)

    public AddActivityCommand(ActivityPlan plan, TravelComponent component) {
        this.plan = plan;
        this.component = component;
    }

    /**
     * Komutu çalıştırır: Bileşeni plan hiyerarşisine ekler.
     */
    @Override
    public void execute() {
        plan.addComponent(component);
        System.out.println("Bileşen plana başarıyla eklendi.");
    }

    /**
     * Geri alma (Undo) işlemi: Eklenen bileşeni plandan geri siler.
     */
    @Override
    public void undo() {
        plan.removeComponent(component);
        System.out.println("Ekleme işlemi geri alındı, bileşen çıkarıldı.");
    }
}