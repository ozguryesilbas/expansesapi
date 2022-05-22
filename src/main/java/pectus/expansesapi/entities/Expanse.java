package pectus.expansesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pectus.expansesapi.constants.ExpansesContansts;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Expanse {

    private String departments;
    private String project_name;
    private Double amount;
    private String amountWithCurrency;
    private Date date;
    private String formattedDate;
    private String member_name;

    private final String pattern = ExpansesContansts.DATEPATTERN;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public Expanse(String departments, String project_name, Double amount, Date date, String member_name) {
        this.departments = departments;
        this.project_name = project_name;
        this.amount = amount;
        this.date = date;
        this.member_name = member_name;
        this.setAmountWithCurrency(amount);
        this.setFormattedDate(date);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    @JsonIgnore
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("amount")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getAmountWithCurrency() {
        return amountWithCurrency;
    }

    public void setAmountWithCurrency(Double amount) {
        if (amount != null) {
            this.amountWithCurrency = amount.toString() + ExpansesContansts.CURRENCYEXTENSIONONEDECIMAL;
        } else {
            this.amountWithCurrency = null;
        }
    }

    @JsonIgnore
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    @JsonProperty("date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(Date date) {
        if (date != null) {
            this.formattedDate = simpleDateFormat.format(date);
        } else {
            this.formattedDate = null;
        }

    }
}
