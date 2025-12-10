package com.micropos.delivery;

import com.micropos.dto.DeliveryRecordDto;
import com.micropos.mapper.DeliveryMapper;
import com.micropos.model.DeliveryRecord;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class DeliveryMapperImpl implements DeliveryMapper {

    @Override
    public DeliveryRecord toRecord(DeliveryRecordDto dto) {
        if ( dto == null ) {
            return null;
        }

        String orderId = null;
        String status = null;
        Long createdAt = null;

        orderId = dto.getOrderId();
        status = dto.getStatus();
        createdAt = dto.getCreatedAt();

        DeliveryRecord deliveryRecord = new DeliveryRecord( orderId, status, createdAt );

        return deliveryRecord;
    }

    @Override
    public DeliveryRecordDto toDto(DeliveryRecord record) {
        if ( record == null ) {
            return null;
        }

        DeliveryRecordDto deliveryRecordDto = new DeliveryRecordDto();

        deliveryRecordDto.setOrderId( record.getOrderId() );
        deliveryRecordDto.setStatus( record.getStatus() );
        deliveryRecordDto.setCreatedAt( record.getCreatedAt() );

        return deliveryRecordDto;
    }

    @Override
    public Collection<DeliveryRecord> toRecords(Collection<DeliveryRecordDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        Collection<DeliveryRecord> collection = new ArrayList<DeliveryRecord>( dtos.size() );
        for ( DeliveryRecordDto deliveryRecordDto : dtos ) {
            collection.add( toRecord( deliveryRecordDto ) );
        }

        return collection;
    }

    @Override
    public Collection<DeliveryRecordDto> toDto(Collection<DeliveryRecord> records) {
        if ( records == null ) {
            return null;
        }

        Collection<DeliveryRecordDto> collection = new ArrayList<DeliveryRecordDto>( records.size() );
        for ( DeliveryRecord deliveryRecord : records ) {
            collection.add( toDto( deliveryRecord ) );
        }

        return collection;
    }
}
