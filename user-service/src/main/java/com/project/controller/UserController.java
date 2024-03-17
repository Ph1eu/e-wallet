package com.project.controller;

import com.project.assembler.UserResourceAssembler;
import com.project.api.rest.security.jwt.JwtServices;
import com.project.exceptions.custom_exception.ValidationInput.MissingRequiredFieldException;
import com.project.service.address.dto.AddressDto;
import com.project.service.paymentcard.dto.PaymentcardDTO;
import com.project.service.user.dto.UserDto;
import com.project.payload.request.CRUDUserInforRequest.AddressCRUD;
import com.project.payload.request.CRUDUserInforRequest.PaymentcardCRUD;
import com.project.payload.response.MessageResponse;
import com.project.payload.response.ResponseEntityWrapper;
import com.project.service.user.entity.User;
import com.project.service_impl.address.AddressServiceImpl;
import com.project.api.rest.security.CustomUserDetail;
import com.project.service_impl.paymentcard.PaymentCardServiceImpl;
import com.project.service_impl.user.UserServiceImpl;
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

    private static final Logger logger = LoggerFactory.getLogger(JwtServices.class);
    @Autowired
    UserServiceImpl userDetailService;
    @Autowired
    PaymentCardServiceImpl paymentCardServiceImpl;
    @Autowired
    AddressServiceImpl addressServiceImpl;
    @Autowired
    JwtServices jwtServices;
    @Autowired
    UserResourceAssembler userResourceAssembler;

    public static boolean validateDateString(String dateStr) {
        String pattern = "^\\d{2}/\\d{2}/\\d{4}$";
        return Pattern.matches(pattern, dateStr);
    }

    // verify user in security context
    public UserDto verifyUserInstance(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user = userDetailService.getById(userId);
        UserDto userDto = null;

        if (principal instanceof UserDetails userDetails) {
            if (Objects.equals(user.getUsername(), userDetails.getUsername())) {
                CustomUserDetail customUserDetail = (CustomUserDetail) userDetailService.loadUserByUsername(user.getUsername());
                userDto= new UserDto(customUserDetail.getUser());
            } else {
                logger.error("Error: Invalid User");
            }
        } else {
            logger.error("Error: Principal is not an instance of UserDetails");
        }
        return userDto;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getOneUser(@PathVariable("userId") String userId) {

        UserDto user = verifyUserInstance(userId);
        UserDto userwithBalance = userDetailService.getUserWithBalanceInformation(user.getEmail());
        if (user == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        } else {
            List<PaymentcardDTO> paymentcardDTOS = paymentCardServiceImpl.getAllByUser(user);
            userwithBalance.setPaymentcards(paymentcardDTOS);
            return ResponseEntity.ok().body(userResourceAssembler.toCollectionModelInWrapper(List.of(userwithBalance)));
        }

    }

    @GetMapping("/address")
    public ResponseEntity<?> getAddress(@RequestParam("username") String username) {
        UserDto user = verifyUserInstance(username);

        if (user == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        } else if (user.getAddress() == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>();
            responseEntityWrapper.setMessage("ADDRESS IS EMPTY");
            BindingResult bindingResult = null;
            responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).deleteAddress(username)).withRel("delete address for user"),
                    linkTo(methodOn(UserController.class).getAddress(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(), username, bindingResult)).withRel("Add address").withType("POST")));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
        } else {
            return ResponseEntity.ok().body(EntityModel.of(userResourceAssembler.toAddressModel(user)));
        }
    }

    @PostMapping("/address")
    public ResponseEntity<?> setAddress(@Valid @RequestBody AddressCRUD addressCRUD, @RequestParam("username") String username, BindingResult bindingResult) {
        handMissingField(bindingResult);
        UserDto user = verifyUserInstance(username);
        if (user == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        } else {
            Class<?> clazz = addressCRUD.getClass();
            AddressDto address = new AddressDto(UUID.randomUUID().toString(), addressCRUD.getStreet_address(),
                    addressCRUD.getCity(), addressCRUD.getProvince(), addressCRUD.getCountry()
            );
            addressServiceImpl.saveAddressForUser(address, user);
            return ResponseEntity.ok().body(
                    userResourceAssembler.toAddressModel(user));
        }
    }

    @DeleteMapping("/address")
    public ResponseEntity<?> deleteAddress(@RequestParam("username") String username) {
        UserDto user = verifyUserInstance(username);
        if (user == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        } else {
            addressServiceImpl.deleteAddressByUser(user);
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>();
            responseEntityWrapper.setMessage("ADDRESS IS NOW DELETED");
            BindingResult bindingResult = null;

            responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).deleteAddress(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).getAddress(username)).withRel("Get address from username"),
                    linkTo(methodOn(UserController.class).setAddress(new AddressCRUD(), username, bindingResult)).withRel("Add address").withType("POST")));
            return ResponseEntity.ok().body(responseEntityWrapper);

        }
    }

    @GetMapping("/cards")
    public ResponseEntity<?> getPaymentCards(@RequestParam("username") String username) throws ParseException {
        UserDto user = verifyUserInstance(username);
        if (user == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        } else {
            List<PaymentcardDTO> paymentcards = paymentCardServiceImpl.getAllByUser(user);
            if (paymentcards.isEmpty()) {
                ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper = new ResponseEntityWrapper<>();
                responseEntityWrapper.setMessage("User has no card yet ");
                responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntityWrapper);
            } else {
                ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper = userResourceAssembler.toCardsCollectionModel(paymentcards, user);
                responseEntityWrapper.setMessage("Get cards successfully");
                return ResponseEntity.ok().body(
                        responseEntityWrapper);
            }
        }
    }

    @PostMapping("/cards")
    public ResponseEntity<ResponseEntityWrapper<?>> setPaymentCards(@RequestParam("username") String username, @RequestBody List<PaymentcardCRUD> paymentcardCRUDs, BindingResult bindingResult) throws ParseException {
        handMissingField(bindingResult);
        UserDto user = verifyUserInstance(username);
        List<PaymentcardDTO> paymentcards = new ArrayList<>();
        if (user == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        } else {
            for (PaymentcardCRUD paymentcardCRUD : paymentcardCRUDs) {
                if (!validateDateString(paymentcardCRUD.getExpiration_date()) && !validateDateString(paymentcardCRUD.getRegistration_date())) {
                    return ResponseEntity.badRequest().body(new ResponseEntityWrapper<>("Bad date request"));
                }
                Date expDate = new SimpleDateFormat("dd/MM/yyyy").parse(paymentcardCRUD.getExpiration_date());
                Date regDate = new SimpleDateFormat("dd/MM/yyyy").parse(paymentcardCRUD.getRegistration_date());
                PaymentcardDTO paymentcard = new PaymentcardDTO(UUID.randomUUID().toString(), paymentcardCRUD.getCard_number(), user,
                        paymentcardCRUD.getCard_holder_name(), paymentcardCRUD.getCard_type(),
                        regDate, expDate);
                paymentcards.add(paymentcard);
            }
            paymentCardServiceImpl.saveAllByCards(paymentcards);
            ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper = userResourceAssembler.toCardsCollectionModel(paymentcards, user);
            responseEntityWrapper.setMessage("Set cards successfully");
            return ResponseEntity.ok().body(responseEntityWrapper);
        }
    }

    @DeleteMapping("/cards")
    public ResponseEntity<ResponseEntityWrapper<?>> deletePaymentCardbyID(@RequestParam String username, @RequestParam String id) throws ParseException {
        UserDto user = verifyUserInstance(username);
        if (user == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        } else {
            paymentCardServiceImpl.deleteByID(id);
            List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
            BindingResult bindingResult = null;
            ResponseEntityWrapper<EntityModel<PaymentcardDTO>> responseEntityWrapper = new ResponseEntityWrapper<>();
            responseEntityWrapper.setMessage("Deleted card for user");
            responseEntityWrapper.setLink(List.of(linkTo(methodOn(UserController.class).deletePaymentCardbyID(username, id)).withSelfRel(),
                    linkTo(methodOn(UserController.class).deleteAllPaymentCard(username)).withRel("delete all payment cards"),
                    linkTo(methodOn(UserController.class).setPaymentCards(username, paymentcardCRUDS, bindingResult)).withRel("Set payment cards"),
                    linkTo(methodOn(UserController.class).getPaymentCards(username)).withRel("get all payment cards")
            ));
            return ResponseEntity.ok().body(responseEntityWrapper);
        }
    }

    @DeleteMapping("/cards/delete/all")
    public ResponseEntity<?> deleteAllPaymentCard(@RequestParam String username) throws ParseException {
        UserDto user = verifyUserInstance(username);
        if (user == null) {
            ResponseEntityWrapper<MessageResponse> responseEntityWrapper = new ResponseEntityWrapper<>("Unmatched user in session");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseEntityWrapper);
        } else {
            paymentCardServiceImpl.deleteAllByUser(user);
            List<PaymentcardCRUD> paymentcardCRUDS = new ArrayList<>();
            BindingResult bindingResult = null;
            return ResponseEntity.ok().body(EntityModel.of(new MessageResponse("Deleted all card for user"),
                    linkTo(methodOn(UserController.class).deleteAllPaymentCard(username)).withSelfRel(),
                    linkTo(methodOn(UserController.class).deletePaymentCardbyID(username, "id")).withRel("Delete by ID"),
                    linkTo(methodOn(UserController.class).setPaymentCards(username, paymentcardCRUDS, bindingResult)).withRel("Set payment cards"),
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
            throw new MissingRequiredFieldException(errorMessage.toString());

        }
    }
}



