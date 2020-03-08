package com.aa.mtg.admin.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class MtgUserDetailsService implements UserDetailsService {
  private final MtgUserRepository userRepository;

  public MtgUserDetailsService(MtgUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<MtgUser> mtgUserOptional = userRepository.findByUsername(username);
    if (mtgUserOptional.isPresent()) {
      MtgUser mtgUser = mtgUserOptional.get();
      return new User(mtgUser.getUsername(), mtgUser.getPassword(), new ArrayList<>());
    }

    throw new UsernameNotFoundException(username);
  }
}
