package com.micropos.mapper;

import com.micropos.dto.DeliveryRecordDto;
import com.micropos.model.DeliveryRecord;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(implementationPackage = "com.micropos.delivery")
public interface DeliveryMapper {
    DeliveryRecord toRecord(DeliveryRecordDto dto);

    DeliveryRecordDto toDto(DeliveryRecord record);

    Collection<DeliveryRecord> toRecords(Collection<DeliveryRecordDto> dtos);

    Collection<DeliveryRecordDto> toDto(Collection<DeliveryRecord> records);
}
