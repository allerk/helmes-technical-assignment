package ee.allerk.helmes_technical_assignment.service.form;

import ee.allerk.helmes_technical_assignment.model.form.Sector;
import ee.allerk.helmes_technical_assignment.model.form.User;
import ee.allerk.helmes_technical_assignment.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    private UserMapper userMapper;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private SectorService sectorService;

    @Test
    @DisplayName("Add new User")
    void addNewUser() {
        // todo: rewrite the request logic with dtos, and possibly response logic also
        List<Long> sectorIds = Arrays.asList(1L, 2L);
        UserDto userDto = UserDto.builder().name("Aleksandr").isTermsAgreed(true).sectorIds(sectorIds).build();

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
        List<Sector> sectors = Arrays.asList(
                mockSector1Deep, mockSector1Deep
        );

        User mappedEntityFromDtoToSave = userMapper.toEntity(userDto);

        User savedUser = userMapper.toEntity(userDto);
        savedUser.setId(1L);
        savedUser.addSector(mockSector1Deep);

        UserDto expectedResult = userMapper.toDto(savedUser);

        when(sectorService.findByIds(sectorIds)).thenReturn(sectors);
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(mappedEntityFromDtoToSave);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(expectedResult);

        UserDto userSaved = userService.create(userDto);

        verify(userRepository).save(userCaptor.capture());

        User toSaveEntity = userCaptor.getValue();

        verify(sectorService, times(1)).findByIds(sectorIds);
        verify(userMapper, times(1)).toEntity(mappedEntityFromDtoToSave);
        verify(userRepository, times(1)).save(toSaveEntity);
        verify(userMapper, times(1)).toDto(savedUser);

        assertEquals(expectedResult, userSaved);
    }

    @Test
    @DisplayName("Remove sectors from User")
    void removeSectorsFromUser() {
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

        userToRemoveSectorFrom.addSector(mockSector1Deep);
        userToRemoveSectorFrom.addSector(mockSector2Deep1);

        User userAfterRemove = User.builder()
                .id(userToRemoveSectorFrom.getId())
                .name(userToRemoveSectorFrom.getName())
                .isTermsAgreed(userToRemoveSectorFrom.isTermsAgreed())
                .build();
        userAfterRemove.addSector(mockSector1Deep);

        UserDto userAfterRemoveDto = userMapper.toDto(userAfterRemove);

        assertEquals(2, userToRemoveSectorFrom.getSectors().size());

        // either use their ids or the dto. not sure yet
        when(userRepository.findById(userToRemoveSectorFrom.getId())).thenReturn(Optional.of(userToRemoveSectorFrom));
        when(sectorService.findOneById(mockSector2Deep1.getId())).thenReturn(mockSector2Deep1);

        when(userRepository.save(any(User.class))).thenReturn(userAfterRemove);
        when(userMapper.toDto(any(User.class))).thenReturn(expectedResult);

        UserDto userWithRemovedSector = userService.removeSector(userToRemoveSectorFrom.getId(), mockSector2Deep1.getId());

        verify(userRepository).save(userCaptor.capture());

        User toSaveEntity = userCaptor.getValue();

        verify(userRepository, times(1)).save(toSaveEntity);

        assertEquals(1, userWithRemovedSector.getSectors().size());
        assertTrue(userWithRemovedSector.getSectors().contains(mockSector1Deep));
        assertFalse(userWithRemovedSector.getSectors().contains(mockSector2Deep1));
        assertEquals(userAfterRemoveDto, userWithRemovedSector);
    }

    @Test
    @DisplayName("Edit User data")
    void editUser() {
        UserDto userDto = UserDto.builder().id(1L).name("Robert").isTermsAgreed(true).build();
        User user = User.builder().id(1L).name("Aleksandr").isTermsAgreed(true).build();

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto updatedUser = userService.update(userDto);

        verify(userRepository, times(1)).findById(dto.getId());

        verify(userMapper, times(1)).partialUpdate(user, userDto);
        verify(userRepository, times(1)).save(userCaptor.capture());

        assertEquals(userDto, updatedUser);
    }
}
