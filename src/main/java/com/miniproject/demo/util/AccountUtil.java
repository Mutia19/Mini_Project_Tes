package com.miniproject.demo.util;

import javax.validation.constraints.NotBlank;

public class AccountUtil {
    @NotBlank(message = "Sort code is mandatory ")
    private String SortCode;

    @NotBlank(message = "AccountDao number is mandatory")
    private String AccountNumber;

    public AccountUtil() {}

    public String getSortCode() {
        return SortCode;
    }
    public void setSortCode(String sortCode) {
        this.SortCode = sortCode;
    }
    public String getAccountNumber() {
        return AccountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.AccountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "AccountInput{" +
                "sortCode='" + SortCode + '\'' +
                ", accountNumber='" + AccountNumber + '\'' +
                '}';
    }
}
