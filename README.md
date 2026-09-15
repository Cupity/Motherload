# Java 1 - 1. cvičení

Seznamte se vývojovým prostředí Eclipse nebo InteliJ IDEA, použijte Javu 25.

Vycházejte z projektu <https://gitlab.vsb.cz/jez04-vyuka/java1/labs/lab01>

- Idea první start: Clone Repository -> URL: viz výše
- Idea už otevřené prostředí: hamburgrové menu -> New -> Project from Version Control... -> URL: viz výše
- Eclipse: Chose workspace (složka s projekty se stejným nastavením) -> File -> Import ... -> Git -> Project from Git (with smart import) -> clone URI -> URI: viz výše -> Next -> Next -> Finish

## Hello World

Vytvořte jednoduchý program v Javě, který vypíše do konzole Hello World

- Vytvořte novou třídu `Greeting`
- třídě vytvořte main metodu `public static void main(String[] args)`
- do konzoly (`System.out`) vypište pozdrav `Hello World` a klidně přidejte nějaké unikódové znaky 🤩
- spusťte třídu `Greeting`

## Vytvořte program, který najde všechna prvočísla menší než 100

- Vytvořte novou třídu `Primes` v balíku lab
- třídě vytvořte main metodu `public static void main(String[] args)`
- Vypište do konzole všechna prvočísla menší než 100. <https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes>
- spusťte třídu `Primes`

## Kreslení

Ve třídě `lab.DrawingThread` v metodě `handle`:

- vytvořte nějaký obrázek (použijte metody fillOval, fillRect, fillPolygon, ...)
- vytvořte metodu `drawPicture`, která vykreslí obrázek z předchopzího úkolu obrázek, metoda bude mít parametry: x,y - pozice obrazu
- vytvořte animovaný pohyb obrázku. Vytvořte instanční proměnné třídy `lab.DrawingThread` x a y a ty postupně měňte a používejte k vykreslení obrázku.
