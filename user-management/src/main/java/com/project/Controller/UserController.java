package com.project.Controller;

import com.project.Assembler.UserResourceAssembler;
import com.project.Configuration.jwt.JwtServices;
import com.project.Model.Address;
import com.project.Model.Paymentcard;
import com.project.Model.User;
import com.project.Payload.DTO.*;
import com.project.Payload.Request.CRUDUserInforRequest.AddressCRUD;
import com.project.Payload.Request.CRUDUserInforRequest.PaymentcardCRUD;
import com.project.Payload.Response.MessageResponse;
import com.project.Service.AddressService;
import com.project.Service.CustomUserDetail;
import com.project.Service.PaymentCardsService;
import com.project.Service.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @GetMapping("/{username}")
    public ResponseEntity<?> getOneUser(@PathVariable("username") String username){

        UserDTO user = verifyUserInstance(username);
        if (user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else{
            return  ResponseEntity.ok().body(userResourceAssembler.toModel(user));
        }

    }
    @GetMapping("/{username}/address")
    public ResponseEntity<?> getAddress(@PathVariable("username")String username){
        UserDTO user = verifyUserInstance(username);
        if (user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else if(user.getAddress() == null){
            return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("Address is empty"),
                    linkTo(methodOn(UserController.class).deleteAddress(username)).withRel("delete address for user"),
                    linkTo(methodOn(UserController.class).getAddress(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(),username)).withRel("Add address").withType("POST")));
        }
        else {
            return ResponseEntity.ok().body(EntityModel.of(userResourceAssembler.toAddressModel(user)));
        }
    }
    @PostMapping("/{username}/address")
    public ResponseEntity<?> setAddress(@Valid @RequestBody AddressCRUD addressCRUD,@PathVariable("username") String username){
        UserDTO user = verifyUserInstance(username);
        if (user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else {
            AddressDTO address = new AddressDTO(addressCRUD.getStreet_address(),
                    addressCRUD.getCity(), addressCRUD.getProvince(), addressCRUD.getCountry()
            );
            addressService.saveAddressForUser(address,user);
            return ResponseEntity.ok().body(EntityModel.of(
                    userResourceAssembler.toAddressModel(user)));
        }
    }

    @DeleteMapping("/{username}/address")
    public ResponseEntity<?> deleteAddress(@PathVariable("username") String username){
        UserDTO user = verifyUserInstance(username);
        if (user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else {
            addressService.deleteAddressByUser(user);
            return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("Successful Delete User"),
                    linkTo(methodOn(UserController.class).deleteAddress(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).getAddress(username)).withRel("Get address from username"),
                    linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(),username)).withRel("Add address").withType("POST")));
        }
    }
    @GetMapping("/{username}/cards")
    public ResponseEntity<?> getPaymentCards(@PathVariable("username")String username ) throws ParseException {
        UserDTO user = verifyUserInstance(username);
        if (user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else {
            List<PaymentcardDTO> paymentcards = paymentCardsService.findByAllUser(user);
            if (paymentcards.isEmpty() ) {
                return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("User has no card yet "),
                        linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")
                ));
            } else {
                return ResponseEntity.ok().body(EntityModel.of(
                        userResourceAssembler.toCardsCollectionModel(paymentcards,user)));
            }
        }
    }
    @PostMapping("/{username}/cards/")
    public ResponseEntity<?> setPaymentCards(@PathVariable("username")String username,@RequestBody List<PaymentcardCRUD> paymentcardCRUDs) throws ParseException {
        UserDTO user = verifyUserInstance(username);
        List<PaymentcardDTO> paymentcards = new ArrayList<>();
        if(user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else{
            for(PaymentcardCRUD paymentcardCRUD :paymentcardCRUDs){
                if(!validateDateString(paymentcardCRUD.getExpiration_date()) && !validateDateString(paymentcardCRUD.getRegistration_date())){
                    return ResponseEntity.badRequest().body(new MessageResponse("Invalid Date format"));
                }
                Date expDate = new SimpleDateFormat("dd/MM/yyyy").parse(paymentcardCRUD.getExpiration_date());
                Date regDate = new SimpleDateFormat("dd/MM/yyyy").parse(paymentcardCRUD.getRegistration_date());
                PaymentcardDTO paymentcard = new PaymentcardDTO(paymentcardCRUD.getCard_number(),user,
                        paymentcardCRUD.getCard_holder_name(),paymentcardCRUD.getCard_type(),
                        regDate,expDate);
                paymentcards.add(paymentcard);
            }
            paymentCardsService.saveAllByCards(paymentcards);
            return ResponseEntity.ok().body(userResourceAssembler.toCardsCollectionModel(paymentcards,user));
        }
    }
    @DeleteMapping("/{username}/cards")
    public ResponseEntity<?> deletePaymentCardbyID(@PathVariable String username ,@RequestParam(value = "id") String id) throws ParseException {
        UserDTO user = verifyUserInstance(username);
        if(user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else{
            paymentCardsService.deleteByID(id);
            List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
            return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("Deleted card for user"),
                    linkTo(methodOn(UserController.class).deletePaymentCardbyID(username,id)).withSelfRel(),
                    linkTo(methodOn(UserController.class).deleteAllPaymentCard(username)).withRel("delete all payment cards"),
                    linkTo(methodOn(UserController.class).setPaymentCards(username,paymentcardCRUDS )).withRel("Set payment cards"),
                    linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")
            ));
    }
    }
    @DeleteMapping("/{username}/cards/delete/all")
    public ResponseEntity<?> deleteAllPaymentCard(@PathVariable String username ) throws ParseException {
        UserDTO user = verifyUserInstance(username);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        } else {
            paymentCardsService.deleteAllByUser(user);
            List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
            return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("Deleted all card for user"),
                    linkTo(methodOn(UserController.class).deleteAllPaymentCard(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).deletePaymentCardbyID(username, "id")).withRel("Delete by ID"),
                    linkTo(methodOn(UserController.class).setPaymentCards(username, paymentcardCRUDS)).withRel("Set payment cards"),
                    linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")
            ));
        }
    }
    }



