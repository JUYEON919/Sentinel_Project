package com.example.demo.model.log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class IpHeaderEntity {
	private int version;
    private int headerLength;
    private int typeOfService;
    private int datagramLength;
    private int identification;
    private int flags;
    private int fragmentOffset;
    private int ttl;
    private int datagramProtocol;
    private int checksum;
    private String sourceIp;
    private String destinationIp;
}
