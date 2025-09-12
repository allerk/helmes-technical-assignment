package ee.allerk.helmes_technical_assignment.mapper.form;

import ee.allerk.helmes_technical_assignment.dto.form.UserDto;
import ee.allerk.helmes_technical_assignment.mapper.common.EntityMapper;
import ee.allerk.helmes_technical_assignment.model.form.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {SectorMapper.class})
public interface UserMapper extends EntityMapper<User, UserDto> {
}
