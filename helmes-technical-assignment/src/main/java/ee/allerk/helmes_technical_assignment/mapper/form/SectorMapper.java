package ee.allerk.helmes_technical_assignment.mapper.form;

import ee.allerk.helmes_technical_assignment.dto.form.SectorDto;
import ee.allerk.helmes_technical_assignment.mapper.common.EntityMapper;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface SectorMapper extends EntityMapper<Sector, SectorDto> {
}
