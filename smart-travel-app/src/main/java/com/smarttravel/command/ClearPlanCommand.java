package com.smarttravel.patterns.command;

import com.smarttravel.patterns.composite.ActivityPlan;
import com.smarttravel.patterns.composite.TravelComponent;
import java.util.ArrayList;
import java.util.List;


// Seyahat planındaki tüm öğeleri tek seferde temizlemek için kullanılan komut sınıfı

/*Bu Sınıf Ne İşe Yarar?
Toplu İşlem Yönetimi: Kullanıcının aktiviteleri tek tek silmek yerine
 tüm planı tek tıkla boşaltmasını sağlar.

Veri Güvenliği (Undo): Yanlışlıkla "Temizle" butonuna basılması durumunda,
 tüm plan hiyerarşisini (Composite yapıdaki tüm çocukları) hatasız bir şekilde eski haline döndürür.

Kapsülleme: Silme ve geri yükleme mantığını arayüzden ayırarak
 CommandManager üzerinden yönetilebilir hale getirir.

Arayüzden Tetiklenme Senaryosu
Arayüzdeki (index.html) seyahat planı panelinde yer alan 
etkileşimler şu şekilde çalışır:

"Clear Plan" (Planı Temizle) Butonuna Basıldığında:

PlannerPanel.java içindeki buton dinleyicisi tetiklenir.

Mevcut ActivityPlan nesnesi parametre olarak verilerek new ClearPlanCommand(plan) oluşturulur.

CommandManager.executeCommand(command) metodu çağrılır.

Bu çağrı hem ekrandaki listeyi temizler hem de komutu undoStack içine atar.

"Undo" (Geri Al) Butonuna Basıldığında:

Sağ üstteki geri al ikonuna basıldığında CommandManager.undo() çalışır.

Stack'ten en üstteki ClearPlanCommand çekilir ve içindeki undo() metodu koşturulur.

Ekrandaki "Travel Plan" listesi saniyeler içinde eski aktivitelerle tekrar dolar. */
public class ClearPlanCommand implements Command {
    private ActivityPlan plan;                    // Üzerinde işlem yapılacak ana plan nesnesi
    private List<TravelComponent> backup;         // Silinen öğeleri geri getirebilmek için tutulan yedek liste

    // Yapıcı metod: Komutun hangi plan üzerinde çalışacağını belirler
    public ClearPlanCommand(ActivityPlan plan) {
        this.plan = plan;
        // Mevcut plandaki tüm öğelerin bir kopyasını yedek listeye aktarır
        this.backup = new ArrayList<>(plan.getComponents());
    }

    @Override
    public void execute() {
        // execute() metodu çağrıldığında plandaki tüm aktiviteleri siler
        plan.clearAll();
    }

    @Override
    public void undo() {
        // undo() metodu çağrıldığında, yedeklenen tüm öğeleri plana sırasıyla geri ekler
        for (TravelComponent component : backup) {
            plan.addComponent(component);
        }
    }
}