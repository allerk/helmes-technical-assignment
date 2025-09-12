package ee.allerk.helmes_technical_assignment.service.form;

import ee.allerk.helmes_technical_assignment.dto.form.UserDto;
import ee.allerk.helmes_technical_assignment.exceptions.AppException;
import ee.allerk.helmes_technical_assignment.mapper.form.UserMapper;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import ee.allerk.helmes_technical_assignment.model.form.User;
import ee.allerk.helmes_technical_assignment.repository.form.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final SectorService sectorService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto create(UserDto userDto) {
        String name = userDto.getName();
        if (userRepository.findByName(name).isPresent()) {
            throw new AppException("User with name %s already exists".formatted(name), 409);
        }
        User toEntity = userMapper.toEntity(userDto);
        User saved = userRepository.save(toEntity);
        return userMapper.toDto(saved);
    }

    @Transactional
    public UserDto removeSector(Long user_id, Long sector_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new AppException("User#%s not found".formatted(user_id), 404));

        Optional<Sector> sector = sectorService.findOneById(sector_id);

        if (sector.isEmpty()) {
            throw new AppException("Sector#%s not found".formatted(sector_id), 404);
        }

        user.setSectors(user.getSectors().stream().filter((s -> !s.getId().equals(sector_id))).collect(Collectors.toSet()));

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserDto partialUpdate(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new AppException("User#" + userDto.getId() + " not found", 404));

        userMapper.partialUpdate(user, userDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
