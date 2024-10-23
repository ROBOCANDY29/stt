package automation;


import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Iterator;

public class Test {

    WebDriver driver;

    @BeforeClass
    public void setUp() {
        // Set up WebDriver for Chrome
        WebDriver.chromedriver().setup();
        driver = new ChromeDriver();
        // Open the registration form URL
        driver.get("https://demo.wpeverest.com/user-registration/course-registration-form/"); // Replace with the actual form URL
        driver.manage().window().maximize();
    }

    @DataProvider(name = "userData")
    public Iterator<Object[]> userData() throws Exception {
        // Load Excel file
        FileInputStream fis = new FileInputStream(new File("C:\\Users\\Hp\\Downloads\\AutomationTesting.xlsx")); // Update with your path
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        // Skip the first row (header)
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        return new Iterator<Object[]>() {
            @Override
            public boolean hasNext() {
                return rowIterator.hasNext();
            }

            @Override
            public Object[] next() {
                Row row = rowIterator.next();
                return new Object[]{
                    getCellValue(row.getCell(0)), // FirstName
                    getCellValue(row.getCell(1)), // LastName
                    getCellValue(row.getCell(2)), // Email
                    getCellValue(row.getCell(3)), // Gender
                    getCellValue(row.getCell(4)), // Mobile
                    getCellValue(row.getCell(5)), // DOB
                    getCellValue(row.getCell(6)), // Address
                    getCellValue(row.getCell(7)), // City
                    getCellValue(row.getCell(8)), // State
                    getCellValue(row.getCell(9)), // Country
                    getCellValue(row.getCell(10)), // Zip
                    getCellValue(row.getCell(11)), // Password
                    getCellValue(row.getCell(12))  // ConfirmPassword
                };
            }
        };
    }

    // Helper method to get cell value based on cell type
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    @Test(dataProvider = "userData")
    public void registerUser(String firstName, String lastName, String email, String gender, String mobile,
                             String dob, String address, String city, String state, String country, 
                             String zip, String password, String confirmPassword) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Updated to use Duration

        // Fill in the form fields with explicit waits
        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"first_name\"]")));
        firstNameField.clear();
        firstNameField.sendKeys(firstName);

        WebElement lastNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"last_name\"]")));
        lastNameField.clear();
        lastNameField.sendKeys(lastName);

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"user_email\"]")));
        emailField.clear();
        emailField.sendKeys(email);


        // Select gender using XPath
        if (gender.equalsIgnoreCase("Male")) {
            WebElement maleRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='radio_1623051748_field']/ul/li[1]/label")));
            maleRadio.click();
        } else if (gender.equalsIgnoreCase("Female")) {
            WebElement femaleRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='radio_1623051748_Female']")));
            femaleRadio.click();
        } else {
            WebElement otherRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='radio_1623051748_field']/ul/li[3]/label")));
            otherRadio.click();
        }

        // Date of Birth
        WebElement dobElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"date_box_1623051693_field\"]/span/input[1]")));
        dobElement.sendKeys(dob);
        dobElement.sendKeys(Keys.ENTER);

        // Address, City, State, Country, Zip
        WebElement addressField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"textarea_1623050614\"]")));
        addressField.clear();
        addressField.sendKeys(address);

        WebElement cityField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"input_box_1623050696\"]")));
        cityField.clear();
        cityField.sendKeys(city);

        WebElement stateField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"input_box_1623050759\"]")));
        stateField.clear();
        stateField.sendKeys(state);

        // Select country from dropdown
        Select countryDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"country_1623050729\"]"))));
        countryDropdown.selectByVisibleText(country);

        WebElement zipField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"input_box_1623050879\"]")));
        zipField.sendKeys(zip);

        // Password
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"user_pass\"]")));
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement confirmPasswordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"user_confirm_password\"]")));
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirmPassword);

        // Submit the form
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"user-registration-form-303\"]/form/div[7]/button")));
        submitButton.click();

        // Log output to console (or file as needed)
        System.out.println("User registered: " + firstName + " " + lastName);
    }

    @AfterClass
    public void tearDown() {
        // Close the browser after execution
        driver.quit();
    }
}
