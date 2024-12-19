import java.util.*;

// Абстрактний клас, що представляє характеристики кави
abstract class Coffee {
    private String name;
    private double pricePerKg;
    private double weight; // вага у кг разом із упаковкою
    private double quality; // якість у діапазоні 0.0 до 10.0

    public Coffee(String name, double pricePerKg, double weight, double quality) {
        if (pricePerKg <= 0 || weight <= 0 || quality < 0 || quality > 10) {
            throw new IllegalArgumentException("Неправильні параметри для створення позиції кави");
        }
        this.name = name;
        this.pricePerKg = pricePerKg;
        this.weight = weight;
        this.quality = quality;
    }

    public String getName() {
        return name;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public double getWeight() {
        return weight;
    }

    public double getQuality() {
        return quality;
    }

    public double getPrice() {
        return pricePerKg * weight;
    }

    @Override
    public String toString() {
        return String.format("%s (Ціна/кг: %.2f, Вага: %.2f, Якість: %.2f)", name, pricePerKg, weight, quality);
    }
}

// Підклас для кавових зерен
class CoffeeBeans extends Coffee {
    public CoffeeBeans(String name, double pricePerKg, double weight, double quality) {
        super(name, pricePerKg, weight, quality);
    }
}

// Підклас для меленої кави
class GroundCoffee extends Coffee {
    public GroundCoffee(String name, double pricePerKg, double weight, double quality) {
        super(name, pricePerKg, weight, quality);
    }
}

// Підклас для розчинної кави
class InstantCoffee extends Coffee {
    public InstantCoffee(String name, double pricePerKg, double weight, double quality) {
        super(name, pricePerKg, weight, quality);
    }
}

// Клас фургона для зберігання вантажу кави
class Truck {
    private double maxVolume;
    private List<Coffee> cargo;

    public Truck(double maxVolume) {
        if (maxVolume <= 0) {
            throw new IllegalArgumentException("Неправильний обсяг фургона");
        }
        this.maxVolume = maxVolume;
        this.cargo = new ArrayList<>();
    }

    public void loadCargo(Coffee coffee) {
        double currentVolume = cargo.stream().mapToDouble(Coffee::getWeight).sum();
        if (currentVolume + coffee.getWeight() > maxVolume) {
            throw new IllegalStateException("Недостатньо місця у фургоні для завантаження кави");
        }
        cargo.add(coffee);
    }

    public void sortByPriceToWeightRatio() {
        cargo.sort(Comparator.comparingDouble(c -> c.getPrice() / c.getWeight()));
    }

    public List<Coffee> findByQualityRange(double minQuality, double maxQuality) {
        if (minQuality < 0 || maxQuality > 10 || minQuality > maxQuality) {
            throw new IllegalArgumentException("Неправильний діапазон якості");
        }
        return cargo.stream()
                .filter(c -> c.getQuality() >= minQuality && c.getQuality() <= maxQuality)
                .toList();
    }

    public void displayCargo() {
        cargo.forEach(System.out::println);
    }
}

public class CoffeeTruckApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Truck truck = null;
        while (truck == null) {
            try {
                System.out.print("Введіть максимальний обсяг фургона: ");
                double maxVolume = scanner.nextDouble();
                truck = new Truck(maxVolume);
            } catch (InputMismatchException e) {
                System.out.println("Неправильний ввід. Введіть число для обсягу фургона.");
                scanner.nextLine(); // clear invalid input
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }

        System.out.println("Додаємо каву у фургон...\n");
        try {
            truck.loadCargo(new CoffeeBeans("Arabica Beans", 20.0, 5.0, 9.0));
            truck.loadCargo(new GroundCoffee("Ground Espresso", 15.0, 3.0, 8.5));
            truck.loadCargo(new InstantCoffee("Instant Gold", 30.0, 2.0, 9.5));
            truck.loadCargo(new CoffeeBeans("Robusta Beans", 18.0, 4.0, 5.0));
            truck.loadCargo(new GroundCoffee("House Blend", 12.0, 6.0, 7.5));
            truck.loadCargo(new InstantCoffee("Quick Brew", 25.0, 1.5, 9.2));
            truck.loadCargo(new CoffeeBeans("Premium Arabica", 22.0, 5.0, 2.0));
            truck.loadCargo(new GroundCoffee("Dark Roast", 17.0, 3.5, 8.9));
            truck.loadCargo(new InstantCoffee("Budget Blend", 10.0, 2.5, 7.0));
            truck.loadCargo(new CoffeeBeans("Ethiopian Sidamo", 24.0, 4.0, 9.7));
            truck.loadCargo(new GroundCoffee("Colombian", 16.0, 3.0, 4.8));
            truck.loadCargo(new InstantCoffee("Cappuccino Mix", 28.0, 1.8, 9.4));
            truck.loadCargo(new CoffeeBeans("Kenya AA", 23.0, 4.5, 3.5));
            truck.loadCargo(new GroundCoffee("French Roast", 14.0, 2.8, 7.8));
            truck.loadCargo(new InstantCoffee("Latte Sachets", 27.0, 2.2, 6.3));
            truck.loadCargo(new CoffeeBeans("Mocha", 19.0, 3.0, 5.9));
            truck.loadCargo(new GroundCoffee("Vienna Roast", 13.0, 2.7, 6.2));
            truck.loadCargo(new InstantCoffee("Black Instant", 20.0, 2.0, 4.0));
        } catch (IllegalStateException e) {
            System.out.println("Помилка під час завантаження вантажу: " + e.getMessage());
        }

        System.out.println("Вміст фургона перед сортуванням:");
        truck.displayCargo();

        truck.sortByPriceToWeightRatio();
        System.out.println("\nВміст фургона після сортування за співвідношенням ціни до ваги:");
        truck.displayCargo();

        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("\nВведіть мінімальну якість (0.0 - 10.0): ");
                double minQuality = scanner.nextDouble();
                System.out.print("Введіть максимальну якість (0.0 - 10.0): ");
                double maxQuality = scanner.nextDouble();

                List<Coffee> filteredCargo = truck.findByQualityRange(minQuality, maxQuality);
                System.out.println("\nВідфільтрований вантаж за діапазоном якості:");
                filteredCargo.forEach(System.out::println);

                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Неправильний ввід. Введіть коректні числа для діапазону якості.");
                scanner.nextLine(); // очищення некоректного вводу
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}