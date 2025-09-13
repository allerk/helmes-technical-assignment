package ee.allerk.helmes_technical_assignment.mapper.form;

import ee.allerk.helmes_technical_assignment.dto.form.UserDto;
import ee.allerk.helmes_technical_assignment.mapper.common.EntityMapper;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import ee.allerk.helmes_technical_assignment.model.form.User;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<User, UserDto> {
    @Override
    @Mapping(target = "sectorIds", source = "sectors")
    UserDto toDto(User entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<UserDto> toDtos(List<User> entityList);

    default Set<Long> mapSectorsToIds(Set<Sector> sectors) {
        if (sectors == null) return new HashSet<>();
        return sectors.stream()
                .map(Sector::getId)
                .collect(Collectors.toSet());
    }

    default Set<Sector> mapIdsToSectors(Set<Long> ids) {
        if (ids == null) return new HashSet<>();
        return ids.stream()
                .map(id -> {
                    Sector s = new Sector();
                    s.setId(id);
                    return s;
                })
                .collect(Collectors.toSet());
    }

    @Override
    @Mapping(target = "sectors", source = "sectorIds")
    User toEntity(UserDto dto);
}
