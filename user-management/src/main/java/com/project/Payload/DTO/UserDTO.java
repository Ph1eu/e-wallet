package com.project.Payload.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.Model.*;

import java.util.*;


public class UserDTO{
    @JsonIgnore
    private String idemail;


    private String username;


    private String password;

    private String first_name;
    private String last_name;
    private Date registration_date;

    private RoleDTO roles ;

    @JsonIgnore
    private AddressDTO addressDTO ;
    @JsonIgnore
    private List<PaymentcardDTO> paymentcardsDTO ;
    private BalanceInformationDTO balanceInformation;

    public UserDTO(){

    }
    public UserDTO(User user) {
        this.idemail = user.getId_email();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.first_name = user.getFirst_name();
        this.last_name = user.getLast_name();
        this.registration_date = user.getRegistration_date();
        if (user.getRoles() == null){
            this.roles= null;
        }
        else{
            this.roles = new RoleDTO(user.getRoles());
        }
        if (user.getAddress() == null){
            this.addressDTO= null;
        }
        else{
            this.addressDTO = new AddressDTO(user.getAddress());
        }

//        if (user.getPaymentcards() == null){
//            this.paymentcardsDTO= new ArrayList<>();
//        }
//        else{
//            for (Paymentcard paymentcard :user.getPaymentcards()){
//                this.paymentcardsDTO.add(new PaymentcardDTO(paymentcard));
//            }
//        }

    }

    public UserDTO(String idemail, String username, String password, String first_name, String last_name, Date registration_date, RoleDTO roles, AddressDTO addressDTO, List<PaymentcardDTO> paymentcardsDTO, BalanceInformationDTO balanceInformation) {
        this.idemail = idemail;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.registration_date = registration_date;
        this.roles = roles;
        this.addressDTO = addressDTO;
        this.paymentcardsDTO = paymentcardsDTO;
        this.balanceInformation = balanceInformation;
    }

    public AddressDTO getAddress() {
        return addressDTO;
    }

    public void setAddress(AddressDTO address) {
        this.addressDTO = address;
    }

    public String getId_email() {
        return this.idemail;
    }

    public void setId_email(String id_email) {
        this.idemail = id_email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    public RoleDTO getRoles() {
        return this.roles;
    }

    public void setRoles(RoleDTO roles) {
        this.roles = roles;
    }

    public List<PaymentcardDTO> getPaymentcards() {
        return this.paymentcardsDTO;
    }

    public void setPaymentcards(List<PaymentcardDTO> paymentcards) {
        this.paymentcardsDTO = paymentcards;
    }

    public String getIdemail() {
        return idemail;
    }

    public void setIdemail(String idemail) {
        this.idemail = idemail;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
    }

    public List<PaymentcardDTO> getPaymentcardsDTO() {
        return paymentcardsDTO;
    }

    public void setPaymentcardsDTO(List<PaymentcardDTO> paymentcardsDTO) {
        this.paymentcardsDTO = paymentcardsDTO;
    }

    public BalanceInformationDTO getBalanceInformation() {
        return balanceInformation;
    }

    public void setBalanceInformation(BalanceInformationDTO balanceInformation) {
        this.balanceInformation = balanceInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(idemail, userDTO.idemail) && Objects.equals(username, userDTO.username) && Objects.equals(password, userDTO.password) && Objects.equals(first_name, userDTO.first_name) && Objects.equals(last_name, userDTO.last_name) && Objects.equals(registration_date, userDTO.registration_date) && Objects.equals(roles, userDTO.roles) && Objects.equals(addressDTO, userDTO.addressDTO) && Objects.equals(paymentcardsDTO, userDTO.paymentcardsDTO) && Objects.equals(balanceInformation, userDTO.balanceInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idemail, username, password, first_name, last_name, registration_date, roles, addressDTO, paymentcardsDTO, balanceInformation);
    }
}