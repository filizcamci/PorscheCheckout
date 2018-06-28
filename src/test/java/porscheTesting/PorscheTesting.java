package porscheTesting;

import static org.testng.Assert.assertEquals;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PorscheTesting {
	// use @Test(priority =1) to give the tests order
	WebDriver driver;
	String parent;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		// Step 1.Open
		// browser=======================================================================================1
		driver = new ChromeDriver();
		// Step 2.Go to url
		// “https://www.porsche.com/usa/modelstart/”================================================2
		driver.get("https://www.porsche.com/usa/modelstart/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void buyingPorsche() throws InterruptedException {
		// Step 3.Select model
		// 718====================================================================================3
		driver.findElement(By.className("b-teaser-preview-wrapper")).click();
		// Step 4.Remember the price of
		// 718Cayman=====================================================================4
		String price1 = driver.findElement(By.className("m-14-model-price")).getText();
		// System.out.println(price1);
		String digitPrice = "";
		for (int i = 0; i < price1.length() - 3; i++) {
			if (Character.isDigit(price1.charAt(i))) {
				digitPrice += price1.charAt(i);
			} else {
				continue;
			}

		}

		int intBasePrice1 = Integer.parseInt(digitPrice);
		// System.out.println("price of 718 Cayman:" + intPrice1);
		// Step 5.Click on Build & Price under 718
		// Cayman=============================================================5
		driver.findElement(By.className("m-14-quick-link")).click();
		// driver.findElement(By.xpath("//*[@id=\"m982120\"]/div[2]/div/a/span")).click();

		// Step 6.Verify that Base price displayed on the page is same as the price from
		// step 4========================6
		Set<String> allWindows = driver.getWindowHandles();// this one collects all open windows sessions
		parent = driver.getWindowHandle(); // this one takes one window session
		// System.out.println("Parent window : " + parent);
		// it shows the size of how many window is opening
		int countWindow = allWindows.size();
		// System.out.println("Total count " + countWindow);
		// we are iterating each window session ids
		for (String child : allWindows) {
			// if parent window is not equal to child window
			if (!parent.equalsIgnoreCase(child)) {
				// then switch to child window
				driver.switchTo().window(child);
				// print the child window
				// System.out.println("Child window " + child);

			}
		}
		String price2 = driver.findElement(By.cssSelector("#s_price > div.ccaTable > div:nth-child(1) > div.ccaPrice"))
				.getText();
		// System.out.println("Base price: "+price2);
		String digitPrice2 = convertDigit(price2);

		int intBasePrice2 = Integer.parseInt(digitPrice2);
		// System.out.println(intPrice2);
		assertEquals(intBasePrice1, intBasePrice2);

		// Step 7.Verify that Price for Equipment is
		// 0==================================================================7
		String equipment = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]")).getText();
		// System.out.println(equipment);
		String priceForEquipment = equipment.substring(equipment.length() - 1);
		int equipmentPrice = Integer.parseInt(priceForEquipment);
		// System.out.println(priceForEquipment);
		assertEquals(equipmentPrice, 0);
		// Step 8.Verify that total price is the sum of base price + Delivery,
		// Processing and Handling Fee==============8
		String deliveryFee = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[3]/div[2]")).getText();
		// System.out.println("delivery fee:"+deliveryFee);
		String fee = convertDigit(deliveryFee);

		int deliveryHandlingFee = Integer.parseInt(fee);
		// System.out.println(deliveryHandlingFee);

		String sTotalPrice = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText();
		// System.out.println("Total price: "+sTotalPrice);
		String total = convertDigit(sTotalPrice);

		// System.out.println("Total price: "+total);
		int totalPrice1 = Integer.parseInt(total);
		// System.out.println(totalPrice1);
		assertEquals(totalPrice1, (intBasePrice2 + deliveryHandlingFee));

		// Step 9.Select color “Miami
		// Blue”============================================================================9
		String customColorPrice = driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_FJ5\"]"))
				.getAttribute("data-price");
		// System.out.println(customColorPrice);
		String colorPrice = convertDigit(customColorPrice);
		int customEquipmentPrice = Integer.parseInt(colorPrice);
		// System.out.println(customEquipmentPrice);
		driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_FJ5\"]/span")).click();
		// Step 10.Verify that Price for Equipment is Equal to Miami Blue
		// price========================================10
		String miamiPrice = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		// System.out.println(miamiPrice);
		String miamip = convertDigit(miamiPrice);
		int miamiBluePrice = Integer.parseInt(miamip);
		// System.out.println(miamiBluePrice);

		assertEquals(miamiBluePrice, customEquipmentPrice);
		// Step 11.Verify that total price is the sum of base price + Price for
		// Equipment + Delivery,
		// Processing and Handling
		// Fee==================================================================================11
		String sTotalPrice2 = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText();
		// System.out.println("Total price: "+sTotalPrice2);
		String total2 = convertDigit(sTotalPrice2);

		// System.out.println("Total price: "+total);
		int totalPrice2 = Integer.parseInt(total2);
		// System.out.println(totalPrice1);
		assertEquals(totalPrice2, (intBasePrice2 + deliveryHandlingFee + customEquipmentPrice));

		// Step 12.Select 20" Carrera Sport
		// Wheels======================================================================12
		driver.findElement(By.xpath("//div[@class='flyout-label-value']")).click();
		driver.findElement(By.xpath("//*[@id=\"submenu_exterieur_x_AA_submenu_x_IRA\"]/a")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_MXRD\"]/span/span")).click();

		// Step 13.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		// Carrera Sport Wheels
		String priceForE = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		String mep = convertDigit(priceForE);
		int miamiEP = Integer.parseInt(mep);

		String priceForWheels = driver.findElement(By.id("s_exterieur_x_MXRD")).getAttribute("data-price");
		String priceWheels = convertDigit(priceForWheels);
		int wheelsPrice = Integer.parseInt(priceWheels);

		String priceForcolor = driver.findElement(By.id("s_exterieur_x_FJ5")).getAttribute("data-price");
		String pricecolor = convertDigit(priceForcolor);
		int mcolorPrice = Integer.parseInt(pricecolor);
		assertEquals((mcolorPrice + wheelsPrice), miamiEP);

		// Step 14.Verify that total price is the sum of base price + Price for
		// Equipment + Delivery, Processing and Handling Fee

		String priceForWheels2 = driver.findElement(By.id("s_exterieur_x_MXRD")).getAttribute("data-price");
		String priceWheels2 = convertDigit(priceForWheels2);
		int wheelsPrice2 = Integer.parseInt(priceWheels2);
		// System.out.println(wheelsPrice);

		String totalmiami = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText();
		String tmp = convertDigit(totalmiami);
		int totalMiamiPrice = Integer.parseInt(tmp);
		assertEquals((totalPrice2 + wheelsPrice2), totalMiamiPrice);

		// Step 15.Select seats ‘Power Sport Seats (14-way) with Memory Package’

		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='flyout-label-value']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"submenu_interieur_x_AI_submenu_x_submenu_parent\"]/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"submenu_interieur_x_AI_submenu_x_submenu_seats\"]/a")).click();
		// <span id="s_interieur_x_PP06" data-link-id="PP06" class="radiobutton"></span>
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"s_interieur_x_PP06\"]")).click();

		// Step 16.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		// Carrera Sport Wheels + Power Sport
		// Seats (14-way) with Memory Package

		String seatPrice = driver.findElement(By.xpath("//*[@id=\"seats_73\"]/div[2]/div[1]/div[3]/div")).getText();
		String sprice = convertDigit(seatPrice);
		int seatprice = Integer.parseInt(sprice);

		String ep = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		String eprice = convertDigit(ep);
		int ePrice = Integer.parseInt(eprice);

		assertEquals((mcolorPrice + wheelsPrice + seatprice), ePrice);

		// Step 17.Verify that total price is the sum of base price + Price for
		// Equipment + Delivery, Processing and
		// Handling Fee
		String totalPrice3s = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText();
		String totald = convertDigit(totalPrice3s);
		int totalPrice3 = Integer.parseInt(totald);

		assertEquals((totalPrice2 + wheelsPrice2 + seatprice), totalPrice3);

		// Step 18.Click on Interior Carbon Fiber

		driver.findElement(By.xpath("//div[@class='flyout-label-value']")).click();
		driver.findElement(
				By.xpath("//*[@id=\"submenu_individualization_x_individual_submenu_x_submenu_parent\"]/span")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"submenu_individualization_x_individual_submenu_x_IIC\"]/a")).click();

		// *[@id="vs_table_IIC_x_PEKH_x_c01_PEKH"]

		// Step 19.Select Interior Trim in Carbon Fiber i.c.w. Standard Interior
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[@id='vs_table_IIC_x_PEKH_x_c01_PEKH'][@class='checkbox']")).click();

		// Step 20.Verify that Price for Equipment is the sum of Miami Blue price + 20"
		// Carrera Sport Wheels + Power Sport
		// Seats (14-way) with Memory Package + Interior Trim in Carbon Fiber i.c.w.
		// Standard Interior

		String ept = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		String dept = convertDigit(ept);
		int iept = Integer.parseInt(dept);

		String cep = driver.findElement(By.xpath("//*[@id=\"vs_table_IIC_x_PEKH\"]/div[1]/div[2]/div")).getText();
		String dcep = convertDigit(cep);
		int icep = Integer.parseInt(dcep);

		assertEquals((mcolorPrice + wheelsPrice + seatprice + icep), iept);
		// Step 21.Verify that total price is the sum of base price + Price for
		// Equipment + Delivery, Processing and
		// Handling Fee

		String totalPrice4s = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText();
		String totald4 = convertDigit(totalPrice4s);
		int totalPrice4 = Integer.parseInt(totald4);

		assertEquals((totalPrice2 + wheelsPrice2 + seatprice + icep), totalPrice4);

		// Step 22.Click on Performance
		driver.findElement(By.xpath("//div[@class='flyout-label-value']")).click();
		driver.findElement(
				By.xpath("//*[@id=\"submenu_individualization_x_individual_submenu_x_submenu_parent\"]/span")).click();
		driver.findElement(By.xpath("//*[@id=\"submenu_individualization_x_individual_submenu_x_IMG\"]")).click();

		// Step 23.Select 7-speed Porsche Doppelkupplung (PDK)
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M250_x_c11_M250\"][@class='radiobutton']")).click();

		// Step 24.Select Porsche Ceramic Composite Brakes (PCCB)
		
		driver.findElement(By.xpath("//*[@id=\"search_x_inp\"]")). sendKeys("porsche ceramic"); driver.findElement(By.xpath(
		  "//*[@id=\"search_x_M450_x_c94_M450_x_shorttext\"]")).click();
		  


		// Step 25.Verify that Price for Equipment is the sum of Miami Blue price + 20" Carrera Sport Wheels + Power Sport 
		//Seats (14-way) with Memory Package + Interior Trim in Carbon Fiber i.c.w. Standard Interior + 7-speed
		// Porsche Doppelkupplung (PDK) + Porsche Ceramic Composite Brakes (PCCB)
		
		String eptd = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		String deptd = convertDigit(eptd);
		int ieptd = Integer.parseInt(deptd);
		
		String dceptd = driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M250\"]/div[1]/div[2]/div")).getText();
		String dcdeptd = convertDigit(dceptd);
		int dcieptd = Integer.parseInt(dcdeptd);


		String ccep = driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M450\"]/div[1]/div[2]/div")).getText();
		String dccep = convertDigit(ccep);
		int iccep = Integer.parseInt(dccep);

		assertEquals((mcolorPrice + wheelsPrice + seatprice + icep+iccep+dcieptd), ieptd);

		// Step 26.Verify that total price is the sum of base price + Price for
		// Equipment + Delivery, Processing and Handling Fee
		String totalPrice5s = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText();
		String totald5 = convertDigit(totalPrice5s);
		int totalPrice5 = Integer.parseInt(totald5);

		assertEquals((totalPrice2 + wheelsPrice2 + seatprice + icep+iccep+dcieptd), totalPrice5);

	}

	public String convertDigit(String s) {
		String digits = "";
		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i))) {
				digits += s.charAt(i);
			} else {
				continue;
			}
		}
		return digits;
	}
	// @AfterClass
	// public void closingDriver() {
	// driver.close();
	// //driver.switchTo();
	// }

}
// Hakan bey's solution
/*
 * package com.cybertek.PorscheCheckout;
 * 
 * import java.util.concurrent.TimeUnit;
 * 
 * import org.openqa.selenium.By; import org.openqa.selenium.WebDriver; import
 * org.openqa.selenium.WebElement; import
 * org.openqa.selenium.chrome.ChromeDriver; import
 * org.openqa.selenium.support.ui.WebDriverWait;
 * 
 * import io.github.bonigarcia.wdm.WebDriverManager;
 * 
 * public class PorscheCheckout {
 * 
 * public static void main(String[] args) throws InterruptedException {
 * WebDriverManager.chromedriver().setup(); WebDriver driver = new
 * ChromeDriver();
 * 
 * // ==========================step 1 Open Browser===============
 * driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
 * driver.manage().window().maximize(); driver.manage().window().fullscreen();
 * 
 * // ==========================step 2 Go to Url===============
 * driver.get("https://www.porsche.com/usa/modelstart/");
 * 
 * // ==========================step 3 Select model 718===============
 * driver.findElement(By.className("b-teaser-preview-wrapper")).click();
 * 
 * // ==========================step 4 Remember the price of 718
 * Cayman=============== String price1 =
 * driver.findElement(By.className("m-14-model-price")).getText(); String
 * subPrice = price1.substring(0, price1.length() - 4);
 * 
 * int firstPrice = somethingPrice(subPrice);
 * 
 * // ==========================step 5 Click on Build & Price under 718
 * Cayman=============== String url2 = "https://bit.ly/2K1ggjw";
 * driver.get(url2); // ==========================step 6 Verify that Base price
 * displayed on the page is same as the price from step 4=============== int
 * basePrice = findBasePrice(driver);
 * 
 * if (firstPrice == basePrice) {
 * System.out.println("PASS. First Price equal to Base Price."); } else {
 * System.out.println("FAIL. First Price is not match Base Price."); } //
 * ==========================step 7 Verify that Price for Equipment is
 * 0=============== int intEquipmentPrice = findEquipmentPrice(driver); if
 * (intEquipmentPrice == 0) { System.out.println("PASS. Equipment Price is 0.");
 * } else { System.out.println("FAIL. Equipment Price is " + intEquipmentPrice);
 * } // ========step 8 Verify that total price is the sum of base price +
 * Delivery, Processing and Handling Fee=============== checkTotalPrice(driver);
 * 
 * // ==========================step 9 Select color “Miami Blue”===============
 * driver.findElement(By.
 * cssSelector("span[style='background-color: rgb(0, 120, 138);']")).click();
 * 
 * // ==========================step 10 Verify that Price for Equipment is Equal
 * to Miami Blue price=============== String colorPrice =
 * driver.findElement(By.id("s_exterieur_x_FJ5")).getAttribute("data-price");
 * 
 * checkEquipmentPrice(driver, colorPrice);
 * 
 * // ======step 11 Verify that total price is the sum of base price + Price for
 * Equipment + Delivery, Processing and Handling Fee=======
 * checkTotalPrice(driver);
 * 
 * // ===============step 12 Select 20" Carrera Sport Wheels====================
 * driver.findElement(By.xpath("//*[@id=\"s_conf_submenu\"]/div/div")).click();
 * driver.findElement(By.xpath(
 * "//*[@id=\"submenu_exterieur_x_AA_submenu_x_IRA\"]/a")).click();
 * Thread.sleep(1000);
 * driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_MXRD\"]/span/span")).
 * click();
 * 
 * // ==========step 13 Verify that Price for Equipment is the sum of Miami Blue
 * price + 20" Carrera Sport Wheels======== String wheelsPrice =
 * driver.findElement(By.xpath(
 * "//*[@id=\"s_exterieur_x_IRA\"]/div[2]/div[1]/div/div[2]")) .getText();
 * 
 * checkEquipmentPrice(driver, colorPrice, wheelsPrice);
 * 
 * // ========step 14 Verify that total price is the sum of base price + Price
 * for Equipment + Delivery, Processing and Handling Fee=====
 * checkTotalPrice(driver);
 * 
 * // ===============step 15 Select seats 'Power Sport Seats (14-way) with
 * Memory Package'====================
 * driver.findElement(By.xpath("//*[@id=\"s_conf_submenu\"]/div/div")).click();
 * driver.findElement(By.xpath(
 * "//*[@id=\"submenu_interieur_x_AI_submenu_x_submenu_parent\"]/span")).click()
 * ; Thread.sleep(1000); driver.findElement(By.xpath(
 * "//*[@id=\"submenu_interieur_x_AI_submenu_x_submenu_seats\"]/a")).click();
 * Thread.sleep(1000);
 * driver.findElement(By.xpath("//*[@id=\"s_interieur_x_PP06\"]")).click();
 * 
 * // ===step 16 Verify that Price for Equipment is the sum of Miami Blue price
 * + 20" Carrera Sport Wheels + Power Sport Seats (14-way) with Memory
 * Package==== String seatPrice =
 * driver.findElement(By.xpath("//*[@id=\"seats_73\"]/div[2]/div[1]/div[3]/div")
 * ).getText();
 * 
 * checkEquipmentPrice(driver, colorPrice, wheelsPrice, seatPrice); //
 * ======step 17 Verify that total price is the sum of base price + Price for
 * Equipment + Delivery, Processing and Handling Fee=====
 * checkTotalPrice(driver);
 * 
 * // ===============step 18 Click on Interior Carbon Fiber====================
 * driver.findElement(By.xpath("//*[@id=\"s_conf_submenu\"]/div/div")).click();
 * driver.findElement( By.xpath(
 * "//*[@id=\"submenu_individualization_x_individual_submenu_x_submenu_parent\"]/span"
 * )).click(); Thread.sleep(3000); driver.findElement(By.xpath(
 * "//*[@id=\"submenu_individualization_x_individual_submenu_x_IIC\"]/a")).click
 * (); Thread.sleep(1000);
 * 
 * // ===============step 19 Select Interior Trim in Carbon Fiber i.c.w.
 * Standard Interior====================
 * driver.findElement(By.xpath("//*[@id=\"vs_table_IIC_x_PEKH_x_c01_PEKH\"]")).
 * click();
 * 
 * // ===============step 20 Verify that Price for Equipment is the sum of Miami
 * Blue price + 20" Carrera Sport // Wheels + Power Sport Seats (14-way) with
 * Memory Package + Interior Trim in // Carbon Fiber i.c.w. Standard
 * Interior======== String interiorPrice = driver.findElement(By.xpath(
 * "//*[@id=\"vs_table_IIC_x_PEKH\"]/div[1]/div[2]/div")) .getText();
 * 
 * checkEquipmentPrice(driver, colorPrice, wheelsPrice, seatPrice,
 * interiorPrice);
 * 
 * // ======step 21 Verify that total price is the sum of base price + Price for
 * Equipment + Delivery, Processing and Handling Fee====
 * checkTotalPrice(driver);
 * 
 * // ===============step 22 Click on Performance====================
 * driver.findElement(By.xpath("//*[@id=\"s_conf_submenu\"]/div/div")).click();
 * Thread.sleep(1000); driver.findElement(By.xpath(
 * "//*[@id=\"submenu_individualization_x_individual_submenu_x_IMG\"]/a")).click
 * (); Thread.sleep(1000);
 * 
 * // ===============step 23 Select 7-speed Porsche Doppelkupplung
 * (PDK)====================
 * driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M250_x_c11_M250\"]")).
 * click();
 * 
 * // ===============step 24 Select Porsche Ceramic Composite Brakes
 * (PCCB)====================
 * driver.findElement(By.xpath("//*[@id=\"search_x_inp\"]")).
 * sendKeys("porsche ceramic"); driver.findElement(By.xpath(
 * "//*[@id=\"search_x_M450_x_c94_M450_x_shorttext\"]")).click();
 * 
 * // ============= step 25 Verify that Price for Equipment is the sum of Miami
 * Blue price + 20" Carrera Sport // Wheels + Power Sport Seats (14-way) with
 * Memory Package + Interior Trim in Carbon Fiber i.c.w. // Standard Interior +
 * 7-speed Porsche Doppelkupplung (PDK) + Porsche Ceramic Composite Brakes
 * (PCCB)=========
 * 
 * String brakesPrice = driver.findElement(By.xpath(
 * "//*[@id=\"vs_table_IMG_x_M450\"]/div[1]/div[2]/div")) .getText(); String
 * speedPrice = driver.findElement(By.xpath(
 * "//*[@id=\"vs_table_IMG_x_M250\"]/div[1]/div[2]/div")) .getText();
 * 
 * checkEquipmentPrice(driver, colorPrice, wheelsPrice, seatPrice,
 * interiorPrice, brakesPrice, speedPrice);
 * 
 * // =======step 26 Verify that total price is the sum of base price + Price
 * for Equipment + Delivery, Processing and Handling Fee=======
 * checkTotalPrice(driver);
 * 
 * } // ================ This method checking Equipment Price
 * checkEquipmentPrice(WebDriver driver, String... prices)
 * ====================== public static void checkEquipmentPrice(WebDriver
 * driver, String... prices) { int optionsPrice = 0; for (String each : prices)
 * { optionsPrice += somethingPrice(each); }
 * 
 * if (findEquipmentPrice(driver) == optionsPrice) {
 * System.out.println("PASS. Equipments Price is equal to the options price.");
 * } else { System.out.println("FAIL. NOT A MATCH EQUIPMENTS PRICE");
 * System.out.println("EQUIPMENTS PRICE : " + findEquipmentPrice(driver));
 * System.out.println(); } }
 * 
 * // ================ This method checking Total Price
 * checkTotalPrice(WebDriver driver) ====================== public static void
 * checkTotalPrice(WebDriver driver) { if (findTotalPrice(driver) ==
 * findBasePrice(driver) + findEquipmentPrice(driver) + findFees(driver)) {
 * System.out.println("PASS. Total price is equal to the sum of given values.");
 * } else { System.out.println("FAIL. NOT A MATCH TOTAL PRICE");
 * System.out.println("TOTAL PRICE : " + findTotalPrice(driver));
 * System.out.println( "ALL VALUE ARE : " + (findBasePrice(driver) +
 * findEquipmentPrice(driver) + findFees(driver))); }
 * 
 * } // ============ This method String price convert to int
 * price=============== public static int somethingPrice(String price) { String
 * digitPrice = ""; for (int i = 0; i < price.length(); i++) { if
 * (Character.isDigit(price.charAt(i))) { digitPrice += price.charAt(i); } else
 * { continue; } } int intPrice = Integer.parseInt(digitPrice);
 * 
 * return intPrice; }
 * 
 * // ============ This method finding Base Price=============== public static
 * int findBasePrice(WebDriver driver) { String basePrice =
 * driver.findElement(By.xpath(
 * "/html/body/div/div/div/section/section/div/div[1]/div[2]")) .getText(); int
 * basePrice1 = somethingPrice(basePrice);
 * 
 * return basePrice1; }
 * 
 * // ============ This method finding Equipment Price=============== public
 * static int findEquipmentPrice(WebDriver driver) { String equipmentPrice =
 * driver.findElement(By.xpath(
 * "/html/body/div/div/div/section/section/div/div[2]/div[2]")) .getText();
 * 
 * int equipmentPrice1 = somethingPrice(equipmentPrice); return equipmentPrice1;
 * }
 * 
 * // ============ This method finding Fees Price=============== public static
 * int findFees(WebDriver driver) { String feesPrice =
 * driver.findElement(By.xpath(
 * "/html/body/div/div/div/section/section/div/div[3]/div[2]")) .getText();
 * 
 * int feesPrice1 = somethingPrice(feesPrice); return feesPrice1; }
 * 
 * // ============ This method finding Total Price=============== public static
 * int findTotalPrice(WebDriver driver) { String totalPrice =
 * driver.findElement(By.xpath(
 * "/html/body/div/div/div/section/section/div/div[4]/div[2]")) .getText();
 * 
 * int totalPrice1 = somethingPrice(totalPrice); return totalPrice1; } }
 * 
 * 
 * 
 * Local group1's project package com.porsche;
 * 
 * import java.util.Iterator; import java.util.Set; import
 * java.util.concurrent.TimeUnit;
 * 
 * import org.openqa.selenium.By; import org.openqa.selenium.JavascriptExecutor;
 * import org.openqa.selenium.WebDriver; import
 * org.openqa.selenium.chrome.ChromeDriver; import org.testng.Assert; import
 * org.testng.annotations.AfterClass; import org.testng.annotations.BeforeClass;
 * import org.testng.annotations.BeforeMethod; import
 * org.testng.annotations.Test;
 * 
 * import io.github.bonigarcia.wdm.WebDriverManager;
 * 
 * public class PorscheCheckoutTNG { WebDriver driver;
 * 
 * int modelStartPrice; int basePrice; int eqPrice; int dphPrice; int
 * totalPrice; int miamiBluePrice; int eqMiamiPrice; int total1Price; int
 * wheelsPrice; int eqMiamiWheelsPrice; int total2Price; int powerSeatsPrice;
 * int eqMiamiWheelsSeatsPrice; int total3Price; int carbonPrice; int
 * eqMiamiWheelsSeatsCarbonPrice; int total4Price; int speed7Price; int
 * brakesPrice; int eqMiamiWheelsSeatsCarbon7BrakesPrice; int total5Price;
 * 
 * 
 * public String getTextfromXpath(String a) {
 * 
 * String b = driver.findElement(By.xpath(a)).getText(); return b; }
 * 
 * public int getIntFromText(String a) { int b =
 * Integer.parseInt(a.replaceAll("\\D+", ""));
 * 
 * return b;
 * 
 * }
 * 
 * 
 * @BeforeClass // runs once for all tests public void setUp() { // 1.open
 * browser WebDriverManager.chromedriver().setup(); driver = new ChromeDriver();
 * driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
 * driver.manage().window().fullscreen();
 * 
 * // 2. go to url “https://www.porsche.com/usa/modelstart/” String url =
 * "https://porsche.com/usa/modelstart/"; driver.get(url); // 3.Select model 718
 * driver.findElement(By.
 * xpath("//div[@class='b-teaser-preview-wrapper']/img[@title='Porsche - 718']")
 * ).click(); // 4.Remember the price of 718Cayman String a =
 * "//div[@id='m982120']/div/div/div[@class='m-14-model-price']"; String start =
 * getTextfromXpath(a); String modelStart = start.replace(".00*", "");
 * modelStartPrice = getIntFromText(modelStart);
 * 
 * // 5.Click on Build & Price under 718Cayman
 * driver.findElement(By.xpath("//div[@id='m982120']/div[2]/div/a/span")).click(
 * );
 * 
 * String parentWindowHandler = driver.getWindowHandle(); String
 * subWindowHandler = null;
 * 
 * Set<String> handles = driver.getWindowHandles(); Iterator<String> iterator =
 * handles.iterator(); while (iterator.hasNext()) { subWindowHandler =
 * iterator.next(); }
 * 
 * driver.switchTo().window(subWindowHandler);
 * 
 * /// 6.Verify that Base price displayed on the page is same as the price from
 * /// step 4 String b =
 * "//section[@id='s_price']//div[@class='ccaPrice'][.='$56,900']"; String base
 * = getTextfromXpath(b); basePrice = getIntFromText(base);
 * 
 * /// 7.Verify that Price for Equipment is 0 String c =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$0']"; String eq =
 * getTextfromXpath(c); eqPrice = getIntFromText(eq);
 * 
 * /// 8.Verify that total price is the sum of base price + Delivery, Processing
 * /// and Handling Fee String d =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$1,050']"; String
 * dph = getTextfromXpath(d); dphPrice = getIntFromText(dph);
 * 
 * String e =
 * "//section[@id='s_price']/div//div[@class='ccaRow priceTotal highlighted']/div[@class='ccaPrice']"
 * ; String total = getTextfromXpath(e); totalPrice = getIntFromText(total);
 * 
 * // 9.Select color “Miami Blue”
 * 
 * driver.findElement(By.xpath("//li[@id='s_exterieur_x_FJ5']/span")).click();
 * String f = "//div[@class='tt_price tt_cell'][.='$2,580']"; String miamiBlue =
 * getTextfromXpath(f); miamiBluePrice = getIntFromText(miamiBlue);
 * 
 * // 10.Verify that Price for Equipment is Equal to Miami Blue price
 * 
 * String g =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$2,580']"; String
 * eqMiami = getTextfromXpath(g); eqMiamiPrice = getIntFromText(miamiBlue);
 * 
 * // 11.Verify that total price is the sum of base price + Price for Equipment
 * + // Delivery, Processing and Handling Fee String h =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$60,530']"; String
 * total1 = getTextfromXpath(h); total1Price = getIntFromText(total1);
 * 
 * // 12.Select 20" Carrera Sport Wheels
 * 
 * driver.findElement(By.xpath(
 * "//li[@id='s_exterieur_x_MXRD']//span[@class='img-element']")).click();
 * 
 * String i = "//div[@id='s_exterieur_x_IRA']//div[@class='tt_price tt_cell']";
 * String wheels = getTextfromXpath(i); wheelsPrice = getIntFromText(wheels);
 * 
 * // 13.Verify that Price for Equipment is the sum of Miami Blue price + 20" //
 * Carrera Sport Wheels
 * 
 * String j =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$6,330']"; String
 * eqMiamiwheels = getTextfromXpath(j); eqMiamiWheelsPrice =
 * getIntFromText(eqMiamiwheels);
 * 
 * // 14.Verify that total price is the sum of base price + Price for Equipment
 * + // Delivery, Processing and Handling Fee String k =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$64,280']"; String
 * total2 = getTextfromXpath(k); total2Price = getIntFromText(total2);
 * 
 * // 15.Select seats ‘Power Sport Seats (14-way) with Memory Package
 * 
 * JavascriptExecutor jse = (JavascriptExecutor) driver;
 * jse.executeScript("window.scrollBy(0,1000)", "");
 * 
 * driver.findElement(By.xpath("//span[@id='s_interieur_x_PP06']")).click();
 * 
 * String l = "//div[@class='pBox']/div[.='$2,330']"; String powerSeats =
 * getTextfromXpath(l); powerSeatsPrice = getIntFromText(powerSeats);
 * 
 * // 16.Verify that Price for Equipment is the sum of Miami Blue price + 20" //
 * Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package String
 * m = "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$8,660']";
 * String eqMiamiWheelsSeats = getTextfromXpath(m); eqMiamiWheelsSeatsPrice =
 * getIntFromText(eqMiamiWheelsSeats);
 * 
 * // 17.Verify that total price is the sum of base price + Price for Equipment
 * + // Delivery, Processing and Handling Fee
 * 
 * String n =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$66,610']"; String
 * total3 = getTextfromXpath(n); total3Price = getIntFromText(total3);
 * 
 * // 18.Click on Interior Carbon Fiber
 * jse.executeScript("window.scrollBy(0,1000)", "");
 * 
 * driver.findElement(By.xpath("//div[@id='IIC_subHdl']")).click(); // 19.Select
 * Interior Trim in Carbon Fiber i.c.w. Standard Interior
 * driver.findElement(By.xpath("//span[@id='vs_table_IIC_x_PEKH_x_c01_PEKH']")).
 * click();
 * 
 * String o =
 * "//div[@id='vs_table_IIC_x_PEKH']//div[@class='pBox']/div[.='$1,540']";
 * String carbon = getTextfromXpath(o); carbonPrice = getIntFromText(carbon);
 * 
 * // 20.Verify that Price for Equipment is the sum of Miami Blue price + 20" //
 * Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package + //
 * Interior Trim in Carbon Fiber i.c.w. Standard Interior String p =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$10,200']"; String
 * eqMiamiWheelsSeatsCarbon = getTextfromXpath(p); eqMiamiWheelsSeatsCarbonPrice
 * = getIntFromText(eqMiamiWheelsSeatsCarbon);
 * 
 * // 21.Verify that total price is the sum of base price + Price for Equipment
 * + // Delivery, Processing and Handling Fee String q =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$68,150']"; String
 * total4 = getTextfromXpath(q); total4Price = getIntFromText(total4);
 * 
 * // 22.Click on Performance
 * driver.findElement(By.xpath("//div[@id='IMG_subHdl']")).click();
 * 
 * // 23.Select 7-speed Porsche Doppelkupplung (PDK)
 * driver.findElement(By.xpath("//span[@id='vs_table_IMG_x_M250_x_c11_M250']")).
 * click();
 * 
 * String r = "//div[@id='vs_table_IMG_x_M250']//div[@class='pBox']/div"; String
 * speed7 = getTextfromXpath(r); speed7Price = getIntFromText(speed7);
 * 
 * // 24.Select Porsche Ceramic Composite Brakes (PCCB)
 * jse.executeScript("window.scrollBy(0,500)", "");
 * 
 * driver.findElement(By.xpath("//span[@id='vs_table_IMG_x_M450_x_c91_M450']")).
 * click();
 * 
 * String s = "//div[@id='vs_table_IMG_x_M450']/div/div[@class='pBox']/div";
 * String brakes = getTextfromXpath(s); brakesPrice = getIntFromText(brakes);
 * 
 * // 25.Verify that Price for Equipment is the sum of Miami Blue price + 20" //
 * Carrera Sport Wheels + Power Sport Seats (14-way) with Memory Package + //
 * Interior Trim in Carbon Fiber i.c.w. Standard Interior + 7-speed Porsche //
 * Doppelkupplung (PDK) + Porsche Ceramic Composite Brakes (PCCB) String t =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$20,820']"; String
 * eqMiamiWheelsSeatsCarbon7Brakes = getTextfromXpath(t);
 * eqMiamiWheelsSeatsCarbon7BrakesPrice =
 * getIntFromText(eqMiamiWheelsSeatsCarbon7Brakes);
 * 
 * // 26.Verify that total price is the sum of base price + Price for Equipment
 * + // Delivery, Processing and Handling Fee String u =
 * "//section[@id='s_price']/div//div[@class='ccaPrice'][.='$78,770']"; String
 * total5 = getTextfromXpath(u); total5Price = getIntFromText(total5); }
 * 
 * @AfterClass public void tearDown() { driver.quit(); }
 * 
 * @Test public void basePriceTest() { int actual = modelStartPrice; int
 * expected = basePrice; Assert.assertEquals(actual, expected); }
 * 
 * @Test public void equipmentPrice0Test() { int actual = 0; int expected =
 * eqPrice; Assert.assertEquals(actual, expected);
 * 
 * }
 * 
 * @Test public void totalPriceStep8Test() { int expected = (basePrice +
 * dphPrice); int actual = totalPrice; Assert.assertEquals(actual, expected); }
 * 
 * @Test public void equipmentMiamiPriceTest() { int expected = miamiBluePrice;
 * int actual = eqMiamiPrice; Assert.assertEquals(actual, expected); }
 * 
 * @Test public void totalPriceStep11Test() { int expected = basePrice +
 * eqMiamiPrice + dphPrice; int actual = total1Price;
 * Assert.assertEquals(actual, expected); }
 * 
 * @Test public void equipmentMiamiWheelsPriceTest() { int expected =
 * miamiBluePrice + wheelsPrice; int actual = eqMiamiWheelsPrice;
 * Assert.assertEquals(actual, expected); }
 * 
 * @Test public void totalPriceStep14Test() { int expected = basePrice +
 * eqMiamiWheelsPrice + dphPrice; int actual = total2Price;
 * Assert.assertEquals(actual, expected); }
 * 
 * @Test public void equipmentMiamiWheelsSeatPriceTest() { int expected =
 * miamiBluePrice + wheelsPrice + powerSeatsPrice; int actual =
 * eqMiamiWheelsSeatsPrice; Assert.assertEquals(actual, expected); }
 * 
 * @Test public void totalPriceStep17Test() { int expected = basePrice +
 * eqMiamiWheelsSeatsPrice + dphPrice; int actual = total3Price;
 * Assert.assertEquals(actual, expected);
 * 
 * }
 * 
 * @Test public void equipmentMiamiWheelsSeatCarbonPriceTest() { int expected =
 * miamiBluePrice + wheelsPrice + powerSeatsPrice + carbonPrice; int actual =
 * eqMiamiWheelsSeatsCarbonPrice; Assert.assertEquals(actual, expected); }
 * 
 * @Test public void totalPriceStep21Test() { int expected = basePrice +
 * eqMiamiWheelsSeatsCarbonPrice + dphPrice; int actual = total4Price;
 * Assert.assertEquals(actual, expected); }
 * 
 * @Test public void equipmentMiamiWheelsSeatCarbonPrice7speedBrakesTest() { int
 * expected = miamiBluePrice + wheelsPrice + powerSeatsPrice + carbonPrice +
 * speed7Price + brakesPrice; int actual = eqMiamiWheelsSeatsCarbon7BrakesPrice;
 * Assert.assertEquals(actual, expected); }
 * 
 * @Test public void totalPriceStep26Test() { int expected = basePrice +
 * eqMiamiWheelsSeatsCarbon7BrakesPrice + dphPrice; int actual = total5Price;
 * Assert.assertEquals(actual, expected); }
 * 
 * }
 */
