package com.smarttravel.iterator;

import com.smarttravel.model.City;

/**
 * Iterator (Yineleyici) Arayüzü:
 * SENG 324 Ders Notları: "Bir koleksiyonun altındaki temsili (ArrayList, Array
 * vb.) ifşa etmeden elemanlarına sırayla erişim sağlar."
 * Neden kullanıyoruz?: Orijinal şehir listemizi bozmadan veya hafızada yeni
 * kopyalar yaratmadan,
 * sadece istediğimiz hava durumuna sahip şehirler üzerinde gezinmek için bir
 * standart belirliyoruz.
 */
public interface WeatherIterator {
    boolean hasNext(); // Koleksiyonda uygun şartı sağlayan başka eleman var mı?

    City next(); // Uygun şartı sağlayan sıradaki elemanı getir.
}