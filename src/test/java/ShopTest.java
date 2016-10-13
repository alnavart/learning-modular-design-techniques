import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ShopTest
{
  @Test
  public void cashier()
  {
    String barcode = "10100011";

    int price = Cashier.read(barcode);

    assertEquals(9, price);
  }

  private static class Cashier
  {
    public static int read(String barcode)
    {
      return 9;
    }
  }
}
