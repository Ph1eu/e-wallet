package com.project.Controller;

import com.project.Assembler.UserResourceAssembler;
import com.project.Configuration.jwt.JwtServices;
import com.project.Model.Address;
import com.project.Model.Paymentcard;
import com.project.Model.User;
import com.project.Payload.Request.CRUDUserInforRequest.AddressCRUD;
import com.project.Payload.Request.CRUDUserInforRequest.PaymentcardCRUD;
import com.project.Payload.Response.MessageResponse;
import com.project.Repository.AddressRepository;
import com.project.Repository.PaymentcardRepository;
import com.project.Repository.UserRepository;
import com.project.Service.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    UserRepository userRepository;
    @Autowired
    PaymentcardRepository paymentcardRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    JwtServices jwtServices;
    @Autowired
    UserResourceAssembler userResourceAssembler;
    private static final Logger logger = LoggerFactory.getLogger(JwtServices.class);

    // verify user in security context
    public User verifyUserInstance(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user = null;

        if (principal instanceof UserDetails userDetails) {
            if (Objects.equals(username, userDetails.getUsername())) {
                user = userRepository.findByUsername(username).orElseThrow();
            } else {
                logger.error("Error: Invalid User");

            }
        }
        return user;
    }
    public static boolean validateDateString(String dateStr) {
        String pattern = "^\\d{2}/\\d{2}/\\d{4}$";
        return Pattern.matches(pattern, dateStr);
    }
    @GetMapping("/{username}")
    public ResponseEntity<?> getOneUser(@PathVariable("username") String username){

        User user = verifyUserInstance(username);
        if (user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else{
            return  ResponseEntity.ok().body(EntityModel.of(userResourceAssembler.toModel(user)));
        }

    }
    @GetMapping("/{username}/address")
    public ResponseEntity<?> getAddress(@PathVariable("username")String username){
        User user = verifyUserInstance(username);
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
    @PostMapping("/{username}/address/set")
    public ResponseEntity<?> setAddress(@Valid @RequestBody AddressCRUD addressCRUD,@PathVariable("username") String username){
        User user = verifyUserInstance(username);
        if (user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else {
            Address address = new Address(addressCRUD.getStreet_address(),
                    addressCRUD.getCity(), addressCRUD.getProvince(), addressCRUD.getCountry()
            );
            user.setAddress(address);
            userRepository.save(user);
            return ResponseEntity.ok().body(EntityModel.of(
                    userResourceAssembler.toAddressModel(user)));
        }
    }

    @DeleteMapping("/{username}/address/delete")
    public ResponseEntity<?> deleteAddress(@PathVariable("username") String username){
        User user = verifyUserInstance(username);
        if (user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else {
            Address address = user.getAddress();
            user.setAddress(null);
            addressRepository.delete(address);
            userRepository.save(user);
            return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("Successful Delete User"),
                    linkTo(methodOn(UserController.class).deleteAddress(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).getAddress(username)).withRel("Get address from username"),
                    linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(),username)).withRel("Add address").withType("POST")));
        }
    }
    @GetMapping("/{username}/cards")
    public ResponseEntity<?> getPaymentCards(@PathVariable("username")String username ){
        User user = verifyUserInstance(username);
        if (user == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid User"));
        }
        else {
            List<Paymentcard> paymentcards = paymentcardRepository.findAllByUser(user);
            if (paymentcards == null) {
                return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("User has no card yet "),
                        linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")
                ));
            } else {
                return ResponseEntity.ok().body(EntityModel.of(
                        userResourceAssembler.toCardsCollectionModel(paymentcards)));
            }
        }
    }
    @PostMapping("/{username}/cards/set")
    public ResponseEntity<?> setPaymentCards(@PathVariable("username")String username,@RequestBody List<PaymentcardCRUD> paymentcardCRUDs) throws ParseException {
        User user = verifyUserInstance(username);
        List<Paymentcard> paymentcards = new ArrayList<>();
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
                Paymentcard paymentcard = new Paymentcard(paymentcardCRUD.getCard_number(),user,
                        paymentcardCRUD.getCard_holder_name(),paymentcardCRUD.getCard_type(),
                        regDate,expDate);
                paymentcards.add(paymentcard);
            }
            paymentcardRepository.saveAll(paymentcards);
            return ResponseEntity.ok().body(userResourceAssembler.toCardsCollectionModel(paymentcards));
        }
    }
    @DeleteMapping("/{username}/cards/delete")
    public ResponseEntity<?> deletePaymentCard(@PathVariable String username ,@RequestParam(value = "id",required = false) String id) throws ParseException {
            User user = verifyUserInstance(username);
            if(id != null){
                    paymentcardRepository.deleteAllByUser(user);
                    List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
                    return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("Deleted all card for user"),
                            linkTo(methodOn(UserController.class).deletePaymentCard(username,id)).withSelfRel(),
                            linkTo(methodOn(UserController.class).setPaymentCards(username,paymentcardCRUDS )).withRel("Set payment cards"),
                            linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")
                            ));
            }
            else{
                paymentcardRepository.deleteById(id);
                List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
                return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("Deleted all card for user"),
                        linkTo(methodOn(UserController.class).deletePaymentCard(username,id)).withSelfRel(),
                        linkTo(methodOn(UserController.class).setPaymentCards(username,paymentcardCRUDS )).withRel("Set payment cards"),
                        linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")
                ));
            }
    }
}


