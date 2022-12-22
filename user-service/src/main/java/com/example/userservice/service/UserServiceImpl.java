package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  UserRepository userRepository;

  @Override
  public UserDto createUser(UserDto userDto) {
    // (1) User 테이블의 PK 값을 랜덤 ID로 부여
    userDto.setUserId(UUID.randomUUID().toString());
    // (2) Mapper 클래스 생성
    ModelMapper mapper = new ModelMapper();
    // (3) mapper 기본 설정 ( 매칭 전략 - 강력하게 딱 맞아 떨어지지 않으면 변환을 안하도록 )
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    // (4) UserDto로부터 UserEntity로 변환시켜주는 함수가 map()
    UserEntity userEntity = mapper.map(userDto, UserEntity.class);
    // (5) 비밀번호 암호화 처리
    userEntity.setEncryptedPwd("encrypted_password");
    // (6) DB에 저장
    userRepository.save(userEntity);
    // (7) UserDto 타입으로 변환 -> 이 부분은 이미 컨트롤러에서 처리하고 있어서 없어도 되긴 한다.
    UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

    return returnUserDto ;
  }
}
