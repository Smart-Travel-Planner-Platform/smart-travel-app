package com.smarttravel.command;/*KARYA */

import java.util.Stack;

/**
 * Bu sınıf 'Invoker'dır. 
 * Komutları çalıştırır ve işlem geçmişini hafızasında tutar.
 *
 */
/*Bu sınıfta Java'nın Stack (Yığın) yapısını kullanacağız.
 Stack, "Son Giren İlk Çıkar" (LIFO) prensibiyle çalışır,
  bu da geri alma işlemi için mükemmeldir. */

  /*Bu Sınıf Ne İş Yapıyor? (Detaylı Analiz)
Stack<Command>: Bu senin "zaman tünelin".
 Kullanıcı 5 tane aktivite eklediyse, bunlar üst üste yığılır.
  "Geri Al" dediğinde en üstteki (en son yapılan) alınır.

redoStack.clear(): Eğer kullanıcı birkaç şeyi geri alıp sonra
 bambaşka yeni bir işlem yaparsa, eski ileri sarma geçmişi mantıksız
  olacağı için temizlenir (Modern yazılımların genel çalışma şeklidir).

Arayüz (GUI) Bağlantısı: Senin MainAppWindow sınıfında "Undo" butonuna
 tıklandığında sadece commandManager.undo() çağrılacak. Hangi işlemin
  geri alınacağıyla butonun kendisi ilgilenmeyecek, yönetici (Manager) halledecek.

Neden Önemli?
Eğer bu sınıf olmasaydı, her buton için ayrı ayrı "Eğer bu ekleme
 butonuna basıldıysa silme yap", "Eğer silme butonuna basıldıysa geri ekle"
  gibi binlerce if-else yazman gerekirdi. CommandManager sayesinde tüm işlemler
   tek bir merkezden yönetilir. */
public class CommandManager {
    // Yapılan işlemleri tutan yığın (Geri alabilmek için)
    private Stack<Command> undoStack = new Stack<>();
    // Geri alınan işlemleri tutan yığın (Tekrar yapabilmek için)
    private Stack<Command> redoStack = new Stack<>();

    /**
     * Bir komutu hem çalıştırır hem de geçmişe kaydeder.
     */
    public void executeCommand(Command command) {
        command.execute();      // Komutu işlet (Örn: Şehri ekle)
        undoStack.push(command); // Geçmişe at
        redoStack.clear();       // Yeni bir işlem yapıldığında ileri sarma listesi silinir
    }

    /**
     * En son yapılan işlemi geri alır.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop(); // Son komutu al
            command.undo();                   // Tersini yap (Örn: Eklenen şehri sil)
            redoStack.push(command);          // Redo listesine ekle
        }
    }

    /**
     * Geri alınan işlemi tekrar uygular.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop(); // Geri alınan komutu al
            command.execute();                // Tekrar çalıştır
            undoStack.push(command);          // Tekrar geri alınabilir listeye koy
        }
    }
}