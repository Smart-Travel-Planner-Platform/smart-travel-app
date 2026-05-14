package com.smarttravel.patterns.command;

import com.smarttravel.patterns.composite.ActivityPlan;
import com.smarttravel.patterns.composite.ActivityComponent;
/*Bu dosya, Command Pattern arayüzünü uygular ve listenin sırasını
 değiştirirken "Undo" (Geri Al) işlemini mümkün kılar. */

// Seyahat planındaki bir öğenin sırasını değiştirmek için kullanılan
//  komut sınıfı

/*Bu Sınıf Ne İşe Yarar?
Sıralama Yönetimi: Kullanıcının seyahat rotasındaki durakların sırasını
 (örneğin sabah gitmek yerine öğleden sonraya kaydırma) yönetir.

Undo/Redo Desteği: Bir aktiviteyi yanlışlıkla yanlış sıraya taşırsanız,
 undo() metodu sayesinde oldIndex bilgisi kullanılarak öğe eski yerine hatasız döndürülür.

Kapsülleme: Listenin iç yapısını (ArrayList vb.) bilmeye gerek kalmadan,
 taşıma işlemini bağımsız bir nesne olarak paketler.

Arayüzden Tetiklenme Senaryosu
Arayüzdeki (index.html) seyahat planı panelinde yer alan butonlar şu şekilde çalışır:

"Up" (Yukarı) Butonuna Basıldığında:

PlannerPanel içindeki ilgili metod çalışır.

Seçili aktivitenin mevcut sırası (index) ve bir üstü (index - 1) hesaplanır.

new MoveActivityCommand(plan, index, index - 1) nesnesi oluşturulur.

CommandManager.execute(command) çağrılarak işlem gerçekleştirilir.

"Down" (Aşağı) Butonuna Basıldığında:

Seçili aktivitenin sırası (index) ve bir altı (index + 1) alınır.

new MoveActivityCommand(plan, index, index + 1) oluşturulur.

Aktivite listede bir basamak aşağı kaydırılır.

"Undo" (Geri Al) Butonuna Basıldığında:

CommandManager (Invoker) yığındaki son MoveActivityCommand nesnesini bulur.

Komutun içindeki undo() metodu tetiklenerek aktiviteyi otomatik olarak newIndex'ten
 oldIndex'e geri taşır. */
public class MoveActivityCommand implements Command {
    private ActivityPlan plan;            // İşlemin yapılacağı ana plan (Composite)
    private int oldIndex;                 // Öğenin işlemden önceki başlangıç sırası
    private int newIndex;                 // Öğenin taşınacağı hedef sırası

    // Yapıcı metod: Hangi planda, hangi öğenin nereden nereye gideceğini belirler
    public MoveActivityCommand(ActivityPlan plan, int oldIndex, int newIndex) {
        this.plan = plan;
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    @Override
    public void execute() {
        // Belirlenen indisteki aktiviteyi plandan geçici olarak alır ve taşır
        move(oldIndex, newIndex);
    }

    @Override
    public void undo() {
        // Geri alma işlemi: Aktiviteyi yeni yerinden alıp eski orijinal yerine koyar
        move(newIndex, oldIndex);
    }

    // Listede kaydırma işlemini yapan yardımcı metod
    private void move(int from, int to) {
        // İlgili indisteki aktivite bileşenini bulur
        ActivityComponent component = plan.getComponent(from);
        // Önce eski yerinden siler
        plan.removeComponent(component);
        // Sonra yeni hedeflenen sıraya yerleştirir
        plan.addComponentAt(to, component);
    }
}