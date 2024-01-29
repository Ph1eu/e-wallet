package com.project.controller;

import com.project.assembler.UserResourceAssembler;
import com.project.configuration.jwt.JwtServices;
import com.project.exceptions.custom_exception.ValidationInput.MissingRequiredFieldException;
import com.project.payload.dto.*;
import com.project.payload.request.CRUDUserInforRequest.AddressCRUD;
import com.project.payload.request.CRUDUserInforRequest.PaymentcardCRUD;
import com.project.payload.response.MessageResponse;
import com.project.payload.response.ResponseEntityWrapper;
import com.project.service.AddressService;
import com.project.service.CustomUserDetail;
import com.project.service.PaymentCardsService;
import com.project.service.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    PaymentCardsService paymentCardsService;
    @Autowired
    AddressService addressService;

    @Autowired
    JwtServices jwtServices;
    @Autowired
    UserResourceAssembler userResourceAssembler;
    private static final Logger logger = LoggerFactory.getLogger(JwtServices.class);

    // verify user in security context
    public UserDTO verifyUserInstance(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserDTO user = null;

        if (principal instanceof UserDetails userDetails) {
            if (Objects.equals(username, userDetails.getUsername())) {
                CustomUserDetail customUserDetail = (CustomUserDetail) userDetailService.loadUserByUsername(username);
                user = new UserDTO(customUserDetail.getUser());
            } else {
                logger.error("Error: Invalid User");
            }
        } else {
            logger.error("Error: Principal is not an instance of UserDetails");
        }
        return user;
    }
    public static boolean validateDateString(String dateStr) {
        String pattern = "^\\d{2}/\\d{2}/\\d{4}$";
        return Pattern.matches(pattern, dateStr);
    }
    @GetMapping("")
    public ResponseEntity<?> getOneUser(@RequestParam("username") String username){

        UserDTO user = verifyUserInstance(username);
        UserDTO userwithBalance = userDetailService.getUserWithBalanceInformation(user.getId_email());
        if (user == null){
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        }
        else{
            List<PaymentcardDTO> paymentcardDTOS= paymentCardsService.findByAllUser(user);
            userwithBalance.setPaymentcards(paymentcardDTOS);
            return  ResponseEntity.ok().body(userResourceAssembler.toCollectionModelInWrapper(List.of(userwithBalance)));
        }

    }
    @GetMapping("/address")
    public ResponseEntity<?> getAddress(@RequestParam("username")String username){
        UserDTO user = verifyUserInstance(username);

        if (user == null){
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        }
        else if(user.getAddress() == null){
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>();
            responseEntityWrapper.setMessage("ADDRESS IS EMPTY");
            BindingResult bindingResult = null;
            responseEntityWrapper.setLink(List.of( linkTo(methodOn(UserController.class).deleteAddress(username)).withRel("delete address for user"),
                    linkTo(methodOn(UserController.class).getAddress(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(),username,bindingResult)).withRel("Add address").withType("POST")));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
        }
        else {
            return ResponseEntity.ok().body(EntityModel.of(userResourceAssembler.toAddressModel(user)));
        }
    }
    @PostMapping("/address")
    public ResponseEntity<?> setAddress(@Valid @RequestBody AddressCRUD addressCRUD,@RequestParam("username") String username,BindingResult bindingResult) {
        handMissingField(bindingResult);
        UserDTO user = verifyUserInstance(username);
        if (user == null){
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        }
        else {
            Class<?> clazz = addressCRUD.getClass();
            AddressDTO address = new AddressDTO(UUID.randomUUID().toString(),addressCRUD.getStreet_address(),
                    addressCRUD.getCity(), addressCRUD.getProvince(), addressCRUD.getCountry()
            );
            addressService.saveAddressForUser(address,user);
            return ResponseEntity.ok().body(
                    userResourceAssembler.toAddressModel(user));
        }
    }

    @DeleteMapping("/address")
    public ResponseEntity<?> deleteAddress(@RequestParam("username") String username){
        UserDTO user = verifyUserInstance(username);
        if (user == null){
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        }
        else {
            addressService.deleteAddressByUser(user);
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>();
            responseEntityWrapper.setMessage("ADDRESS IS NOW DELETED");
            BindingResult bindingResult = null;

            responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).deleteAddress(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).getAddress(username)).withRel("Get address from username"),
                    linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(),username,bindingResult)).withRel("Add address").withType("POST")));
            return ResponseEntity.ok().body(responseEntityWrapper);

        }
    }
    @GetMapping("/cards")
    public ResponseEntity<?> getPaymentCards(@RequestParam("username")String username ) throws ParseException {
        UserDTO user = verifyUserInstance(username);
        if (user == null){
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        }
        else {
            List<PaymentcardDTO> paymentcards = paymentCardsService.findByAllUser(user);
            if (paymentcards.isEmpty() ) {
                ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper = new ResponseEntityWrapper<>();
                responseEntityWrapper.setMessage("User has no card yet ");
                responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
            } else {
                ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper =userResourceAssembler.toCardsCollectionModel(paymentcards,user);
                responseEntityWrapper.setMessage("Get cards successfully");
                return ResponseEntity.ok().body(
                        responseEntityWrapper);
            }
        }
    }
    @PostMapping("/cards")
    public ResponseEntity<?> setPaymentCards(@RequestParam("username")String username,@RequestBody List<PaymentcardCRUD> paymentcardCRUDs,BindingResult bindingResult) throws ParseException {
        handMissingField(bindingResult);
        UserDTO user = verifyUserInstance(username);
        List<PaymentcardDTO> paymentcards = new ArrayList<>();
        if(user == null){
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        }
        else{
            for(PaymentcardCRUD paymentcardCRUD :paymentcardCRUDs){
                if(!validateDateString(paymentcardCRUD.getExpiration_date()) && !validateDateString(paymentcardCRUD.getRegistration_date())){
                    return ResponseEntity.badRequest().body(new MessageResponse("Invalid Date format"));
                }
                Date expDate = new SimpleDateFormat("dd/MM/yyyy").parse(paymentcardCRUD.getExpiration_date());
                Date regDate = new SimpleDateFormat("dd/MM/yyyy").parse(paymentcardCRUD.getRegistration_date());
                PaymentcardDTO paymentcard = new PaymentcardDTO(UUID.randomUUID().toString(),paymentcardCRUD.getCard_number(),user,
                        paymentcardCRUD.getCard_holder_name(),paymentcardCRUD.getCard_type(),
                        regDate,expDate);
                paymentcards.add(paymentcard);
            }
            paymentCardsService.saveAllByCards(paymentcards);
            ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper =userResourceAssembler.toCardsCollectionModel(paymentcards,user);
            responseEntityWrapper.setMessage("Set cards successfully");
            return ResponseEntity.ok().body(responseEntityWrapper);
        }
    }
    @DeleteMapping("/cards")
    public ResponseEntity<?> deletePaymentCardbyID(@RequestParam String username ,@RequestParam String id) throws ParseException {
        UserDTO user = verifyUserInstance(username);
        if(user == null){
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        }
        else{
            paymentCardsService.deleteByID(id);
            List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
            BindingResult bindingResult= null;
            ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper = new ResponseEntityWrapper<>();
            responseEntityWrapper.setMessage("Deleted card for user");
            responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).deletePaymentCardbyID(username,id)).withSelfRel(),
                    linkTo(methodOn(UserController.class).deleteAllPaymentCard(username)).withRel("delete all payment cards"),
                    linkTo(methodOn(UserController.class).setPaymentCards(username,paymentcardCRUDS,bindingResult)).withRel("Set payment cards"),
                    linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")
            ));
            return ResponseEntity.ok().body(responseEntityWrapper);
    }
    }
    @DeleteMapping("/cards/delete/all")
    public ResponseEntity<?> deleteAllPaymentCard(@RequestParam String username ) throws ParseException {
        UserDTO user = verifyUserInstance(username);
        if (user == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        } else {
            paymentCardsService.deleteAllByUser(user);
            List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
            BindingResult bindingResult= null;
            return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("Deleted all card for user"),
                    linkTo(methodOn(UserController.class).deleteAllPaymentCard(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).deletePaymentCardbyID(username, "id")).withRel("Delete by ID"),
                    linkTo(methodOn(UserController.class).setPaymentCards(username, paymentcardCRUDS,bindingResult)).withRel("Set payment cards"),
                    linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")
            ));
        }
    }
    private void handMissingField(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors: ");
            for (FieldError error : bindingResult.getFieldErrors()) {
                String fieldName = error.getField();
                String errorMessageForField = error.getDefaultMessage();
                errorMessage.append(fieldName).append(" - ").append(errorMessageForField).append(";");
            }
            throw  new MissingRequiredFieldException(errorMessage.toString());

        }
    }
    }



