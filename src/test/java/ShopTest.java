import lombok.AllArgsConstructor;
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

  private String product1Barcode = "10100011";
  private String product2Barcode = "10100012";

  @Test
  public void getsProduct1Price()
  {
    int price = Cashier.getPrice(product1Barcode);

    assertEquals(9, price);
  }

  @Test
  public void getsProduct2Price()
  {
    int price = Cashier.getPrice(product2Barcode);

    assertEquals(10, price);
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

  private static class Cashier
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
}
