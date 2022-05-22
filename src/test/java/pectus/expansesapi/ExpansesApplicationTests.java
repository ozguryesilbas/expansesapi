package pectus.expansesapi;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pectus.expansesapi.business.abstracts.ExpansesService;
import pectus.expansesapi.constants.ExpansesContansts;
import pectus.expansesapi.entities.Expanse;
import pectus.expansesapi.entities.TotalSum;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;


@SpringBootTest
class ExpansesApplicationTests {

    private ExpansesService expansesService;

    @Autowired
    public ExpansesApplicationTests(ExpansesService expansesService) {
        this.expansesService = expansesService;
    }

    @Test
    void getAllExpanses() {
        List<Expanse> expanses = expansesService.getAllExpanses();
        Assert.assertEquals(expanses.size(), 149);
    }

    @Test
    void getFilteredExpanses() throws ParseException {
        List<Expanse> expanses = expansesService.getExpanses(null, "Gaama", null, null,
                "4639", null, null, "04/10/2021", "Sam", ExpansesContansts.DEPARTMENTS + ","
                        + ExpansesContansts.PROJECTNAME, ExpansesContansts.DEPARTMENTS, ExpansesContansts.DESCENDING);
        Assert.assertEquals(expanses.size(), 1);
        Assert.assertEquals(expanses.get(0).getDepartments(), "Marketing");
    }

    @Test
    void getTotalSum() {
        List<TotalSum> totalSums = expansesService.getTotalSum(ExpansesContansts.DEPARTMENTS);
        Double sum = 0.0;
        for (TotalSum totalSum : totalSums) {
            if (totalSum.getAggregatesBy().equals("Marketing")) {
                sum = totalSum.getTotalSum();
                break;
            }
        }
        Assert.assertEquals(sum, new Double(76421.0));
    }


}
