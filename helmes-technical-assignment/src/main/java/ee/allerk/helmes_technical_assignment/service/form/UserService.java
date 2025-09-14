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

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        validateSelectedSectors(userDto.getSectorIds());
        User entity = userMapper.toEntity(userDto);
        User saved = userRepository.save(entity);
        return userMapper.toDto(saved);
    }

    @Transactional
    public UserDto partialUpdate(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new AppException("User#" + userDto.getId() + " not found", 404));
        validateSelectedSectors(userDto.getSectorIds());
        userMapper.partialUpdate(user, userDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User#" + id + " not found", 404));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    protected void validateSelectedSectors(Set<Long> sectorIds) {
        Set<Sector> sectors = sectorService.findByIds(sectorIds);
        List<String> invalidSectors = sectors.stream()
                .filter(s -> !s.getChildren().isEmpty())
                .map(Sector::getLabel)
                .toList();

        if (!invalidSectors.isEmpty()) {
            throw new AppException(
                    "The following sectors are parent sectors, select a leaf sector(s): "
                            + String.join(", ", invalidSectors),
                    302
            );
        }
    }
}
