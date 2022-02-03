package market.onlc;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class FileParsingTest {

    private ClassLoader cl = FileParsingTest.class.getClassLoader();

    @Test
    void zipFileTest() throws Exception {

        ZipFile zipFile = new ZipFile(new File(cl.getResource("files/sample.zip").toURI()));

        try (InputStream CsvInputStream = zipFile.getInputStream(zipFile.getEntry("hom.csv"))) {
            CSVReader csvReader = new CSVReader(new InputStreamReader(CsvInputStream));
            List<String[]> list = csvReader.readAll();
            assertThat(list)
                    .hasSize(3)
                    .contains(
                            new String[]{"Альбина,Тим"}
                    );
        }

        try (InputStream pdfInputStream = zipFile.getInputStream(zipFile.getEntry("purchase-61f59d8700bbd.pdf"))) {
            PDF parsed = new PDF(pdfInputStream);
            assertThat(parsed.author).contains("222");
        }

        try (InputStream xlsInputStream = zipFile.getInputStream(zipFile.getEntry("home.xlsx"))) {
            XLS parsed = new XLS(xlsInputStream);
            assertThat(parsed.excel.getSheetAt(0).getRow(1).getCell(2).getStringCellValue())
                    .isEqualTo("Артикул");
        }
    }

}