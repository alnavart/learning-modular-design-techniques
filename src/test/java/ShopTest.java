import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ShopTest
{
  HardwareCashierScanner hardwareCashierScanner;
  CashierScanner CashierScanner = new CashierScanner();

  @Test
  public void getsProduct1Price()
  {
    int price = Cashier.getPrice("10100011");

    assertEquals(9, price);
  }

  @Test
  public void getsProduct2Price()
  {
    int price = Cashier.getPrice("10100012");

    assertEquals(10, price);
  }

  @Test
  public void scansProduct1()
  {
    String barcode = CashierScanner.read();

    assertEquals("10100011", barcode);
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

  private class CashierScanner
  {
    public String read()
    {
      return "10100011";
    }
  }

  private class HardwareCashierScanner
  {
  }
}
