import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShopTest
{
  @Mock
  private HardwareCashierScanner hardwareCashierScanner;
  @Mock
  private HardwareDisplay hardwareDisplay;

  private String product1Barcode = "10100011";
  private String product2Barcode = "10100012";
  private Integer product1Price = 9;
  private Integer product2Price = 10;

  @Test
  public void getsProduct1Price()
  {
    Integer price = BarcodeToPriceConverter.getPrice(product1Barcode);

    assertEquals(product1Price, price);
  }

  @Test
  public void getsProduct2Price()
  {
    Integer price = BarcodeToPriceConverter.getPrice(product2Barcode);

    assertEquals(product2Price, price);
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
    Display display = new Display(hardwareDisplay);
    display.show("hello");

    verify(hardwareDisplay).show("hello");
  }

  @Test
  public void showsProduct1PriceUseCase()
  {
    when(hardwareCashierScanner.read()).thenReturn(product1Barcode);
    CashierScanner cashierScanner = new CashierScanner(hardwareCashierScanner);
    Cashier cashier = new Cashier(cashierScanner, hardwareDisplay);

    cashier.scan();

    verify(hardwareDisplay).show(product1Price.toString());
  }

  private static class BarcodeToPriceConverter
  {
    public static int getPrice(String barcode)
    {
      if(barcode.equals("10100011"))
      {
        return 9;
      }else {
        return 10;
      }
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

  private class HardwareCashierScanner
  {
    public String read()
    {
      return null;
    }
  }

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
    CashierScanner cashierScanner;
    HardwareDisplay hardwareDisplay;

    public void scan()
    {
      String barcode = hardwareCashierScanner.read();
      Integer price = BarcodeToPriceConverter.getPrice(barcode);
      hardwareDisplay.show(price.toString());
    }
  }
}
