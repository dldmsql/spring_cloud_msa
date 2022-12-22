package com.example.userservice.dto;

import java.util.Date;
import lombok.Data;

@Data
public class UserDto {
  private String email;
  private String pwd; // 보안처리 안한채로 저장하면 안된다.
  private String name; // 여기까지 사용자로부터 들어오는 데이터
  private String userId; // UUID로 지정
  private Date createdAt; // meta 데이터
  private String encryptedPwd; // 암호화처리를 한 뒤에 저장
}