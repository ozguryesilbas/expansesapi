package pectus.expansesapi.business.abstracts;

import pectus.expansesapi.entities.Expanse;
import pectus.expansesapi.entities.TotalSum;

import java.text.ParseException;
import java.util.List;

public interface ExpansesService {

    List<Expanse> getAllExpanses();

    List<Expanse> getExpanses(
            String departments,
            String project_name,
            String amount,
            String amountgte,
            String amountlte,
            String date,
            String dategte,
            String datelte,
            String member_name,
            String fields,
            String sort,
            String order
    ) throws ParseException;

    List<TotalSum> getTotalSum(String by);

}
