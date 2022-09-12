package ir.payeshgaran.person.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.payeshgaran.person.model.PersonModel;
import ir.payeshgaran.person.service.imp.PersonServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/person")
@Slf4j
public record PersonController(PersonServiceImp personService) {

    @PostMapping()
    public ResponseEntity<String> createPerson(@RequestBody PersonModel personModel) {
        personService.save(personModel);
        return ResponseEntity.ok().body("Done!");
    }

    @GetMapping("/get_details/{username}")
    public PersonModel personDetains(@PathVariable("username") String username) {
        return personService.getPersonByUsername(username);
    }

    @GetMapping("/remain-money")
    public ResponseEntity<?> remainMoney(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = getUsernameByToken(request, response);

        Double amount = personService.getRemainMoney(username);
        if (amount == -1.0)
            return ResponseEntity.ok().body("ERROR occurred -- Try AGAIN LATER");

        return ResponseEntity.ok().body(amount);
    }

    @GetMapping("/get-type/{username}")
    public String getPersonType(@PathVariable("username") String username) {
        return personService.getType(username);
    }

    @GetMapping("/forget-password/{username}")
    public String forgetPassword(@PathVariable String username, @RequestBody PersonModel personModel) {
        return personService.sendPassword(username, personModel);
    }

    @GetMapping("/update-score/{username}/{score}")
    public Boolean updateScore(@PathVariable("username") String username, @PathVariable("score") int score) {
        personService.updateScore(username, score);
        return true;
    }

    @GetMapping("/show-score")
    public ResponseEntity<Integer> showScore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = getUsernameByToken(request, response);
        return ResponseEntity.ok().body(personService.showScore(username));
    }

    public static String getUsernameByToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String username = "";
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
                String token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                username = decodedJWT.getSubject();
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> errors = new HashMap<>();
                errors.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
        } else {
            throw new RuntimeException("Refresh Token id missing");
        }
        return username;
    }

}
