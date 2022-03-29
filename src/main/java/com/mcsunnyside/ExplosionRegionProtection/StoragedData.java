package com.mcsunnyside.ExplosionRegionProtection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
class StoragedData {
    private List<ProtectWidget> widgets;
}
