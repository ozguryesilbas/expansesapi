package pectus.expansesapi.api.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pectus.expansesapi.business.abstracts.ExpansesService;
import pectus.expansesapi.entities.Expanse;
import pectus.expansesapi.entities.TotalSum;

import java.util.List;

@RestController
@RequestMapping("/")
@ApiOperation("Expanses API")
public class ExpansesController {

    private ExpansesService expansesService;

    @Autowired
    public ExpansesController(ExpansesService expansesService) {
        this.expansesService = expansesService;
    }

    /**
     * Retrieves all expanses
     *
     * @return all expanses as response entity
     */
    @ApiOperation(value = "Get all expanses", notes = "Returns all expanses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Not found - Expanses not found")
    })
    @GetMapping("/expanses_data/getAllExpanses")
    public ResponseEntity<?> getAllExpanses() {

        try {
            List<Expanse> expanses = expansesService.getAllExpanses();
            ;
            if (!CollectionUtils.isEmpty(expanses)) {
                return new ResponseEntity<>(expanses, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error occurred while fetching expanses", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while fetching expanses", HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Retrieves all expanses
     * @param departments parameter for filtering by departments
     * @param project_name parameter for filtering by project_name
     * @param amount parameter for filtering by amount
     * @param amountgte parameter for filtering grater than amount
     * @param amountlte parameter for filtering less than amount
     * @param date parameter for filtering by date
     * @param dategte parameter for filtering grater than date
     * @param datelte parameter for filtering less than date
     * @param member_name parameter for filtering by member_name
     * @param fields parameter for fetching expanses with only given fields
     * @param sort parameter for sorting expanses
     * @param order parameter for sorting order
     * @return expanses as response entity
     */
    @ApiOperation(value = "Get expanses", notes = "Returns expanses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Not found - Expanses data not found")
    })
    @GetMapping("/expanses_data")
    public ResponseEntity<?> getExpansesData(
            @RequestParam(required = false) String departments,
            @RequestParam(required = false) String project_name,
            @RequestParam(required = false) String amount,
            @RequestParam(required = false) String amountgte,
            @RequestParam(required = false) String amountlte,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String dategte,
            @RequestParam(required = false) String datelte,
            @RequestParam(required = false) String member_name,
            @RequestParam(required = false) String fields,
            @RequestParam(required = false, defaultValue = "member_name") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {

        try {
            List<Expanse> expanses = expansesService.getExpanses(departments, project_name, amount, amountgte,
                    amountlte, date, dategte, datelte, member_name, fields, sort, order);
            return new ResponseEntity<>(expanses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while fetching expanses: " + e.toString(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves total sums by grouping
     * @param by parameter for grouping by expanses
     * @return total sums as response entity
     */
    @ApiOperation(value = "Get total sums", notes = "Returns total sums")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Not found - Total sum data not found")
    })
    @GetMapping("/aggregates")
    public ResponseEntity<?> getTotalSum(@RequestParam String by) {

        try {
            List<TotalSum> totalSums = expansesService.getTotalSum(by);
            return new ResponseEntity<>(totalSums, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while calculating total sum: " + e.toString(), HttpStatus.NOT_FOUND);
        }
    }

}
