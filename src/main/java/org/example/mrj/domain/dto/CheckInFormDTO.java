package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.EventInformation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckInFormDTO {

    String date;

    String time;

    String address;

    public CheckInFormDTO(EventInformation eventInformation) {
        this.date = eventInformation.getDate();
        this.time = eventInformation.getTime();
        this.address = eventInformation.getAddress();
    }

}
