package ee.allerk.helmes_technical_assignment.service.form;

import ee.allerk.helmes_technical_assignment.dto.form.SectorDto;
import ee.allerk.helmes_technical_assignment.dto.form.UserDto;
import ee.allerk.helmes_technical_assignment.exceptions.AppException;
import ee.allerk.helmes_technical_assignment.mapper.form.SectorMapper;
import ee.allerk.helmes_technical_assignment.mapper.form.SectorMapperImpl;
import ee.allerk.helmes_technical_assignment.mapper.form.UserMapper;
import ee.allerk.helmes_technical_assignment.mapper.form.UserMapperImpl;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import ee.allerk.helmes_technical_assignment.model.form.User;
import ee.allerk.helmes_technical_assignment.repository.form.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import org.mockito.Spy;
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    private UserMapper userMapper = new UserMapperImpl();;

    @Spy
    private SectorMapper sectorMapper = new SectorMapperImpl();;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SectorService sectorService;

    @InjectMocks
    private UserService userService;


    @BeforeEach
    public void iniMapperDependencies(){
        ReflectionTestUtils.setField(userMapper,"sectorMapper",sectorMapper);
        ReflectionTestUtils.setField(sectorMapper,"userMapper",userMapper);
    }

    @Test
    @DisplayName("Add new User")
    void addNewUser() {
        Sector mockSector1Deep = Sector.builder()
                .id(1L)
                .label("Manufacturing")
                .build();
        Sector mockSector2Deep1 = Sector.builder()
                .id(2L)
                .label("Construction materials")
                .build();

        mockSector2Deep1.setParent(mockSector1Deep);
        Set<Sector> mockSector1DeepChilds = new HashSet<>();
        mockSector1DeepChilds.add(mockSector2Deep1);
        mockSector1Deep.setChildren(mockSector1DeepChilds);

        Set<SectorDto> sectorDtos = Set.of(
                sectorMapper.toDto(mockSector1Deep),
                sectorMapper.toDto(mockSector2Deep1)
        );
        UserDto userDto = UserDto.builder().name("Aleksandr").isTermsAgreed(true).sectors(sectorDtos).build();
        User toSave = userMapper.toEntity(userDto);

        User savedUser = userMapper.toEntity(userDto);

        UserDto expectedResult = userMapper.toDto(savedUser);

        when(userMapper.toEntity(any(UserDto.class))).thenReturn(toSave);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(expectedResult);

        UserDto userSaved = userService.create(userDto);

        verify(userRepository).save(userCaptor.capture());

        User toSaveEntity = userCaptor.getValue();

        // NB! Number of calls displayed here is not the actual number of how many calls were called in userService.create method.
        // The actual number is: number == times(value) - number_of_mapper_call_...(either toEntity or toDto)
        verify(userMapper, times(3)).toEntity(userDto);
        verify(userRepository, times(1)).save(toSaveEntity);
        verify(userMapper, times(2)).toDto(savedUser);

        assertEquals(expectedResult, userSaved);
    }

    @Test
    @DisplayName("Add new User failed - user already exist")
    void addNewUserFailedUserAlreadyExist() {
        UserDto userDto = UserDto.builder().name("Aleksandr").isTermsAgreed(true).build();
        String errorMsg = "User with name %s already exists".formatted(userDto.getName());

        User user = userMapper.toEntity(userDto);
        user.setId(1L);

        when(userRepository.findByName(any())).thenReturn(Optional.of(user));

        AppException exception = assertThrows(AppException.class, () -> userService.create(userDto));
        int code = 409;

        assertEquals(errorMsg, exception.getMessage());
        assertEquals(code, exception.getCode());
    }

    @Test
    @DisplayName("Remove sector from User")
    void removeSectorFromUser() {
        User userToRemoveSectorFrom = User.builder().id(1L).name("Aleksandr").isTermsAgreed(true).build();
        Sector mockSector1Deep = Sector.builder()
                .id(1L)
                .label("Manufacturing")
                .build();
        Sector mockSector2Deep1 = Sector.builder()
                .id(2L)
                .label("Construction materials")
                .build();
        mockSector2Deep1.setParent(mockSector1Deep);
        Set<Sector> mockSector1DeepChilds = new HashSet<>();
        mockSector1DeepChilds.add(mockSector2Deep1);
        mockSector1Deep.setChildren(mockSector1DeepChilds);

        userToRemoveSectorFrom.setSectors(Set.of(
                mockSector1Deep,
                mockSector2Deep1
        ));

        User userAfterRemove = User.builder()
                .id(userToRemoveSectorFrom.getId())
                .name(userToRemoveSectorFrom.getName())
                .isTermsAgreed(userToRemoveSectorFrom.isTermsAgreed())
                .build();
        userAfterRemove.setSectors(Set.of(mockSector1Deep));

        UserDto userAfterRemoveDto = userMapper.toDto(userAfterRemove);

        when(userRepository.findById(userToRemoveSectorFrom.getId())).thenReturn(Optional.of(userToRemoveSectorFrom));
        when(sectorService.findOneById(mockSector2Deep1.getId())).thenReturn(Optional.of(mockSector2Deep1));

        when(userRepository.save(any(User.class))).thenReturn(userAfterRemove);
        when(userMapper.toDto(any(User.class))).thenReturn(userAfterRemoveDto);

        assertEquals(2, userToRemoveSectorFrom.getSectors().size());

        UserDto userWithRemovedSector = userService.removeSector(userToRemoveSectorFrom.getId(), mockSector2Deep1.getId());

        verify(userRepository).save(userCaptor.capture());

        User toSaveEntity = userCaptor.getValue();

        verify(userRepository, times(1)).save(toSaveEntity);

        assertEquals(1, userWithRemovedSector.getSectors().size());
        assertTrue(userWithRemovedSector.getSectors().contains(sectorMapper.toDto(mockSector1Deep)));
        assertFalse(userWithRemovedSector.getSectors().contains(sectorMapper.toDto(mockSector2Deep1)));
        assertEquals(userAfterRemoveDto, userWithRemovedSector);
    }

    @Test
    @DisplayName("Remove sector from User failed - user not found by id")
    void removeSectorFromUserFailedNotFoundUser() {
        Long user_id = 1L;
        Long sector_id = 2L;
        String errorMsg = "User#%s not found".formatted(user_id);
        when(userRepository.findById(any())).thenThrow(new AppException(errorMsg, 404));

        AppException exception = assertThrows(AppException.class, () -> userService.removeSector(user_id, sector_id));

        int code = 404;

        assertEquals(errorMsg, exception.getMessage());
        assertEquals(code, exception.getCode());
    }

    @Test
    @DisplayName("Remove sector from User failed - sector not found by id")
    void removeSectorFromUserFailedNotFoundSector() {
        UserDto userDto = UserDto.builder().id(1L).name("Aleksandr").isTermsAgreed(true).build();
        User user = userMapper.toEntity(userDto);
        Long sector_id = 2L;
        String errorMsg = "Sector#%s not found".formatted(sector_id);

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.ofNullable(user));
        when(sectorService.findOneById(any())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> userService.removeSector(userDto.getId(), sector_id));

        verify(userRepository, times(1)).findById(userDto.getId());

        int code = 404;

        assertEquals(errorMsg, exception.getMessage());
        assertEquals(code, exception.getCode());
    }

    @Test
    @DisplayName("Edit User data")
    void editUser() {
        User user = User.builder().id(1L).name("Aleksandr").isTermsAgreed(true).build();
        UserDto userDto = UserDto.builder().id(1L).name("Robert").isTermsAgreed(true).build();

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto updatedUser = userService.partialUpdate(userDto);

        verify(userRepository, times(1)).findById(userDto.getId());

        verify(userMapper, times(1)).partialUpdate(user, userDto);
        verify(userRepository, times(1)).save(userCaptor.capture());

        assertEquals(userDto, updatedUser);
    }

    @Test
    @DisplayName("Edit User data failed - user not found")
    void editUserFailedNotFoundUser() {
        UserDto userDto = UserDto.builder().id(1L).name("Aleksandr").isTermsAgreed(true).build();
        String errorMsg = "User#%s not found".formatted(userDto.getId());
        when(userRepository.findById(any())).thenThrow(new AppException(errorMsg, 404));

        AppException exception = assertThrows(AppException.class, () -> userService.partialUpdate(userDto));

        int code = 404;

        assertEquals(errorMsg, exception.getMessage());
        assertEquals(code, exception.getCode());
    }
}
