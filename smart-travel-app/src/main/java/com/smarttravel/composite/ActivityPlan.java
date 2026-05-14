package com.smarttravel.composite;/*KARYA */

import java.util.ArrayList;
import java.util.List;
/**Bu Sınıfın Sırrı Nedir? (Detaylı Açıklama)
Liste Tutma (List<TravelComponent>): Bu sınıfın içinde bir liste var.
 Bu liste sayesinde "İtalya Gezisi" planının içine
  "Roma Turu" ve "Venedik Turu" gibi aktiviteleri koyabiliyoruz.

Özyineli (Recursive) Yapı: En can alıcı nokta burası!
 getTotalCost() metodunu çağırdığında, bu sınıf içindeki 
 her bir child'a gidip getTotalCost() diye sorar.

Eğer child bir Activity ise direkt fiyatını söyler.

Eğer child başka bir ActivityPlan ise, o da kendi içindekilere sorar.

Sonuçta hepsi toplanıp en yukarıya iletilir. */

/**
 * Bu sınıf 'Composite' nesnesidir. 
 * İçinde birden fazla TravelComponent (Activity veya başka Plan) tutabilir.
 */


/*Ne İşe Yarar ve Nerede Tetiklenir?
Ağaç Yapısı: Uygulamanın sol tarafında veya bir
 panelde gezi hiyerarşisini (Şehir -> Günler -> Aktiviteler)
  göstermek için kullanılır.

Buton Etkileşimi: Kullanıcı bir şehri seyahat planına
 eklediğinde (Ekle butonu), sen bir ActivityPlan nesnesine
  yeni bir bileşen eklemiş olursun.

Otomatik Güncelleme: Bir aktivitenin fiyatı değiştiğinde
 veya yeni bir şey eklendiğinde, sadece en üstteki getTotalCost()'u
  çağırmak tüm planın bütçesini güncel tutmanı sağlar. */
public class ActivityPlan implements TravelComponent {
    private String planName;
    // İçindeki alt bileşenleri (çocukları) tutan liste
    private List<TravelComponent> components = new ArrayList<>();

    public ActivityPlan(String planName) {
        this.planName = planName;
    }

    // Listeye yeni bir aktivite veya alt plan ekler
    public void addComponent(TravelComponent component) {
        components.add(component);
    }

    // Listeden bir bileşeni çıkarır
    public void removeComponent(TravelComponent component) {
        components.remove(component);
    }

    @Override
    public double getTotalCost() {
        double total = 0;
        // Özyineli (Recursive) İşlem: Listedeki her bir elemana kendi maliyetini sorar
        for (TravelComponent child : components) {
            total += child.getTotalCost();
        }
        return total;
    }

    @Override
    public double getTotalDuration() {
        double total = 0;
        // Listedeki her bir elemanın süresini toplar
        for (TravelComponent child : components) {
            total += child.getTotalDuration();
        }
        return total;
    }

    @Override
    public void displayPlan(int indentLevel) {
        String indent = "  ".repeat(indentLevel);
        System.out.println(indent + "+ Plan: " + planName);
        // Altındaki tüm elemanlara "kendini yazdır" emri verir
        for (TravelComponent child : components) {
            child.displayPlan(indentLevel + 1);
        }
    }
}