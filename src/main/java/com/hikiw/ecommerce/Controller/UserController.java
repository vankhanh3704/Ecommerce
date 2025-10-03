package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.UserCreationRequest;
import com.hikiw.ecommerce.Model.Request.UserUpdateRequest;
import com.hikiw.ecommerce.Model.Response.UserResponse;
import com.hikiw.ecommerce.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/users")
public class UserController {
    UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody UserCreationRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }
    @GetMapping
    public List<UserResponse> getUsers(){
        return userService.getUsers();
    }
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }
    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId, request);
    }
}
