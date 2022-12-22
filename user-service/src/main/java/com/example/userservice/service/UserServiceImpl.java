package com.example.userservice.service;

import com.example.userservice.dto.ResponseOrder;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
  UserRepository userRepository;
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

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
    userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
    // (6) DB에 저장
    userRepository.save(userEntity);
    // (7) UserDto 타입으로 변환 -> 이 부분은 이미 컨트롤러에서 처리하고 있어서 없어도 되긴 한다.
    UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

    return returnUserDto ;
  }

  @Override
  public UserDto getUserByUserId(String userId) {
    // (1) DB로부터 userId를 이용해 UserEntity 값 조회
    UserEntity userEntity = userRepository.findByUserId(userId);

    // (2) 존재하지 않다면, 에러 발생
    if(userEntity == null) {
      throw new UsernameNotFoundException("User not found"); // 이 에러는 적절하진 않다.
    }

    // (3) 존재한다면, Mapper를 통해 UserDto로 변환
    UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
    // (4) ResponseOrder를 담기 위한 빈 리스트 생성
    List<ResponseOrder> orderList = new ArrayList<>();
    userDto.setOrders(orderList);

    return userDto;
  }

  @Override
  public Iterable<UserEntity> getUserByAll() {
    return userRepository.findAll();
  }
}
