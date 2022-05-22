package pectus.expansesapi.business.concretes;

import com.google.common.io.Resources;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pectus.expansesapi.business.abstracts.ExpansesService;
import pectus.expansesapi.constants.ExpansesContansts;
import pectus.expansesapi.entities.Expanse;
import pectus.expansesapi.entities.TotalSum;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExpansesServiceImpl implements ExpansesService {

    /**
     * Retrieves all Expanses as list
     *
     * @return list of expanse
     */
    @Override
    public List<Expanse> getAllExpanses() {

        URL url = Resources.getResource("expanses.csv");
        List<Expanse> expanses = new ArrayList<>();

        try {
            List<String> lines = Resources.readLines(url, StandardCharsets.UTF_8);
            lines.remove(0);

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    expanses.add(new Expanse(
                            parts[0],
                            parts[1],
                            Double.parseDouble((parts[2].replace("\"", "") +
                                    parts[3].replace("\"", ""))
                                    .replace(ExpansesContansts.CURRENCYEXTENSIONTWODECIMAL, "")),
                            new SimpleDateFormat(ExpansesContansts.DATEPATTERN).parse(parts[4]),
                            parts[5]));
                } else if (parts.length == 5) {
                    expanses.add(new Expanse(
                            parts[0],
                            parts[1],
                            Double.parseDouble(parts[2].replace("\"", "")
                                    .replace(".00€", "")),
                            new SimpleDateFormat(ExpansesContansts.DATEPATTERN).parse(parts[3]),
                            parts[4]));
                }

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return expanses;
    }

    /**
     * Retrieves Expanses as list
     * according to given parameters
     * @return list of expanse
     */
    @Override
    public List<Expanse> getExpanses(
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
    ) throws ParseException {
        List<Expanse> expanses = getAllExpanses();

        if (departments != null) {
            expanses = expanses
                    .stream()
                    .filter(e -> departments.equals(e.getDepartments()))
                    .collect(Collectors.toList());
        }
        if (project_name != null) {
            expanses = expanses
                    .stream()
                    .filter(e -> project_name.equals(e.getProject_name()))
                    .collect(Collectors.toList());
        }

        if (amount != null) {
            Double amountDouble = Double.parseDouble(amount);
            expanses = expanses
                    .stream()
                    .filter(e -> e.getAmount().equals(amountDouble))
                    .collect(Collectors.toList());
        }

        if (amountgte != null) {
            Double amountgteDouble = Double.parseDouble(amountgte);
            expanses = expanses
                    .stream()
                    .filter(e -> e.getAmount() >= amountgteDouble)
                    .collect(Collectors.toList());
        }

        if (amountlte != null) {
            Double amountlteDouble = Double.parseDouble(amountlte);
            expanses = expanses
                    .stream()
                    .filter(e -> e.getAmount() <= amountlteDouble)
                    .collect(Collectors.toList());
        }

        if (date != null) {
            Date formattedDate = new SimpleDateFormat(ExpansesContansts.DATEPATTERN).parse(date);
            expanses = expanses
                    .stream()
                    .filter(e -> e.getDate().equals(formattedDate))
                    .collect(Collectors.toList());
        }

        if (dategte != null) {
            Date formattedDate = new SimpleDateFormat(ExpansesContansts.DATEPATTERN).parse(dategte);
            List<Expanse> equalExpanses = expanses
                    .stream()
                    .filter(e -> e.getDate().equals(formattedDate))
                    .collect(Collectors.toList());

            expanses = expanses
                    .stream()
                    .filter(e -> e.getDate().after(formattedDate))
                    .collect(Collectors.toList());

            expanses.addAll(equalExpanses);
        }

        if (datelte != null) {
            Date formattedDate = new SimpleDateFormat(ExpansesContansts.DATEPATTERN).parse(datelte);
            List<Expanse> equalExpanses = expanses
                    .stream()
                    .filter(e -> e.getDate().equals(formattedDate))
                    .collect(Collectors.toList());
            expanses = expanses
                    .stream()
                    .filter(e -> e.getDate().before(formattedDate))
                    .collect(Collectors.toList());

            expanses.addAll(equalExpanses);
        }

        if (member_name != null) {
            expanses = expanses
                    .stream()
                    .filter(e -> member_name.equals(e.getMember_name()))
                    .collect(Collectors.toList());
        }

        if (sort != null && order != null && (order.equalsIgnoreCase(ExpansesContansts.ASCENDING)
                || order.equalsIgnoreCase(ExpansesContansts.DESCENDING))) {
            Comparator comparator = null;

            if (sort.equals(ExpansesContansts.DEPARTMENTS)) {
                comparator = Comparator.comparing(Expanse::getDepartments);
            } else if (sort.equals(ExpansesContansts.PROJECTNAME)) {
                comparator = Comparator.comparing(Expanse::getProject_name);
            } else if (sort.equals(ExpansesContansts.AMOUNT)) {
                comparator = Comparator.comparing(Expanse::getAmount);
            } else if (sort.equals(ExpansesContansts.DATE)) {
                comparator = Comparator.comparing(Expanse::getDate);
            } else if (sort.equals(ExpansesContansts.MEMBERNAME)) {
                comparator = Comparator.comparing(Expanse::getMember_name);
            }

            if (order.equalsIgnoreCase(ExpansesContansts.DESCENDING)) {
                comparator.reversed();
            }

            expanses = (List<Expanse>) expanses.stream().sorted(comparator).collect(Collectors.toList());

        }

        if (fields != null) {
            String[] fieldsArray = fields.split(",");
            if (fieldsArray != null && fieldsArray.length > 0 && !CollectionUtils.isEmpty(expanses)) {

                List<String> allFields = new ArrayList<>();
                allFields.add(ExpansesContansts.DEPARTMENTS);
                allFields.add(ExpansesContansts.PROJECTNAME);
                allFields.add(ExpansesContansts.AMOUNT);
                allFields.add(ExpansesContansts.DATE);
                allFields.add(ExpansesContansts.MEMBERNAME);

                for (String field : fieldsArray) {
                    allFields.remove(field);
                }

                if (!CollectionUtils.isEmpty(allFields)) {
                    for (Expanse expanse : expanses) {
                        for (String fieldName : allFields) {
                            if (fieldName.equalsIgnoreCase(ExpansesContansts.DEPARTMENTS)) {
                                expanse.setDepartments(null);
                            }
                            if (fieldName.equalsIgnoreCase(ExpansesContansts.PROJECTNAME)) {
                                expanse.setProject_name(null);
                            }
                            if (fieldName.equalsIgnoreCase(ExpansesContansts.AMOUNT)) {
                                expanse.setAmountWithCurrency(null);
                            }
                            if (fieldName.equalsIgnoreCase(ExpansesContansts.DATE)) {
                                expanse.setFormattedDate(null);
                            }
                            if (fieldName.equalsIgnoreCase(ExpansesContansts.MEMBERNAME)) {
                                expanse.setMember_name(null);
                            }
                        }
                    }
                }
            }
        }

        return expanses;
    }

    /**
     * Retrieves grouped total sums as list
     *
     * @return list of total sums
     */
    @Override
    public List<TotalSum> getTotalSum(String by) {
        List<Expanse> expanses = getAllExpanses();

        Map<String, List<Double>> expansesMap = null;
        if (by.equalsIgnoreCase(ExpansesContansts.DEPARTMENTS)) {
            expansesMap = expanses.stream().collect(Collectors.groupingBy(Expanse::getDepartments, TreeMap::new,
                    Collectors.mapping(Expanse::getAmount, Collectors.toList())));
        } else if (by.equalsIgnoreCase(ExpansesContansts.PROJECTNAME)) {
            expansesMap = expanses.stream().collect(Collectors.groupingBy(Expanse::getProject_name, TreeMap::new,
                    Collectors.mapping(Expanse::getAmount, Collectors.toList())));
        } else if (by.equalsIgnoreCase(ExpansesContansts.DATE)) {
            expansesMap = expanses.stream().collect(Collectors.groupingBy(Expanse::getFormattedDate, TreeMap::new,
                    Collectors.mapping(Expanse::getAmount, Collectors.toList())));
        } else if (by.equalsIgnoreCase(ExpansesContansts.MEMBERNAME)) {
            expansesMap = expanses.stream().collect(Collectors.groupingBy(Expanse::getMember_name, TreeMap::new,
                    Collectors.mapping(Expanse::getAmount, Collectors.toList())));
        }

        List<TotalSum> totalSums = new ArrayList<>();
        if(!MapUtils.isEmpty(expansesMap)) {
            for (String fieldName : expansesMap.keySet()) {
                List<Double> amountList = expansesMap.get(fieldName);
                Double sum = 0.0;
                for (Double amount : amountList) {
                    sum = sum + amount;
                }
                totalSums.add(new TotalSum(fieldName, sum));
            }
        }

        return totalSums;
    }

}
