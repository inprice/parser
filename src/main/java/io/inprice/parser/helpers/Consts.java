package io.inprice.parser.helpers;

import io.inprice.parser.info.Country;

public class Consts {

  public static class Words {
    public static String NOT_AVAILABLE = "NA";
  }

  public static class Limits {
    public static int SKU = 70;
    public static int NAME = 500;
    public static int BRAND = 150;
    public static int SELLER = 150;
    public static int SHIPMENT = 150;
    public static int SPEC_KEY = 100;
    public static int SPEC_VALUE = 500;
  }
  
  public static class Countries {
  	public static final Country US = new Country("us", "United States");
  	public static final Country UK = new Country("uk", "United Kingdom");
  	public static final Country CA = new Country("ca", "Canada");
  	public static final Country AU = new Country("au", "Australia");
  	public static final Country DE = new Country("de", "Germany");
  	public static final Country NL = new Country("nl", "Netherlands");
  	public static final Country FR = new Country("fr", "France");
  	public static final Country IT = new Country("it", "Italy");
  	public static final Country ES = new Country("es", "Spain");
  	public static final Country TR = new Country("tr", "Turkey");
  	public static final Country TR_DE = new Country("tr", "Turkey", "Germany");
  	public static final Country TR_US = new Country("tr", "Turkey", "UnitedStates");
  }

}
