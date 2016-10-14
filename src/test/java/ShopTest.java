import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ShopTest
{
  @Mock
  private HardwareDisplay hardwareDisplay;

  private Display display;
  private Cashier cashier;

  private String product1Barcode = "12345";
  private String product2Barcode = "23456";
  private String productNotFoundBarcode = "234222";
  private String emptyBarcode = "";
  private String product1Price = "EUR 7,95";
  private String product2Price = "EUR 12,50";
  private String product1PlusProduct2Price = "EUR 20,45";

  @Before
  public void setUp()
  {
    display = new Display(hardwareDisplay);
    cashier = new Cashier(display);
  }

  @Test
  public void getsProduct1Price()
  {
    assertThat(Catalog.findPrice(product1Barcode), equalTo(7.95));
  }

  @Test
  public void getsProduct2Price()
  {
    assertThat(Catalog.findPrice(product2Barcode), equalTo(12.5));
  }

  @Test
  public void showsPriceInTheDisplay()
  {
    display.showPrice(55.6);

    verify(hardwareDisplay).show("EUR 55,60");
  }

  @Test
  public void scansProduct1()
  {
    cashier.scan(product1Barcode);

    verify(hardwareDisplay).show(product1Price);
  }

  @Test
  public void scansProduct2()
  {
    cashier.scan(product2Barcode);

    verify(hardwareDisplay).show(product2Price);
  }

  @Test
  public void scansProductNotFound()
  {
    cashier.scan(productNotFoundBarcode);

    verify(hardwareDisplay).show(String.format("Product not found for %s", productNotFoundBarcode));
  }

  @Test
  public void scansProductEmptyBarcode()
  {
    cashier.scan(emptyBarcode);

    verify(hardwareDisplay).show("Error scanning barcode");
  }

  @Test
  public void scansSomeProducts()
  {
    cashier.scan(product1Barcode);
    verify(hardwareDisplay).show(product1Price);
    cashier.scan(product2Barcode);
    verify(hardwareDisplay).show(product2Price);

    cashier.displayTotalAmount();

    verify(hardwareDisplay).show(product1PlusProduct2Price);
  }

  private static class Catalog
  {
    public static Double findPrice(String barcode)
    {
      final Map<String, Double> pricesByBarcode = new HashMap<String, Double>()
      {
        {
          put("12345", 7.95);
          put("23456", 12.50);
        }
      };
      return pricesByBarcode.get(barcode);
    }
  }

  //REQ ASCII only, not Unicode
  private interface HardwareDisplay
  {
    void show(String text);
  }

  @AllArgsConstructor
  private class Display
  {
    private HardwareDisplay hardwareDisplay;

    public void showPrice(Double price)
    {
      hardwareDisplay.show(formatPrice(price));
    }

    private String formatPrice(Double price)
    {
      NumberFormat formatter = new DecimalFormat("#.00");
      return String.format("EUR %s", formatter.format(price).replace(".", ","));
    }

    public void showBarcodeNotFound(String barcode)
    {
      hardwareDisplay.show(String.format("Product not found for %s", barcode));
    }

    public void scannedEmptyBarcode(String barcode)
    {
      hardwareDisplay.show("Error scanning barcode");
    }
  }

  @RequiredArgsConstructor
  private class Cashier
  {
    private final Display display;
    private Double totalPrice = 0.0;

    public void scan(String barcode)
    {
      if ("".equals(barcode))
      {
        display.scannedEmptyBarcode(barcode);
        return;
      }
      Double price = Catalog.findPrice(barcode);
      if (Objects.isNull(price))
      {
        display.showBarcodeNotFound(barcode);
      }
      else
      {
        totalPrice= totalPrice + price;
        display.showPrice(price);
      }
    }

    public void displayTotalAmount()
    {
      display.showPrice(totalPrice);
    }
  }
}
