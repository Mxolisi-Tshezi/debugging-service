package com.sumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sumer.entity.DetailField;
import com.sumer.entity.PolicyFinance;
import com.sumer.entity.PolicyHeader;
import com.sumer.entity.Risk;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PolicyDTO {
    private PolicyHeader header;
    private PolicyFinance finance;
    private List<Risk> risks;
    private Set<DetailField> detailFields;

}
