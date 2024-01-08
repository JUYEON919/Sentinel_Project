package com.example.demo.model.log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EthernetHeaderEntity {
	private String destinationMacAddr;
    private String sourceMacAddr;
    private String ethernetType;
}
