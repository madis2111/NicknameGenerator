import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger counter3 = new AtomicInteger(0);
    public static AtomicInteger counter4 = new AtomicInteger(0);
    public static AtomicInteger counter5 = new AtomicInteger(0);

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void incrementCounterByLength(int length) {
        switch (length) {
            case 3:
                counter3.incrementAndGet();
                break;
            case 4:
                counter4.incrementAndGet();
                break;
            case 5:
                counter5.incrementAndGet();
                break;
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                StringBuilder builder1 = new StringBuilder(texts[i]);
                builder1.reverse();
                StringBuilder builder2 = new StringBuilder(texts[i]);
                if (builder1.equals(builder2)) {
                    incrementCounterByLength(texts[i].length());
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            vneshniyTsikl:
            for (int i = 0; i < texts.length; i++) {
                char firstLetter = texts[i].charAt(0);
                    for (int j = 1; j < texts[i].length(); j++) {
                    if (texts[i].charAt(j) != firstLetter) {
                        continue vneshniyTsikl;
                    }
                }
                incrementCounterByLength(texts[i].length());
            }
        });

        Thread thread3 = new Thread(() -> {
            vneshniyTsikl:
            for (int i = 0; i < texts.length; i++) {
                for (int j = 0; j < texts[i].length() - 1; j++) {
                    if (texts[i].charAt(j) > texts[i].charAt(j+1)) {
                        continue vneshniyTsikl;
                    }
                }
                incrementCounterByLength(texts[i].length());
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.printf("Красивых слов с длиной 3: %s шт. ", counter3);
        System.out.printf("Красивых слов с длиной 4: %s шт. ", counter4);
        System.out.printf("Красивых слов с длиной 5: %s шт. ", counter5);
    }
}