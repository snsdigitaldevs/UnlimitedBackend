//package com.simonschuster.pimsleur.unlimited.data.domain;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "address_book")
//public class AddressBook implements Serializable{
//    @Id
//    @Column(name = "address_book_id")
//    private Integer addressBookId;
//
//    @Column(name = "customers_id")
//    private Integer customersId;
//
//    @Column(name = "entry_gender")
//    private String entryGender;
//
//    @Column(name = "entry_company")
//    private String entryCompany;
//
//    @Column(name = "entry_firstname")
//    private String entryFirstName;
//
//    @Column(name = "entry_lastname")
//    private String entryLastName;
//
//    @Column(name = "entry_street_address")
//    private String entryStreetAddress;
//
//    @Column(name = "entry_suburb")
//    private String entrySuburb;
//
//    @Column(name = "entry_postcode")
//    private String entryPostCode;
//
//    @Column(name = "entry_city")
//    private String entryCity;
//
//    @Column(name = "entry_state")
//    private String entryState;
//
//    @Column(name = "entry_country_id")
//    private Integer entryCountryId;
//
//    @Column(name = "entry_zone_id")
//    private Integer entryZoneId;
//
//    @Column(name = "txn_st")
//    private Integer txnST;
//
//    @Column(name = "entry_country_code")
//    private char entryCountryCode;
//
//    @Column(name = "entry_zone_code")
//    private char entryZoneCode;
//
//    @Column(name = "entry_salutation")
//    private String entrySalutation;
//
//    @Column(name = "entry_phone")
//    private String entryPhone;
//
//    @Column(name = "entry_email")
//    private String entryEmail;
//
//    public Integer getAddressBookId() {
//        return addressBookId;
//    }
//
//    public void setAddressBookId(Integer addressBookId) {
//        this.addressBookId = addressBookId;
//    }
//
//    public Integer getCustomersId() {
//        return customersId;
//    }
//
//    public void setCustomersId(Integer customersId) {
//        this.customersId = customersId;
//    }
//
//    public String getEntryGender() {
//        return entryGender;
//    }
//
//    public void setEntryGender(String entryGender) {
//        this.entryGender = entryGender;
//    }
//
//    public String getEntryCompany() {
//        return entryCompany;
//    }
//
//    public void setEntryCompany(String entryCompany) {
//        this.entryCompany = entryCompany;
//    }
//
//    public String getEntryFirstName() {
//        return entryFirstName;
//    }
//
//    public void setEntryFirstName(String entryFirstName) {
//        this.entryFirstName = entryFirstName;
//    }
//
//    public String getEntryLastName() {
//        return entryLastName;
//    }
//
//    public void setEntryLastName(String entryLastName) {
//        this.entryLastName = entryLastName;
//    }
//
//    public String getEntryStreetAddress() {
//        return entryStreetAddress;
//    }
//
//    public void setEntryStreetAddress(String entryStreetAddress) {
//        this.entryStreetAddress = entryStreetAddress;
//    }
//
//    public String getEntrySuburb() {
//        return entrySuburb;
//    }
//
//    public void setEntrySuburb(String entrySuburb) {
//        this.entrySuburb = entrySuburb;
//    }
//
//    public String getEntryPostCode() {
//        return entryPostCode;
//    }
//
//    public void setEntryPostCode(String entryPostCode) {
//        this.entryPostCode = entryPostCode;
//    }
//
//    public String getEntryCity() {
//        return entryCity;
//    }
//
//    public void setEntryCity(String entryCity) {
//        this.entryCity = entryCity;
//    }
//
//    public String getEntryState() {
//        return entryState;
//    }
//
//    public void setEntryState(String entryState) {
//        this.entryState = entryState;
//    }
//
//    public Integer getEntryCountryId() {
//        return entryCountryId;
//    }
//
//    public void setEntryCountryId(Integer entryCountryId) {
//        this.entryCountryId = entryCountryId;
//    }
//
//    public Integer getEntryZoneId() {
//        return entryZoneId;
//    }
//
//    public void setEntryZoneId(Integer entryZoneId) {
//        this.entryZoneId = entryZoneId;
//    }
//
//    public Integer getTxnST() {
//        return txnST;
//    }
//
//    public void setTxnST(Integer txnST) {
//        this.txnST = txnST;
//    }
//
//    public char getEntryCountryCode() {
//        return entryCountryCode;
//    }
//
//    public void setEntryCountryCode(char entryCountryCode) {
//        this.entryCountryCode = entryCountryCode;
//    }
//
//    public char getEntryZoneCode() {
//        return entryZoneCode;
//    }
//
//    public void setEntryZoneCode(char entryZoneCode) {
//        this.entryZoneCode = entryZoneCode;
//    }
//
//    public String getEntrySalutation() {
//        return entrySalutation;
//    }
//
//    public void setEntrySalutation(String entrySalutation) {
//        this.entrySalutation = entrySalutation;
//    }
//
//    public String getEntryPhone() {
//        return entryPhone;
//    }
//
//    public void setEntryPhone(String entryPhone) {
//        this.entryPhone = entryPhone;
//    }
//
//    public String getEntryEmail() {
//        return entryEmail;
//    }
//
//    public void setEntryEmail(String entryEmail) {
//        this.entryEmail = entryEmail;
//    }
//}
