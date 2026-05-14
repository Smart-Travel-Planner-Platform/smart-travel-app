package com.smarttravel.command;/*KARYA */

import com.smarttravel.composite.ActivityPlan;
import com.smarttravel.composite.TravelComponent;

/**
 * Command Tasarım Örüntüsü - Somut Komut (Concrete Command) Sınıfı.
 * Bu sınıf, plandan bir bileşenin çıkarılması işlemini kapsüller.
 */
public class RemoveActivityCommand implements Command {
    private ActivityPlan plan;           // İşlemin yapılacağı hedef plan
    private TravelComponent component;    // Çıkarılacak olan bileşen

    public RemoveActivityCommand(ActivityPlan plan, TravelComponent component) {
        this.plan = plan;
        this.component = component;
    }

    /**
     * Komutu çalıştırır: Belirlenen bileşeni plandan siler.
     */
    @Override
    public void execute() {
        plan.removeComponent(component);
        System.out.println("Bileşen plandan çıkarıldı.");
    }

    /**
     * Geri alma (Undo) işlemi: Silinen bileşeni eski yerine geri ekler.
     * Command Pattern sayesinde kullanıcı "yanlışlıkla sildim" dediğinde veri kaybolmaz.
     */
    @Override
    public void undo() {
        plan.addComponent(component);
        System.out.println("Silme işlemi geri alındı, bileşen plana iade edildi.");
    }
}