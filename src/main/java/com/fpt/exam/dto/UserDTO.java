package com.fpt.exam.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String roleName;
    private Boolean status;
}
