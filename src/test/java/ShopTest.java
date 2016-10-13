import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShopTest
{
  @Mock
  private HardwareCashierScanner hardwareCashierScanner;
  @Mock
  private HardwareDisplay hardwareDisplay;
  private Display display;

  private String product1Barcode = "12345";
  private String product2Barcode = "23456";
  private String product1Price = "EUR 7,95";
  private String product2Price = "EUR 12,50";

  @Before
  public void setUp()
  {
    display = new Display(hardwareDisplay);
  }

  @Test
  public void getsProduct1Price()
  {
    assertEquals(product1Price, BarcodeToPriceConverter.getPrice(product1Barcode));
  }

  @Test
  public void getsProduct2Price()
  {
    assertEquals(product2Price, BarcodeToPriceConverter.getPrice(product2Barcode));
  }

  @Test
  public void scansProduct1()
  {
    when(hardwareCashierScanner.read()).thenReturn(product1Barcode);
    CashierScanner cashierScanner = new CashierScanner(hardwareCashierScanner);

    assertEquals(product1Barcode, cashierScanner.read());
  }

  @Test
  public void scansProduct2()
  {
    when(hardwareCashierScanner.read()).thenReturn(product2Barcode);
    CashierScanner cashierScanner = new CashierScanner(hardwareCashierScanner);

    assertEquals(product2Barcode, cashierScanner.read());
  }

  @Test
  public void showsTextInTheDisplay()
  {
    display.show("hello");

    verify(hardwareDisplay).show("hello");
  }

  @Test
  public void showsProduct1PriceUseCase()
  {
    when(hardwareCashierScanner.read()).thenReturn(product1Barcode);
    CashierScanner cashierScanner = new CashierScanner(hardwareCashierScanner);
    Cashier cashier = new Cashier(cashierScanner, display);

    cashier.scan();

    verify(hardwareDisplay).show(product1Price);
  }

  //REQ products not found 499999
  private static class BarcodeToPriceConverter
  {
    public static String getPrice(String barcode)
    {
      final Map<String, String> pricesByBarcode = new HashMap<String, String>()
      {
        {
          put("12345", "EUR 7,95");
          put("23456", "EUR 12,50");
        }
      };

      return pricesByBarcode.get(barcode);
    }
  }

  @AllArgsConstructor
  private class CashierScanner
  {
    private HardwareCashierScanner hardwareCashierScanner;

    public String read()
    {
      return hardwareCashierScanner.read();
    }
  }

  //REQ Failed scanning no barcode
  private class HardwareCashierScanner
  {
    public String read()
    {
      return null;
    }
  }

  //REQ ASCII only, not Unicode
  private class HardwareDisplay
  {
    public void show(String text)
    {
    }
  }

  @AllArgsConstructor
  private class Display
  {
    private HardwareDisplay hardwareDisplay;

    public void show(String text)
    {
      hardwareDisplay.show(text);
    }
  }

  @AllArgsConstructor
  private class Cashier
  {
    private CashierScanner cashierScanner;
    private Display display;

    public void scan()
    {
      String barcode = cashierScanner.read();
      String price = BarcodeToPriceConverter.getPrice(barcode);
      display.show(price);
    }
  }
}
